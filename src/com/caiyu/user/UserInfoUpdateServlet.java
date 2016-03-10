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

import com.caiyu.common.Verifier;
import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class UserInfoUpdateServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			String outputStr = "";
			int userId = Integer.parseInt(req.getParameter(Param.REQ_USER_ID));
			String userName = req.getParameter(Param.REQ_USER_NAME);
			String psw = req.getParameter(Param.REQ_PSW);
			String realName = req.getParameter(Param.REQ_REAL_NAME);
			String sex = req.getParameter(Param.REQ_SEX);
			Class.forName(DB.DRIVER);
			Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			if (Verifier.isEffectiveStr(userName)) {
				String queryUserNameSql = "select id from user where user_name='%1$s' and id!=%2$d;";
				rs = stmt.executeQuery(String.format(queryUserNameSql, userName, userId));
				if (rs.first()) {
					outputStr = "¸ÃêÇ³ÆÒÑ±»×¢²á";
				}
				else {
					String updateSql = "update user set user_name='%1$s' where id=%2$d;";
					stmt.execute(String.format(updateSql, userName, userId));
					outputStr = Param.SUCCESS;
				}
			}
			else if (Verifier.isEffectiveStr(psw)) {
				String updateSql = "update user set password='%1$s' where id=%2$d;";
				stmt.execute(String.format(updateSql, psw, userId));
				outputStr = Param.SUCCESS;
			}
			else if (Verifier.isEffectiveStr(realName)) {
				String updateSql = "update user set name='%1$s' where id=%2$d;";
				stmt.execute(String.format(updateSql, realName, userId));
				outputStr = Param.SUCCESS;
			}
			else if (Verifier.isEffectiveStr(sex)) {
				String updateSql = "update user set sex='%1$s' where id=%2$d;";
				stmt.execute(String.format(updateSql, sex, userId));
				outputStr = Param.SUCCESS;
			}
			else {
				outputStr = Param.FAILURE;
			}
			res.getWriter().write(outputStr);
			if (rs != null) rs.close();
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
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
