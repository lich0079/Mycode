class ChuFa{
	public static void main(String[] args){
		int num1;
		int num2;
		int result;
		try{
			num1=Integer.parseInt(args[0]);
			num2=Integer.parseInt(args[1]);
			result=num1/num2;
			System.out.println(result);
		}catch(ArrayIndexOutOfBoundsException aoe){
			System.out.println("�����в�������");
		}catch(ArithmeticException ae){
			System.out.println("��������");
		}
	}
}