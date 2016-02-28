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

import org.json.JSONException;
import org.json.JSONObject;

import com.caiyu.constants.DB;
import com.caiyu.constants.Param;

public class LoginServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) {
		res.setContentType("application/json;charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		try {
			JSONObject jsonObject = new JSONObject();
			String userName = req.getParameter(Param.REQ_USER_NAME);
			String psw = req.getParameter(Param.REQ_PSW);
			if (userName == null || psw == null) {
				jsonObject.put(Param.RES_LOGIN_RESULT, "参数个数错误");
			}
			else {
				Class.forName(DB.DRIVER);
				Connection conn = DriverManager.getConnection(DB.URL, DB.USER, DB.PSW);
				Statement stmt = conn.createStatement();
				String querySQL = "select id from user where user_name='" + userName + "' and password='" + psw + "';";
				ResultSet rs = stmt.executeQuery(querySQL);
				if (rs.first()) {
					jsonObject.put(Param.RES_LOGIN_RESULT, "success");
					jsonObject.put(Param.RES_USER_ID, rs.getInt(1));
				}
				else {
					jsonObject.put(Param.RES_LOGIN_RESULT, "用户名或密码错误");
				}
				if (rs!=null) rs.close();
				if (stmt!=null) stmt.close();
				if (conn!=null) conn.close();
			}
			res.getWriter().write(jsonObject.toString());	
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

