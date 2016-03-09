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

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class RegisterServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("application/json;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		
		String userName = req.getParameter(Param.REQ_USER_NAME);
		String studyNo = req.getParameter(Param.REQ_STUDY_NO);
		String psw = req.getParameter(Param.REQ_PSW);
		
		String outputStr = "";
		try {
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			
			//����û����Ƿ����
			String queryUserNameSQL = "select * from user where user_name='%1$s';";
			ResultSet rs = stmt.executeQuery(String.format(queryUserNameSQL, userName));
			if (rs.first()) {
				res.getWriter().write("���û����ѱ�ע��");
			}
			else {
				//���ѧ���Ƿ����
				String queryStudyNoSQL = "select * from user where study_no='%1$s';";
				rs = stmt.executeQuery(String.format(queryStudyNoSQL, studyNo));
				if (rs.first()) {
					outputStr = "��ѧ���ѱ�ע��";
				}
				else {
					//���רҵ���Ƿ���ȷ
					String professionNo = studyNo.substring(0, 6);	//רҵ����ȡѧ�ŵ�ǰ6λ
					String queryProNoSQL = "select * from user where profession_no='%1$s';";
					rs = stmt.executeQuery(String.format(queryProNoSQL, professionNo));
					if (!rs.first()) {
						outputStr = "�Ҳ���ѧ�Ŷ�Ӧ��רҵ";
					}
					else {
						String classNo = studyNo.substring(0, 8);
						int grade = Integer.parseInt(studyNo.substring(4, 6));
						int number = Integer.parseInt(studyNo.substring(8, 10));
						String insertSql = "insert into user values " +
								"(null,'%1$s','%2$s', '', '', '%3$s', '%4$s', '%5$s', '%6$d', '%7$d');";
						stmt.execute(String.format(insertSql, userName, psw, 
										studyNo, professionNo, classNo, grade, number));
						outputStr = Param.SUCCESS;
					}
				}
			}
			if (rs!=null) rs.close();
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
			res.getWriter().write(outputStr);
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
