import java.awt.Button;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;






public class Server {

	ServerSocket ss = null;
	boolean started = false;
	Frame display=null;
	TextArea ta=null;
	//clients 表示当前在线客户的集合
	static List<Client> clients = new ArrayList<Client>();
	
	public Server() {
		try {
			
			ss = new ServerSocket(8888);
			started = true;
			display=new Frame();
			ta=new TextArea();
			display.add(ta);
			display.setTitle("ServerOperating..."+InetAddress.getLocalHost().getHostAddress());
			display.setBounds(700,200,400,300);
			display.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
				}
			});
			display.setVisible(true);
		} catch (BindException e) {
			ta.append("端口使用中....");
			ta.append("请关掉相关程序并重新运行服务器！");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			while(started) {
				//阻塞式等待连接，当有客户端连接后，产生一包含此连接新的线程，
				//由此线程处理该客户端的请求，及与该客户端的通信
				//主线程等待回到while的开始等待新的连接
				Socket s = ss.accept();
				Client c = new Client(s);
				ta.append("a client connected!"+"\n");
				new Thread(c).start();
				//clients.add(c);
				//dis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//有人登入时,查找该用户的好友信息（id），然后在clients中查找看好友是否在线，在就发送信息通知他
	public static void someoneLogin(String id){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//谁接受到？login ?mainui
						clients.get(j).getDos().writeUTF("afriendlogin#"+id);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void someoneLogout(String id){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//谁接受到？login ?mainui
						clients.get(j).getDos().writeUTF("afriendlogout#"+id);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	//表示每一个客户端连接的类，其中包含了以下信息
		//客户端的ip
		//与该客户的连接
		//客户在数据库的id
		//是否连接
	//该线程主要处理的事务包括
		//客户端登录时验证客户的id，pw，及返回相应的登陆信息，返回客户的好友信息（包括当前是否在线）
		//当有客户登陆及登出，掉线时，修改数据库内客户是否在线的数据，并发送相应的信息给该客户的好友
		//待开发功能，比如搜索用户
		//处理事务时 主要根据客户端发来的信息驱动处理相应事件
	class Client implements Runnable {
		private String ip=null;
		private Socket s;
		private String id;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		
		public Client(Socket s) {
			this.s = s;
			try {
				ip=s.getInetAddress().getHostAddress();
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//登陆成功时服务器的操作
			//修改数据库中该用户的 在线 信息
			//从数据库中取出该用户好友的信息
			//将该用户好友的信息打包成一个字符串，发送至该客户端
		public void login(String id,String ip){
			DB.setUserOnline(id,ip);
			ArrayList<String> al=DB.getUserFriends(id);
			StringBuffer friendsInfo=new StringBuffer("friendsinfo"+"#");
			for(int i=0;i<al.size();i++){
				friendsInfo.append(DB.getUserInfo(al.get(i))+"#");
			}
			try {
				dos.writeUTF(friendsInfo.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		


		public void analyseRequest(String request){
			String[] command=request.split("@");
			if(command[0].equals("login")){
				//当请求为登陆时，用validateUser方法验证
					//为真时 把该客户添加至在线客户集合中，并执行相应的登陆操作
					//否时  返回一个登陆失败的信息
				if(DB.validateUser(command[1],command[2])){
					this.id=command[1];
					clients.add(this);
					login(id,ip);
					Server.someoneLogin(id);
				}else{
					try {
						dos.writeUTF("false"+"#");
					} catch (IOException e) {
						e.printStackTrace();
					}
					this.bConnected=false;
				}
			}
		}
		
		//run方法的思想是 不停的从客户端读请求，客户端的请求是以 请求类型@请求信息@请求信息@...  的形式发来的
			//线程收到后用 analyseRequest 方法进行分析 后，执行相应的操作
			//同时 因为不停的保持与客户端的连接  当客户端发生意外 如 强制关闭 断电 等情况时，会产生exception
			// 此时可以扑捉到此异常，并由此得知客户端发生意外，来不及发送退出的消息， 
			// 此时由catch块执行客户退出在数据库相应的操作，并发消息给该客户的好友
		public void run() {
			try {
				while(bConnected) {
					String request = dis.readUTF();
					ta.append(request+"\n");
					this.analyseRequest(request);
				}
			} catch (EOFException e) {
				ta.append("Client closed!"+"\n");
				DB.setUserOffline(id);
				clients.remove(this);
				Server.someoneLogout(id);
			} catch (IOException e) {
				ta.append("Client closed!"+"\n");
				DB.setUserOffline(id);
				clients.remove(this);
				Server.someoneLogout(id);
			} catch (Exception e) {
				ta.append("UNknowclosed!"+"\n");
				DB.setUserOffline(id);
				clients.remove(this);
				Server.someoneLogout(id);
			} finally {
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null)  {
						s.close();
						//s = null;
					}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
			}
		}
		public String getIp() {
			return ip;
		}
		public Socket getS() {
			return s;
		}
		public String getId() {
			return id;
		}
		public DataInputStream getDis() {
			return dis;
		}
		public DataOutputStream getDos() {
			return dos;
		}	
	}	

	public static void main(String[] args) {
		new Server();
	}

}
