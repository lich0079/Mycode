import java.util.*;
import javax.swing.*;

class Testquicksort{
		
		static int div(int a[],int b,int e){
				int i,j,k,m;
				m=a[b];
				while(true){
					for(i=b+1;i<=e && a[i]<=m;i++){}
					for(j=e;a[j]>m;j--){}
					if(i>=j)break;
					k=a[i];
					a[i]=a[j];
					a[j]=k;
				}
				k=a[j];
				a[j]=a[b];
				a[b]=k;
				
				return j;//因为j<i所以j不可能为e  最大只能为e-1，如{2，1，1，1，1，1，1，3}
				//if {2,3,3,3,3,3,3}此时j=0;  再次quicksort时会因为if(b<e)  0<-1 跳出递归
			}
		
		static void quicksort(int a[],int b,int e){
				if(b<e){
						int m=div(a,b,e);
						quicksort(a,b,m-1);
						quicksort(a,m+1,e);
					}
			}
		
		
		public static void main(String[] args){
			int num=10000;
			int[] a=new int[num];
			int[] b=new int[num];
			Random r=new Random();
			//给a数组 b数组同样的赋值 并打印
			for(int i=0;i<a.length;i++){
					a[i]=r.nextInt(num)+1;
					b[i]=a[i];
					System.out.print(a[i]+" ");
				}
				System.out.println();
				for(int i=0;i<b.length;i++){
					System.out.print(b[i]+" ");
				}
				System.out.println();
				
			//a数组进行quicksort	
			long atime=System.currentTimeMillis();
			quicksort(a,0,num-1);
			atime=System.currentTimeMillis()-atime;
				
			//打印排序后的a数组
			System.out.println();
			for(int i=0;i<a.length;i++){
					System.out.print(a[i]+" ");
				}
				
			
			
			//b数组进行selectsort
			long btime=System.currentTimeMillis();
			for(int z=0;z<b.length;z++){
					int min=100000000,mi=0,t;
					for(int y=z;y<b.length;y++){
							if(b[y]<min){
									min=b[y];
									mi=y;
								}
						}
						t=b[z];
						b[z]=b[mi];
						b[mi]=t;
				}
				btime=System.currentTimeMillis()-btime;		
			
			//打印排序后的b数组
				System.out.println();
				System.out.println();
				
				for(int i=0;i<b.length;i++){
					System.out.print(b[i]+" ");
				}
				
				
				//输出时间
				System.out.println("\nquicksort: "+atime+" ms");
				System.out.println("\nselectsort: "+btime+" ms");
				
				System.out.println("\n对"+num+"位数进行排序时2种算法时间差为: "+(btime-atime)+" ms");
		}
		
	}