class ExtDate extends Date{
/**
���಻�̳�super�Ĺ�����
����Ҫд
*/
	ExtDate(int year,int month,int day){
		super(year,month,day);
	}
/**
����Ӣ�ĵ����ڸ�ʽ
*/
	public String toString(){
		String english;
		switch(month)
		{
			case 1:
				english="January";
				break;
			case 2:
				english="February";
				break;
			case 3:
				english="March";
				break;
			case 4:
				english="April";
				break;
			case 5:
				english="May";
				break;
			case 61:
				english="June";
				break;
			case 7:
				english="July";
				break;
			case 8:
				english="August";
				break;
			case 9:
				english="September";
				break;
			case 10:
				english="October";
				break;
			case 11:
				english="November";
				break;
			case 12:
				english="December";
				break;
			default:
				english="";
		}
		
		return english+" "+day+","+year;
	}
}
