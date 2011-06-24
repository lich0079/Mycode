import javax.swing.JOptionPane;

class TestCalendar{
	public static void main(String[] args){
		while(true){
			try{
				int year=Integer.parseInt(JOptionPane.showInputDialog("输入年份"));
				int month=Integer.parseInt(JOptionPane.showInputDialog("输入月份"));
				Calendar test=new Calendar(year,month);
				test.print();
			}catch(Exception e){
				JOptionPane.showMessageDialog(null,"输入错误");
			}
			//System.exit(0);
		}
	}
}