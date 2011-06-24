import java.io.*;
import javax.swing.JOptionPane;

class Search{
	static int dnum=0;
	static int fnum=0;
	static long len=0;

		static void print(File dir,String s){
			File[] list=dir.listFiles();
			for (int i=0;i<list.length;i++ )
			{
				if(list[i].isDirectory()){
					dnum++;
					Search.print(list[i],s);
				}
				else{
					if(list[i].getName().equals(s)){
						System.out.println(s+"在"+list[i].getAbsolutePath());
						System.exit(0);
					}				
				}			
			}
		} 


	public static void main(String[] args)throws Exception{
		String s=JOptionPane.showInputDialog("输入要查找的文件名：");
		try{
			File a=new File(".");
			Search.print(a,s);
			System.out.println("此目录下无"+s);
		}catch(Exception e){
			throw e;
		}

	}
}