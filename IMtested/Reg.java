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

//注册界面
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
		Label lid=new Label("帐号");
		Label lpw=new Label("密码");
		Label lrpw=new Label("重复密码");
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
		Button reg=new Button("注册");
		reg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				register();
			}	
		});
		Button cancl=new Button("取消");
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
		setTitle("注册界面");
		setVisible(true);
	}
	//先看输入的 用户名 是否符合规范(字母,0-9,密码不为空)  2次密码一样 用户名20 密码长度在10以内  不对就提示用户
	//访问服务器 服务器验证用户是否存在 不存在 就执行相应操做  返回注册成功信息
	//提示注册成功 关闭窗口
	public void register(){
		if(!id.getText().trim().matches("\\w+") || !pw.getText().trim().matches("\\w+") 
				|| !pw.getText().trim().equals(rpw.getText().trim()) || 
				id.getText().trim().length()>20 || pw.getText().trim().length()>10){
			JOptionPane.showMessageDialog(this,"帐号长度不高于20，密码不高于10，用户名、密码必须为字母或数字，并且不能为空");
			return;
		}
//JOptionPane.showMessageDialog(this,"输入正确");
		String request="register@"+id.getText().trim()+"@"+pw.getText().trim();
		try {
			dos.writeUTF(request);
			String message=dis.readUTF();
			String[] command=message.split("#");
			if(command[0].equals("regdone")){
				JOptionPane.showMessageDialog(this,"注册成功");
				this.dispose();
			}else if(command[0].equals("regfalse")){
				JOptionPane.showMessageDialog(this,"注册失败，请更换用户名");
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
