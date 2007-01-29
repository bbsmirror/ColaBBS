package colabbs.DATA.MENU;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;

public final class AllMenu
{
	private LinkList Menus;
	public byte[][] BackGrounds=null;

	public AllMenu()
	{
		int state;
		String ReadBuf;
		Menus=new LinkList();
		MenuItems MenuBuf=null;
		FileInputStream MenuFileIS=null;

		try
		{
			MenuFileIS=new FileInputStream("MENU.INI");
			String EnglishName=null;
			StreamTokenizer MenuFile=new StreamTokenizer(MenuFileIS);

			MenuFile.wordChars('0','z');
			MenuFile.whitespaceChars(' ','/');
			MenuFile.commentChar('#');
			MenuFile.ordinaryChar('=');
			MenuFile.quoteChar('"');
			while((state=MenuFile.nextToken())!=StreamTokenizer.TT_EOF)
			{
				if(state==StreamTokenizer.TT_WORD||state=='"')
				{
					if(MenuFile.sval.indexOf('[')>=0)
					{
						EnglishName=MenuFile.sval.substring(MenuFile.sval.indexOf('[')+1,MenuFile.sval.indexOf(']'));
						if(EnglishName.equals("BackGround"))
						{
							String backname;
							MenuFile.nextToken();

							int i,num=(new Integer(MenuFile.sval)).intValue();
							BackGrounds=new byte[num][];

							for(i=0;i<num;i++)
							{
								MenuFile.nextToken();
								backname=MenuFile.sval;
								InputStream inf=null;
								
								try
								{
									File readf=new File(ColaServer.INI.BBSHome+"etc"+File.separator+backname);

									if(!readf.exists())
										continue;
									inf=new FileInputStream(readf);

									BackGrounds[i]=new byte[(int)readf.length()];
									inf.read(BackGrounds[i],0,(int)readf.length());
								}
								catch(IOException e)
								{
									e.printStackTrace();
								}
								finally
								{
									try
									{
										if(inf!=null)
										{
											inf.close();
											inf=null;
										}	
									}
									catch(IOException e)
									{
										e.printStackTrace();
									}	
								}	
							}
						}
					}
					else if(MenuFile.sval.equals("Attrib"))
					{
						int mx,my,backval;
						String Name;

						MenuFile.nextToken();
						MenuFile.nextToken();
						Name=MenuFile.sval;
						MenuFile.nextToken();
						mx=(new Integer(MenuFile.sval)).intValue();
						MenuFile.nextToken();
						my=(new Integer(MenuFile.sval)).intValue();
						MenuFile.nextToken();
						backval=(new Integer(MenuFile.sval)).intValue();
						MenuBuf=AddMenu(Name,EnglishName,mx,my,backval);
					}
					else if(MenuFile.sval.length()>0)
					{
						String EName,CName,CMD;

						EName=MenuFile.sval;
						MenuFile.nextToken();
						MenuFile.nextToken();
						CName=MenuFile.sval;
						MenuFile.nextToken();
						CMD=MenuFile.sval;
						MenuFile.nextToken();
						MenuBuf.AddItem(EName,CName,CMD,Perm.StrToPerm(MenuFile.sval));
					}
				}
			}
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
		catch(NumberFormatException e2){}
		finally
		{
			try
			{
				if(MenuFileIS!=null)
				{
					MenuFileIS.close();
					MenuFileIS=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}	
		}	
	}
	public MenuItems AddMenu(String Name,String MenuName,int x,int y,int Back)
	{
		MenuItems MenuBuf=new MenuItems(Name,MenuName,x,y,Back);
		Menus.radditem(MenuBuf);
		return MenuBuf;
	}
	public void Dump()
	{
		MenuItems MenuBuf;
		LinkType Base=Menus.GetBase();
		LinkType ptr=Base;

		do
		{
			MenuBuf=(MenuItems)ptr.obj;
			ptr=ptr.next;
			if(MenuBuf!=null)
				MenuBuf.Dump();
		}while(ptr!=Base);
	}
	public MenuItems SearchMenu(String SKey)
	{
		MenuItems MenuBuf;
		LinkType Base=Menus.GetBase();
		LinkType ptr=Base;

		do
		{
			MenuBuf=(MenuItems)ptr.obj;
			ptr=ptr.next;
			if(MenuBuf!=null)
				if(MenuBuf.MenuName.equals(SKey))
					return MenuBuf;
		}while(ptr!=Base);
		return null;
	}
}