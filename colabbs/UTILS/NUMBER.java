package colabbs.UTILS;

import java.util.*;

public final class NUMBER
{
	private final static Random random = new Random((new Date()).getTime());
	
	public static int getIntRnd(int range)
	{
		return Math.abs(random.nextInt() % range);
	}
	
	public static long getLongRnd(long range) 
	{
		return Math.abs(random.nextLong() % range);
	}
	
	public static int GetInt(byte[] buf,int index)
	{
		int buffer=0;

		buffer=(int)(buf[index]&0x000000ff);
		buffer<<=8;
		buffer|=(int)(buf[index+1]&0x000000ff);
		buffer<<=8;
		buffer|=(int)(buf[index+2]&0x000000ff);
		buffer<<=8;
		buffer|=(int)(buf[index+3]&0x000000ff);
		return buffer;
	}
	public static long GetLong(byte[] buf,int index)
	{
		long buffer=0;

		buffer=(long)(buf[index]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+1]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+2]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+3]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+4]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+5]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+6]&0x00000000000000ffL);
		buffer<<=8;
		buffer|=(long)(buf[index+7]&0x00000000000000ffL);
		return buffer;
	}
	public static short GetShort(byte[] buf,int index)
	{
		short buffer=0;

		buffer=(short)(buf[index]&0x00ff);
		buffer<<=8;
		buffer|=(short)(buf[index+1]&0x00ff);
		return buffer;
	}
	public static void PutNum(byte[] buf,int index,int num)
	{
		int i;

		for(i=0;i<4;i++)
			buf[index+i]=(byte)((num>>((3-i)*8))&0x000000ff);
	}
	public static void PutNum(byte[] buf,int index,long num)
	{
		int i;

		for(i=0;i<8;i++)
			buf[index+i]=(byte)((num>>((7-i)*8))&0x00000000000000ffL);
	}
	public static void PutNum(byte[] buf,int index,short num)
	{
		int i;

		for(i=0;i<2;i++)
			buf[index+i]=(byte)((num>>((1-i)*8))&0x00ff);
	}
}
