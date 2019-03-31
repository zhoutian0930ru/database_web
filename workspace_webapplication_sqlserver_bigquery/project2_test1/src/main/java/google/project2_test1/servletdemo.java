package google.project2_test1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryResponse;
import com.google.cloud.bigquery.TableResult;

/**
 * Servlet implementation class servletdemo
 */
@WebServlet("/servletdemo")
public class servletdemo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public servletdemo() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		response.setContentType("text/json; charset=utf-8");
		String sql=request.getParameter("sql");
		String module=request.getParameter("module");
		PrintWriter out = response.getWriter();
		
		//
		if(module.equals("sql_server")){
			try {
		          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		      } catch (ClassNotFoundException e) {
		          // TODO Auto-generated catch block
		          System.out.println("Can't find sqlserver jdbc driver.");
		          e.printStackTrace();
		      }	
			String url="jdbc:sqlserver://localhost:1433;DatabaseName=test";
			String username="sa";
			String password="zhoutian";	
			
			Connection conn;
	        try {
	            conn = (Connection) DriverManager.getConnection(url, username,password);
	            Statement stmt = (Statement) conn.createStatement(); 
	            System.out.println("Successful database connection.");
	            boolean sql_type=stmt.execute(sql);
	            //SELECT
	            if(sql_type==true) {
	            	ResultSet rs = stmt.getResultSet();
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
	                System.out.println("select..."); 
	                out=response.getWriter();
	                out.println(db);
	                System.out.println(db); 
	                rs.close();                  
	                stmt.close();
	                conn.close();  
	            //INSERT,DELETE...
	            }else {
	            	int count = stmt.getUpdateCount();
	            	JSONArray db1 = new JSONArray();
	            	JSONObject jsonObj = new JSONObject();
	            	String name="operation_success";
	            	String value1="("+count+" rows affected)";
	            	try {
	  					jsonObj.put(name, value1);
	  				     } catch (JSONException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}
	            	db1.put(jsonObj);
	            	
	            	System.out.println("insert..."); 
	            	
	            	out=response.getWriter();
	            	out.println(db1);
	            	stmt.close();
	                conn.close();  
	            }
	            
	       
	           
	        } catch (SQLException e){
	            System.out.println("fail to connect the database!");
	            e.printStackTrace();
	        }
		}
		//bigquery
		else {
			App test=new App();
			JSONArray resultjson=new JSONArray();
			try {
				resultjson = test.main(sql);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
//		    JSONArray db1 = new JSONArray();
//	    	JSONObject jsonObj = new JSONObject();
//	    	String name="operation_success";
//	    	String value1=resultset;
//	    	try {
//					jsonObj.put(name, value1);
//				     } catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	    	db1.put(jsonObj);
	    	
	    	System.out.println("result:"); 
	    	
	    	out=response.getWriter();
	    	out.println(resultjson);
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