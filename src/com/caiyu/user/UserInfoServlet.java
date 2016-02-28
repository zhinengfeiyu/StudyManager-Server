package com.caiyu.user;

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

public class UserInfoServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int userId = Integer.parseInt(req.getParameter(Param.REQ_USER_ID));
			
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String querySQL = 
					"select name, study_no, user_name, sex, class.profession_grade_name from user,class" +
					" where user.id=%1$d and user.profession=class.profession_grade;";
			ResultSet rs = stmt.executeQuery(String.format(querySQL, userId));
			if (rs.first()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Param.RES_REAL_NAME, rs.getString(1));
				jsonObject.put(Param.RES_STUDY_NO, rs.getString(2));
				jsonObject.put(Param.RES_USER_NAME, rs.getString(3));
				jsonObject.put(Param.RES_SEX, rs.getString(4));
				jsonObject.put(Param.RES_PROFESSION, rs.getString(5));
				res.getWriter().write(jsonObject.toString());
			}
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
