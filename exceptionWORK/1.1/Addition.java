import javax.swing.JOptionPane;
public class Addition{
	public static void main(String[] args){
		String firstNumber;
		String secondNumber;
		int number1;
		int number2;
		int sum;
		boolean input=true;
		while(input){
			firstNumber=JOptionPane.showInputDialog("输入第一个数");
			secondNumber=JOptionPane.showInputDialog("输入第二个数");
			try{
				number1=Integer.parseInt(firstNumber);
				number2=Integer.parseInt(secondNumber);
				sum=number1+number2;
				input=false;
				System.out.println(sum);
				JOptionPane.showMessageDialog(null,"结果是"+sum,"结果",JOptionPane.PLAIN_MESSAGE);
			}catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(null,"Please input numeric degits!");
			}finally{
				firstNumber=null;
				secondNumber=null;
				System.out.println("Finally 已执行");
			}

		}
		System.exit(0);
	}
}

