import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;



public class Friend {

	private String ip=null;
	private Socket s;
	private String id;
	private String hid;
	private boolean online;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean bConnected = false;
	private FrdButton b;
	private Talk t;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public Friend(String hid,String id, String ip, boolean online) {
		super();
		this.hid=hid;
		this.ip = ip;
		this.id = id;
		this.online = online;
		b=new FrdButton(id);
		t=new Talk();;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public Socket getS() {
		return s;
	}


	public void setS(Socket s) {
		this.s = s;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public boolean isOnline() {
		return online;
	}


	public void setOnline(boolean online) {
		this.online = online;
	}


	public boolean isBConnected() {
		return bConnected;
	}


	public void setBConnected(boolean connected) {
		bConnected = connected;
	}


	public Button getB() {
		return b;
	}


	public void setB(FrdButton b) {
		this.b = b;
	}

	private class FrdButton extends Button implements ActionListener{

		FrdButton(String id){
			super(id);
			this.addActionListener(this);
		}
		public void actionPerformed(ActionEvent e) {
			t.setVisible(true);
			b.setBackground(new Color(215,187,129));
		}
		
	}
	
	
	
	 class Talk extends Frame{

		TextArea input=new TextArea(5,50);
		TextArea display=new TextArea(15,50);
		Button b1=new Button("确定");
		DatagramSocket dsspeak=null;
		DatagramPacket dp=null;
		boolean enter=false;
		boolean ctrl=false;
		
		Talk(){
			super("与 "+id+" 聊天中");
			Panel p1=new Panel();
			Panel p2=new Panel();
			p1.setLayout(new BorderLayout());
			p1.add(input,BorderLayout.SOUTH);
			p1.add(display,BorderLayout.NORTH);
			p2.add(b1,BorderLayout.SOUTH);
			add(p1,BorderLayout.NORTH);
			add(p2,BorderLayout.SOUTH);
			pack();
			this.setLocation(300,300);
			this.setResizable(false);
			this.setAlwaysOnTop(true);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					setVisible(false);
				}
			});
			b1.addActionListener(new InListener());
			input.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if(b.getBackground().equals(Color.RED)){
						b.setBackground(new Color(215,187,129));
					}
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						enter=true;
					}else if(e.getKeyCode()==KeyEvent.VK_CONTROL){
						ctrl=true;
					}
					type();
				}
				
				public void keyReleased(KeyEvent e){
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						enter=false;
					}else if(e.getKeyCode()==KeyEvent.VK_CONTROL){
						ctrl=false;
					}
				}
			});
			setVisible(false);	
		}
		
		public void type(){
			if(enter && ctrl){
				in();
			}
		}
		
		public void in(){
			String message=hid+"#"+hid+"  "+new Date().toLocaleString()+"\n"+input.getText().trim()+"\n"+"\n";
			byte[] buf=message.getBytes();
			try {
				dp=new DatagramPacket(buf,buf.length,new InetSocketAddress(ip,5555));
				dsspeak=new DatagramSocket(8888);
				dsspeak.send(dp);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}catch(IOException e2){
				e2.printStackTrace();
			}finally{
				dsspeak.close();
				dsspeak=null;
				dp=null;
			}
			display.append(hid+"  "+new Date().toLocaleString()+"\n" );
			display.append(input.getText().trim());
			display.append("\n");
			display.append("\n");
			input.setText("");
		}
		
		class InListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				in();
			}
		}

		public TextArea getDisplay() {
			return display;
		}
	
	}



	public Talk getT() {
		return t;
	}

	
}
