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

import com.caiyu.common.Verifier;
import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class ForumListMain extends HttpServlet {
	
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			String subjectId = req.getParameter(Param.REQ_SUBJECT_ID);
			
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String querySQL = Verifier.isEffectiveStr(subjectId) ?
					"select forum_main.id, forum_main.subject_id, subject.name, title, content, user_name, time"
					+ " from forum_main, user, subject"
					+ " where forum_main.subject_id=" + subjectId
					+ " and forum_main.subject_id=subject.id"
					+ " and forum_main.user_id=user.id;" 
					:
					"select forum_main.id, forum_main.subject_id, subject.name, title, content, user_name, time"
					+ " from forum_main, user, subject"
					+ " where forum_main.subject_id=subject.id"
					+ " and forum_main.user_id=user.id;";
			ResultSet rs = stmt.executeQuery(querySQL);
			if (rs.first()) {
				JSONArray jsonArray = new JSONArray();
				do {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(Param.RES_TOPIC_ID, rs.getInt(1));
					jsonObject.put(Param.RES_SUBJECT_ID, rs.getInt(2));
					jsonObject.put(Param.RES_SUBJECT_NAME, rs.getString(3));
					jsonObject.put(Param.RES_TITLE, rs.getString(4));
					jsonObject.put(Param.RES_CONTENT, rs.getString(5));
					jsonObject.put(Param.RES_AUTHOR, rs.getString(6));
					jsonObject.put(Param.RES_TIME, rs.getLong(7));
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
