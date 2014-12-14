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

        selection();

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