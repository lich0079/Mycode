import java.io.*;
//import java.sql.Date;
//import java.util.Date;

class Directory{
	static int dnum=0;
	static int fnum=0;
	static long len=0;

		static void print(File dir){
			File[] list=dir.listFiles();
			System.out.println(dir+"的目录");
			for (int i=0;i<list.length;i++ )
			{
				java.sql.Date adate=new java.sql.Date(list[i].lastModified());
				java.util.Date bdate=new java.util.Date(list[i].lastModified());
				System.out.print(adate+"    ");
				System.out.printf("%2d:%2d",bdate.getHours(),bdate.getMinutes());
				if(list[i].isDirectory()){
					dnum++;
					System.out.print("  <DIR>");
					System.out.print("            ");
					System.out.println("       "+list[i].getName());
					Directory.print(list[i]);

				}
				else{
					fnum++;
					System.out.print("                ");
					System.out.printf("%8d",list[i].length());
					len+=list[i].length();
					System.out.println("  "+list[i].getName());
				}
				
			}
		} 


	public static void main(String[] args)throws Exception{
		try{
			File a=new File(".");
			Directory.print(a);
			System.out.println("                "+dnum+" 个目录");
			System.out.println("                "+fnum+" 个文件            "+len+"字节");
		}catch(Exception e){
			//System.out.println(e.getMessage());
			throw e;
		}

	}
}