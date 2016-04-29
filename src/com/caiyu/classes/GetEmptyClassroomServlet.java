package com.caiyu.classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class GetEmptyClassroomServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			int startClassOrder = Integer.parseInt(req.getParameter(Param.REQ_START_CLASS_ORDER));
			int endClassOrder = Integer.parseInt(req.getParameter(Param.REQ_END_CLASS_ORDER));
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			String queryAllClassSql = "select * from class;";
			ResultSet rs = stmt.executeQuery(queryAllClassSql);
			Set<Integer> classList = new HashSet<Integer>();
			if (rs.first()) {
				do {
					for (int i=startClassOrder+3;i<=endClassOrder+3;i++) {
						int classInfoId = rs.getInt(i);
						if (classInfoId != -1)
							classList.add(classInfoId);
					}
				} while(rs.next());
			}
			StringBuilder sb = new StringBuilder();
			if (!classList.isEmpty()) {
				sb.append(" class_info.id not in (");
				for (int classInfoId : classList) {
					sb.append(classInfoId);
					sb.append(',');
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append(")");
			}
			String notInSql = sb.toString();
			String queryResultSql = "select class_room.name from class_info,class_room where" +
					notInSql + " and class_info.class_room=class_room.id;";
			rs = stmt.executeQuery(queryResultSql);
			JSONArray jsonArray = new JSONArray();
			if (rs.first()) {
				do {
					jsonArray.put(rs.getString(1));
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
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) {
		this.doGet(req, res);
	}
}
