import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class CodeCounter {
	
	static long normalLines = 0;
	static long commentLines = 0;
	static long whiteLines = 0;
	
	public static void main(String[] args)throws Exception {
		File f = new File(".");
		File[] codeFiles = f.listFiles();
		for(File child : codeFiles){
			if(child.getName().matches(".*\\.java$")) {
				parse(child);
			}
		}
		
		StringBuffer s=new StringBuffer("normalLines:" + normalLines+"\n");
		s.append("commentLines:" + commentLines+"\n");
		s.append("whiteLines:" + whiteLines+"\n");
		FileOutputStream fos=new FileOutputStream(""+new Date().getMinutes()+".txt");
		String ss=s.toString();
		fos.write(ss.getBytes());	//把字符串转换成字节数组
		//System.out.println("文件写入完毕");
		fos.close();
		
	}

	 static void parse(File f) {
		BufferedReader br = null;
		boolean comment = false;
		try {
			br = new BufferedReader(new FileReader(f));
			String line = "";
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.matches("^[\\s&&[^\\n]]*$")) {
					whiteLines ++;
				} else if (line.startsWith("/*") && !line.endsWith("*/")) {
					commentLines ++;
					comment = true;	
				} else if (line.startsWith("/*") && line.endsWith("*/")) {
					commentLines ++;
				} else if (true == comment) {
					commentLines ++;
					if(line.endsWith("*/")) {
						comment = false;
					}
				} else if (line.startsWith("//")) {
					commentLines ++;
				} else {
					normalLines ++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
