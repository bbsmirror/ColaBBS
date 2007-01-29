package colabbs.DATA.MENU;

import java.net.*;
import java.io.*;
import java.util.*;

public final class MainMenuItem
{
	public int Perm;
	public String EName,Name,FuncName;




	public MainMenuItem(String ENameBuf,String NameBuf,String FuncNameBuf,int PermBuf)
	{
		EName=ENameBuf;
		Name=NameBuf;
		FuncName=FuncNameBuf;
		Perm=PermBuf;
	}
}