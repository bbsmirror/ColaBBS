package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;

public final class LineEdit
{
	public boolean InEditor=false,Busy=false;
	private boolean echo;
	private int OrgX,pid,buflen;
	public int cursor;
	private StringBuffer inputbuf;
	private TelnetUser User = null;

	public LineEdit(StringBuffer buf,int len,int pidbuf,boolean echoflag)
	{
		int i;

		inputbuf=buf;
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		buflen=len;
		echo=echoflag;
		cursor=inputbuf.length();
		//        OrgX=ColaServer.BBSUsers[pid].ScX-cursor;
		OrgX=User.ScX;
		
		User.sends("[0;34;47m");
		for(i=0;i<len;i++)
			User.sends(" ");
		User.move(OrgX,User.ScY);
		
		if(inputbuf.length()!=0)
			User.sends(inputbuf.toString());
	}
	public int DoEdit()
	{
		int inkey;
		byte echobuf[]=new byte[1];

		Busy=true;

		try
		{
			while((inkey=User.getch())!=13)
			{
				if(inkey<0)
				{
					inputbuf.setLength(0);
					return inkey;
				}
				switch(inkey)
				{
				case Keys.Del:
					if(inputbuf.length()>0&&cursor>0)
					{
						int tmpx=User.ScX,i;

						User.move(tmpx-1,User.ScY);
						if(echo)
							User.sends(inputbuf.toString().substring(cursor)+" ");
						else
						{
							for(i=cursor;i<inputbuf.length();i++)
							{
								echobuf[0]=(byte)'*';
								User.sends(echobuf);
							}
							echobuf[0]=(byte)' ';
							User.sends(echobuf);
						}
						User.move(tmpx-1,User.ScY);
						for(i=cursor-1;i<inputbuf.length()-1;i++)
							inputbuf.setCharAt(i,inputbuf.charAt(i+1));
					}
					inputbuf.setLength(inputbuf.length()-1);
					continue;
				case Keys.BackSpace:
					if(inputbuf.length()>0&&cursor>0)
					{
						if(cursor==inputbuf.length())
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
							if(echo)
								User.sends(inputbuf.toString().substring(cursor)+" ");
							else
							{
								for(i=cursor;i<inputbuf.length();i++)
								{
									echobuf[0]=(byte)'*';
									User.sends(echobuf);
								}
								echobuf[0]=(byte)' ';
								User.sends(echobuf);
							}
							User.move(tmpx-1,User.ScY);
							for(i=cursor-1;i<inputbuf.length()-1;i++)
								inputbuf.setCharAt(i,inputbuf.charAt(i+1));
						}
						inputbuf.setLength(inputbuf.length()-1);
						cursor--;
					}
					continue;
				case Keys.Left:
					if(cursor>0)
					{
						cursor--;
						User.move(User.ScX-1,User.ScY);
					}
					continue;
				case Keys.Right:
					if(cursor<inputbuf.length())
					{
						cursor++;
						User.move(User.ScX+1,User.ScY);
					}
					continue;
				default:
					if(inkey<32||(inkey&0xff00)!=0)
						continue;
					if(inputbuf.length()>=buflen)
						continue;
					if(cursor==inputbuf.length())
					{
						inputbuf.append((char)inkey);
						if(echo)
						{
							echobuf[0]=(byte)inkey;
							User.sends(echobuf);
						}
						else
						{
							echobuf[0]=(byte)'*';
							User.sends(echobuf);
						}
					}
					else
					{
						int tmpx=User.ScX;

						inputbuf.insert(cursor,(char)inkey);
						if(echo)
							User.sends(inputbuf.toString().substring(cursor));
						else
						{
							int i;

							for(i=cursor;i<inputbuf.length();i++)
							{
								echobuf[0]=(byte)'*';
								User.sends(echobuf);
							}
						}
						User.move(tmpx+1,User.ScY);
					}

					cursor++;
					break;
				}
			}
			return inputbuf.length();
		}
		finally
		{
			User.sends("[m");
			Busy=false;
		}
	}
	public void ReDraw()
	{
		int i;

		User.move(OrgX,User.ScY);
		User.sends("[0;34;47m");
		for(i=0;i<buflen;i++)
			User.sends(" ");
		User.move(OrgX,User.ScY);
		User.sends(inputbuf.toString());
	}
}