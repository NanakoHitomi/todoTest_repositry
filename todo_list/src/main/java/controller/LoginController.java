package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HashGenerator;

@WebServlet("/login")
//DBに接続するためのURL
public class LoginController extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost/todo_practice";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
//ここまで

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	//String view = でviewを定義
        String view = "/WEB-INF/views/index.jsp";
        //ここで定義したviewをgetrequestDispatcher()に入れる
        req.getRequestDispatcher(view).forward(req, res);
        //getRequestDispatcher : servletからjspを表示させるためのインターフェイス
        //sample : .getRequestDispatcher("./WEB-INF/views/index.jsp").forward(request, response);
        //forward(request, response) : RequestDipatcherインターフェイスを使うことで使用可能
        //forwardを使うことで指定のjspに飛ばすことができる
    }
//form methodがpostで来ているからここから始める
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	//ユーザーから送信されたユーザーネームとパスワードを取得
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        //ユーザーネームかつパスワードが空白でないとき行う
        //trimは空白認識
        //タブとかの空白かつ入力がされてない時は一番したのelseにいって　loginに
        if (!username.trim().isEmpty() && !password.trim().isEmpty()){
        
        	//これいないとjdbcに繋がらない
            try{
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        	try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        	//DBに対する処理
            // DBに登録されているパスワードはハッシュ化されているためハッシュ化した値で照合する
            String hashedPassword = HashGenerator.generateHash(password);
           
            //実行するSQL
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            //?を二つ使っているから、パラメータを二つ指定する必要がある
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                
                //SQLの実行
                ResultSet rs = stmt.executeQuery();
                
                //もしユーザーネームとパスワードが一致しているユーザーが存在した時
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String profile = rs.getString("profile");
                    // サーバーの保持するセッションを取得する
                    HttpSession session = req.getSession();
                    // キーと値のペアでセッションに登録する
                    session.setAttribute("id", id);
                    session.setAttribute("username", username);
                    session.setAttribute("profile", profile);
                    //照合取れたらwelcomeへ
                    res.sendRedirect("welcome");
                    
                    //ログインに失敗したらindex.jspに返される
                } else {
                    String view = "/WEB-INF/views/index.jsp";
                    req.getRequestDispatcher(view).forward(req, res);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database Connection Failed", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServletException("Generate hash Failed", e);
        }
        //空白の時は以下の処理を行う、ログイン画面にリダイレクト
        } else {
        	 res.sendRedirect("login");
        }
    }
}