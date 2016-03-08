package com.caiyu.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class SearchServlet extends HttpServlet {
	
	private static final int TYPE_CLASS = 1;
    private static final int TYPE_CLASS_ROOM = 2;
    private static final int TYPE_TEACHER = 3;
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int searchType = Integer.parseInt(req.getParameter(Param.REQ_SEARCH_TYPE));
			String input = req.getParameter(Param.REQ_INPUT);
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String outputStr = "";
			String querySql = "";
			switch (searchType) {
			case TYPE_CLASS:
				break;
			case TYPE_CLASS_ROOM:
				querySql = "select name from class_room where name like '%" + input + "%';";
				break;
			case TYPE_TEACHER:
				querySql = "select id, name, sex, phone, academy, position" +
						" from teacher where name like '%" + input + "%'" +
						" or academy like '%" + input + "%'" +
						" or position like '%" + input + "%';";
				break;
			}
			ResultSet rs = stmt.executeQuery(querySql);
			switch (searchType) {
			case TYPE_CLASS:
				break;
			case TYPE_CLASS_ROOM:
				if (rs.first()) {
					JSONArray jsonArray = new JSONArray();
					do {
						jsonArray.put(rs.getString(1));
					} while (rs.next());
					outputStr = jsonArray.toString();
				}
				break;
			case TYPE_TEACHER:
				if (rs.first()) {
					JSONArray jsonArray = new JSONArray();
					do {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put(Param.RES_TEACHER_ID, rs.getInt(1));
						jsonObject.put(Param.RES_TEACHER, rs.getString(2));
						jsonObject.put(Param.RES_SEX, rs.getString(3));
						jsonObject.put(Param.RES_PHONE, rs.getString(4));
						jsonObject.put(Param.RES_ACADEMY, rs.getString(5));
						jsonObject.put(Param.RES_POSITION, rs.getString(6));
						jsonArray.put(jsonObject);
					} while (rs.next());
					outputStr = jsonArray.toString();
				}
				break;
			}
			res.getWriter().write(outputStr);
			if (rs != null) rs.close();
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) {
		this.doGet(req, res);
	}
}
