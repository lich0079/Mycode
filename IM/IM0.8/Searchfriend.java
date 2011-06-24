import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;


public class Searchfriend extends Frame{

	ArrayList<Friend> friends;
	String message;
	Mainui host;
	
	Searchfriend(Mainui host){
		this.host=host;
		this.friends=host.friends;
		setBounds(400,100,300,600);
		this.sortUI();
		this.setAlwaysOnTop(true);
		setTitle("搜索界面");
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);					
				}
			}
		);
		setVisible(false);
	}

	public void display(){
		sortUI();
		setVisible(true);
	}
	
	public void sortUI(){
		removeAll();
		Panel view=new Panel();
		view.setLayout(new GridLayout(22,1));
		Panel ph=new Panel();
		ph.setLayout(new GridLayout(1,3));
		Label hid=new Label("id");
		Label hip=new Label("ip");
		Label hnull=new Label();
		ph.add(hid);
		ph.add(hip);
		ph.add(hnull);
		add(ph,BorderLayout.NORTH);
		
		try {
			if(message==null)return;
			System.out.println(message);
			String[] command=message.split("#");
			for(int i=1;i<command.length;i++){
				String[] onlineuserinfo=command[i].split("@");
				String id=onlineuserinfo[0];
				String ip=onlineuserinfo[1];
				boolean have=false;
				for(int j=0;j<friends.size();j++){
					if(friends.get(j).getId().equals(id)){
						have=true;
					}
				}
				if(!have){
					OnlineUser ou=new OnlineUser(id,ip);
					view.add(ou.p);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		add(view,BorderLayout.CENTER);
		Panel botton =new Panel();
		Button refresh=new Button("刷新列表");
		refresh.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					host.dos.writeUTF("searchOnline@"+host.id);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}	
		});
		botton.add(refresh);
		add(botton,BorderLayout.SOUTH);
		
		show();
	}
	
	class OnlineUser implements ActionListener{
		Label id;
		Label ip;
		Button sel;
		Panel p;
		
		OnlineUser(String id,String ip){
			this.id=new Label(id);
			this.ip=new Label(ip);
			this.sel=new Button("添加至好友");
			sel.addActionListener(this);
			p=new Panel();
			p.setLayout(new GridLayout(1,3));
			p.add(this.id);
			p.add(this.ip);
			p.add(this.sel);
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("addFriend@"+host.id+"@"+host.ip+"@"+id.getText()+"@"+ip.getText());
			try {
				host.dos.writeUTF("addFriend@"+host.id+"@"+host.ip+"@"+id.getText()+"@"+ip.getText());
				host.dos.writeUTF("searchOnline@"+host.id);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
