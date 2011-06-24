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





//服务器
public class Server {

	ServerSocket ss = null;
	boolean started = false;
	Frame display=null;
	static TextArea ta=null;
	//clients 表示当前在线客户的集合
	static List<Client> clients = new ArrayList<Client>();
	
	public Server() {
		try {
			
			ss = new ServerSocket(8888);
			started = true;
			display=new Frame();
			display.setAlwaysOnTop(true);
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
				ta.append("online: "+clients.size()+"  threads: "+Thread.activeCount()+"\n");
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
	public static void someoneLogin(String id,String ip){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//谁接受到？login ?mainui
						clients.get(j).getDos().writeUTF("afriendlogin#"+id+"#"+ip);
						ta.append("afriendlogin#"+id+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	//有人登出时,查找该用户的好友信息（id），然后在clients中查找看好友是否在线，在就发送信息通知他
	public static void someoneLogout(String id){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//谁接受到？login ?mainui
						clients.get(j).getDos().writeUTF("afriendlogout#"+id);
						ta.append("afriendlogout#"+id+"\n");
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
		
		//当请求为登陆时，用validateUser方法验证
		//为真时 把该客户添加至在线客户集合中，并执行相应的登陆操作
			//登陆成功时服务器的操作
			//修改数据库中该用户的 在线 信息
			//从数据库中取出该用户好友的信息
			//将该用户好友的信息打包成一个字符串，发送至该客户端
			//由服务器通知好友 用户已上线 
		//否时  返回一个登陆失败的信息
		public void login(String uid,String pw){
			if(DB.validateUser(uid,pw)){
				this.id=uid;
				clients.add(this);
				DB.setUserOnline(id,ip);
				ArrayList<String> al=DB.getUserFriends(id);
				StringBuffer friendsInfo=new StringBuffer("friendsinfo"+"#");
				for(int i=0;i<al.size();i++){
					friendsInfo.append(DB.getUserInfo(al.get(i))+"#");
				}
				try {
					dos.writeUTF(friendsInfo.toString());
					ta.append(friendsInfo.toString()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Server.someoneLogin(id,ip);
			}else{
				try {
					dos.writeUTF("false"+"#");
					ta.append("false"+"#"+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.bConnected=false;
			}	
		}
		//注册 验证用户是否存在 存在就返回 false
			//	不存在 往数据库userinfo 插入数据,建立 该注册用户的好友表friendlist_id,返回 true
		//结束该线程
		public void register(String id,String pw){
			if(DB.validateUsername(id)){
				try {
					dos.writeUTF("regfalse"+"#");
				} catch (IOException e) {
					e.printStackTrace();
				}
				ta.append("regfalse"+"#"+"\n");
				this.bConnected=false;
			}else{
				DB.insertUserinfo(id,pw);
				DB.createFriendlist(id);
				try {
					dos.writeUTF("regdone"+"#");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ta.append("regdone"+"#"+"\n");
			}
			bConnected=false;
		}
		//搜索在线用户 把信息写回客户端
		public void searchOnline(String id){
			try {
				String message=DB.searchOnline(id);
				dos.writeUTF(message);
				ta.append(message+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//添加好友 接受客户端的添加好友请求信息(hid hip fid fip),
			//往hid的好友数据表中插入fid的信息
			//往fid的好友数据表中插入hid的信息
			//分别给hid fid 发信息 添加好友的信息
		public void addFriend(String hid,String hip,String fid,String fip){
			DB.addFriend(hid,fid);
			DB.addFriend(fid,hid);
			for(int i=0;i<clients.size();i++){
				if(clients.get(i).id.equals(hid)){
					try {
						clients.get(i).dos.writeUTF("addFriend#"+fid+"#"+fip);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(clients.get(i).id.equals(fid)){
					try {
						clients.get(i).dos.writeUTF("addFriend#"+hid+"#"+hip);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		//分析请求的命令
		public void analyseRequest(String request){
			ta.append(request+"\n");
			String[] command=request.split("@");
			if(command[0].equals("login")){
				login(command[1],command[2]);
			}else if(command[0].equals("register")){
				register(command[1],command[2]);
			}else if(command[0].equals("searchOnline")){
				searchOnline(command[1]);
			}else if(command[0].equals("addFriend")){
				addFriend(command[1],command[2],command[3],command[4]);
			}
		}
		//有人断开连接时的操作
		public void out(){
			ta.append("Client closed!"+"\n");
			//结束client线程
			bConnected=false;
			//从在线用户中去除
			clients.remove(this);
			//设置数据库数据
			if(id!=null){
				DB.setUserOffline(id);
				//发送消息给好友
				Server.someoneLogout(id);
			}
			ta.append("online: "+clients.size()+"  threads: "+Thread.activeCount()+"\n");
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
					this.analyseRequest(request);
				}
			} catch (EOFException e) {	
				out();
			} catch (IOException e) {
				out();
			} catch (Exception e) {
				out();
			} finally {
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null)  {
						s.close();
						s = null;
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
