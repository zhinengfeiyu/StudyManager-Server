package com.caiyu.forum;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class ForumAddMain extends HttpServlet {
	
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int subjectId = Integer.parseInt(req.getParameter(Param.REQ_SUBJECT_ID));
			int userId = Integer.parseInt(req.getParameter(Param.REQ_USER_ID));
			String title = req.getParameter(Param.REQ_TITLE);
			String content = req.getParameter(Param.REQ_CONTENT);
			
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String insertSQL = 
					"insert into forum_main values" +
					"(null, " + subjectId + "," + userId + ", '" + title + "',"
					+ (content==null ? "null": "'" + content + "'") + ","
					+ System.currentTimeMillis() 
					+ ");";
			stmt.execute(insertSQL);
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
			res.getWriter().write("success");	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) {
		this.doGet(req, res);
	}
}
