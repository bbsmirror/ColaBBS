package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.BOARD.*;
import colabbs.DATA.BOARDRC.*;

public final class ListBoards extends ListMenu
{
	private boolean NewMode=false;
	private short EGroupNum;
	Vector list;
	
	public ListBoards(int pidbuf,short EGroupNumBuf)
	{
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[78];
		CleanStr="[m  ";
		EGroupNum=EGroupNumBuf;

		pid = pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];

		list = ColaServer.BList.getSortedEGroupList(EGroupNum, User.UFD.Perm, User.BoardSortNum);
		if (User.BoardListCurrent != null)
		{
			now = list.indexOf(User.BoardListCurrent);
			if (now == -1)
				now = 0;
			cursor = now % 20;
			now = now - cursor;
		}
		
		//LoadBrc();
	}
	
	private void ShowBoards(Vector list)
	{
		User.move(1,4);

		int i;
		int N, M;
		N = now / 20;
		M = list.size() - N * 20;
		if (M > 20)
			M = 20;
		
		BoardItem bi;
		for (i = 1; i <= M; i++)
		{
			bi = (BoardItem)list.elementAt(now + i - 1);
			
			if(NewMode)
			{
				if(bi.BM.length()!=0)
					User.sends("   " + STRING.CutLeft("" + bi.getPostNumber(), 5) + haveNew(bi) + STRING.Cut(bi.Name, 17) + "  " + STRING.Cut(bi.Title, 38) + STRING.Cut(bi.BM, 13) + "\r\n");
				else
					User.sends("   " + STRING.CutLeft("" + bi.getPostNumber(), 5) + haveNew(bi) + STRING.Cut(bi.Name, 17) + "  " + STRING.Cut(bi.Title, 38) + STRING.Cut(Prompt.Msgs[52], 13) + "\r\n");
			}
			else
			{
				if(bi.BM.length()!=0)
					User.sends("   " + STRING.CutLeft("" + (now + i), 5) + "  " + STRING.Cut(bi.Name, 17) + "  " + STRING.Cut(bi.Title, 38) + STRING.Cut(bi.BM, 13) + "\r\n");
				else
					User.sends("   " + STRING.CutLeft("" + (now + i), 5) + "  " + STRING.Cut(bi.Name, 17) + "  " + STRING.Cut(bi.Title, 38) + STRING.Cut(Prompt.Msgs[52], 13) + "\r\n");
			}			
		}
		
		for (; i <= 20; i++)
		{
			User.move(1, i + 3);
			User.clrtoeol();
		}
	}
	
	public void DoList()
	{
		int i,j,MyBoardNum=0;
		String sendbuf,OldMode=User.usermode;

		BBS.SetUserMode(pid,Modes.BoardList);

		StringBuffer sb = null;
		
		BoardItem bi;
		
		if ((ListMax = list.size() - 1) == -1)
			return;
		
		User.move(1,1);
		User.printtitle(Prompt.Msgs[72]);
		User.move(1,2);
		User.sends(Prompt.Msgs[73]);
		User.sends(Prompt.Msgs[74]);

		//LoadBrc(); //Maybe put another place is better?
		
ListBoardsLoop:
		while(true)
		{
			ShowBoards(list);			

			switch(ReadKey(true))
			{
			case 'h':
			case 'H':
				User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"boardreadhelp");
				User.PressAnyKey();
				User.Clear();
				User.printtitle(Prompt.Msgs[72]);
				User.move(1,2);
				User.sends(Prompt.Msgs[73]);
				User.sends(Prompt.Msgs[74]);
				break;
			case 'c':
				setNewMode(NewMode^true);
				break;
			case 'S':
				bi = (BoardItem)list.elementAt(now + cursor);
				User.BoardSortNum++;
				if (User.BoardSortNum >= ColaServer.BList.BoardSortNums)
					User.BoardSortNum = 0;
				list = ColaServer.BList.getSortedEGroupList(EGroupNum, User.UFD.Perm, User.BoardSortNum);
				now = list.indexOf(bi);
				cursor = now % 20;
				now = now - cursor;
				break;
			case '/':	
				sb = new StringBuffer();
				User.move(1, 24);
				User.clrtoeol();
				User.move(1, 24);
				User.sends(Prompt.Msgs[468]);
				User.GetData = new LineEdit(sb, 16, pid, true);
				User.GetData.DoEdit();
				User.GetData = null;
				bi = ColaServer.BList.get(sb.toString());
				if (bi != null)
				{
					now = list.indexOf(bi);
					cursor = now % 20;
					now = now - cursor;
				}
				User.printendline();
				break;
			case 's':
				bi = ColaServer.BList.get(User.BoardComplete(Prompt.Msgs[468]));
				if (bi != null)
				{
					now = list.indexOf(bi);
					cursor = now % 20;
					now = now - cursor;
				}
				User.move(1,2);
				User.sends(Prompt.Msgs[73]);
				User.sends(Prompt.Msgs[74]);
				User.printendline();
				break;
			case 'q':
			case 'e':
			case Keys.Left:
				User.BoardListCurrent = (BoardItem)list.elementAt(now + cursor);
				break ListBoardsLoop;
			case 'r':
			case Keys.Enter:
			case Keys.Right:
				int count;
				
				bi = (BoardItem)list.elementAt(now + cursor);

				User.CurrentBoard=bi.Name;
				
				User.Clear();
				
				//Add by WilliamWey
				//for ¶iª©µe­±
				/*String WelcomeFileName = ColaServer.INI.BBSHome + "boards" + File.separator + bi.Name + File.separator + ".Welcome";
				File wf = new File(WelcomeFileName);
				if (wf.exists() && wf.length() > 2)
				{
					User.ansimore(WelcomeFileName);
					User.PressAnyKey();
				}
				WelcomeFileName = null;
				wf = null;*/
				//	
				
				User.DirListBuf=new PostList(bi, pid);
				((PostList)(User.DirListBuf)).Welcome();
				User.DirListBuf.DrawTitle();
				User.DirListBuf.DoList();
				User.DirListBuf=null;
				
				System.gc();

				User.Clear();       //­«µe
				User.move(1,1);
				User.printtitle(Prompt.Msgs[72]);
				User.move(1,2);
				User.sends(Prompt.Msgs[73]);
				User.sends(Prompt.Msgs[74]);
				break;
			case 'v': //³]©w¬°³£Åª¨ú					
				{
					bi = (BoardItem)list.elementAt(now + cursor);
				
					/*Vector vthebrc=(Vector)User..UserPassItem.BRC.get(bi.Name);
					if(vthebrc == null)
					{
						vthebrc = new Vector();
						User..UserPassItem.BRC.put(bi.Name, vthebrc);
					}
				
					vthebrc.removeAllElements();
				
					RecordType xrt = new PostType();
					Object xlockObj = bi;
					String xCurrentPath = ColaServer.INI.BBSHome + "boards" + File.separator + bi.Name + File.separator;
					String xMyDir = Consts.DotDir;
				
					int xn = 0;
					if ((xn = (int)RecordHandler.recordNumber(xrt, xCurrentPath, xMyDir)) < 0)
					{
						User..UserPassItem.BRC.remove(bi.Name);
						break;
					}
					RecordHandler rh = new RecordHandler(new PostType(), xCurrentPath, xMyDir, false);
				
					PostType xr;
					int xb = 0;
					if (xn > ColaServer.INI.MaxBoardRC)
						xb = xn - ColaServer.INI.MaxBoardRC - 1;
					for (int xi = xn - 1; xi >= xb; xi--)
					{
						xr = (PostType)rh.getRecord(xi + 1);
						vthebrc.addElement(new Long(xr.getFileTime()));
					}
					rh.close();*/
					BoardRCItem brci = User.UFD.brclist.getBRC(bi);				
					brci.setArticleNotViewedAll();
				
					RecordType xrt = new PostType();
					Object xlockObj = bi;
					String xCurrentPath = ColaServer.INI.BBSHome + "boards" + File.separator + bi.Name + File.separator;
					String xMyDir = Consts.DotDir;
				
					int xn = 0;
					if ((xn = (int)RecordHandler.recordNumber(xrt, xCurrentPath, xMyDir)) < 0)
						break;
					RecordHandler rh = new RecordHandler(new PostType(), xCurrentPath, xMyDir, false);
				
					PostType xr;
					int xb = 0;
					if (xn > ColaServer.INI.MaxBoardRC)
						xb = xn - ColaServer.INI.MaxBoardRC - 1;
					for (int xi = xn; xi >= xb; xi--)
					{
						xr = (PostType)rh.getRecord(xi);
						brci.setArticleViewed(xr.deleteBody());
					}
					rh.close();
					break;
				}
			case 'V': //³]©w¬°³£¥¼Åª¨ú					
				{
					bi = (BoardItem)list.elementAt(now + cursor);
				
					/*Vector Vthebrc=(Vector)User..UserPassItem.BRC.get(bi.Name);
					if(Vthebrc!=null)
						User..UserPassItem.BRC.remove(bi.Name);*/
					BoardRCItem brci = User.UFD.brclist.getBRC(bi);
					brci.setArticleNotViewedAll();
					break;
				}
			case 0:
				break;
			case -1:
				break ListBoardsLoop;
			}
		}
		BBS.SetUserMode(pid,OldMode);
	}

	private String haveNew(BoardItem board) 
	{
		/*Vector thebrc=(Vector)User..UserPassItem.BRC.get(board.Name);
		if(thebrc!=null&&thebrc.contains(new Long(board.filetime)))
			return Prompt.Msgs[376];
		return Prompt.Msgs[375];*/
		BoardRCItem brci = User.UFD.brclist.getBRC(board.Name);
		if (brci.isArticleViewed(board.filetime))
			return Prompt.Msgs[376];
		return Prompt.Msgs[375];
	}

/*	private void LoadBrc()
	{
		File BrcFile;
		
		if(User..UserPassItem.BRC==null)
		{
			User..UserPassItem.BRC=new Hashtable();
			if((BrcFile=new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User..UFD.ID.charAt(0))+File.separator+User..UFD.ID,".boardrc")).exists())
			{
				RandomAccessFile brc=null;

				try
				{
					byte databuf[]=new byte[16];
					brc=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User..UFD.ID.charAt(0))+File.separator+User..UFD.ID+File.separator+".boardrc","r");
					int brclen=(int)brc.length();
					byte looptimes;
					int index,x;
					long longbuf;
					String boardname=null;

					index=0;

					while(index<brclen)
					{
						Vector tempbrc=new Vector(ColaServer.INI.MaxBoardRC);
						brc.read(databuf);
						looptimes=databuf[15];
						boardname=new String(databuf,0,15);
						if(boardname.indexOf(0)!=-1)
							boardname=boardname.substring(0,boardname.indexOf(0));
						index+=16;
						for(x=0;x<looptimes;x++)
							tempbrc.addElement(new Long(brc.readLong()));
						index+=(looptimes*8);
						User..UserPassItem.BRC.put(boardname,tempbrc);
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(brc!=null)
						{
							brc.close();
							brc=null;
						}	
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}*/

	public void setNewMode(boolean mode) 
	{
		NewMode=mode;
	}
}