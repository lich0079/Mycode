/**
Title: Calendar类<br>
Description:主要用来在控制台上生成选定时间的日历<br>
@author:zy<br>
@version:0.50

*/
class Calendar{
/**
@param yearmonth  用来存储年和月的一个ExtDate类
@param firstday      用来存储当月的第一天
	
*/	
	ExtDate yearmonth;
	Day firstday;
/**
这是Calendar类的构造函数

*/	
	Calendar(int year,int month){
		yearmonth=new ExtDate(year,month,1);
		firstday=new Day(year,month,1);
	}
/**
设置年份
需要注意的是： 当年份改变时  对象月份的第一天也会改变

*/	
	void setYear(int year){
		this.yearmonth.setYear(year);
		this.firstday.setDay(year,this.firstday.month,this.firstday.day);
	}

/**
设置月份
需要注意的是： 当月份改变时  对象月份的第一天也会改变

*/		
	void setMonth(int month){
		this.yearmonth.setMonth(month);
		this.firstday.setDay(this.firstday.year,month,this.firstday.day);
	}
/**
返回年份

*/		
	int getYear(){
		return this.yearmonth.year;
	}
/**
返回月份

*/	
	int getMonth(){
		return this.yearmonth.month;
	}
/**
打印月历

*/	
	void print(){
		int start;
		int i,j,k;		
		if(this.firstday.name=="Sun"){
			start=0;
		}else if(this.firstday.name=="Mon"){
			start=1;
		}else if(this.firstday.name=="Tue"){
			start=2;
		}else if(this.firstday.name=="Wed"){
			start=3;
		}else if(this.firstday.name=="Thu"){
			start=4;
		}else if(this.firstday.name=="Fri"){
			start=5;
		}else{
			start=6;
		}
		System.out.print("        ");
		System.out.println("                        "+this.yearmonth.year+"年"+this.yearmonth.month+"月");
		System.out.print("        ");
		System.out.println("Sun     Mon     Tue     Wed     Thu     Fri     Sat");
		System.out.print("        ");
		for(i=0;i<start;i++){
			System.out.print("        ");
		}
		for(j=1;j<=this.yearmonth.getFullDays(this.yearmonth.year,this.yearmonth.month);j++){
			System.out.print(j+"      ");
			if(j<10){
				System.out.print(" ");
			}
			start++;
            if(start%7==0){
				System.out.println();
				System.out.println();
				System.out.print("        ");
			}
            
		}
		System.out.println();
	}
	
	/*
	public static void main(String[] args){
		Calendar t=new Calendar(2006,11);
		System.out.println(t.yearmonth.year+"年"+t.yearmonth.month+"月 "+t.firstday.name);
		t.setYear(2003);
		System.out.println(t.yearmonth.year+"年"+t.yearmonth.month+"月 "+t.firstday.name);
		t.setMonth(9);
		System.out.println(t.yearmonth.year+"年"+t.yearmonth.month+"月 "+t.firstday.name);
		t.print();
		
	}*/
}