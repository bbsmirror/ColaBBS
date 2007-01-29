package colabbs;

import java.util.*;

public final class Crypt
{
	static byte iobuf[]=new byte[16];
	static byte crypt_block[]=new byte[72];	/* 72 is next multiple of 8 bytes after 66 */
	static int KS[]=new int[32];
	static int S0H[]=new int[64], S1H[]=new int[64], S2H[]=new int[64], S3H[]=new int[64];
	static int S4H[]=new int[64], S5H[]=new int[64], S6H[]=new int[64], S7H[]=new int[64];
	static int S0L[]=new int[64], S1L[]=new int[64], S2L[]=new int[64], S3L[]=new int[64];
	static int S4L[]=new int[64], S5L[]=new int[64], S6L[]=new int[64], S7L[]=new int[64];
	static int out96[]=new int[4];

	final static boolean FDES8Byte=true;

	public Crypt()
	{
		init ((short)0, S0L, S0H);
		init ((short)1, S1L, S1H);
		init ((short)2, S2L, S2H);
		init ((short)3, S3L, S3H);
		init ((short)4, S4L, S4H);
		init ((short)5, S5L, S5H);
		init ((short)6, S6L, S6H);
		init ((short)7, S7L, S7H);
	}
	public String DoCrypt (String pw,String salt)
	{
		String Ans;

		int salt0[] =
		{
		18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
		32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
		48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 5, 6, 7, 8, 9, 10, 11, 12,
	    13, 14, 15, 16, 17,

		18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
	    32, 33, 34, 35, 36, 37, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
		42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
	    58, 59, 60, 61, 62, 63, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
		12, 13, 14, 15, 16, 17,

	    18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
		32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
	    48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,

	    18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
		32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
	    48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
		0, 1, 2, 3, 4
		};
		int salt1[] =
		{
		1152, 1216, 1280, 1344, 1408, 1472, 1536, 1600, 1664,
	    1728, 1792, 1856, 1920, 1984, 2048, 2112, 2176, 2240, 2304,
		2368, 2432, 2496, 2560, 2624, 2688, 2752, 2816, 2880, 2944,
	    3008, 3072, 3136, 3200, 3264, 3328, 3392, 3456, 3520, 3584,
		3648, 3712, 3776, 3840, 3904, 3968, 4032, 0, 64, 128, 192, 256,
	    320, 384, 448, 512, 576, 640, 704, 320, 384, 448, 512, 576, 640,
		704, 768, 832, 896, 960, 1024, 1088,

	    1152, 1216, 1280, 1344, 1408, 1472, 1536, 1600, 1664,
		1728, 1792, 1856, 1920, 1984, 2048, 2112, 2176, 2240, 2304,
	    2368, 2048, 2112, 2176, 2240, 2304, 2368, 2432, 2496, 2560,
		2624, 2688, 2752, 2816, 2880, 2944, 3008, 3072, 3136, 3200,
	    3264, 3328, 3392, 3456, 3520, 3584, 3648, 3712, 3776, 3840,
		3904, 3968, 4032, 0, 64, 128, 192, 256, 320, 384, 448, 512, 576,
	    640, 704, 768, 832, 896, 960, 1024, 1088,

		1152, 1216, 1280, 1344, 1408, 1472, 1536, 1600, 1664,
	    1728, 1792, 1856, 1920, 1984, 2048, 2112, 2176, 2240, 2304,
		2368, 2432, 2496, 2560, 2624, 2688, 2752, 2816, 2880, 2944,
	    3008, 3072, 3136, 3200, 3264, 3328, 3392, 3456, 3520, 3584,
		3648, 3712, 3776, 3840, 3904, 3968, 4032, 0, 64, 128, 192, 256,
	    320, 384, 448, 512, 576, 640, 704, 768, 832, 896, 960, 1024,
		1088,

	    1152, 1216, 1280, 1344, 1408, 1472, 1536, 1600, 1664,
		1728, 1792, 1856, 1920, 1984, 2048, 2112, 2176, 2240, 2304,
	    2368, 2432, 2496, 2560, 2624, 2688, 2752, 2816, 2880, 2944,
		3008, 3072, 3136, 3200, 3264, 3328, 3392, 3456, 3520, 3584,
	    3648, 3712, 3776, 3840, 3904, 3968, 4032, 0, 64, 128, 192, 256
		};

		byte lastone[] =
		{
		(byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)65, (byte)66,
	    (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82,
		(byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103,
	    (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116,
		(byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)123, (byte)124, (byte)125, (byte)126, (byte)127, (byte)128, (byte)129,
	    (byte)130, (byte)131, (byte)132, (byte)133, (byte)134, (byte)135, (byte)136, (byte)137, (byte)138, (byte)139, (byte)140, (byte)141, (byte)142,
		(byte)143, (byte)144, (byte)145, (byte)146, (byte)147, (byte)148, (byte)149, (byte)150, (byte)151, (byte)152, (byte)153, (byte)154, (byte)155,
	    (byte)156, (byte)157, (byte)158, (byte)159, (byte)160, (byte)161, (byte)162, (byte)163, (byte)164, (byte)165, (byte)166, (byte)167, (byte)168,
		(byte)169, (byte)170, (byte)171, (byte)172, (byte)173, (byte)174, (byte)175, (byte)176, (byte)177, (byte)178, (byte)179, (byte)180, (byte)181,
	    (byte)182, (byte)183, (byte)184, (byte)185, (byte)186, (byte)187, (byte)188, (byte)189, (byte)190, (byte)191, (byte)192, (byte)193, (byte)194,
		(byte)195, (byte)196, (byte)197, (byte)198, (byte)199, (byte)200, (byte)201, (byte)202, (byte)203, (byte)204, (byte)205, (byte)206, (byte)207,
	    (byte)208, (byte)209, (byte)210, (byte)211, (byte)212, (byte)213, (byte)214, (byte)215, (byte)216, (byte)217, (byte)218, (byte)219, (byte)220,
		(byte)221, (byte)222, (byte)223, (byte)224, (byte)225, (byte)226, (byte)227, (byte)228, (byte)229, (byte)230, (byte)231, (byte)232, (byte)233,
	    (byte)234, (byte)235, (byte)236, (byte)237, (byte)238, (byte)239, (byte)240, (byte)241, (byte)242, (byte)243, (byte)244, (byte)245, (byte)246,
		(byte)247, (byte)248, (byte)249, (byte)250, (byte)251, (byte)252, (byte)253, (byte)254, (byte)255,
	    /* Truncate overflow bits at 256 */
		(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15,
	    (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31,
		(byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47,
	    (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58
		};

	    byte k;
	    short i, j;
	    short lip;
		int saltvalue;

		for (lip=(short)(crypt_block.length-1);lip>=0;lip--)
			crypt_block[lip]=0;

		for (i=0,j=0; i < 64; j++)
		{
			if(pw.length()>j)
				k=(byte)pw.charAt(j);
			else
				k=0;
	        crypt_block[i++] = (byte)((k >>> 6) & 1);
			crypt_block[i++] = (byte)((k >>> 5) & 1);
			crypt_block[i++] = (byte)((k >>> 4) & 1);
			crypt_block[i++] = (byte)((k >>> 3) & 1);
			crypt_block[i++] = (byte)((k >>> 2) & 1);
			crypt_block[i++] = (byte)((k >>> 1) & 1);
			crypt_block[i++] = (byte)((k >>> 0) & 1);
			i++; //skip parity bit
		}

		fsetkey ();

		for (lip=(short)(crypt_block.length-1);lip>=0;lip--)
			crypt_block[lip]=0;

		iobuf[0] = (byte)salt.charAt(0);
		iobuf[1] = (byte)salt.charAt(1);

		saltvalue = salt0[iobuf[0]] | salt1[iobuf[1]];
		saltvalue = TF_TO_SIXBIT (saltvalue);

//		System.out.println("saltvalue="+saltvalue);

		XForm (saltvalue);
		UnXForm ();

		for (i = 0; i < 11; i++)
		{
	        k=0;

			for (j=0;j<6;j++)
	            k=(byte)((k<<1)|crypt_block[i*6+j]);
			iobuf[i+2]=lastone[k];
		}

		iobuf[i+2]=0;

		if (iobuf[1]==0)
			iobuf[1]=iobuf[0];
//		Ans=new String(iobuf);
		Ans=new String(iobuf,0);
		return (Ans.substring(0,Ans.indexOf(0)));
	}
	private void fsetkey ()
	{
		byte KeyToKS[]=
		{
		9, 50, 33, 59, 48, 16, 32, 56, 1, 8, 18, 41, 2, 34, 25, 24,
	    43, 57, 58, 0, 35, 26, 17, 40, 21, 27, 38, 53, 36, 3, 46, 29,
		4, 52, 22, 28, 60, 20, 37, 62, 14, 19, 44, 13, 12, 61, 54, 30,
	    1, 42, 25, 51, 40, 8, 24, 48, 58, 0, 10, 33, 59, 26, 17, 16,
		35, 49, 50, 57, 56, 18, 9, 32, 13, 19, 30, 45, 28, 62, 38, 21,
	    27, 44, 14, 20, 52, 12, 29, 54, 6, 11, 36, 5, 4, 53, 46, 22,
		50, 26, 9, 35, 24, 57, 8, 32, 42, 49, 59, 17, 43, 10, 1, 0,
	    48, 33, 34, 41, 40, 2, 58, 16, 60, 3, 14, 29, 12, 46, 22, 5,
		11, 28, 61, 4, 36, 27, 13, 38, 53, 62, 20, 52, 19, 37, 30, 6,
	    34, 10, 58, 48, 8, 41, 57, 16, 26, 33, 43, 1, 56, 59, 50, 49,
		32, 17, 18, 25, 24, 51, 42, 0, 44, 54, 61, 13, 27, 30, 6, 52,
	    62, 12, 45, 19, 20, 11, 60, 22, 37, 46, 4, 36, 3, 21, 14, 53,
		18, 59, 42, 32, 57, 25, 41, 0, 10, 17, 56, 50, 40, 43, 34, 33,
	    16, 1, 2, 9, 8, 35, 26, 49, 28, 38, 45, 60, 11, 14, 53, 36,
		46, 27, 29, 3, 4, 62, 44, 6, 21, 30, 19, 20, 54, 5, 61, 37,
	    2, 43, 26, 16, 41, 9, 25, 49, 59, 1, 40, 34, 24, 56, 18, 17,
		0, 50, 51, 58, 57, 48, 10, 33, 12, 22, 29, 44, 62, 61, 37, 20,
	    30, 11, 13, 54, 19, 46, 28, 53, 5, 14, 3, 4, 38, 52, 45, 21,
		51, 56, 10, 0, 25, 58, 9, 33, 43, 50, 24, 18, 8, 40, 2, 1,
	    49, 34, 35, 42, 41, 32, 59, 17, 27, 6, 13, 28, 46, 45, 21, 4,
		14, 62, 60, 38, 3, 30, 12, 37, 52, 61, 54, 19, 22, 36, 29, 5,
	    35, 40, 59, 49, 9, 42, 58, 17, 56, 34, 8, 2, 57, 24, 51, 50,
		33, 18, 48, 26, 25, 16, 43, 1, 11, 53, 60, 12, 30, 29, 5, 19,
	    61, 46, 44, 22, 54, 14, 27, 21, 36, 45, 38, 3, 6, 20, 13, 52,
		56, 32, 51, 41, 1, 34, 50, 9, 48, 26, 0, 59, 49, 16, 43, 42,
	    25, 10, 40, 18, 17, 8, 35, 58, 3, 45, 52, 4, 22, 21, 60, 11,
		53, 38, 36, 14, 46, 6, 19, 13, 28, 37, 30, 62, 61, 12, 5, 44,
	    40, 16, 35, 25, 50, 18, 34, 58, 32, 10, 49, 43, 33, 0, 56, 26,
		9, 59, 24, 2, 1, 57, 48, 42, 54, 29, 36, 19, 6, 5, 44, 62,
	    37, 22, 20, 61, 30, 53, 3, 60, 12, 21, 14, 46, 45, 27, 52, 28,
		24, 0, 48, 9, 34, 2, 18, 42, 16, 59, 33, 56, 17, 49, 40, 10,
	    58, 43, 8, 51, 50, 41, 32, 26, 38, 13, 20, 3, 53, 52, 28, 46,
		21, 6, 4, 45, 14, 37, 54, 44, 27, 5, 61, 30, 29, 11, 36, 12,
	    8, 49, 32, 58, 18, 51, 2, 26, 0, 43, 17, 40, 1, 33, 24, 59,
		42, 56, 57, 35, 34, 25, 16, 10, 22, 60, 4, 54, 37, 36, 12, 30,
	    5, 53, 19, 29, 61, 21, 38, 28, 11, 52, 45, 14, 13, 62, 20, 27,
		57, 33, 16, 42, 2, 35, 51, 10, 49, 56, 1, 24, 50, 17, 8, 43,
	    26, 40, 41, 48, 18, 9, 0, 59, 6, 44, 19, 38, 21, 20, 27, 14,
		52, 37, 3, 13, 45, 5, 22, 12, 62, 36, 29, 61, 60, 46, 4, 11,
	    41, 17, 0, 26, 51, 48, 35, 59, 33, 40, 50, 8, 34, 1, 57, 56,
		10, 24, 25, 32, 2, 58, 49, 43, 53, 28, 3, 22, 5, 4, 11, 61,
	    36, 21, 54, 60, 29, 52, 6, 27, 46, 20, 13, 45, 44, 30, 19, 62,
		25, 1, 49, 10, 35, 32, 48, 43, 17, 24, 34, 57, 18, 50, 41, 40,
	    59, 8, 9, 16, 51, 42, 33, 56, 37, 12, 54, 6, 52, 19, 62, 45,
		20, 5, 38, 44, 13, 36, 53, 11, 30, 4, 60, 29, 28, 14, 3, 46,
	    17, 58, 41, 2, 56, 24, 40, 35, 9, 16, 26, 49, 10, 42, 33, 32,
		51, 0, 1, 8, 43, 34, 25, 48, 29, 4, 46, 61, 44, 11, 54, 37,
	    12, 60, 30, 36, 5, 28, 45, 3, 22, 27, 52, 21, 20, 6, 62, 38
		};

	    short i,k;
	    int r;

		k=0;

		for (i = 0; i < 32; i++)
	    {
	        r = (int) crypt_block[KeyToKS[k++]];
			r |= (int) crypt_block[KeyToKS[k++]] << 1;
	        r |= (int) crypt_block[KeyToKS[k++]] << 2;
			r |= (int) crypt_block[KeyToKS[k++]] << 3;
			r |= (int) crypt_block[KeyToKS[k++]] << 4;
			r |= (int) crypt_block[KeyToKS[k++]] << 5;
			r |= (int) crypt_block[KeyToKS[k++]] << (2 + 6);
			r |= (int) crypt_block[KeyToKS[k++]] << (2 + 7);
			r |= (int) crypt_block[KeyToKS[k++]] << (2 + 8);
			r |= (int) crypt_block[KeyToKS[k++]] << (2 + 9);
			r |= (int) crypt_block[KeyToKS[k++]] << (2 + 10);
			r |= (int) crypt_block[KeyToKS[k++]] << (2 + 11);
			r |= (int) crypt_block[KeyToKS[k++]] << (4 + 12);
			r |= (int) crypt_block[KeyToKS[k++]] << (4 + 13);
			r |= (int) crypt_block[KeyToKS[k++]] << (4 + 14);
			r |= (int) crypt_block[KeyToKS[k++]] << (4 + 15);
			r |= (int) crypt_block[KeyToKS[k++]] << (4 + 16);
			r |= (int) crypt_block[KeyToKS[k++]] << (4 + 17);
			r |= (int) crypt_block[KeyToKS[k++]] << (6 + 18);
			r |= (int) crypt_block[KeyToKS[k++]] << (6 + 19);
			r |= (int) crypt_block[KeyToKS[k++]] << (6 + 20);
			r |= (int) crypt_block[KeyToKS[k++]] << (6 + 21);
			r |= (int) crypt_block[KeyToKS[k++]] << (6 + 22);
			r |= (int) crypt_block[KeyToKS[k++]] << (6 + 23);
			KS[i] = r;
		}
	}
	private static void init(short tableno,int [] lowptr,int [] highptr)
	{
		byte P[] =
		{
	    (byte)15, (byte)6, (byte)19, (byte)20,
		(byte)28, (byte)11, (byte)27, (byte)16,
	    (byte)0, (byte)14, (byte)22, (byte)25,
		(byte)4, (byte)17, (byte)30, (byte)9,
	    (byte)1, (byte)7, (byte)23, (byte)13,
		(byte)31, (byte)26, (byte)2, (byte)8,
	    (byte)18, (byte)12, (byte)29, (byte)5,
		(byte)21, (byte)10, (byte)3, (byte)24,
		};

		byte E[] =
		{
		(byte)31, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4,
	    (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8,
		(byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12,
	    (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16,
		(byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20,
	    (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24,
		(byte)23, (byte)24, (byte)25, (byte)26, (byte)27, (byte)28,
	    (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)0,
		};

		byte tmp32[]=new byte[32];
		byte tmpP32[]=new byte[32];
		byte tmpE[]=new byte[48];

		byte k,j;

		short i;
		int tablenoX4;

		tablenoX4 = tableno * 4;

		for(j=0;j<64;j++)
		{
	        k=lookupS(tableno,j);

			for(i=0;i<32;i++)
			    tmp32[i] = 0;

			for(i=0;i<4;i++)
			    tmp32[tablenoX4+i]=(byte)((k>>>i)&01);

			for(i=0;i<32;i++)
			    tmpP32[i]=tmp32[P[i]];

			for(i=0;i<48;i++)
			    tmpE[i]=tmpP32[E[i]];

			lowptr[j]=0;
			highptr[j]=0;

			for(i=0;i<24;i++)
	            lowptr[j]|=(int)tmpE[i]<<i;

			for (k=0,i=24;i<48;i++,k++)
			    highptr[j]|=(int)tmpE[i]<<k;

			lowptr[j]=TF_TO_SIXBIT(lowptr[j]);
			highptr[j]=TF_TO_SIXBIT(highptr[j]);
		}
	}
/********* INITIALISATION ROUTINES *********/

	private static byte lookupS (short tableno,byte t6bits)
	{
		byte S[] = //8*64
		{
	    (byte)14, (byte)4, (byte)13, (byte)1, (byte)2, (byte)15, (byte)11, (byte)8, (byte)3, (byte)10, (byte)6, (byte)12, (byte)5, (byte)9, (byte)0, (byte)7,
		(byte)0, (byte)15, (byte)7, (byte)4, (byte)14, (byte)2, (byte)13, (byte)1, (byte)10, (byte)6, (byte)12, (byte)11, (byte)9, (byte)5, (byte)3, (byte)8,
	    (byte)4, (byte)1, (byte)14, (byte)8, (byte)13, (byte)6, (byte)2, (byte)11, (byte)15, (byte)12, (byte)9, (byte)7, (byte)3, (byte)10, (byte)5, (byte)0,
		(byte)15, (byte)12, (byte)8, (byte)2, (byte)4, (byte)9, (byte)1, (byte)7, (byte)5, (byte)11, (byte)3, (byte)14, (byte)10, (byte)0, (byte)6, (byte)13,

	    (byte)15, (byte)1, (byte)8, (byte)14, (byte)6, (byte)11, (byte)3, (byte)4, (byte)9, (byte)7, (byte)2, (byte)13, (byte)12, (byte)0, (byte)5, (byte)10,
		(byte)3, (byte)13, (byte)4, (byte)7, (byte)15, (byte)2, (byte)8, (byte)14, (byte)12, (byte)0, (byte)1, (byte)10, (byte)6, (byte)9, (byte)11, (byte)5,
	    (byte)0, (byte)14, (byte)7, (byte)11, (byte)10, (byte)4, (byte)13, (byte)1, (byte)5, (byte)8, (byte)12, (byte)6, (byte)9, (byte)3, (byte)2, (byte)15,
		(byte)13, (byte)8, (byte)10, (byte)1, (byte)3, (byte)15, (byte)4, (byte)2, (byte)11, (byte)6, (byte)7, (byte)12, (byte)0, (byte)5, (byte)14, (byte)9,

	    (byte)10, (byte)0, (byte)9, (byte)14, (byte)6, (byte)3, (byte)15, (byte)5, (byte)1, (byte)13, (byte)12, (byte)7, (byte)11, (byte)4, (byte)2, (byte)8,
		(byte)13, (byte)7, (byte)0, (byte)9, (byte)3, (byte)4, (byte)6, (byte)10, (byte)2, (byte)8, (byte)5, (byte)14, (byte)12, (byte)11, (byte)15, (byte)1,
	    (byte)13, (byte)6, (byte)4, (byte)9, (byte)8, (byte)15, (byte)3, (byte)0, (byte)11, (byte)1, (byte)2, (byte)12, (byte)5, (byte)10, (byte)14, (byte)7,
		(byte)1, (byte)10, (byte)13, (byte)0, (byte)6, (byte)9, (byte)8, (byte)7, (byte)4, (byte)15, (byte)14, (byte)3, (byte)11, (byte)5, (byte)2, (byte)12,

	    (byte)7, (byte)13, (byte)14, (byte)3, (byte)0, (byte)6, (byte)9, (byte)10, (byte)1, (byte)2, (byte)8, (byte)5, (byte)11, (byte)12, (byte)4, (byte)15,
		(byte)13, (byte)8, (byte)11, (byte)5, (byte)6, (byte)15, (byte)0, (byte)3, (byte)4, (byte)7, (byte)2, (byte)12, (byte)1, (byte)10, (byte)14, (byte)9,
	    (byte)10, (byte)6, (byte)9, (byte)0, (byte)12, (byte)11, (byte)7, (byte)13, (byte)15, (byte)1, (byte)3, (byte)14, (byte)5, (byte)2, (byte)8, (byte)4,
		(byte)3, (byte)15, (byte)0, (byte)6, (byte)10, (byte)1, (byte)13, (byte)8, (byte)9, (byte)4, (byte)5, (byte)11, (byte)12, (byte)7, (byte)2, (byte)14,

	    (byte)2, (byte)12, (byte)4, (byte)1, (byte)7, (byte)10, (byte)11, (byte)6, (byte)8, (byte)5, (byte)3, (byte)15, (byte)13, (byte)0, (byte)14, (byte)9,
		(byte)14, (byte)11, (byte)2, (byte)12, (byte)4, (byte)7, (byte)13, (byte)1, (byte)5, (byte)0, (byte)15, (byte)10, (byte)3, (byte)9, (byte)8, (byte)6,
	    (byte)4, (byte)2, (byte)1, (byte)11, (byte)10, (byte)13, (byte)7, (byte)8, (byte)15, (byte)9, (byte)12, (byte)5, (byte)6, (byte)3, (byte)0, (byte)14,
		(byte)11, (byte)8, (byte)12, (byte)7, (byte)1, (byte)14, (byte)2, (byte)13, (byte)6, (byte)15, (byte)0, (byte)9, (byte)10, (byte)4, (byte)5, (byte)3,

	    (byte)12, (byte)1, (byte)10, (byte)15, (byte)9, (byte)2, (byte)6, (byte)8, (byte)0, (byte)13, (byte)3, (byte)4, (byte)14, (byte)7, (byte)5, (byte)11,
		(byte)10, (byte)15, (byte)4, (byte)2, (byte)7, (byte)12, (byte)9, (byte)5, (byte)6, (byte)1, (byte)13, (byte)14, (byte)0, (byte)11, (byte)3, (byte)8,
	    (byte)9, (byte)14, (byte)15, (byte)5, (byte)2, (byte)8, (byte)12, (byte)3, (byte)7, (byte)0, (byte)4, (byte)10, (byte)1, (byte)13, (byte)11, (byte)6,
		(byte)4, (byte)3, (byte)2, (byte)12, (byte)9, (byte)5, (byte)15, (byte)10, (byte)11, (byte)14, (byte)1, (byte)7, (byte)6, (byte)0, (byte)8, (byte)13,

	    (byte)4, (byte)11, (byte)2, (byte)14, (byte)15, (byte)0, (byte)8, (byte)13, (byte)3, (byte)12, (byte)9, (byte)7, (byte)5, (byte)10, (byte)6, (byte)1,
		(byte)13, (byte)0, (byte)11, (byte)7, (byte)4, (byte)9, (byte)1, (byte)10, (byte)14, (byte)3, (byte)5, (byte)12, (byte)2, (byte)15, (byte)8, (byte)6,
	    (byte)1, (byte)4, (byte)11, (byte)13, (byte)12, (byte)3, (byte)7, (byte)14, (byte)10, (byte)15, (byte)6, (byte)8, (byte)0, (byte)5, (byte)9, (byte)2,
		(byte)6, (byte)11, (byte)13, (byte)8, (byte)1, (byte)4, (byte)10, (byte)7, (byte)9, (byte)5, (byte)0, (byte)15, (byte)14, (byte)2, (byte)3, (byte)12,

	    (byte)13, (byte)2, (byte)8, (byte)4, (byte)6, (byte)15, (byte)11, (byte)1, (byte)10, (byte)9, (byte)3, (byte)14, (byte)5, (byte)0, (byte)12, (byte)7,
		(byte)1, (byte)15, (byte)13, (byte)8, (byte)10, (byte)3, (byte)7, (byte)4, (byte)12, (byte)5, (byte)6, (byte)11, (byte)0, (byte)14, (byte)9, (byte)2,
		(byte)7, (byte)11, (byte)4, (byte)1, (byte)9, (byte)12, (byte)14, (byte)2, (byte)0, (byte)6, (byte)10, (byte)13, (byte)15, (byte)3, (byte)5, (byte)8,
	    (byte)2, (byte)1, (byte)14, (byte)7, (byte)4, (byte)10, (byte)8, (byte)13, (byte)15, (byte)12, (byte)9, (byte)0, (byte)3, (byte)5, (byte)6, (byte)11,
		};
		byte fixed6bits;
		byte r;
		byte fixedr;

		fixed6bits=(byte)(((t6bits&1)<<5)+(((t6bits>>>1)&1)<<3)+(((t6bits>>>2)&1)<<2)+(((t6bits>>>3)&1)<<1)+(((t6bits>>>4)&1)<<0)+(((t6bits>>>5)&1)<<4));
		r = S[tableno*64+fixed6bits];
		fixedr=(byte)((((r>>>3)&1)<<0)+(((r>>>2)&1)<<1)+(((r>>>1)&1)<<2)+((r&1)<<3)); //reverse bit locate

		return (fixedr);
	}
	private static int SIXBIT_TO_TF(int sb)
	{	// transfer six-bits pair next to each other in 4 bytes
		return ((sb&0x3f)|((sb&0x3f00)>>>2)|((sb&0x3f0000)>>>4)|((sb&0x3f000000)>>>6));
	}
	private static int TF_TO_SIXBIT(int tf)
	{	// transfer six-bits pair have 2 bits '00' between each other in 4 bytes
		return ((tf & 077)|((tf & 07700) << 2)|((tf & 0770000) << 4)|((tf & 077000000) << 6));
	}
	private void UnXForm ()
	{
		int Rl,Rh,Ll,Lh;
		int mask;
		short ptr,lip;

		Ll = SIXBIT_TO_TF (out96[0]);
		Lh = SIXBIT_TO_TF (out96[1]);
		Rl = SIXBIT_TO_TF (out96[2]);
		Rh = SIXBIT_TO_TF (out96[3]);

		for (lip=(short)(crypt_block.length-1);lip>=0;lip--)
			crypt_block[lip]=0;

		ptr=0;
		mask = 0x000400;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x400000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000400;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x400000;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000200;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x200000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000200;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x200000;
		if((Rh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000100;
		if((Rl & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x100000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000100;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x100000;
		if((Rh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000080;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x080000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000080;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x080000;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000010;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x010000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000010;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x010000;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000008;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
	    mask = 0x008000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000008;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x008000;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000004;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x004000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000004;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
	    if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x004000;
		if((Rh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000002;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x002000;
		if((Rl & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Ll & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x000002;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
		mask = 0x002000;
		if((Rh & mask)!=0)
	        crypt_block[ptr] = 0x01;
		ptr++;
		if((Lh & mask)!=0)
			crypt_block[ptr] = 0x01;
		ptr++;
	}
	private void XForm (int saltvalue)
	{
	    byte sdata[]=new byte[8];
		int Rl,Rh,Ll,Lh;
		int k,tmpi;
	    short loop,kloop,dp,kp;

		Ll = Lh = Rl = Rh = 0;

	    for (loop=25;loop>0;loop--)
		{
	        kp=0;
			for (kloop=8;kloop>0;kloop--)
			{
		        k = (Rl^Rh)&saltvalue;
		        tmpi = (k^Rl^ KS[kp++]);
				sdata[3] = (byte)((tmpi>>>24)&0x00ff);
		        sdata[2] = (byte)((tmpi>>>16)&0x00ff);
				sdata[1] = (byte)((tmpi>>>8)&0x00ff);
		        sdata[0] = (byte)((tmpi)&0x00ff);
				tmpi = (k^Rh^KS[kp++]);
		        sdata[7] = (byte)((tmpi>>>24)&0x00ff);
				sdata[6] = (byte)((tmpi>>>16)&0x00ff);
		        sdata[5] = (byte)((tmpi>>>8)&0x00ff);
				sdata[4] = (byte)((tmpi)&0x00ff);

				//STEP=++,Start=0
				dp = 0;
				Lh^=S0H[sdata[dp]];
				Ll^=S0L[sdata[dp++]];
				Lh^=S1H[sdata[dp]];
				Ll^=S1L[sdata[dp++]];
				Lh^=S2H[sdata[dp]];
				Ll^=S2L[sdata[dp++]];
				Lh^=S3H[sdata[dp]];
				Ll^=S3L[sdata[dp++]];
			    Lh^=S4H[sdata[dp]];
				Ll^=S4L[sdata[dp++]];
			    Lh^=S5H[sdata[dp]];
				Ll^=S5L[sdata[dp++]];
			    Lh^=S6H[sdata[dp]];
				Ll^=S6L[sdata[dp++]];
			    Lh^=S7H[sdata[dp]];
				Ll^=S7L[sdata[dp++]];

				k = (Ll^Lh)&saltvalue;

			    tmpi=(k^Ll^KS[kp++]);
			    sdata[3] =(byte)((tmpi>>>24)&0x00ff);
			    sdata[2] =(byte)((tmpi>>>16)&0x00ff);
			    sdata[1] =(byte)((tmpi>>>8)&0x00ff);
			    sdata[0] =(byte)((tmpi)&0x00ff);
			    tmpi=(k^Lh^KS[kp++]);
			    sdata[7] =(byte)((tmpi>>>24)&0x00ff);
			    sdata[6] =(byte)((tmpi>>>16)&0x00ff);
				sdata[5] =(byte)((tmpi>>>8)&0x00ff);
			    sdata[4] =(byte)((tmpi)&0x00ff);

			    //STEP=++,Start=0
			    dp=0;
			    Rh^=S0H[sdata[dp]];
				Rl^=S0L[sdata[dp++]];
			    Rh^=S1H[sdata[dp]];
				Rl^=S1L[sdata[dp++]];
			    Rh^=S2H[sdata[dp]];
				Rl^=S2L[sdata[dp++]];
			    Rh^=S3H[sdata[dp]];
				Rl^=S3L[sdata[dp++]];
				Rh^=S4H[sdata[dp]];
				Rl^=S4L[sdata[dp++]];
			    Rh^=S5H[sdata[dp]];
				Rl^=S5L[sdata[dp++]];
			    Rh^=S6H[sdata[dp]];
				Rl^=S6L[sdata[dp++]];
			    Rh^=S7H[sdata[dp]];
				Rl^=S7L[sdata[dp++]];
			}

		    Ll^=Rl;
			Lh^=Rh;
			Rl^=Ll;
			Rh^=Lh;
			Ll^=Rl;
			Lh^=Rh;
		}
		{
	    short qp;
		qp = 0;
	    out96[qp++] = Ll;
		out96[qp++] = Lh;
	    out96[qp++] = Rl;
		out96[qp++] = Rh;
		}
	}
}