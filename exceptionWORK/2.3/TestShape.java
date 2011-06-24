interface Areaable{
	double getArea();
}

class Shape implements Areaable{
	public double getArea(){
		return -1;
	}
	
}

class Rectangle extends Shape{
	double height;
	double weight;
	
	Rectangle(){}
	
	Rectangle(String[] args){
		this.height=Double.parseDouble(args[0]);
		this.weight=Double.parseDouble(args[1]);
	}
	
	public double getArea(){
		return height*weight;
	}
}

class Square extends Shape{
	double side;
	
	Square(){}
	
	Square(String[] args){
		this.side=Double.parseDouble(args[0]);
	}
	
	public double getArea(){
		return side*side;
	}
}


    /**
     * �������в���Ϊһʱ���������β���ӡ���
     * �������в���Ϊ��ʱ���쳤���β���ӡ���
     * �������в���Ϊ��ʱ�׳��쳣
     * �����������쳣ʱ�� �� ������������ �ַ� ʱ�� ��ϵͳ�׳��쳣  ��main catch ����ӡ ���������
     */
class TestShape{
	public static void main(String[] args){
		try{
			if(args.length==1){
				System.out.println("�����������:"+new Square(args).getArea());
			}else if(args.length==2){
				System.out.println("�����������:"+new Rectangle(args).getArea());
			}else if(args.length==0){
				throw new ArrayIndexOutOfBoundsException();
			}
		}catch(ArrayIndexOutOfBoundsException ae){
			System.out.println("û�������в���");
		}
		catch(Exception e){
			System.out.println("�������");
		}
	}
}