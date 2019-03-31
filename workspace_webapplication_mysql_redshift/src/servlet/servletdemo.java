package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*//import com.mysql.*;
import com.amazon.redshift.jdbc.Driver;*/

/**
 * Servlet implementation class servletdemo
 */
@WebServlet("/servletdemo")
public class servletdemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servletdemo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/json; charset=utf-8");
		String sql=request.getParameter("sql");
		String module=request.getParameter("module");
		PrintWriter out = response.getWriter();
				
		String url="jdbc:redshift://redshift-cluster-2.cdicylrkj8z9.us-east-2.redshift.amazonaws.com:5439/dev?useSSL=false&serverTimezone=GMT";
		String username="zhoutian1";
		String password="ZhouTian1";	
		
		if(module.equals("mysql")){
			
			//mysql connection url id password
			url="jdbc:mysql://guokai.cj4kfmcqgzrm.us-east-1.rds.amazonaws.com:3306/mydb?useSSL=false&serverTimezone=GMT";
            username="guokai123";
            password="guokai123";
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println("Can't find mysql jdbc driver.");
                e.printStackTrace();
            }
		}
		else {
			//redshift connection url id password
			url="jdbc:redshift://redshift-cluster-2.cdicylrkj8z9.us-east-2.redshift.amazonaws.com:5439/dev?useSSL=false&serverTimezone=GMT";
			username="zhoutian1";
			password="ZhouTian1";
			try {
	            Class.forName("com.amazon.redshift.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            System.out.println("Can't find redshift jdbc driver.");
	            e.printStackTrace();
	        }		
		}
        
        Connection conn;
        try {
            conn = (Connection) DriverManager.getConnection(url, username,password);
            Statement stmt = (Statement) conn.createStatement(); 
            System.out.print("Successful database connection.");
            ResultSet rs = stmt.executeQuery(sql);

           int columns = rs.getMetaData().getColumnCount();
           
           JSONArray db = new JSONArray();

           ResultSetMetaData metaData = rs.getMetaData();   
        
           while(rs.next())
           {
        	   JSONObject jsonObj = new JSONObject();
        	   for(int i=1;i<=columns;i++){
               
                String columnName =metaData.getColumnLabel(i);   
          	  
                String value = rs.getString(columnName);
               
                try {
					jsonObj.put(columnName, value);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}           
               }
        	   db.put(jsonObj);
     
           }
           //System.out.println(db); 
           out=response.getWriter();
           out.println(db);
           rs.close();
     
            stmt.close();
            conn.close();
        } catch (SQLException e){
            System.out.println("fail to connect the database!");
            e.printStackTrace();
        }
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
