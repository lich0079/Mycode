import java.lang.Exception;


class Bank{
	double base;    	//银行中所有的钱 
	
	Bank(){}
	Bank(double base){
		this.base=base;
	}
	
	void getMoney(double demand)throws MoneyException{	//demand是要取的钱
		if(base<demand){
			throw new MoneyException("余额不足");
		}
		base=base-demand;
		
	}
	
	void addMoney(double number){
		base=base+number;
	}
}

class MoneyException extends Exception{					
	String message;

	MoneyException(String message){
		this.message=message;
	}

	public String getMessage(){
		return message;
	}
}

public class TestBank{
	public static void main(String[] args){
	
		Bank abank=new Bank(10000);			//建立一个有10000钱的银行
		System.out.println(abank.base);
		abank.addMoney(500);				//存钱
		System.out.println(abank.base);
		try{
			abank.getMoney(50000);				//取钱
			System.out.println(abank.base);
		}catch(MoneyException me){
			System.out.println(me.getMessage());//打印异常信息
		}
	}
}
