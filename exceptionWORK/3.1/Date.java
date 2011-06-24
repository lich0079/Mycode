/**
class��  ʵ����Cloneable�ӿ� ��Ϊ��clone()����
*/

class Date implements Cloneable{
/**
3������ 
�� �� ��
*/

	int year;
	int month;
	int day;
/**
�޲����Ĺ�����
*/	
	Date(){}
/**
�в����Ĺ�����
�ڹ���ǰ�ȵ����� check ���� ��������Ƿ����
*/
	Date(int year,int month,int day){
		if(this.check(year,month,day)){
			this.year=year;
			this.month=month;
			this.day=day;
		}
		else {
			System.out.println("�����������");
		}
	}
/**
���������ȷ�Եĺ���

�ж���ݺ��·�
�ж�����
�ж�����ʱ����isLeapYear�����ж��Ƿ�����
*/	
	boolean check(int year,int month,int day){
		if(year<=0 || month>12 || month<1)
		{
			return false;
		}
		if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
			if(day<1 ||day>31)
			{
				return false;
			}
		}
		else if(month==4 || month==6 || month==9 || month==11){
			if(day<1 || day>30)
			{
				return false;
			}
		}
		else if(month==2){
			if(this.isLeapYear(year)){
				if(day<1 || day>29)
				{
					return false;
				}
			}
			else {
				if(day<1 || day>28)
				{
					return false;
				}
			}
		}
		return true;
		
	}
/**
�ж��Ƿ�����ĺ���
*/	
	boolean isLeapYear(int year){
		if((year%4==0)&&(year%100!=0) || (year%400==0)){
			System.out.println("����");
			return true;
		}
		else {
			return false;
		}
	}
/**
�������ڵĺ���
�������ж�������ȷ�Եĺ���
*/
	void setDate(int year,int month,int day){
		if(this.check(year,month,day)){
			this.year=year;
			this.month=month;
			this.day=day;
		}
		else {
			System.out.println("�����������");
		}
	}
/**
override��Object��toString����
������ public
*/	
	public String toString(){
		return month+"-"+day+"-"+year;
	}
/**
�����·�
�·ݱ��� ����Ҫ���������ȷ��
*/	
	void setMonth(int month){
		if(this.check(year,month,day)){
			this.month=month;
		}
		else {
			System.out.println("���������");
		}
	}
/**
������
���������ȷ��
*/	
	void setDay(int day){
		if(this.check(year,month,day)){
			this.day=day;
		}
		else {
			System.out.println("���������");
		}
	}
/**
������
���������ȷ��
*/		
	void setYear(int year){
		if(this.check(year,month,day)){
			this.year=year;
		}
		else {
			System.out.println("���������");
		}
	}
/**
������
*/	
	int getMonth(){
		return month;
	}
/**
������
*/	
	int getYear(){
		return year;
	}
/**
������
*/	
	int getDay(){
		return day;
	}
/**
���ص�������
*/	
	int getFullDays(int year,int month){
		if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
			return 31;
		}
		else if(month==4 || month==6 || month==9 || month==11){
			return 30;
		}
		else if(month==2){
			if(this.isLeapYear(year)){
				return 29;
			}
			else {
				return 28;
			}
		}
		else return -1;
		
	}
/**
��������һ���еĵڼ���
*/	
	//int pastDays(int year,int month,int day){
	int pastDays(){
		int days=day;
		for(int i=1;i<month;i++)
		{
			if(i==1 || i==3 || i==5 || i==7 || i==8 || i==10 || i==12){
				days+=31;
			}
			else if(i==4 || i==6 || i==9 || i==11){
				days+=30;
			}
			else
			{
				if(this.isLeapYear(year)){
					days+=29;
				}
				else {
					days+=28;
				}
			}
		}
		return days;
	}
/**
���ص��껹ʣ������
*/	
	int remainDays(){
		if(this.isLeapYear(year)){
			return 366-this.pastDays();
		}
		else return 365-this.pastDays();
	}
/**
����������Ϊ����num��������

�㷨��
���ж�num�Ƿ񳬹��˵���ʣ�µ�����
	�����Ļ�
		num��ȥ����ʣ�µ����� ���·�++   ��ʱӦע�����·ݳ���12  Ҫ����++  ���¸�1
		Ȼ��num���μ�ȥÿ���µ����� ֱ��ʣ�µ�num���㵱������ ��num����day
	�������Ļ�
		day+=num
*/
	void addDays(int num){
		if((day+num)>getFullDays(year,month)){
			num-=(getFullDays(year,month)-day);
			month++;
			if(month>12){
				year++;	
				month=1;
			}
			//System.out.println(num);
			while(num>getFullDays(year,month)){
				num-=getFullDays(year,month);
				month++;
				if(month>12){
					year++;
					month=1;
				}
				//System.out.println(num);
			}
			day=num;
		}
		else {
			day+=num;
		}
		
	}
/**
��¡����
������core java
*/	
	public Object clone() throws CloneNotSupportedException{		
		return super.clone() ;
	}
}
