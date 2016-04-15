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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class ForumListSub extends HttpServlet {
	
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int topicId = Integer.parseInt(req.getParameter(Param.REQ_TOPIC_ID));
			
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String querySQL = 
					"select forum_sub.id, content, forum_sub.user_id, u1.user_name, u2.user_name, time" +
					" from forum_sub, user as u1, user as u2" +
					" where forum_main_id=" + topicId + " and forum_sub.user_id=u1.id" +
					" and forum_sub.reply_user_id=u2.id order by time;";
			ResultSet rs = stmt.executeQuery(querySQL);
			if (rs.first()) {
				JSONArray jsonArray = new JSONArray();
				do {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(Param.RES_DISCUSS_ID, rs.getInt(1));
					jsonObject.put(Param.RES_CONTENT, rs.getString(2));
					jsonObject.put(Param.RES_AUTHOR_ID, rs.getInt(3));
					jsonObject.put(Param.RES_AUTHOR, rs.getString(4));
					jsonObject.put(Param.RES_REPLY_TO, rs.getString(5));
					jsonObject.put(Param.RES_TIME, rs.getLong(6));
					jsonArray.put(jsonObject);
				} while (rs.next());
				res.getWriter().write(jsonArray.toString());
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
