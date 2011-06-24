/**
���ԣ�
�� �� ��  ����
Date���Ա���п���  �õ�����Ϊ�������췽���е��õ�check������������ڵ���ȷ��   ��˿�ʡ����Day����д�˴���  ����Ϊ�˱���Day�Ķ�������ò��ô˷���
*/
class Day{
	int year;
	int month;
	int day;
	String name;
	
	Date adate;
	Day(){}
	//���¹�����֮����Ҫ����һ��Date����Ϊ�˵���Date�е�cehck�������ж��������ڵ���Ч��  
	Day(int year,int month,int day){
		adate=new Date(year,month,day);
		this.year=adate.year;
		this.month=adate.month;
		this.day=adate.day;
		name=getName();
	}
/**
������
*/	
	void setDay(int year,int month,int day){
		adate=new Date(year,month,day);
		this.year=adate.year;
		this.month=adate.month;
		this.day=adate.day;
		name=getName();
	}
/**
override��Object��toString����
������ public
*/	
	public String toString(){
		return year+"��"+month+"��"+day+"��";
	}
/**
���ر��������
�����㷨��������ڼ�
��ת������
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
�������������
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
�������������
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
����number��������
�˴�ѭ��������nextDayName()���ѷ���ֵ�����˱���  �������ᵼ�±�������ڵĸı�  ��������temp1��temp2������this.name�����ֵ ����ٻ�����
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

