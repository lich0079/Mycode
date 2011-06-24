import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;



//主界面 功能 
	//显示 在线 好友 离线好友
	//接受服务器发送来的 好友 上线 下线的信息 ，并重新布置界面
//以下未实现 
	//放弃----主界面 也是 一服务器 ，可以不断接收 好友的 连接，放入一单独的线程中，由此线程管理与好友的通话
	//好友间通话采用UDP 每个用户都有个接收端口 发送端口 
	//接收端口接收好友信息  该信息打包的第一个字段说明他由谁发来,然后进行相应处理--设置该用户按钮变色，添加信息至Talkframe
class Mainui extends Frame{
	
	Socket connectServer=null;
	ArrayList<Friend> friends=null;
	String id;
	String ip;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;
	DatagramSocket dslisten=null;
	DatagramPacket dp=null;
	Panel p1=null;
	Searchfriend sf;
	
	Mainui(String id,String ip,Socket connectServer,ArrayList<Friend> friends){
		this.id=id;
		this.ip=ip;
		this.connectServer=connectServer;
		this.friends=friends;
		try {
			dslisten=new DatagramSocket(9999);
			dos = new DataOutputStream(connectServer.getOutputStream());
			dis = new DataInputStream(connectServer.getInputStream());
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		bConnected = true;
		this.setTitle(id);
		p1=new Panel();
		p1.setLayout(new GridLayout(20,1));
		//setLayout(new FlowLayout());
		this.sortButton();
		add(p1, BorderLayout.CENTER);
		Button search=new Button("搜索");
		search.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				search();
			}	
		});
		add(search, BorderLayout.SOUTH);
		setBounds(300,300,100,400);
		this.setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
					System.exit(0);							
				}
			}
		);
		setVisible(true);
		sf=new Searchfriend(this);
		this.setAlwaysOnTop(true);
		//分别起2个接受线程 接受 服务器  接受好友
		new Thread(new RecvServer()).start();
		new Thread(new RecvFriend()).start();
	}
	//发送搜索信息
	public void search(){
		try {
			dos.writeUTF("searchOnline@"+id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//布置按钮的排布
	public void sortButton(){
		p1.removeAll();
		for(int i=0;i<friends.size();i++){
			if(friends.get(i).isOnline()){
				friends.get(i).getB().setEnabled(true);
				p1.add(friends.get(i).getB());
			}
		}
		for(int i=0;i<friends.size();i++){
			if(!friends.get(i).isOnline()){
				friends.get(i).getB().setBackground(new Color(215,187,129));
				friends.get(i).getB().setEnabled(false);
				p1.add(friends.get(i).getB());
			}
		}
		this.show();
		/*this.setVisible(false);
		this.setVisible(true);*/
	}
	
	private class RecvFriend implements Runnable {
		
		//收到好友消息时的操作 分解消息成 好友id， info  并显示在相应id的talk上
		public void receiveFriendMessage(){
			byte[] buf=new byte[1024];
			dp=new DatagramPacket(buf,buf.length);
			try {
				dslisten.receive(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message=new String(buf,0,dp.getLength());
			String[] command=message.split("#");
			String id=command[0];
			String info=command[1];
			for(int i=0;i<friends.size();i++){
				if(id.equals(friends.get(i).getId())){
					friends.get(i).getT().getDisplay().append(info);
					friends.get(i).getB().setBackground(Color.RED);
					break;
				}
			}
		}
		
		public void run() {
			while(bConnected){
				receiveFriendMessage();
			}	
		}
		
	}
	//接受服务器发来的好友 登进 登出消息 并处理
	private class RecvServer implements Runnable {
		//好友上线
		public void afriendLogin(String id,String ip){
			for(int i=0;i<friends.size();i++){
				if(id.equals(friends.get(i).getId())){
					friends.get(i).setOnline(true);
					friends.get(i).setIp(ip);
					friends.get(i).getB().setBackground(Color.ORANGE);
					break;
				}
			}
			sortButton();
		}
		//好友下线
		public void afriendLogout(String id){
			for(int i=0;i<friends.size();i++){
				if(id.equals(friends.get(i).getId())){
					friends.get(i).setOnline(false);
					break;
				}
			}
			sortButton();
		}
		//添加好友
		public void addFriend(String fid,String ip){
			friends.add(new Friend(id,fid,ip,true));
			sortButton();
		}
		
		//分析服务器的命令类型
		public void analyseResult(String message){
			String[] command=message.split("#");
			if(command[0].equals("afriendlogin")){
				this.afriendLogin(command[1],command[2]);
			}else if(command[0].equals("afriendlogout")){
				this.afriendLogout(command[1]);
			}else if(command[0].equals("onlineUser")){
				sf.message=message;
				sf.display();
			}else if(command[0].equals("addFriend")){
				addFriend(command[1],command[2]);
			}
		}
		
		//不停的接受命令
		public void run() {
			try {
				while(bConnected) {
					String message = dis.readUTF();
					System.out.println(message);
					this.analyseResult(message);
				}
			} catch (SocketException e) {
				System.out.println("退出了，bye!");
			} catch (EOFException e) {
				System.out.println("推出了，bye - bye!");
			} catch (IOException e) {
				e.printStackTrace();
			} 	
		}
	}
	
	public static void main(String[] args){
		//new Mainui();
	}
}
