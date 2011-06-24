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
     * 当命令行参数为一时构造正方形并打印面积
     * 当命令行参数为二时构造长方形并打印面积
     * 当命令行参数为零时抛出异常
     * 当出现其他异常时， 如 命令行输入了 字符 时， 由系统抛出异常  由main catch 并打印 “输入错误”
     */
class TestShape{
	public static void main(String[] args){
		try{
			if(args.length==1){
				System.out.println("正方形面积是:"+new Square(args).getArea());
			}else if(args.length==2){
				System.out.println("长方形面积是:"+new Rectangle(args).getArea());
			}else if(args.length==0){
				throw new ArrayIndexOutOfBoundsException();
			}
		}catch(ArrayIndexOutOfBoundsException ae){
			System.out.println("没有命令行参数");
		}
		catch(Exception e){
			System.out.println("输入错误");
		}
	}
}