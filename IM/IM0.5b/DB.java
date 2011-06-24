
import java.sql.*;
import java.util.ArrayList;
public class DB {

	private static DB adb=new DB();
	private static Connection con=null;
	private static PreparedStatement pstmt=null;
	private static Statement stmt=null;
	private static ResultSet rs=null;
	
	private DB(){
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");				
		}catch(ClassNotFoundException e){
				e.printStackTrace();			
		}
	}
	
	public static Connection getConnection() throws SQLException{
		String url="jdbc:sqlserver://localhost:1433;databaseName=mydb";
		return DriverManager.getConnection(url,"sa","760079");
	}
	
	public static boolean validateUser(String id,String pw){
		try{
			con=DB.getConnection();
			String sql="select * from userinfo where id='"+id+"' and pw='"+pw+"'";		
			stmt=con.createStatement();
			stmt.execute(sql);
			rs=stmt.getResultSet();
			if(rs.next()){
				return true;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<String> getUserFriends(String id){
		try{
			con=DB.getConnection();
			String sql="select * from friendlist_"+id+" ";		
			stmt=con.createStatement();
			stmt.execute(sql);
			rs=stmt.getResultSet();
			ArrayList<String> al=new ArrayList<String>();
			while(rs.next()){
				al.add(rs.getString("id"));
			}
			return al;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String getUserInfo(String id){
		try{
			con=DB.getConnection();
			String sql="select * from userinfo where id='"+id+"'";		
			stmt=con.createStatement();
			stmt.execute(sql);
			rs=stmt.getResultSet();
			if(rs.next()){
				return rs.getString("id")+"@"+rs.getString("ip")+"@"+rs.getBoolean("online");
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void setUserOnline(String id,String ip){
		try{
			con=DB.getConnection();
			String sql="update userinfo set ip='"+ip+"', online="+1+" where id='"+id+"'";		
			stmt=con.createStatement();
			stmt.execute(sql);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void setUserOffline(String id){
		try{
			con=DB.getConnection();
			String sql="update userinfo set online="+0+" where id='"+id+"'";		
			stmt=con.createStatement();
			stmt.execute(sql);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs){
		try{
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt){
		try{
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void close(Connection con){
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)throws Exception{
		Connection con=DB.getConnection();
		System.out.println("connected");
		System.out.println(DB.getUserInfo("10000"));
	}
}

