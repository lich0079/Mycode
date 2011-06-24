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





//������
public class Server {

	ServerSocket ss = null;
	boolean started = false;
	Frame display=null;
	static TextArea ta=null;
	//clients ��ʾ��ǰ���߿ͻ��ļ���
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
	//���˵���ʱ,���Ҹ��û��ĺ�����Ϣ��id����Ȼ����clients�в��ҿ������Ƿ����ߣ��ھͷ�����Ϣ֪ͨ��
	public static void someoneLogin(String id,String ip){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//˭���ܵ���login ?mainui
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
	//���˵ǳ�ʱ,���Ҹ��û��ĺ�����Ϣ��id����Ȼ����clients�в��ҿ������Ƿ����ߣ��ھͷ�����Ϣ֪ͨ��
	public static void someoneLogout(String id){
		ArrayList<String> fids=DB.getUserFriends(id);
		for(int i=0;i<fids.size();i++){
			for(int j=0;j<clients.size();j++){
				if(fids.get(i).equals(clients.get(j).getId())){
					try {
						//˭���ܵ���login ?mainui
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
		
		//������Ϊ��½ʱ����validateUser������֤
		//Ϊ��ʱ �Ѹÿͻ���������߿ͻ������У���ִ����Ӧ�ĵ�½����
			//��½�ɹ�ʱ�������Ĳ���
			//�޸����ݿ��и��û��� ���� ��Ϣ
			//�����ݿ���ȡ�����û����ѵ���Ϣ
			//�����û����ѵ���Ϣ�����һ���ַ������������ÿͻ���
			//�ɷ�����֪ͨ���� �û������� 
		//��ʱ  ����һ����½ʧ�ܵ���Ϣ
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
		//ע�� ��֤�û��Ƿ���� ���ھͷ��� false
			//	������ �����ݿ�userinfo ��������,���� ��ע���û��ĺ��ѱ�friendlist_id,���� true
		//�������߳�
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
		//���������û� ����Ϣд�ؿͻ���
		public void searchOnline(String id){
			try {
				String message=DB.searchOnline(id);
				dos.writeUTF(message);
				ta.append(message+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//��Ӻ��� ���ܿͻ��˵���Ӻ���������Ϣ(hid hip fid fip),
			//��hid�ĺ������ݱ��в���fid����Ϣ
			//��fid�ĺ������ݱ��в���hid����Ϣ
			//�ֱ��hid fid ����Ϣ ��Ӻ��ѵ���Ϣ
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
		//�������������
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
		//���˶Ͽ�����ʱ�Ĳ���
		public void out(){
			ta.append("Client closed!"+"\n");
			//����client�߳�
			bConnected=false;
			//�������û���ȥ��
			clients.remove(this);
			//�������ݿ�����
			if(id!=null){
				DB.setUserOffline(id);
				//������Ϣ������
				Server.someoneLogout(id);
			}
			ta.append("online: "+clients.size()+"  threads: "+Thread.activeCount()+"\n");
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
