package colabbs.DATA.ACBOARD;

import java.util.*;

/** 
 * ¬¡°Ê¬ÝªO by WilliamWey
 * 1999/07/05
 * 
 * ¬¡°Ê¬ÝªOªº¥DÅé
 * 
 */
public class ACBoardList
{
	private Vector ACBList = null;
	
	private Random R;

	public ACBoardList()
	{
		ACBList = new Vector();
		clear();
		R = new Random();
	}
	
	public void add(ACBoardItem acbi)
	{
		ACBList.addElement(acbi);
	}
	
	public void add(String Text)
	{
		ACBList.addElement(new ACBoardItem(Text));
	}

	public void clear()
	{
		ACBList.removeAllElements();
		add("        [1;44m[33m infoX Studio \u00a6\u00b8\u00a5\u0040\u00ac\u00f6\u00b8\u00ea\u00b0\u0054\u00a4\u00e5\u00a4\u00c6\u00a4\u0075\u00a7\u0040\u00ab\u00c7 http://www.infoX.net/        \r\n" + 
		    "[m     [1;44m[33m  Cola BBS HomePage http://www.infoX.net/product/colabbs/    \r\n" +
		    "[m        [1;35m \u00a7\u00da\u00ad\u00cc\u00a8\u00e3\u00a6\u00b3\u00b1\u004d\u00b7\u007e\u00aa\u00ba\u00b5\u007b\u00a6\u00a1\u00b3\u005d\u00ad\u0070\u00a1\u0042\u00ba\u00f4\u00ad\u00b6\u00bb\u0073\u00a7\u0040\u00a1\u0042\u00a6\u00f8\u00aa\u0041\u00be\u00b9\u00ac\u005b\u00b3\u005d\u00a4\u0075\u00b5\u007b\u00ae\u0076              \r\n" +
		    "[m        [1;36m \u00ad\u0059\u00b1\u007a\u00a6\u00b3\u00bb\u00dd\u00ad\u006e\u00a1\u0041\u00bd\u00d0\u00bb\u0050\u00a7\u00da\u00ad\u00cc\u00c1\u0070\u00b5\u00b8\u00a1\u0047                                      \r\n" + 
		    "[m        [1;36m   \u00b5\u007b\u00a6\u00a1\u00b3\u005d\u00ad\u0070/\u00a8\u0074\u00b2\u00ce\u00ba\u00fb\u00c5\u0040\u00a4\u0075\u00b5\u007b\u00ae\u0076  \u00a7\u0064\u00ad\u005e\u00bb\u00a8 Email:is85003@cis.nctu.edu.tw \r\n");
	}
	
	public String getString()
	{
		return getItem().Text; 
	}

	public ACBoardItem getItem()
	{
		int S = ACBList.size();
		int n = R.nextInt();
		if (n < 0)
			n += Integer.MAX_VALUE + 1;
		n %= S;
		return (ACBoardItem)ACBList.elementAt(n);
	}
}
