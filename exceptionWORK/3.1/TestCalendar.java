import javax.swing.JOptionPane;

class TestCalendar{
	public static void main(String[] args){
		while(true){
			try{
				int year=Integer.parseInt(JOptionPane.showInputDialog("�������"));
				int month=Integer.parseInt(JOptionPane.showInputDialog("�����·�"));
				Calendar test=new Calendar(year,month);
				test.print();
			}catch(Exception e){
				JOptionPane.showMessageDialog(null,"�������");
			}
			//System.exit(0);
		}
	}
}