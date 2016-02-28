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
		try {
			String userName = req.getParameter(Param.REQ_USER_NAME);
			String psw = req.getParameter(Param.REQ_PSW);
			if (userName == null || psw == null) {
				res.getWriter().write("参数个数错误");
			}
			else {
				Class.forName(DB.DRIVER);
				Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
				Statement stmt = conn.createStatement();
				String querySQL = "select * from user where user_name='" + userName + "';";
				ResultSet rs = stmt.executeQuery(querySQL);
				if (rs.first()) {
					res.getWriter().write("该用户名已被注册");
				}
				else {
					String insertSql = "insert into user values (null,'" +
								userName + "','" + psw + "', null, null, null, null, null, null);";
					stmt.execute(insertSql);
					res.getWriter().write("success");
				}
				if (rs!=null) rs.close();
				if (stmt!=null) stmt.close();
				if (conn!=null) conn.close();
			}
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
