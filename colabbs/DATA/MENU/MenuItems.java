package colabbs.DATA.MENU;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.telnet.*;
import colabbs.UTILS.*;

public final class MenuItems
{
	public String Name,MenuName;
	public int x,y,BackGround;
	private int ItemNum;
	private LinkList Items;

	public MenuItems(String NameBuf,String MenuNameBuf,int xbuf,int ybuf,int Backbuf)
	{
		Name=NameBuf;
		MenuName=MenuNameBuf;
		x=xbuf;
		y=ybuf;
		BackGround=Backbuf;
		ItemNum=0;
		Items=new LinkList();
	}
	public void AddItem(String EName,String Name,String ItemName,int Perm)
	{
		Items.radditem(new MainMenuItem(EName,Name,ItemName,Perm));
		ItemNum++;
	}
	public void Dump()
	{
		MainMenuItem ItemBuf;
		LinkType Base=Items.GetBase();
		LinkType ptr=Base;

		System.out.println("In Menu "+Name+"["+MenuName+"]");
		do
		{
			ItemBuf=(MainMenuItem)ptr.obj;
			ptr=ptr.next;
			if(ItemBuf!=null)
				System.out.println("Name->"+ItemBuf.EName+" "+ItemBuf.Name+" "+ItemBuf.FuncName);
		}while(ptr!=Base);
	}
	public void Dump(int pid,int start)
	{
		int ly=y,i;
		MainMenuItem ItemBuf;
		LinkType Base=Items.GetBase();
		LinkType ptr=Base;

		((TelnetUser)ColaServer.BBSUsers[pid]).move(1,5+ColaServer.INI.MaxActBoard);
		if(BackGround!=-1)
			ColaServer.BBSUsers[pid].fastsends(ColaServer.BBSMenus.BackGrounds[BackGround]);
/*		else
		{
			for(i=5+ColaServer.INI.MaxActBoard;i<24;i++)
			{
				((TelnetUser)ColaServer.BBSUsers[pid]).move(1,i);
				((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			}
		}*/
		((TelnetUser)ColaServer.BBSUsers[pid]).move(1,1);
		((TelnetUser)ColaServer.BBSUsers[pid]).printtitle(Name);
		((TelnetUser)ColaServer.BBSUsers[pid]).printendline();

		for(i=0;i<start;i++)
			ptr=ptr.next;
		do
		{
			ItemBuf=(MainMenuItem)ptr.obj;
			if(ItemBuf!=null&&BBS.HasOnePerm(pid,((MainMenuItem)ptr.obj).Perm))
			{
				((TelnetUser)ColaServer.BBSUsers[pid]).move(x,ly);
				ColaServer.BBSUsers[pid].sends(ItemBuf.Name);
				ly++;
			}
			ptr=ptr.next;
		}while(ptr!=Base&&ly<23);
	}
	public int GetNum(int pid)
	{
		int i,num=1;
		LinkType ptr=Items.GetBase();

		for(i=1;i<ItemNum;i++)
		{
			if(BBS.HasOnePerm(pid,((MainMenuItem)ptr.obj).Perm))
				num++;
			ptr=ptr.next;
		}

		return num;
	}
	public int Goto(int pid,String FuncNameBuf)
	{
		int index=0;
		MainMenuItem ItemBuf;
		LinkType Base=Items.GetBase();
		LinkType ptr=Base;

		do
		{
			ItemBuf=(MainMenuItem)ptr.obj;
			ptr=ptr.next;
			if(ItemBuf!=null&&BBS.HasOnePerm(pid,ItemBuf.Perm))
			{
				if(ItemBuf.FuncName.equals(FuncNameBuf))
					return index;
			}
			else
				continue;
			index++;
		}while(ptr!=Base);
		return -1;
	}
	public MainMenuItem ItemAt(int pid,int index)
	{
		int i;
		MainMenuItem ItemBuf;
		LinkType ptr=Items.GetBase();

//		for(i=0;i<index;i++)
		i=0;
		while(true)
		{
			if(ptr==null)
				return null;
			if(BBS.HasOnePerm(pid,((MainMenuItem)ptr.obj).Perm))
			{
				i++;
				if(i>index)
					break;
				ptr=ptr.next;
			}	
			else
			{
				ptr=ptr.next;
			}	
		}

		ItemBuf=(MainMenuItem)ptr.obj;
		return ItemBuf;
	}
	public int Where(int pid,char OneKey)
	{
		int index=0;
		MainMenuItem ItemBuf;
		LinkType Base=Items.GetBase();
		LinkType ptr=Base;

		do
		{
			ItemBuf=(MainMenuItem)ptr.obj;
			ptr=ptr.next;
			if(ItemBuf!=null&&BBS.HasOnePerm(pid,ItemBuf.Perm))
			{
				if(ItemBuf.EName.toUpperCase().charAt(0)==Character.toUpperCase(OneKey))
					return index;
			}
			else
				continue;
			index++;
		}while(ptr!=Base);
		return -1;
	}
}