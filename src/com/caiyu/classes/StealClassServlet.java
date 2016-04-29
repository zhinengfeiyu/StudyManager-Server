package com.caiyu.classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class StealClassServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int subjectId = Integer.parseInt(req.getParameter(Param.REQ_SUBJECT_ID));
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String queryClassInfoSQL = 
					"select class_info.id, subject.name, " +
					"class_room.name, teacher.name, class_info.start_week, class_info.end_week" +
					" from class_info, subject, class_room, teacher" +
					" where class_info.subject_id=" + subjectId + 
					" and class_info.subject_id=subject.id" +
					" and class_info.class_room=class_room.id" +
					" and class_info.teacher=teacher.id";				  
			ResultSet rs = stmt.executeQuery(queryClassInfoSQL);
			List<ClassInfoBean> classInfoList = new ArrayList<ClassInfoBean>();
			if (rs.first()) {
				ClassInfoBean bean = new ClassInfoBean();
				bean.classInfoId = rs.getInt(1);
				bean.subjectName = rs.getString(2);
				bean.classRoom = rs.getString(3);
				bean.teacher = rs.getString(4);
				bean.startWeek = rs.getInt(5);
				bean.endWeek = rs.getInt(6);
				classInfoList.add(bean);
			}
			String queryClassSql = "select * from class";
			rs = stmt.executeQuery(queryClassSql);
			JSONArray jsonArray = new JSONArray();
			if (rs.first()) {
				int firstClassColumn = rs.findColumn("class11");
				do {
					for (int i = firstClassColumn; i < firstClassColumn + 24; i++) {
						for (ClassInfoBean classInfoBean : classInfoList) {
							if (rs.getInt(i) == classInfoBean.classInfoId) {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put(Param.RES_PROFESSION, rs.getString("profession_name"));
								jsonObject.put(Param.RES_CLASS_ORDER, i - firstClassColumn + 1);
								jsonObject.put(Param.RES_SUBJECT_NAME, classInfoBean.subjectName);
								jsonObject.put(Param.RES_CLASS_ROOM, classInfoBean.classRoom);
								jsonObject.put(Param.RES_TEACHER, classInfoBean.teacher);
								jsonObject.put(Param.RES_START_WEEK, classInfoBean.startWeek);
								jsonObject.put(Param.RES_END_WEEK, classInfoBean.endWeek);
								jsonArray.put(jsonObject);
							}
							break;
						}
					}
				} while(rs.next());
			}
			res.getWriter().write(jsonArray.toString());
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
	
	static class ClassInfoBean {
		int classInfoId;
		String subjectName;
		String classRoom;
		String teacher;
		int startWeek;
		int endWeek;
	}
}
