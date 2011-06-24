import java.lang.Exception;


class Bank{
	double base;    	//���������е�Ǯ 
	
	Bank(){}
	Bank(double base){
		this.base=base;
	}
	
	void getMoney(double demand)throws MoneyException{	//demand��Ҫȡ��Ǯ
		if(base<demand){
			throw new MoneyException("����");
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
	
		Bank abank=new Bank(10000);			//����һ����10000Ǯ������
		System.out.println(abank.base);
		abank.addMoney(500);				//��Ǯ
		System.out.println(abank.base);
		try{
			abank.getMoney(50000);				//ȡǮ
			System.out.println(abank.base);
		}catch(MoneyException me){
			System.out.println(me.getMessage());//��ӡ�쳣��Ϣ
		}
	}
}
