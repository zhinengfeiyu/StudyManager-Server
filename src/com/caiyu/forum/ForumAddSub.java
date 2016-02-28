package com.caiyu.forum;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class ForumAddSub extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			
			String discussId = req.getParameter(Param.REQ_DISCUSS_ID);
			String content = req.getParameter(Param.REQ_CONTENT);
			if (discussId != null) {
				String updateSql = "update forum_sub set content='" + content + "'"
								+  " where id=" + discussId + ";";
				stmt.execute(updateSql);
			}
			else {
				int forumMainId = Integer.parseInt(req.getParameter(Param.REQ_TOPIC_ID));
				int userId = Integer.parseInt(req.getParameter(Param.REQ_USER_ID));
				
				String replyDiscussStr = req.getParameter(Param.REQ_REPLY_DISCUSS_ID);
				int replyDiscussId = -1;
				if (replyDiscussStr != null) {
					replyDiscussId = Integer.parseInt(replyDiscussStr);
				}
				
				int replyUserId = -1;
				if (replyDiscussId != -1) {
					String querySql = "select user_id from forum_sub where id=" + replyDiscussId + ";";
					ResultSet rs = stmt.executeQuery(querySql);
					if (rs.first()) {
						replyUserId = rs.getInt(1);
					}
					if (rs != null)
						rs.close();
				}
				String insertSQL = 
						"insert into forum_sub values" +
						"(null, " + forumMainId + ", " + userId + ", " + replyUserId + ", '"
						+ content + "', "
						+ System.currentTimeMillis() 
						+ ");";
				stmt.execute(insertSQL);
			}
			res.getWriter().write("success");	
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
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
