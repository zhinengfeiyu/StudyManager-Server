package com.caiyu.classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

/**
 * 根据教师姓名，查找教师信息
 * @author 渝
 *
 */
public class GetTeacherServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			String teacherName = req.getParameter(Param.REQ_TEACHER_NAME);
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String querySql = "select id, sex, phone, academy, position from teacher"
							+ " where name='%1$s'";
			ResultSet rs = stmt.executeQuery(String.format(querySql, teacherName));
			JSONObject jsonObject = new JSONObject();
			if (rs.first()) {
				jsonObject.put(Param.RES_TEACHER_ID, rs.getInt(1));
				jsonObject.put(Param.RES_SEX, rs.getString(2));
				jsonObject.put(Param.RES_PHONE, rs.getString(3));
				jsonObject.put(Param.RES_ACADEMY, rs.getString(4));
				jsonObject.put(Param.RES_POSITION, rs.getString(5));
			}
			res.getWriter().write(jsonObject.toString());
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
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
