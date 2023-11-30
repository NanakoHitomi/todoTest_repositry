package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//ハッシュジェネレーターのインポート文
import utils.HashGenerator;


@WebServlet("/createaccount")
public class CreateController extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost/todo_practice";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String view = "/WEB-INF/views/create.jsp";
        req.getRequestDispatcher(view).forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String profile = req.getParameter("profilel");
        
//        if (username.trim().isEmpty() && !password.trim().isEmpty())
        	
        //これ入れないと動かないよ
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // フォームに入力したパスワードを平文のままにせずハッシュ化する
            String hashedPassword = HashGenerator.generateHash(password);
            String sql = "INSERT INTO users (username, password, profile) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, profile);
                stmt.execute();

                String view = "/WEB-INF/views/index.jsp";
                req.getRequestDispatcher(view).forward(req, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database Connection Failed", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServletException("Generate hash Failed", e);
        }
    }
}