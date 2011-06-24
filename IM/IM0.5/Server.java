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
	//clients ��ʾ��ǰ���߿ͻ��ļ���
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
			ta.append("�˿�ʹ����....");
			ta.append("��ص���س����������з�������");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			while(started) {
				//����ʽ�ȴ����ӣ����пͻ������Ӻ󣬲���һ�����������µ��̣߳�
				//�ɴ��̴߳���ÿͻ��˵����󣬼���ÿͻ��˵�ͨ��
				//���̵߳ȴ��ص�while�Ŀ�ʼ�ȴ��µ�����
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
	//���˵���ʱ,���Ҹ��û��ĺ�����Ϣ��id����Ȼ����clients�в��ҿ������Ƿ����ߣ��ھͷ�����Ϣ֪ͨ��
	public static void someoneLogin(String id){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//˭���ܵ���login ?mainui
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
						//˭���ܵ���login ?mainui
						clients.get(j).getDos().writeUTF("afriendlogout#"+id);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	//��ʾÿһ���ͻ������ӵ��࣬���а�����������Ϣ
		//�ͻ��˵�ip
		//��ÿͻ�������
		//�ͻ������ݿ��id
		//�Ƿ�����
	//���߳���Ҫ������������
		//�ͻ��˵�¼ʱ��֤�ͻ���id��pw����������Ӧ�ĵ�½��Ϣ�����ؿͻ��ĺ�����Ϣ��������ǰ�Ƿ����ߣ�
		//���пͻ���½���ǳ�������ʱ���޸����ݿ��ڿͻ��Ƿ����ߵ����ݣ���������Ӧ����Ϣ���ÿͻ��ĺ���
		//���������ܣ����������û�
		//��������ʱ ��Ҫ���ݿͻ��˷�������Ϣ����������Ӧ�¼�
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
		//��½�ɹ�ʱ�������Ĳ���
			//�޸����ݿ��и��û��� ���� ��Ϣ
			//�����ݿ���ȡ�����û����ѵ���Ϣ
			//�����û����ѵ���Ϣ�����һ���ַ������������ÿͻ���
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
				//������Ϊ��½ʱ����validateUser������֤
					//Ϊ��ʱ �Ѹÿͻ���������߿ͻ������У���ִ����Ӧ�ĵ�½����
					//��ʱ  ����һ����½ʧ�ܵ���Ϣ
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
		
		//run������˼���� ��ͣ�Ĵӿͻ��˶����󣬿ͻ��˵��������� ��������@������Ϣ@������Ϣ@...  ����ʽ������
			//�߳��յ����� analyseRequest �������з��� ��ִ����Ӧ�Ĳ���
			//ͬʱ ��Ϊ��ͣ�ı�����ͻ��˵�����  ���ͻ��˷������� �� ǿ�ƹر� �ϵ� �����ʱ�������exception
			// ��ʱ������׽�����쳣�����ɴ˵�֪�ͻ��˷������⣬�����������˳�����Ϣ�� 
			// ��ʱ��catch��ִ�пͻ��˳������ݿ���Ӧ�Ĳ�����������Ϣ���ÿͻ��ĺ���
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
