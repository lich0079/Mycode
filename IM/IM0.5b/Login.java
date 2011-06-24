import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

class Login extends Frame implements ActionListener{
		TextField tid;
		TextField tpw;
		TextField tsip;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		Socket s=null;
		ArrayList<Friend> friends=null;
		
	Login(){
		
		Panel p1=new Panel();
		Panel p2=new Panel();
		Label lid=new Label("id��");
		Label sip=new Label("������ip");
		lid.setAlignment(Label.CENTER);
		Label lpw=new Label("����");
		lpw.setAlignment(Label.CENTER);
		sip.setAlignment(Label.CENTER);
		tid=new TextField();
		tpw=new TextField();
		tsip=new TextField();
		tsip.setText("127.0.0.1");
		Button blog=new Button("��½");
		blog.addActionListener(this);
		Button bcl=new Button("ȡ��");
		bcl.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}	
		});
		setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(3,2));
		p1.add(lid);
		p1.add(tid);
		p1.add(lpw);
		p1.add(tpw);
		p1.add(sip);
		p1.add(tsip);
		p2.add(blog);
		p2.add(bcl);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
					System.exit(0);							
				}
			}
		);
		add(p1,BorderLayout.NORTH);
		add(p2,BorderLayout.SOUTH);
		setBounds(300,300,200,130);
		this.setResizable(false);
		tpw.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					login();
				}
			}
		});
		tsip.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					login();
				}
			}
		});
		setTitle("��½����");
		setVisible(true);
	}
	//������ص���Ϣ��friendsinfo��ʾ�ɹ� ����Ϣ��������friend����Ϣ��id,ip,online�����������������friends������
		//Ȼ�� ���� �����棨id,ip,������������Ӳ���,�����б�
	//������ϢΪ��ʱ ��ʾ ������Ϣ
	public void analyseResult(String result){
		String[] command=result.split("#");
		if(command[0].equals("friendsinfo")){
			friends=new ArrayList<Friend>();
			for(int i=1;i<command.length;i++){
				String[] friendinfo=command[i].split("@");
				friends.add(new Friend(tid.getText().trim(),friendinfo[0],friendinfo[1],friendinfo[2].equals("true")?true:false));
			}
			try {
				
				new Mainui(tid.getText().trim(),InetAddress.getLocalHost().getHostAddress() , s, friends);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.dispose();
		}else if(command[0].equals("false")){
			JOptionPane.showMessageDialog(this,"�޴��û����������");
		}
	}
	
	public void actionPerformed(ActionEvent e){
		login();
	}
		
	public void login(){
		//��½ʱ �Ĳ��� ����id pw���������ˣ����ܷ��ص���Ϣ���������ص���Ϣ������Ӧ�Ĵ���
		try {
			//�ж�id����Ϊ����������ַ� pw����Ϊ�� �������������� ����ִ�к�������     ��Ӧ�ж����� ��ʡ��
			if(!this.tid.getText().trim().matches("\\d+") || this.tpw.getText().trim().equals("")){
				return;
			}
			s=new Socket(this.tsip.getText().trim(),8888);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(""+"login"+"@"+this.tid.getText().trim()+"@"+this.tpw.getText().trim());
			String result=dis.readUTF();
			//System.out.println(result);
			this.analyseResult(result);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	


	public static void main(String[] args){
		new Login();
	}
}
