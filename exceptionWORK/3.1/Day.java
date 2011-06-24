/**
属性：
年 月 日  名称
Date类成员可有可无  用到他是为了他构造方法中调用的check函数来检查日期的正确性   如此可省下在Day中在写此代码  但如为了保持Day的独立性最好不用此方法
*/
class Day{
	int year;
	int month;
	int day;
	String name;
	
	Date adate;
	Day(){}
	//以下构造器之所以要调用一个Date类是为了调用Date中的cehck方法来判断输入日期的有效性  
	Day(int year,int month,int day){
		adate=new Date(year,month,day);
		this.year=adate.year;
		this.month=adate.month;
		this.day=adate.day;
		name=getName();
	}
/**
设置器
*/	
	void setDay(int year,int month,int day){
		adate=new Date(year,month,day);
		this.year=adate.year;
		this.month=adate.month;
		this.day=adate.day;
		name=getName();
	}
/**
override了Object的toString函数
所以是 public
*/	
	public String toString(){
		return year+"年"+month+"月"+day+"日";
	}
/**
返回本天的名称
先用算法算出是星期几
在转成名称
*/	
	String getName(){
		int i,days,week;
        days=day;
        for(i=1;i<month;i++)
        {
			switch(i)
            {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					days+=31;
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					days+=30;
					break;
				case 2:
					if((year%4==0)&&(year%100!=0) || (year%400==0))
						days+=29;
					else
						days+=28;
             }
        }
        week=((year-1)+(year-1)/4-(year-1)/100+(year-1)/400+days)%7;
		switch(week)
		{
			case 0:
				return "Sun";
			case 1:
				return "Mon";
			case 2:
				return "Tue";
			case 3:
				return "Wed";
			case 4:
				return "Thu";
			case 5:
				return "Fri";
			case 6:
				return "Sat";
			default:
				return "";
		}
	}
/**
返回明天的名称
*/	
	String nextDayName(){
		if(this.name=="Sun"){
			return "Mon";
		}else if(this.name=="Mon"){
			return "Tue";
		}else if(this.name=="Tue"){
			return "Wed";
		}else if(this.name=="Wed"){
			return "Thu";
		}else if(this.name=="Thu"){
			return "Fri";
		}else if(this.name=="Fri"){
			return "Sat";
		}else{
			return "Sun";
		}
	}
/**
返回昨天的名称
*/	
	String beforeDayName(){
		if(this.name=="Tue"){
			return "Mon";
		}else if(this.name=="Wed"){
			return "Tue";
		}else if(this.name=="Thu"){
			return "Wed";
		}else if(this.name=="Fri"){
			return "Thu";
		}else if(this.name=="Sat"){
			return "Fri";
		}else if(this.name=="Sun"){
			return "Sat";
		}else{
			return "Mon";
		}
	}
/**
返回number天后的名称
此处循环调用了nextDayName()并把返回值赋给了本天  这样做会导致本天的日期的改变  所以用了temp1，temp2来保留this.name最初的值 最后再还给他
*/	
	String nextManyDayName(int number){
		String temp1,temp2;
		temp1=this.name;
		
		for(int i=number;i>0;i--){
			this.name=nextDayName();
		}
		temp2=this.name;
		this.name=temp1;
		return temp2;
	}

	/*
	public static void main(String[] args){
		Day a=new Day(2000,2,30);
		a.setDay(2007,12,1);
		System.out.println(a.getName());
		System.out.println("today is "+a.name);
		System.out.println("the next day is "+a.nextDayName());
		System.out.println("the before day is "+a.beforeDayName());
		System.out.println("the next many day is "+a.nextManyDayName(16));
		System.out.println("today is "+a.name);
	}*/
}

