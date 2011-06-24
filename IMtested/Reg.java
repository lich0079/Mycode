import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

//ע�����
public class Reg extends Frame{

	TextField id;
	TextField pw;
	TextField rpw;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	Socket s=null;
	
	Reg(Socket s){
		this.s=s;
		try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		id=new TextField();
		pw=new TextField();
		rpw=new TextField();
		pw.setEchoChar('*');
		rpw.setEchoChar('*');
		Panel p1=new Panel();
		p1.setLayout(new GridLayout(3,2));
		Label lid=new Label("�ʺ�");
		Label lpw=new Label("����");
		Label lrpw=new Label("�ظ�����");
		lid.setAlignment(Label.CENTER);
		lpw.setAlignment(Label.CENTER);
		lrpw.setAlignment(Label.CENTER);
		p1.add(lid);
		p1.add(id);
		p1.add(lpw);
		p1.add(pw);
		p1.add(lrpw);
		p1.add(rpw);
		Panel p2=new Panel();
		Button reg=new Button("ע��");
		reg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				register();
			}	
		});
		Button cancl=new Button("ȡ��");
		p2.add(reg);
		p2.add(cancl);
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
					dispose();						
				}
			}
		);
		add(p1,BorderLayout.NORTH);
		add(p2,BorderLayout.SOUTH);
		setBounds(500,300,200,130);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		setTitle("ע�����");
		setVisible(true);
	}
	//�ȿ������ �û��� �Ƿ���Ϲ淶(��ĸ,0-9,���벻Ϊ��)  2������һ�� �û���20 ���볤����10����  ���Ծ���ʾ�û�
	//���ʷ����� ��������֤�û��Ƿ���� ������ ��ִ����Ӧ����  ����ע��ɹ���Ϣ
	//��ʾע��ɹ� �رմ���
	public void register(){
		if(!id.getText().trim().matches("\\w+") || !pw.getText().trim().matches("\\w+") 
				|| !pw.getText().trim().equals(rpw.getText().trim()) || 
				id.getText().trim().length()>20 || pw.getText().trim().length()>10){
			JOptionPane.showMessageDialog(this,"�ʺų��Ȳ�����20�����벻����10���û������������Ϊ��ĸ�����֣����Ҳ���Ϊ��");
			return;
		}
//JOptionPane.showMessageDialog(this,"������ȷ");
		String request="register@"+id.getText().trim()+"@"+pw.getText().trim();
		try {
			dos.writeUTF(request);
			String message=dis.readUTF();
			String[] command=message.split("#");
			if(command[0].equals("regdone")){
				JOptionPane.showMessageDialog(this,"ע��ɹ�");
				this.dispose();
			}else if(command[0].equals("regfalse")){
				JOptionPane.showMessageDialog(this,"ע��ʧ�ܣ�������û���");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		try {
			new Reg(new Socket("127.0.0.1",8888));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
