
import java.sql.*;
import java.util.ArrayList;
//数据库操作类
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
		return DriverManager.getConnection(url,"sa","");
	}
	//验证客户的id pw
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
	//验证用户id是否存在
	public static boolean validateUsername(String id){
		try{
			con=DB.getConnection();
			String sql="select * from userinfo where id='"+id+"'";		
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
	//获取用户好友信息
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
	//获取用户 id ip 是否在线
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
	//搜索在线用户（不包括id）
	public static String searchOnline(String id){
		try{
			con=DB.getConnection();
			String sql="select id,ip from userinfo where id <>'"+id+"' and online=1" ;		
			stmt=con.createStatement();
			stmt.execute(sql);
			rs=stmt.getResultSet();
			StringBuffer onlineUser=new StringBuffer("onlineUser#");
			while(rs.next()){
				onlineUser.append(rs.getString("id")+"@"+rs.getString("ip")+"#");
			}
			return onlineUser.toString();
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
	//设置该用户上线 并保存ip
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
	//设置该用户下线
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
	//往hid的好友表插入fid
	public static void addFriend(String hid,String fid){
		try{
			con=DB.getConnection();
			String sql="insert into friendlist_"+hid+" values('"+fid+"')";
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
	//插入新注册用户信息
	public static void insertUserinfo(String id,String pw){
		try{
			con=DB.getConnection();
			String sql="insert into userinfo values('"+id+"','"+pw+"','127.0.0.1',0)";		
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
	//创造该用户的好友列表
	public static void createFriendlist(String id){
		try{
			con=DB.getConnection();
			String sql="create table friendlist_"+id+"(	id varchar(20) primary key )";		
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
System.out.println("connected...");
		Connection con=DB.getConnection();
		System.out.println("connected");
		System.out.println(DB.getUserInfo("10000"));
	}
}

