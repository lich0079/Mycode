/**
Title: Calendar��<br>
Description:��Ҫ�����ڿ���̨������ѡ��ʱ�������<br>
@author:zy<br>
@version:0.50

*/
class Calendar{
/**
@param yearmonth  �����洢����µ�һ��ExtDate��
@param firstday      �����洢���µĵ�һ��
	
*/	
	ExtDate yearmonth;
	Day firstday;
/**
����Calendar��Ĺ��캯��

*/	
	Calendar(int year,int month){
		yearmonth=new ExtDate(year,month,1);
		firstday=new Day(year,month,1);
	}
/**
�������
��Ҫע����ǣ� ����ݸı�ʱ  �����·ݵĵ�һ��Ҳ��ı�

*/	
	void setYear(int year){
		this.yearmonth.setYear(year);
		this.firstday.setDay(year,this.firstday.month,this.firstday.day);
	}

/**
�����·�
��Ҫע����ǣ� ���·ݸı�ʱ  �����·ݵĵ�һ��Ҳ��ı�

*/		
	void setMonth(int month){
		this.yearmonth.setMonth(month);
		this.firstday.setDay(this.firstday.year,month,this.firstday.day);
	}
/**
�������

*/		
	int getYear(){
		return this.yearmonth.year;
	}
/**
�����·�

*/	
	int getMonth(){
		return this.yearmonth.month;
	}
/**
��ӡ����

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
		System.out.println("                        "+this.yearmonth.year+"��"+this.yearmonth.month+"��");
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
		System.out.println(t.yearmonth.year+"��"+t.yearmonth.month+"�� "+t.firstday.name);
		t.setYear(2003);
		System.out.println(t.yearmonth.year+"��"+t.yearmonth.month+"�� "+t.firstday.name);
		t.setMonth(9);
		System.out.println(t.yearmonth.year+"��"+t.yearmonth.month+"�� "+t.firstday.name);
		t.print();
		
	}*/
}