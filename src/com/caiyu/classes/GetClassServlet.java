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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class GetClassServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int userId = Integer.parseInt(req.getParameter(Param.REQ_USER_ID));
			
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String queryClassIdSQL = 
					"select class.* from user,class where user.id=" +  userId 
				  + " and user.profession=class.profession_grade;";
			ResultSet rs = stmt.executeQuery(queryClassIdSQL);
			int[] ids = new int[25];
			String queryClassDetailSql = "select subject.name, class_room.name, teacher.name" +
										", class_info.start_week, class_info.end_week" +
										" from class_info, subject, class_room, teacher" +
										" where class_info.id=%1$d and class_info.subject_id=subject.id" +
										" and class_info.class_room=class_room.id" +
										" and class_info.teacher=teacher.id;"; 
			JSONArray jsonArray = new JSONArray();
			if (rs.first()) {
				for (int i=0;i<25;i++) {
					ids[i] = rs.getInt(i+4);
				}
				for (int i=0;i<25;i++) {	
					JSONObject jsonObject = new JSONObject();
					if (ids[i] == -1) {
						jsonObject.put(Param.RES_CLASS_NAME, "");
					}
					else {
						rs = stmt.executeQuery(String.format(queryClassDetailSql, ids[i]));
						rs.first();
						jsonObject.put(Param.RES_CLASS_NAME, rs.getString(1));
						jsonObject.put(Param.RES_CLASS_ROOM, rs.getString(2));
						jsonObject.put(Param.RES_TEACHER, rs.getString(3));
						jsonObject.put(Param.RES_START_WEEK, rs.getInt(4));
						jsonObject.put(Param.RES_END_WEEK, rs.getInt(5));
					}
					jsonArray.put(jsonObject);
				}
			}
			res.getWriter().write(jsonArray.toString());
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
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
