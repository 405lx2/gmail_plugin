package test3;
import java.io.*;

public class Test1 {
    
//	//服务项目
//	public static int al[][] = new int[5][15];
//	//服务个数
//	public static int len[] = new int[5];
//	//可靠性对应
	public static double select[][] = new double[15][2];
	//方案记录
	public static double Recvice[][] = new double[15][3];
	//服务名记录
	public static int count = 0;
	//可靠性、价格
//	public static double reliability = 1,price = 0;

    public static void main(String[] args)throws Exception{
//               
//    	long startTime = System.currentTimeMillis();
//    	FileReader f3 = new FileReader("D:\\SERVICE.txt");
//        BufferedReader b3 = new BufferedReader(f3);
//        String s3;
//        int i = 0,mark1,mark2,mark3,temp;
//        double rel,pri;
//        for(int j = 0;j<15;j++){
//			select[j][0] = 100;
//			Recvice[j][0] = 0;
//			Recvice[j][1] = 100;
//        }
//        
//        while((s3 = b3.readLine())!=null){
//        	if(i < 500)
//        	{
//        		mark1 = s3.indexOf(' ',6);
//        		mark2 = s3.indexOf(' ',mark1+1);
//        		mark3 = s3.indexOf(' ',mark2+1);
//        		pri = Double.parseDouble(s3.substring(mark3+1));
//        		rel = Double.parseDouble(s3.substring(mark1+1,mark2));
//        		temp = (int)(100*rel-90);
//        		if(pri<select[temp][0])
//        		{
//        			select[temp][0] = pri;
//        			select[temp][1] = i+1;
//        		}
//        		i++;
//        	}
//        	else
//        	{
//        		i = 0;
//        		selection();
//        		count++;
//        		for(int j = 1;j<15;j++)
//        			select[j][0] = 100;
//        		mark1 = s3.indexOf(' ',6);
//        		mark2 = s3.indexOf(' ',mark1+1);
//        		mark3 = s3.indexOf(' ',mark2+1);
//        		pri = Double.parseDouble(s3.substring(mark3+1));
//        		rel = Double.parseDouble(s3.substring(mark1+1,mark2));
//        		temp = (int)(100*rel-90);
//        		if(pri<select[temp][0])
//        		{
//        			select[temp][0] = pri;
//        			select[temp][1] = i+1;
//        		}
//        		i++;
//        	}
//        }
//        b3.close();
        selection();
//        
//        //文件绝对路径改成你自己的文件路径
//        FileReader f1=new FileReader("D:\\PROCESS.txt");
//        //可以换成工程目录下的其他文本文件
//        BufferedReader b1=new BufferedReader(f1);
//        String s1; 
//        int cnt=0;
//        while((s1=b1.readLine())!=null){
//           len[cnt] = pick(s1,cnt);
//           cnt++;         
//            //System.out.println(s1);
//            //System.out.println("The number is "+len[cnt-1]);
//        }
//        b1.close();   
//        
//        FileReader input = new FileReader("D:\\PROCESS.txt");
//		FileWriter output = new FileWriter("D:\\RESULT.txt");
//		cnt = 0;
//		
//		int c = input.read();
//		while(c != -1){
//			if(c == 10)
//			{
//				output.write(",Reliability =");
//				for(int j = 0;j < len[cnt];j++){
//					reliability*=Recvice[al[cnt][j]-65][0];
//					price+=Recvice[al[cnt][j]-65][1];
//				}
//				
//				output.write(String.format("%.2f", reliability));
//				output.write(",Cost=");
//				output.write(String.format("%.2f", price));
//				output.write(",Q=");
//				output.write(String.format("%.2f",reliability-price/100));
//				reliability = 1;
//				price = 0;
//				cnt++;
//				
//			}
//			output.write(c);
//			if(c<=78&&c>=65){
//				output.write("-");
//				output.write((int)Recvice[c-65][2]+"");
//			}
//			System.out.print((char)c);
//			c = input.read();
//		}
//		input.close();
//		output.write(10);
//		long endTime = System.currentTimeMillis();
//		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); 
//		output.write("运行时间为:");
//		output.write(endTime-startTime+"");
//		output.write("ms");
//		output.close();
    }
//    
// 
//   public static int pick(String s,int cnt){
//    	int n=0;
//    	for (int i=65;i<=78;i++){
//    		if(s.indexOf(i)!=-1){ al[cnt][n]=i;n++;}
//    	}
//    	for(int  i=0;i<n;i++)
//    	{
//    		System.out.println(al[cnt][i]);
//    	}
//    	return n;
//    }
//   

   public static void selection(){
	   int i,j,k;
	   count=1;
	   select[1][0]=120;
	   select[2][1]=5;
	   select[2][0]=10;
	   select[3][0]=30;
	   select[4][0]=40;
	   select[5][0]=50;
	   select[7][0]=160;
	   select[6][0]=160;
	   select[8][0]=150;
	   select[9][0]=140;
	   select[10][0]=130;
//	   select[11][0]=120;
	   Recvice[1][2]=5;
	   Recvice[1][1]=450;
	   Recvice[1][0]=4;
	   for( i = 10;i > 1;i--)
		   for( k = i-1;i > 0;i--)
		   {
			   if((select[i][0] < select[k][0])&&select[i][0] != 100)
			   {
				select[k][0] = 100;
			   	System.out.println(select[k][0]);
			   	System.out.println(select[i][0]);
			   	
			   }
			   
		   }
	   for( j = 1;j < 11;j++)
	   {
		   if(select[j][0]!=100)
		   {
			   if((90+j-select[j][0]) > (Recvice[count][0]*100-Recvice[count][1]))
			   {
				   Recvice[count][0] = (double)j/100+0.9;
				   Recvice[count][1] = select[j][0];
				   Recvice[count][2] = select[j][1];
			   }
		   }
	   }
	   System.out.println(Recvice[count][0]);
	   System.out.println(Recvice[count][1]);
	   System.out.println(Recvice[count][2]);
   }
}