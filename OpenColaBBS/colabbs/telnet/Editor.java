package colabbs.telnet;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;
import colabbs.DATA.BOARD.*;

public final class Editor
{
	private boolean InsertMode = true;
	private boolean ColorMode = false;
	private boolean NormalEditor = false;
	private boolean EditArticle = false;
	private short LineNum, EditX, EditY, ScrX, ScrY;
	private LinkList Data = new LinkList();
	private LinkType first, nowptr;
	private int AnsiNum;
	private String SavePath;
	private String OwnBuf = null;
	private boolean AnonyFlag;
	private BoardItem Board;
	private String Title;

	private int pid = -1;
	private TelnetUser User = null;
	
	public Editor(int xpid)
	{
		pid = xpid;
		User = (TelnetUser)ColaServer.BBSUsers[xpid];
	}
	
	public Editor(TelnetUser tu)
	{
		pid = tu.pid;
		User = tu;
	}
	
	private void init()
	{
		LineNum = 0;
		EditX = 0;
		EditY = 0;
		ScrX = 0;
		ScrY = 0;
		Title = null;
		Board = null;
		AnonyFlag = false;
		NormalEditor = false;
	}

	private void addSigFile(int no)
	{
		String SigTemp[] = User.getSignFile(no);
		if (SigTemp != null)
		{
			Data.radditem(new StringBuffer("--"));
			LineNum++;
			for (int i = 0; i < SigTemp.length; i++)
			{
				Data.radditem(new StringBuffer(SigTemp[i]));
				LineNum++;
			}
		}		
	}
	
	public String DoEdit(String FileName)
	{
		try
		{
			
			String ReadBuffer;
			
			init();
			
			NormalEditor = true;
			SavePath = FileName;
			
			File F = new File(FileName);
			if (F.exists())
			{
				DataInputStream SourceFile = new DataInputStream(new FileInputStream(F));
				while((ReadBuffer = SourceFile.readLine()) != null)
				{
					Data.radditem(new StringBuffer(ReadBuffer));
					LineNum++;
				}
				SourceFile.close();
			}
			
			if (LineNum == 0)
			{
				Data.radditem(new StringBuffer());
				LineNum++;
			}

			first = Data.GetBase();
			nowptr = first;
			
		}
		catch (Exception e)
		{
			System.err.println("SHIT!!");
		}
		
		return DoEdit();
	}

	//¦^«H¥ó
	public String DoEdit(String TitleBuf, File From, String Target, String QuoteID, char QuoteMode, byte Sig)
	{
		try
		{
			
			String ReadBuffer;
			
			init();

			Title = TitleBuf;
			SavePath = Target;
			Board = null;
			AnonyFlag = false;
			
			if(QuoteMode=='E')
			{
				OwnBuf=QuoteID;
				EditArticle=true;
			}
			
			if(From.exists())
			{
				DataInputStream SigFile = null;
				DataInputStream SourceFile = new DataInputStream(new FileInputStream(From));
				
				while((ReadBuffer = SourceFile.readLine())!=null)
				{
					if(ReadBuffer.length() == 0)
						break;
					else if(ReadBuffer.length() > Prompt.Msgs[46].length() && ReadBuffer.substring(0, Prompt.Msgs[46].length()).equals(Prompt.Msgs[46]))
					{
						OwnBuf = ReadBuffer.substring(Prompt.Msgs[46].length());
						if (OwnBuf.lastIndexOf(')') != -1)
							OwnBuf = OwnBuf.substring(0, OwnBuf.lastIndexOf(')') + 1);
						else if (OwnBuf.indexOf(' ') != -1)
							OwnBuf = OwnBuf.substring(0, OwnBuf.indexOf(' ') + 1);
						if (QuoteID.indexOf('@') != -1)
							OwnBuf = QuoteID;
					}
				}

				if(QuoteMode!='E')
				{
					if(OwnBuf != null)
						Data.radditem(new StringBuffer(Prompt.Msgs[38] + OwnBuf+Prompt.Msgs[270]));
					else
						Data.radditem(new StringBuffer(Prompt.Msgs[38] + QuoteID+Prompt.Msgs[270]));
					LineNum++;
				}

				while((ReadBuffer = SourceFile.readLine()) != null)
				{
					if(QuoteMode!='R'&&QuoteMode!='E')
					{
						int level = 0, LastIndex = 0;
						while (ReadBuffer.length() > LastIndex + 1)
						{
							if(ReadBuffer.substring(LastIndex, LastIndex+2).equals(": "))
								level++;
							else
								break;
							LastIndex += 2;
						}
						LastIndex = 0;
						if (((ReadBuffer.length() >= 7 && (ReadBuffer.substring(0, 7).equals(Prompt.Msgs[48])) || (ReadBuffer.length() >= 8 && ReadBuffer.substring(0, 8).equals(Prompt.Msgs[38])))) || (ReadBuffer.length() >= 3 && ReadBuffer.substring(0, 3).equals("==>")) || (ReadBuffer.length() >= 5 && ReadBuffer.substring(0, 5).equals(Prompt.Msgs[49])))
							level++;
						if (level >= ColaServer.INI.MaxQuoteLevel)
							continue;
					}
					if (ReadBuffer.length() > 1 && ReadBuffer.equals("--") && (QuoteMode == 'Y' || (QuoteMode == 'E' && QuoteID.equalsIgnoreCase(User.UFD.ID))))
						break;
					if (QuoteMode == 'E' || ReadBuffer.length() != 0)
					{
						if (QuoteMode != 'R' && QuoteMode != 'E')
							Data.radditem(new StringBuffer(": " + ReadBuffer));
						else
							Data.radditem(new StringBuffer(ReadBuffer));
						LineNum++;
					}
				}
				SourceFile.close();

				Data.radditem(new StringBuffer());
				LineNum++;
				
				if (Sig != 0)
					addSigFile(Sig);				
			}
			
			if (LineNum == 0)
			{
				Data.radditem(new StringBuffer());
				LineNum++;
			}

			first=Data.GetBase();
			nowptr=first;
			
		}
		catch (Exception e)
		{
			System.err.println("SHIT!!");
		}
		
		return DoEdit();
	}

	public String DoEdit(String TitleBuf, File From, String Target, String QuoteID, char QuoteMode, byte Sig, boolean Anony, BoardItem BoardBuf)
	{
		try
		{
			
			init();
			
			String ReadBuffer;

			Title = TitleBuf;
			SavePath = Target;
			Board = BoardBuf;
			AnonyFlag = Anony;
			
			if (QuoteMode == 'E')
			{
				OwnBuf = QuoteID;
				EditArticle = true;
			}
			if(From.exists())
			{
				DataInputStream SourceFile = new DataInputStream(new FileInputStream(From));
				DataInputStream SigFile = null;
				
				while ((ReadBuffer = SourceFile.readLine()) != null)
				{
					if (ReadBuffer.length() == 0)
						break;
					else if (ReadBuffer.length() > Prompt.Msgs[46].length() && ReadBuffer.substring(0, Prompt.Msgs[46].length()).equals(Prompt.Msgs[46]))
					{
						OwnBuf = ReadBuffer.substring(Prompt.Msgs[46].length());
						if (OwnBuf.lastIndexOf(')') != -1)
							OwnBuf = OwnBuf.substring(0, OwnBuf.lastIndexOf(')') + 1);
						else if (OwnBuf.indexOf(' ') != -1)
							OwnBuf = OwnBuf.substring(0, OwnBuf.indexOf(' ') + 1);
					}
				}

				if (QuoteMode != 'E')
				{
					if (OwnBuf != null)
						Data.radditem(new StringBuffer(Prompt.Msgs[38] + OwnBuf+Prompt.Msgs[270]));
					else
						Data.radditem(new StringBuffer(Prompt.Msgs[38] + QuoteID+Prompt.Msgs[270]));
					LineNum++;
				}

				while((ReadBuffer=SourceFile.readLine())!=null)
				{
					if(QuoteMode!='R'&&QuoteMode!='E')
					{
						int level=0,LastIndex=0;

						level=0;
						LastIndex=0;
						while(ReadBuffer.length()>LastIndex+1)
						{
							if(ReadBuffer.substring(LastIndex,LastIndex+2).equals(": "))
								level++;
							else
								break;
							LastIndex+=2;
						}
						LastIndex=0;
						if(((ReadBuffer.length()>=7&&(ReadBuffer.substring(0,7).equals(Prompt.Msgs[48]))||(ReadBuffer.length()>=8&&ReadBuffer.substring(0,8).equals(Prompt.Msgs[38]))))||(ReadBuffer.length()>=3&&ReadBuffer.substring(0,3).equals("==>"))||(ReadBuffer.length()>=5&&ReadBuffer.substring(0,5).equals(Prompt.Msgs[49])))
							level++;
						if(level>=ColaServer.INI.MaxQuoteLevel)
							continue;
					}
					if(ReadBuffer.length()>1&&ReadBuffer.equals("--")&&(QuoteMode=='Y'||(QuoteMode=='E'&&QuoteID.equalsIgnoreCase(User.UFD.ID))))
						break;
					if(QuoteMode=='E'||ReadBuffer.length()!=0)
					{
						if(QuoteMode!='R'&&QuoteMode!='E')
							Data.radditem(new StringBuffer(": "+ReadBuffer));
						else
							Data.radditem(new StringBuffer(ReadBuffer));
						LineNum++;
					}
				}
				
				if (QuoteMode != 'E')
				{
					Data.radditem(new StringBuffer());
					LineNum++;
				}
				SourceFile.close();
				
				if (Sig != 0 && (!AnonyFlag))
					addSigFile(Sig);				
			}

			if (LineNum == 0)
			{
				Data.radditem(new StringBuffer());
				LineNum++;
			}
			
			first=Data.GetBase();
			nowptr=first;

		}
		catch (Exception e)
		{
			System.err.println("SHIT!!");
		}
		
		return DoEdit();
	}
	
	public String DoEdit(String TitleBuf, String Target, byte Sig)
	{
		init();
		
		String ReadBuffer;

		LineNum = 1;
		Title = TitleBuf;
		SavePath = Target;

		Data.radditem(new StringBuffer());

		if (Sig != 0)
			addSigFile(Sig);
		
		first=Data.GetBase();
		nowptr=first;

		return DoEdit();
	}
	
	
	public String DoEdit(String TitleBuf, String Target, byte Sig, boolean Anony, BoardItem BoardBuf)
	{
		init();
		
		String ReadBuffer;
		
		LineNum = 1;
		Title = TitleBuf;
		SavePath = Target;
		Board = BoardBuf;
		AnonyFlag = Anony;

		Data.radditem(new StringBuffer());

		if (Sig != 0 && (!AnonyFlag))
			addSigFile(Sig);			

		first=Data.GetBase();
		nowptr=first;
		
		return DoEdit();
	}
	
	private String DoEdit()
	{
		boolean ReDrawFlag=false;
		byte echobuf[]=new byte[1];
		int UserKey,tries;
		short LastX=0,Page=0;

		User.Clear();
		User.move(1,24);
		if(InsertMode)
			User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
		else
			User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
		User.Ansi=ColorMode;
		ReDrawScreen();
		User.move(ScrX+1,ScrY+1);
EditLoop:
		while(true)
		{
			UserKey=User.getch();
			switch(UserKey)
			{
			case -1:
				ColaServer.BBSlog.Write("Connection closed abnormally!");
				User.Ansi=true;
				return null;
			case 1:
			case Keys.Home:
				EditX=0;
				ScrX=0;
				if(Page!=EditX/79)
				{
					ReDrawScreen();
					Page=(short)(EditX/79);
				}
				break;
			case ((int)'e'-(int)'a'+1):
			case Keys.End:
				EditX=(short)((StringBuffer)nowptr.obj).length();
				ScrX=(short)(EditX%79);
				if(Page!=EditX/79)
				{
					ReDrawScreen();
					Page=(short)(EditX/79);
				}
				break;
			case ((int)'p'-(int)'a'+1):
			case Keys.Up:
				if(EditY<=0)
					continue;
				nowptr=nowptr.last;
				EditY--;
				ScrY--;
				ReDrawFlag=false;

				if(((StringBuffer)nowptr.obj).length()<EditX)
				{
					if(EditX>LastX)
						LastX=EditX;
					EditX=(short)((StringBuffer)nowptr.obj).length();
					ScrX=(short)(EditX%79);
				}
				if(((StringBuffer)nowptr.obj).length()>LastX)
				{
					EditX=LastX;
					ScrX=(short)(EditX%79);
				}
				else
				{
					EditX=(short)((StringBuffer)nowptr.obj).length();
					ScrX=(short)(EditX%79);
				}
				
				if(ScrY<0&&Page==EditX/79)
				{
					int rdstart,rdend;
					
					ScrY=0;
					User.ScrollDown();
					User.move(1,1);
					User.clrtoeol();
					rdstart=0;
					rdend=((StringBuffer)nowptr.obj).length();
					if(rdend>80)
					{
						rdstart=EditX-ScrX;
						if(rdend-rdstart>80)
							rdend=rdstart+80;
					}
					User.sends(nowptr.obj.toString().substring(rdstart,rdend)+"\r\n");
					User.Ansi=true;
					User.move(1,24);
					if(InsertMode)
						User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
					else
						User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
					User.Ansi=ColorMode;
					User.move(ScrX+1,ScrY+1);
				}
				else 
				{
					if(ScrY<0)
						ScrY=0;
					if(Page!=EditX/79)
					{
						ReDrawScreen();
						Page=(short)(EditX/79);
					}
				}
				break;
			case ((int)'n'-(int)'a'+1):
			case Keys.Down:
				if(EditY>=LineNum-1)
					continue;
				nowptr=nowptr.next;
				EditY++;
				ScrY++;
				ReDrawFlag=false;
				
				if(((StringBuffer)nowptr.obj).length()<EditX)
				{
					if(EditX>LastX)
						LastX=EditX;
					EditX=(short)((StringBuffer)nowptr.obj).length();
					ScrX=(short)(EditX%79);
				}
				if(((StringBuffer)nowptr.obj).length()>LastX)
				{
					EditX=LastX;
					ScrX=(short)(EditX%79);
				}
				else
				{
					EditX=(short)((StringBuffer)nowptr.obj).length();
					ScrX=(short)(EditX%79);
				}
				
				if(ScrY>22&&Page==(EditX/79))
				{
					int rdstart,rdend;
					
					ScrY=22;
					
					User.move(1,24);
					User.clrtoeol();
					rdstart=0;
					rdend=((StringBuffer)nowptr.obj).length();
					if(rdend>80)
					{
						rdstart=EditX-ScrX;
						if(rdend-rdstart>80)
							rdend=rdstart+80;
					}
					User.sends(nowptr.obj.toString().substring(rdstart,rdend)+"\r\n");
					
					User.Ansi=true;
					User.move(1,24);
					if(InsertMode)
						User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
					else
						User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
					User.Ansi=ColorMode;
					User.move(ScrX+1,ScrY+1);
				}
				else 
				{
					if(ScrY>22)
						ScrY=22;
					if(Page!=EditX/79)
					{
						ReDrawScreen();
						Page=(short)(EditX/79);
					}
				}
				break;
			case ((int)'r'-(int)'a'+1):
			case Keys.Left:
				if(EditX<=0)
				{
					if(EditY!=0)
					{
						EditY--;
						ScrY--;
						nowptr=nowptr.last;
						if(ScrY<0)
						{
							ReDrawScreen();
							ScrY=0;
						}
						EditX=(short)(((StringBuffer)nowptr.obj).length());
						ScrX=(short)(EditX%79);
						if(Page!=EditX/79)
						{
							ReDrawScreen();
							Page=(short)(EditX/79);
						}
						break;
					}
					else
						continue;
				}
				EditX--;
				ScrX--;
				LastX=EditX;
				if(ScrX<0)
				{
					ScrX=78;
					Page--;
					ReDrawScreen();
				}
				break;
			case ((int)'v'-(int)'a'+1):
			case Keys.Right:
				if(EditX>((StringBuffer)nowptr.obj).length()-1)
				{
					if(EditY<LineNum-1)
					{
						EditX=0;
						ScrX=0;
						EditY++;
						ScrY++;
						nowptr=nowptr.next;

						ReDrawFlag=false;
						if(ScrY>22)
						{
							ScrY=22;
							ReDrawScreen();
							ReDrawFlag=true;
						}
						if(Page!=EditX/79)
						{
							if(!ReDrawFlag)
								ReDrawScreen();
							Page=(short)(EditX/79);
						}
						break;
					}
					else
						continue;
				}
				EditX++;
				ScrX++;
				LastX=EditX;
				if(ScrX>78)
				{
					ScrX=0;
					Page++;
					ReDrawScreen();
				}
				break;
			case ((int)'z'-(int)'a'+1):
				User.Ansi=true;
				User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"edithelp");
				User.PressAnyKey();
				User.Clear();
				User.move(1,24);
				if(InsertMode)
					User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
				else
					User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
				User.Ansi=ColorMode;

				ReDrawScreen();
				break;
			case ((int)'w'-(int)'a'+1):
			case ((int)'x'-(int)'a'+1):
				User.move(1,24);
				User.Ansi=true;
				User.clrtoeol();
				User.sends(Prompt.Msgs[153]);
				switch(User.MakeSure())
				{
				case 'A':
					User.Ansi=true;
					return null;
				case 'S':
				case 0:
					break EditLoop;
				default:
					break;
				}

				User.move(1,24);
				if(InsertMode)
					User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
				else
					User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
				User.Ansi=ColorMode;
				User.move(ScrX+1,ScrY+1);
				break;
			case ((int)'y'-(int)'a'+1):
				if(LineNum==0)
					continue;
				if(LineNum==1)
				{
					if(((StringBuffer)nowptr.obj).length()!=0)
					{
						EditX=0;
						ScrX=0;
						((StringBuffer)nowptr.obj).setLength(0);
						User.move(1,1);
						User.clrtoeol();
						//                            ReDrawScreen();
						break;
					}
					continue;
				}
				boolean LastLineFlag=false;
				
				ReDrawFlag=false;
				if(nowptr.next!=first)
				{
					nowptr=nowptr.next;
					Data.delitem(nowptr.last);
					first=Data.GetBase();
				}
				else
				{
					LastLineFlag=true;
					nowptr=nowptr.last;
					Data.delitem(nowptr.next);
					EditY--;
					ScrY--;
				}

				LineNum--;
				if(((StringBuffer)nowptr.obj).length()<EditX)
				{
					if(EditX>LastX)
						LastX=EditX;
					EditX=(short)((StringBuffer)nowptr.obj).length();
					ScrX=(short)(EditX%79);
					if(Page!=EditX/79)
					{
						ReDrawScreen();
						ReDrawFlag=true;
						Page=(short)(EditX/79);
					}
				}
				if(((StringBuffer)nowptr.obj).length()>LastX)
				{
					EditX=LastX;
					ScrX=(short)(EditX%79);
					if(Page!=EditX/79)
					{
						ReDrawScreen();
						ReDrawFlag=true;
						Page=(short)(EditX/79);
					}
				}
				else
				{
					EditX=(short)((StringBuffer)nowptr.obj).length();
					ScrX=(short)(EditX%79);
					if(Page!=EditX/79)
					{
						ReDrawScreen();
						ReDrawFlag=true;
						Page=(short)(EditX/79);
					}
				}
				if(!ReDrawFlag)
				{
					if(LastLineFlag)
					{
						if(ScrY==-1)
						{
							ScrY=0;
							User.move(1,1);
							User.clrtoeol();
							if(((StringBuffer)nowptr.obj).length()>79)
								User.sends(nowptr.obj.toString().substring(0,79)+"\r\n");
							else
								User.sends(nowptr.obj.toString()+"\r\n");
						}
						else
							User.ScrollUp(ScrY+2,23);
						if(ScrY==21)
						{
							User.move(1,23);
							User.clrtoeol();
							User.Ansi=true;
							User.sends(Prompt.Msgs[195]+"\r\n");
							User.Ansi=ColorMode;
						}
						else
						{
							User.move(1,23);
							User.clrtoeol();
							User.sends("~\r\n");
						}
					}
					else
					{
						int i;
						LinkType ptr;
						
						ptr=nowptr;
						User.ScrollUp(ScrY+1,23);
						for(i=0;(i<(22-ScrY))&&(i<(LineNum-EditY-1));i++)
							ptr=ptr.next;
						if(i==(22-ScrY))
						{
							User.move(1,23);
							if(((StringBuffer)ptr.obj).length()>79)
								User.sends(ptr.obj.toString().substring(0,79)+"\r\n");
							else
								User.sends(ptr.obj.toString()+"\r\n");
						}
						else if(i==(22-ScrY-1))
						{
							User.move(1,23);
							User.clrtoeol();
							User.Ansi=true;
							User.sends(Prompt.Msgs[195]+"\r\n");
							User.Ansi=ColorMode;
						}
						else
						{
							User.move(1,23);
							User.clrtoeol();
							User.sends("~\r\n");
						}
					}
				}
				break;
			case Keys.Enter:
				if(LineNum==ColaServer.INI.EditorMaxLines)
					continue;
				Data.additem(nowptr,nowptr.next,new StringBuffer(((StringBuffer)nowptr.obj).toString().substring(EditX)));
				first=Data.GetBase();
				((StringBuffer)nowptr.obj).setLength(EditX);
				if(EditX>78)
				{
					EditX=0;
					ScrX=0;
					ReDrawScreen();
				}	
				else if(ScrY<22)
				{
					User.ScrollDown(ScrY+2,23);
					User.move(1,ScrY+2);
					User.clrtoeol();
					User.move(1,ScrY+1);
					User.clrtoeol();
					if(((StringBuffer)nowptr.obj).length()>79)
						User.sends(nowptr.obj.toString().substring(0,79)+"\r\n");
					else
						User.sends(nowptr.obj.toString()+"\r\n");
					if(((StringBuffer)nowptr.next.obj).length()>79)
						User.sends(nowptr.next.obj.toString().substring(0,79)+"\r\n");
					else
						User.sends(nowptr.next.obj.toString()+"\r\n");
				}
				else if(ScrY==22)
				{
					User.ScrollUp(1,23);
					User.move(1,22);
					User.clrtoeol();
					if(((StringBuffer)nowptr.obj).length()>79)
						User.sends(nowptr.obj.toString().substring(0,79)+"\r\n");
					else
						User.sends(nowptr.obj.toString()+"\r\n");
					if(((StringBuffer)nowptr.next.obj).length()>79)
						User.sends(nowptr.next.obj.toString().substring(0,79)+"\r\n");
					else
						User.sends(nowptr.next.obj.toString()+"\r\n");
				}
				nowptr=nowptr.next;
				LineNum++;
				EditY++;
				ScrY++;
				if(ScrY>22)
					ScrY=22;
				EditX=0;
				ScrX=0;
				LastX=EditX;
				break;
			case Keys.BackSpace:
				if(((StringBuffer)nowptr.obj).length()>0&&EditX>0)
				{
					if(EditX==((StringBuffer)nowptr.obj).length())
					{
						echobuf[0]=8;
						User.sends(echobuf);
						echobuf[0]=32;
						User.sends(echobuf);
						echobuf[0]=8;
						User.sends(echobuf);
					}
					else
					{
						int tmpx=User.ScX,i;

						User.move(tmpx-1,User.ScY);
						User.sends(STRING.Cut(nowptr.obj.toString().substring(EditX),79-ScrX));
						User.move(tmpx-1,User.ScY);
						for(i=EditX-1;i<((StringBuffer)nowptr.obj).length()-1;i++)
							((StringBuffer)nowptr.obj).setCharAt(i,((StringBuffer)nowptr.obj).charAt(i+1));
					}
					((StringBuffer)nowptr.obj).setLength(((StringBuffer)nowptr.obj).length()-1);
					if(EditX<=0)
						break;
					EditX--;
					ScrX--;
					LastX=EditX;
					if(ScrX<0)
					{
						ScrX=78;
						ReDrawScreen();
					}
				}
				else if(EditX==0&&EditY!=0&&((StringBuffer)nowptr.last.obj).length()<ColaServer.INI.EditorMaxLong)
				{
					EditX=(short)((StringBuffer)nowptr.last.obj).length();
					ScrX=(short)(EditX%79);
					LastX=EditX;
					((StringBuffer)nowptr.last.obj).append(((StringBuffer)nowptr.obj).toString());
					Data.delitem(nowptr);
					nowptr=nowptr.last;
					LineNum--;
					ScrY--;
					if(ScrY<0)
						ScrY=0;
					EditY--;
					if(EditX<79)
					{
						int i;
						LinkType ptr=nowptr;
						
						User.ScrollUp(ScrY+2,23);
						User.move(1,ScrY+1);
						User.clrtoeol();
						if(((StringBuffer)nowptr.obj).length()>79)
							User.sends(nowptr.obj.toString().substring(0,79)+"\r\n");
						else
							User.sends(nowptr.obj.toString()+"\r\n");
						
						for(i=0;(i<(22-ScrY))&&(i<(LineNum-EditY-1));i++)
							ptr=ptr.next;
						if(i==22-ScrY)
						{
							User.move(1,23);
							User.clrtoeol();
							if(((StringBuffer)ptr.obj).length()>79)
								User.sends(ptr.obj.toString().substring(0,79)+"\r\n");
							else
								User.sends(ptr.obj.toString()+"\r\n");
						}
						else if(i==22-ScrY-1)
						{
							User.move(1,23);
							User.clrtoeol();
							User.Ansi=true;
							User.sends(Prompt.Msgs[195]+"\r\n");
							User.Ansi=ColorMode;
						}
						else
						{
							User.move(1,23);
							User.clrtoeol();
							User.sends("~\r\n");
						}
					}
					else
						ReDrawScreen();
				}

				break;
			case ((int)'d'-(int)'a'+1):
			case Keys.Del:
				if(((StringBuffer)nowptr.obj).length()>0&&EditX<((StringBuffer)nowptr.obj).length())
				{
					if(EditX==((StringBuffer)nowptr.obj).length()-1)
					{
						echobuf[0]=32;
						User.sends(echobuf);
						echobuf[0]=8;
						User.sends(echobuf);
					}
					else
					{
						int tmpx=User.ScX,i;

						User.sends(STRING.Cut(nowptr.obj.toString().substring(EditX+1),78-ScrX));
						User.move(tmpx,User.ScY);
						for(i=EditX;i<((StringBuffer)nowptr.obj).length()-1;i++)
							((StringBuffer)nowptr.obj).setCharAt(i,((StringBuffer)nowptr.obj).charAt(i+1));
					}
					((StringBuffer)nowptr.obj).setLength(((StringBuffer)nowptr.obj).length()-1);
					if(EditX<=0)
						break;
					LastX=EditX;
				}
				else if(EditX==((StringBuffer)nowptr.obj).length()&&EditY!=(LineNum-1)&&EditX<ColaServer.INI.EditorMaxLong)
				{
					((StringBuffer)nowptr.obj).append(((StringBuffer)nowptr.next.obj).toString());
					Data.delitem(nowptr.next);
					LineNum--;
					
					int i;
					LinkType ptr=nowptr;
					
					if(ScrY!=22)
						User.ScrollUp(ScrY+2,23);
					User.move(1,ScrY+1);
					if(((StringBuffer)ptr.obj).length()>(EditX-ScrX+79))
						User.sends(ptr.obj.toString().substring(EditX-ScrX,EditX-ScrX+79)+"\r\n");
					else
					{
						User.clrtoeol();
						if(((StringBuffer)ptr.obj).length()>EditX-ScrX)
							User.sends(ptr.obj.toString().substring(EditX-ScrX)+"\r\n");
						else
							User.sends("\r\n");
					}
					
					for(i=0;(i<(22-ScrY))&&(i<(LineNum-EditY-1));i++)
						ptr=ptr.next;
					if(i==22-ScrY)
					{
						User.move(1,23);
						if(((StringBuffer)ptr.obj).length()>(EditX-ScrX+79))
							User.sends(ptr.obj.toString().substring(EditX-ScrX,EditX-ScrX+79)+"\r\n");
						else
						{
							User.clrtoeol();
							if(((StringBuffer)ptr.obj).length()>EditX-ScrX)
								User.sends(ptr.obj.toString().substring(EditX-ScrX)+"\r\n");
							else
								User.sends("\r\n");
						}
					}
					else if(i==22-ScrY-1)
					{
						User.move(1,23);
						User.clrtoeol();
						User.Ansi=true;
						User.sends(Prompt.Msgs[195]+"\r\n");
						User.Ansi=ColorMode;
					}
					else
					{
						User.move(1,23);
						User.clrtoeol();
						User.sends("~\r\n");
					}
				}
				break;
			case Keys.Ins:
				InsertMode=!InsertMode;
				User.move(64,24);
				User.Ansi=true;
				if(InsertMode)
					User.sends(Prompt.Msgs[81]);
				else
					User.sends(Prompt.Msgs[77]);
				User.Ansi=ColorMode;
				break;
			case Keys.Tab:
				if(((StringBuffer)nowptr.obj).length()>=ColaServer.INI.EditorMaxLong-7)
					continue;
				if(EditX==((StringBuffer)nowptr.obj).length())
				{
					((StringBuffer)nowptr.obj).append("        ");
					User.sends("        ");
				}
				else
				{
					int tmpx=User.ScX;

					((StringBuffer)nowptr.obj).insert(EditX,"        ");
					User.sends(STRING.Cut(nowptr.obj.toString().substring(EditX),79-ScrX));
					User.move(tmpx+8,User.ScY);
				}
				EditX+=8;
				ScrX+=8;
				LastX=EditX;
				if(ScrX>78)
				{
					ScrX=(short)(EditX%79);
					ReDrawScreen();
				}
				break;
			case Keys.Esc:
				if(User.EscArg!=27)
				{
					String AddCodes;
					
					switch(User.EscArg)
					{
					case 'c':
						User.Clear();
						User.Ansi=true;
						ReDrawScreen();
						User.Ansi=true;
						User.move(1,24);
						User.clrtoeol();
						User.PressAnyKey();
						User.Clear();
						User.move(1,24);
						if(InsertMode)
							User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
						else
							User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");

						User.Ansi=ColorMode;
						ReDrawScreen();
						break;
					case 'f':
						if(ColorMode)
							continue;
						User.move(1,24);
						User.clrtoeol();
						User.Ansi=true;
						User.sends(Prompt.Msgs[314]);
						User.Ansi=ColorMode;
						switch(User.getch())
						{
						case -1:
							ColaServer.BBSlog.Write("Connection closed abnormally!");
							User.Ansi=true;
							return null;
						case '0':
							AddCodes="[30m";
							break;
						case '1':
							AddCodes="[31m";
							break;
						case '2':
							AddCodes="[32m";
							break;
						case '3':
							AddCodes="[33m";
							break;
						case '4':
							AddCodes="[34m";
							break;
						case '5':
							AddCodes="[35m";
							break;
						case '6':
							AddCodes="[36m";
							break;
						case '7':
							AddCodes="[37m";
							break;
						default:
							continue;
						}
						if(((StringBuffer)nowptr.obj).length()>=ColaServer.INI.EditorMaxLong)
							continue;
						if(EditX==((StringBuffer)nowptr.obj).length())
							((StringBuffer)nowptr.obj).append(AddCodes);
						else
							((StringBuffer)nowptr.obj).insert(EditX,AddCodes);
						EditX+=5;
						ScrX+=5;
						LastX=EditX;
						if(ScrX>78)
						{
							ScrX=(short)(EditX%79);
							if(Page!=EditX/79)
							{
								if(!ReDrawFlag)
									ReDrawScreen();
								Page=(short)(EditX/79);
							}
						}
						else
						{
							User.move(1,ScrY+1);
							User.clrtoeol();
							if(((StringBuffer)nowptr.obj).length()>EditX-ScrX)
								User.sends(nowptr.obj.toString().substring(EditX-ScrX));
						}
						User.Ansi=true;
						User.move(1,24);
						if(InsertMode)
							User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
						else
							User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
						User.Ansi=ColorMode;
						User.move(ScrX+1,ScrY+1);
						break;
					case 'b':
						if(ColorMode)
							continue;
						User.move(1,24);
						User.clrtoeol();
						User.Ansi=true;
						User.sends(Prompt.Msgs[315]);
						User.Ansi=ColorMode;
						switch(User.getch())
						{
						case -1:
							ColaServer.BBSlog.Write("Connection closed abnormally!");
							User.Ansi=true;
							return null;
						case '0':
							AddCodes="[40m";
							break;
						case '1':
							AddCodes="[41m";
							break;
						case '2':
							AddCodes="[42m";
							break;
						case '3':
							AddCodes="[43m";
							break;
						case '4':
							AddCodes="[44m";
							break;
						case '5':
							AddCodes="[45m";
							break;
						case '6':
							AddCodes="[46m";
							break;
						case '7':
							AddCodes="[47m";
							break;
						default:
							continue;
						}
						if(((StringBuffer)nowptr.obj).length()>=ColaServer.INI.EditorMaxLong)
							continue;
						if(EditX==((StringBuffer)nowptr.obj).length())
							((StringBuffer)nowptr.obj).append(AddCodes);
						else
							((StringBuffer)nowptr.obj).insert(EditX,AddCodes);
						EditX+=5;
						ScrX+=5;
						LastX=EditX;
						if(ScrX>78)
						{
							ScrX=(short)(EditX%79);
							if(Page!=EditX/79)
							{
								if(!ReDrawFlag)
									ReDrawScreen();
								Page=(short)(EditX/79);
							}
						}
						else
						{
							User.move(1,ScrY+1);
							User.clrtoeol();
							if(((StringBuffer)nowptr.obj).length()>EditX-ScrX)
								User.sends(nowptr.obj.toString().substring(EditX-ScrX));
						}
						User.Ansi=true;
						User.move(1,24);
						if(InsertMode)
							User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
						else
							User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
						User.Ansi=ColorMode;
						User.move(ScrX+1,ScrY+1);
						break;
					}
					break;
				}
			default:
				if((UserKey!=27&&UserKey<32)||(UserKey&0xff00)!=0)
					continue;
				if(((StringBuffer)nowptr.obj).length()>=ColaServer.INI.EditorMaxLong)
					continue;
				if(EditX==((StringBuffer)nowptr.obj).length())
				{
					((StringBuffer)nowptr.obj).append((char)UserKey);
					echobuf[0]=(byte)UserKey;
					User.sends(echobuf);
				}
				else
				{
					int tmpx=User.ScX;

					if(InsertMode)
						((StringBuffer)nowptr.obj).insert(EditX,(char)UserKey);
					else
						((StringBuffer)nowptr.obj).setCharAt(EditX,(char)UserKey);
					User.sends(STRING.Cut(nowptr.obj.toString().substring(EditX),79-ScrX));
					User.move(tmpx+1,User.ScY);
				}
				EditX++;
				ScrX++;
				LastX=EditX;
				if(ScrX>78)
				{
					ScrX=0;
					Page++;
					ReDrawScreen();
				}
				break;
			}
			User.move(71,24);
			User.Ansi=true;
			User.sends("[1;33;42m"+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+"[m");
			User.Ansi=ColorMode;
			User.move(ScrX+1,ScrY+1);
		}

		if(NormalEditor)
		{
			RandomAccessFile TargetFile=null;
			tries=0;
			while(tries<10&&(new File(SavePath)).exists()&&!(new File(SavePath)).delete())
			{
				TIME.Delay(100);
				tries++;
			}	
			try
			{
				TargetFile=new RandomAccessFile(SavePath,"rw");
				LinkType ptr;

				ptr=Data.GetBase();
				first=ptr;
				TargetFile.writeBytes(((StringBuffer)ptr.obj).toString()+"\r\n");
				ptr=ptr.next;

				while(ptr!=first)
				{
					TargetFile.writeBytes(((StringBuffer)ptr.obj).toString()+"\r\n");
					ptr=ptr.next;
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}
			finally
			{
				try
				{
					if(TargetFile!=null)
					{
						TargetFile.close();
						TargetFile=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					User.Ansi=true;
				}	
			}
			return SavePath;
		}
		
		String SaveFile;
		
		if(EditArticle==true)
		{
			SaveFile=SavePath;
			tries=0;
			while(tries<10&&(new File(SavePath)).exists()&&!(new File(SavePath)).delete())
			{
				TIME.Delay(100);
				tries++;
			}	
		}
		else
		{
			char appendChar='A';
			do
			{
				SaveFile="M."+((int)((new Date()).getTime()/1000))+"."+appendChar;
				appendChar++;
			}while((new File(SavePath,SaveFile)).exists());
			SavePath=SavePath+SaveFile;
		}
		
		{
			RandomAccessFile TargetFile=null;
			
			try
			{
				TargetFile=new RandomAccessFile(SavePath,"rw");
				LinkType ptr;

				if(Title!=null)
				{
					if(Board!=null)
					{
						if(AnonyFlag)
							TargetFile.writeBytes(Prompt.Msgs[46]+STRING.Cut("Anonymous"+Prompt.Msgs[242],46)+Prompt.Msgs[183]+STRING.Cut(Board.Name,20)+"[m\r\n");
						else if(EditArticle)
							TargetFile.writeBytes(Prompt.Msgs[46]+STRING.Cut(OwnBuf,46)+Prompt.Msgs[183]+STRING.Cut(Board.Name,20)+"[m\r\n");
						else
							TargetFile.writeBytes(Prompt.Msgs[46]+STRING.Cut(User.UFD.ID+" ("+User.UFD.NickName+")",46)+Prompt.Msgs[183]+STRING.Cut(Board.Name,20)+"[m\r\n");
					}
					else
						TargetFile.writeBytes(Prompt.Msgs[46]+STRING.Cut(User.UFD.ID+" ("+User.UFD.NickName+")",73)+"\r\n");
					TargetFile.writeBytes(Prompt.Msgs[65]+STRING.Cut(Title,73)+"[m\r\n");

					TargetFile.writeBytes(Prompt.Msgs[271]+STRING.Cut(ColaServer.SysDATE.DateFormatter2.format(new Date()),73)+"[m\r\n");
					TargetFile.writeBytes(Prompt.Msgs[237]);
					TargetFile.writeBytes("\r\n");
				}

				ptr=Data.GetBase();
				first=ptr;
				TargetFile.writeBytes(((StringBuffer)ptr.obj).toString()+"\r\n");
				ptr=ptr.next;

				while(ptr!=first)
				{
					TargetFile.writeBytes(((StringBuffer)ptr.obj).toString()+"\r\n");
					ptr=ptr.next;
				}

				try
				{
					if(!EditArticle||(OwnBuf.indexOf(' ')!=-1&&User.UFD.ID.equalsIgnoreCase(OwnBuf.substring(0,OwnBuf.indexOf(' ')))))
					{
						TargetFile.writeBytes("\r\n--\r\n");
						if(Board!=null&&AnonyFlag)
						{
							TargetFile.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
							TargetFile.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[51]);
						}
						else if(ColaServer.INI.PrintOrg||Board==null)
						{
							TargetFile.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
							TargetFile.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m[FROM: "+User.Home+"][m");
						}
						else
							TargetFile.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
					}
				}
				catch(NumberFormatException e){}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}
			finally
			{
				try
				{
					if(TargetFile!=null)
					{
						TargetFile.close();
						TargetFile=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					User.Ansi=true;
				}	
			}
		}	
		return SaveFile;
	}

	public void ReDrawScreen()
	{
		short i,LineLong;
		LinkType ptr;

		User.move(1,1);
		if(ColorMode)
		{
			LineLong=79;
		}
		else
			LineLong=79;
		ptr=nowptr;
		for(i=0;i<ScrY;i++)
			ptr=ptr.last;
		if(((StringBuffer)ptr.obj).length()>(EditX-ScrX+LineLong))
			User.sends(ptr.obj.toString().substring(EditX-ScrX,EditX-ScrX+LineLong)+"\r\n");
		else
		{
			User.clrtoeol();
			if(((StringBuffer)ptr.obj).length()>EditX-ScrX)
				User.sends(ptr.obj.toString().substring(EditX-ScrX)+"\r\n");
			else
				User.sends("\r\n");
		}
		ptr=ptr.next;
		for(i=0;i<22;i++)
		{
			if(ptr==first)
				break;
			if(((StringBuffer)ptr.obj).length()>(EditX-ScrX+LineLong))
				User.sends(ptr.obj.toString().substring(EditX-ScrX,EditX-ScrX+LineLong)+"\r\n");
			else
			{
				User.clrtoeol();
				if(((StringBuffer)ptr.obj).length()>EditX-ScrX)
					User.sends(ptr.obj.toString().substring(EditX-ScrX)+"\r\n");
				else
					User.sends("\r\n");
			}
			ptr=ptr.next;
		}
		if(i<22)
		{
			User.Ansi=true;
			User.clrtoeol();
			User.sends(Prompt.Msgs[195]+"\r\n");
			i++;
			User.Ansi=ColorMode;
		}
		for(;i<22;i++)
		{
			User.clrtoeol();
			User.sends("~"+"\r\n");
		}
		User.move(1,24);
		User.Ansi=true;
		if(InsertMode)
			User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[307]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
		else
			User.sends("[1;31;42m Cola[35mEd[33mit[36mor"+Prompt.Msgs[308]+STRING.CutLeft(""+(EditY+1),4)+","+STRING.CutLeft(""+(EditX+1),3)+" [m");
		User.Ansi=ColorMode;
	}
}