/**
class类  实现了Cloneable接口 是为了clone()方法
*/

class Date implements Cloneable{
/**
3个变量 
年 月 日
*/

	int year;
	int month;
	int day;
/**
无参数的构造器
*/	
	Date(){}
/**
有参数的构造器
在构造前先调用了 check 函数 检查日期是否存在
*/
	Date(int year,int month,int day){
		if(this.check(year,month,day)){
			this.year=year;
			this.month=month;
			this.day=day;
		}
		else {
			System.out.println("日期输入错误");
		}
	}
/**
检查日期正确性的函数

判断年份和月份
判断天数
判断天数时调用isLeapYear方法判断是否闰年
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
判断是否闰年的函数
*/	
	boolean isLeapYear(int year){
		if((year%4==0)&&(year%100!=0) || (year%400==0)){
			System.out.println("闰年");
			return true;
		}
		else {
			return false;
		}
	}
/**
设置日期的函数
调用了判断日期正确性的函数
*/
	void setDate(int year,int month,int day){
		if(this.check(year,month,day)){
			this.year=year;
			this.month=month;
			this.day=day;
		}
		else {
			System.out.println("日期输入错误");
		}
	}
/**
override了Object的toString函数
所以是 public
*/	
	public String toString(){
		return month+"-"+day+"-"+year;
	}
/**
设置月份
月份变了 所以要检查日期正确性
*/	
	void setMonth(int month){
		if(this.check(year,month,day)){
			this.month=month;
		}
		else {
			System.out.println("月输入错误");
		}
	}
/**
设置日
检查日期正确性
*/	
	void setDay(int day){
		if(this.check(year,month,day)){
			this.day=day;
		}
		else {
			System.out.println("日输入错误");
		}
	}
/**
设置年
检查日期正确性
*/		
	void setYear(int year){
		if(this.check(year,month,day)){
			this.year=year;
		}
		else {
			System.out.println("年输入错误");
		}
	}
/**
返回月
*/	
	int getMonth(){
		return month;
	}
/**
返回年
*/	
	int getYear(){
		return year;
	}
/**
返回日
*/	
	int getDay(){
		return day;
	}
/**
返回当月天数
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
返回这是一年中的第几天
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
返回当年还剩多少天
*/	
	int remainDays(){
		if(this.isLeapYear(year)){
			return 366-this.pastDays();
		}
		else return 365-this.pastDays();
	}
/**
把日期设置为加上num天后的日期

算法：
先判断num是否超过了当月剩下的天数
	超过的话
		num减去当月剩下的天数 把月份++   此时应注意如月份超过12  要把年++  把月赋1
		然后num依次减去每个月的天数 直到剩下的num不足当月天数 把num赋给day
	不超过的话
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
克隆函数
出处：core java
*/	
	public Object clone() throws CloneNotSupportedException{		
		return super.clone() ;
	}
}
