package colabbs.net;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import colabbs.*;
import colabbs.DATA.BOARD.*;
import colabbs.UTILS.*;
import colabbs.record.*;

//Add by WilliamWey
import java.lang.reflect.*;
import colabbs.WW.*;
//


/**
 * This type was writen by yhwu.
 */
public class NNTPClient
{
	public static SimpleDateFormat formatter=new SimpleDateFormat("dd MMM yyyy hh:mm:ss z",Locale.UK);
	private short port=119;
	private String BBSName=null,Address=null,Command=null;

	private char appendChar='A'; //for creat file and refresh board time....
	private long theTime=-1;
	
	private String poster=null,title=null,date=null,Site=null; //for head
	//Add by WilliamWey
	private String MIME=null;
	//
	
	private Socket theClient=null; //for connect
	private DataOutputStream pw=null;
	private DataInputStream br=null;
	
	private int firstnum=0,lastnum=0; //for group command
/**
 * This method was writen by yhwu.
 * @param theBBSName java.lang.String
 * @param theAddr java.lang.String
 * @param theport short
 * @param theCMD java.lang.String
 */
public NNTPClient(String theBBSName, String theAddr, short theport, String theCMD)
{
	BBSName=theBBSName;
	Address=theAddr;
	port=theport;
	Command=theCMD;
}
/**
 * This method was writen by yhwu.
 */
public synchronized void connect()
{
	boolean isihave=false;
	try
	{
		System.out.println((new Date()).toString()+" NNTPClient start "+BBSName+","+Address);
		String rbuf=null;
		NewsFeedType nft=new NewsFeedType();
		int n=(int)RecordHandler.recordNumber(nft,ColaServer.INI.BBSHome,Consts.NewsFeed);
NextFeed:
		for(int i=0;i<=n;i++)
		{
			poster=null;
			title=null;
			date=null; //for head
			theTime=-1;
	
			firstnum=0;
			lastnum=0; //for group command
	
			RecordHandler.getRecord(i,Consts.NewsFeed,nft,ColaServer.INI.BBSHome,Consts.NewsFeed);
			if(nft.getBBSName().equals(BBSName))
			{
				//System.err.println(Calendar.getInstance().getTime().toString() + ", BBS: " + nft.getBBSName() + ", BOARD: " + nft.getBoardName());
				
				int start=-1;
				String filename=null;
//				if(!group(nft.getNewsGroup()))
//					continue;
				//search the board item....
				
				BoardItem found = ColaServer.BList.get(nft.getBoardName());
				
/*				LinkType Base=ColaServer.BBSBoards.Boards.GetBase();
				LinkType ptr=Base;
				BoardItem BoardBuf=null,found=null;

				if(Base==null)
					continue;

				do
				{
					BoardBuf=(BoardItem)ptr.obj;
					if(BoardBuf.Name.equalsIgnoreCase(nft.getBoardName()))
					{
						found=BoardBuf;
						break;
					}
					ptr=ptr.next;
				}while(ptr!=Base);*/
				if(found==null)
					continue;
//				if(thept!=null)
//					theTime=thept.getFileTime();

				if(Command.equalsIgnoreCase("ihave"))
					isihave=true;
				else
					isihave=false;
				if(!isihave)
				{
					if(theClient==null) // connect just when needed!!
					{
						theClient=new Socket(Address,port);
						pw=new DataOutputStream(new BufferedOutputStream(theClient.getOutputStream()));
						br=new DataInputStream(theClient.getInputStream());
						rbuf=getResponse();
						if(rbuf.charAt(0)!='2')
							return;
					}
					if(((nft.getMode()&Consts.ModeIn)!=0)&&group(nft.getNewsGroup()))
					{
						// geting articles....
						start=nft.getMax();
						if(start!=lastnum)
						{
							if(firstnum<nft.getMin())
							{
								nft.setMin(firstnum);
								start=firstnum;
							}
							if(firstnum>start)
								start=firstnum;
							if(lastnum-start>ColaServer.INI.GetPostMax)
								start=lastnum-ColaServer.INI.GetPostMax;
							do
							{
								start++;
								pw.writeBytes("stat "+start+"\r\n");
								pw.flush();
								rbuf=getResponse();
//								System.out.println("start = "+start+"first="+firstnum+getResponse());
//								if(rbuf.charAt(0)!='2')
//									continue;
							}while(rbuf.charAt(0)!='2'&&start<=lastnum);
							if(start<=lastnum)
							{
/*								else
								{
									pw.writeBytes("stat "+start+"\r\n");
									rbuf=getResponse();
									if(rbuf.charAt(0)!='2')
									{
										start=lastnum;
										pw.writeBytes("stat "+start+"\r\n");
										rbuf=getResponse();
									}
									nextArticle(); // get from the next of the last article....
								}*/
								System.out.println("start = "+start+",first="+firstnum+",lastnum="+lastnum);
								do
								{
									if(getArticleHead())
									{
										//Remark by WilliamWey
/*										//Change by WilliamWey
										//filename=createFile(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator);
										filename=createFileByDate(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator, date);
										//
										RandomAccessFile theFile=null;

										if(title.length()>255)
												title=title.substring(0,255);
						
										RecordHandler.append(found,new PostType(filename,poster,title,(byte)'L'),ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator,Consts.DotDir);
										try
										{
											int last2=0,last1=0;
											theFile=new RandomAccessFile(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator+filename,"rw");
											theFile.writeBytes(Prompt.Msgs[46]+STRING.Cut(poster,46)+Prompt.Msgs[183]+STRING.Cut(found.Name,20)+"[m\r\n");
											theFile.writeBytes(Prompt.Msgs[65]+STRING.Cut(title,73)+"[m\r\n");
											theFile.writeBytes(Prompt.Msgs[271]+STRING.Cut(Site+" ("+date+")",73)+"[m\r\n");
											theFile.writeBytes(Prompt.Msgs[237]);
											theFile.writeBytes("\r\n");
							
											while(true)
											{
												int buf=br.read();
												if(buf==-1)
													break;
												if(((last2==(int)'\r')||(last2==(int)'\n'))&&(last1==(int)'.')&&(buf!=(int)'.'))
													break;
												else if(last1=='.')
													theFile.writeByte('.');
												last2=last1;
												last1=buf;
												if(buf!='.')
													theFile.writeByte(buf);
											}	
										
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										finally
										{
											if(theFile!=null)
											{
												theFile.close();
												theFile=null;
											}
										}*/
										//
										//Add by WilliamWey for NewsFilter
										NewsArticle NA = new NewsArticle(poster, title, date, Site, MIME, new StringBuffer()); //Modified by yhwu
										int last2 = 0, last1 = 0;
										while(true)
										{
											int buf = br.read();
											if(buf == -1)
												break;
											if(((last2 == (int)'\r')||(last2 == (int)'\n'))&&(last1 == (int)'.')&&(buf != (int)'.'))
												break;
											else if(last1 == '.')
												NA.writeByte('.');
											last2=last1;
											last1=buf;
											if(buf != '.')
												NA.writeByte(buf);
										}
										if (ColaServer.NFilter.Go(NA) != NewsFilterReturnMode.Cancle)
										{
											filename = createFileByDate(ColaServer.INI.BBSHome + "boards" + File.separator+found.Name+File.separator, date);
											RandomAccessFile theFile = null;
											
											if(NA.title.length()>255) // Modified by yhwu
												RecordHandler.append(found,new PostType(filename,NA.poster,NA.title.substring(0,255),(byte)'L'),ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator,Consts.DotDir);
											else
												RecordHandler.append(found,new PostType(filename,NA.poster,NA.title,(byte)'L'),ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator,Consts.DotDir);
											try
											{
												theFile = new RandomAccessFile(ColaServer.INI.BBSHome + "boards" + File.separator+found.Name + File.separator + filename, "rw");
												theFile.writeBytes(Prompt.Msgs[46] + STRING.Cut(NA.poster,46) + Prompt.Msgs[183] + STRING.Cut(found.Name,20) + "[m\r\n");
												theFile.writeBytes(Prompt.Msgs[65] + STRING.Cut(NA.title,73) + "[m\r\n");
												theFile.writeBytes(Prompt.Msgs[271] + STRING.Cut(NA.Site + " ("+NA.date+")",73) + "[m\r\n");
												theFile.writeBytes(Prompt.Msgs[237]);
												theFile.writeBytes("\r\n");
												theFile.writeBytes(NA.ArticleBody.toString()); //Modified by yhwu
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
											finally
											{
												if(theFile!=null)
												{
													theFile.close();
													theFile=null;
												}
											}
										}
										//
										NA=null;
										System.gc();
									}
								}while(nextArticle());
							}
						}
					}
				}
				// posting articles.....
				PostType thept=null;
				if(((nft.getMode()&Consts.ModeOut)!=0))
				{
//					System.out.println("posting....");
					Vector needPost=new Vector();
					RecordHandler rh=null;
					int len=-1;

					synchronized(found) // get all posts needed to transfer....
					{
						try
						{
							rh=new RecordHandler(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator,Consts.DotDir,true);
							len=(int)rh.recordNumber(new PostType());
							long last=nft.getLastTime();
							for(int j=0;j<=len;j++)
							{
								PostType pt=new PostType();
								rh.getRecord(len-j,pt);
//								System.out.println("filetime = " + pt.getFileTime()+" last="+last);
								if(pt.getFileTime()<=last)
									break;
//								System.out.println("tag = "+(int)pt.getTag());
								if(pt.getTag()==(byte)'S')
									needPost.insertElementAt(pt,0);
							}
						}
						finally
						{
							if(rh!=null)
							{
								rh.close();
								rh=null;
							}
						}
					}

					if(needPost.size()!=0&&theClient==null) // connect just when needed!!
					{
						theClient=new Socket(Address,port);
						pw=new DataOutputStream(new BufferedOutputStream(theClient.getOutputStream()));
						br=new DataInputStream(theClient.getInputStream());
						rbuf=getResponse();
						if(rbuf.charAt(0)!='2')
							return;
					}
					Enumeration allpost=needPost.elements();
//					System.out.println("number = "+needPost.size());
					
					while(allpost.hasMoreElements())
					{
//						System.out.println("sending article....");
						DataInputStream thebody=null;
						thept=(PostType)allpost.nextElement();
						if((new File(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator,thept.deleteBody())).exists())
						{
							try
							{
								String theDate=null;
								
								thebody=new DataInputStream(new FileInputStream(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator+thept.deleteBody()));
								if(thebody==null)
									continue;
								if(isihave)
								{
//									pw.writeBytes("ihave <123456@"+ColaServer.INI.BBSAddress+">\r\n");
									pw.writeBytes("ihave <"+("boards"+thept.deleteBody()).hashCode()+"@"+ColaServer.INI.BBSAddress+">\r\n");
									System.out.println("IHAVE "+("boards"+thept.deleteBody()).hashCode());
								}
								else
									pw.writeBytes("post\r\n");
//								System.out.println(getResponse());
								if(getResponse().charAt(0)!=2)
									continue;
								pw.writeBytes("Path: "+ColaServer.INI.IBBSName+"\r\n");
								pw.writeBytes("From: "+thept.getPoster()+".bbs@"+ColaServer.INI.BBSAddress+"\r\n");
								pw.writeBytes("Newsgroups: "+nft.getNewsGroup()+"\r\n");
								pw.writeBytes("Subject: "+thept.getTitle()+"\r\n");
								theDate=formatter.format(new Date(thept.getFileTime()));
								if(theDate.indexOf('+')!=-1)
									theDate=theDate.substring(0,theDate.indexOf('+'));
								pw.writeBytes("Date: "+theDate+"\r\n");
								pw.writeBytes("Organization: "+ColaServer.INI.BBSName+"\r\n");
								pw.writeBytes("Message-ID: <"+("boards"+thept.deleteBody()).hashCode()+"@"+ColaServer.INI.BBSAddress+">\r\n");
								pw.writeBytes("\r\n");
								//the article body.....
//								System.out.println("post file : "+thept.deleteBody());
								String buf=null;
								while((buf=thebody.readLine())!=null)
									if(buf.length()==0)
										break;
								while((buf=thebody.readLine())!=null)
								{
									if(buf.length()>0&&buf.charAt(0)=='.')
										pw.write((byte)'.');
									pw.writeBytes(buf+"\r\n");
								}
								pw.writeBytes(".\r\n");
								getResponse();
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
							finally
							{
								if(thebody!=null)
								{
									thebody.close();
									thebody=null;
								}
							}
						}
					}
				}
//				if(theTime==-1&&thept!=null)
//					theTime=thept.getFileTime();
				if(!isihave&&!group(nft.getNewsGroup()))
					continue;
				nft.setMax(lastnum);
				nft.setMin(firstnum);
				if(theTime!=-1)
					nft.setLastTime(theTime*1000L+(appendChar-'A'));
				else if(thept!=null)
					nft.setLastTime(thept.getFileTime());
				RecordHandler.update(i,Consts.NewsFeed,nft,ColaServer.INI.BBSHome,Consts.NewsFeed);
//				System.out.println("the time="+(theTime*1000L+(appendChar-'A')));
				if(theTime!=-1)
				{
					found.filetime=theTime*1000L+(appendChar-'A');
//					System.out.println("filetime"+(theTime*1000L+(appendChar-'A')));
					
					theTime=-1;
				}
			}
		}
		quit();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		try
		{
			if(pw!=null)
			{
				pw.close();
				pw=null;
			}
			if(br!=null)
			{
				br.close();
				br=null;
			}
			if(theClient!=null)
			{
				theClient.close();
				theClient=null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	System.out.println("NNTPClient end "+BBSName+","+Address);
}
/**
 * This method was writen by yhwu.
 * @return java.lang.String
 * @param thePath java.lang.String
 */
private String createFile(String thePath)
{
	String SaveFileName;
	// search the file name for create, just for safe....
	appendChar='A'-1; //for creat file and refresh board time....
	theTime=-1;
	
	theTime=((new Date()).getTime()/1000);
	do
	{
		appendChar++;
		SaveFileName="M."+theTime+"."+appendChar;
	}while((new File(thePath,SaveFileName)).exists());
	return SaveFileName;
}
//Add by WilliamWey
private String createFileByDate(String thePath, String D)
{
	String DD = STRING.String_A2U(D);
	//System.err.println(Calendar.getInstance().getTime().toString() + ", thaPath: " + thePath + ", D: " + DD);
	
	Date DA = null;
	try
	{
		DA = new Date(D);
	}
	catch (Exception e)
	{	
	}
	if (DA == null)
		DA = new Date();
		
	String SaveFileName;
	// search the file name for create, just for safe....
	appendChar='A'-1; //for creat file and refresh board time....
	theTime=-1;
	
	//theTime=((new Date(D)).getTime()/1000);
	theTime=(DA.getTime()/1000);
	do
	{
		appendChar++;
		SaveFileName="M."+theTime+"."+appendChar;
	}while((new File(thePath,SaveFileName)).exists());
	return SaveFileName;
}
//
/**
 * This method was writen by yhwu.
 */
private boolean getArticleHead()
{
	String rbuf=null;
	boolean ignore=false;
	
	try
	{
//		pw.println("article");
		pw.writeBytes("article\r\n");
		rbuf=getResponse();
		if(rbuf.charAt(0)!='2')
		{
			ColaServer.BBSlog.Write("Get article header error: "+rbuf);
			return false;
		}
		StringBuffer clientdata=new StringBuffer(512);
		int r=0;

		clientdata.setLength(0);
		while(true)
		{
HeadLoop:
			while((r=br.read())!=-1)
			{
				switch(r)
				{
					case 10:
						break;
					case 13:
						rbuf=clientdata.toString();
						clientdata.setLength(0);
						break HeadLoop;
					default:
						clientdata.append((char)r);
						break;
				}	
			}
			if(r==-1)
				return false;
			if(rbuf.length()<=0)
				break;
			if(rbuf.length()>=6&&rbuf.substring(0,6).equalsIgnoreCase("from: "))
				poster=rbuf.substring(6);
			if(rbuf.length()>=9&&rbuf.substring(0,9).equalsIgnoreCase("subject: "))
				title=rbuf.substring(9);
			if(rbuf.length()>=6&&rbuf.substring(0,6).equalsIgnoreCase("date: "))
			{
				date=rbuf.substring(6);
				try
				{
					date = ColaServer.SysDATE.DateFormatter2.format(NNTPClient.formatter.parse(date));
				}
				catch (Exception e)
				{
				}
			}
			if(rbuf.length()>=14&&rbuf.substring(0,14).equalsIgnoreCase("Organization: "))
				Site=rbuf.substring(14);
			if(rbuf.length()>=6&&rbuf.substring(0,6).equalsIgnoreCase("Path: ")&&rbuf.indexOf(ColaServer.INI.IBBSName)!=-1) // Filter for ignore loop back article!
			{
				ignore=true;
			}
			//Add by WilliamWey
			if(rbuf.length()>=14&&rbuf.substring(0,14).equalsIgnoreCase("Mime-Version: "))
				MIME=rbuf.substring(14);
			//
		}
		if(ignore)
		{
			int last2 = 0, last1 = 0;
			while(true)
			{
				int buf = br.read();
				if(buf == -1)
				break;
				if(((last2 == (int)'\r')||(last2 == (int)'\n'))&&(last1 == (int)'.')&&(buf != (int)'.'))
					break;
				last2=last1;
				last1=buf;
			}
			return false;
		}
		
	}
	catch(IOException e)
	{
		e.printStackTrace();
		return false;
	}
//	System.out.println("title="+title);
	return true;
}
/**
 * This method was writen by yhwu.
 * @return java.lang.String
 */
private String getResponse()
{
	String rbuf=null;
	StringBuffer clientdata=new StringBuffer();
	int r=0;

	try
	{
		pw.flush();

		clientdata.setLength(0);
ReplyLoop:	
		while((r=br.read())!=-1)
		{
			switch(r)
			{
				case 10:
					break;
				case 13:
					rbuf=clientdata.toString();
					clientdata.setLength(0);
					break ReplyLoop;
				default:
					clientdata.append((char)r);
					break;
			}
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	System.out.println("Server response: "+rbuf);
	return rbuf;
}
/**
 * This method was writen by yhwu.
 * @param thegroup java.lang.String
 */
private boolean group(String thegroup)
{
	String rbuf=null;
	try
	{
//		pw.println("group "+thegroup);
		pw.writeBytes("group "+thegroup+"\r\n");
		rbuf=getResponse();
//		System.out.println(rbuf);
		StringTokenizer st=new StringTokenizer(rbuf);
		if(st.hasMoreElements()) // state
		{
			if(st.nextToken().charAt(0)!='2')
			{
				ColaServer.BBSlog.Write("Set news group error: "+rbuf);
				return false;
			}
		}
		else
			return false;
					
		if(st.hasMoreElements()) //estimated number of articles in group
			st.nextToken();
		else
			return false;
					
		if(st.hasMoreElements()) //first article number in the group
		{
			try
			{
				firstnum=Integer.parseInt(st.nextToken());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
			return false;
					
		if(st.hasMoreElements()) //last article number in the group
		{
			try
			{
				lastnum=Integer.parseInt(st.nextToken());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
			return false;
					
		if(st.hasMoreElements())
			st.nextToken();
		else
			return false;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 * @return boolean
 */
private boolean nextArticle()
{
	String rbuf=null;

	try
	{
//		pw.println("next");
		pw.writeBytes("next\r\n");
		rbuf=getResponse();
		if(rbuf.charAt(0)!='2')
		{
//			ColaServer.BBSlog.Write("Next article error: "+rbuf);
			return false;
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 */
private void quit()
{
	String rbuf=null;
	try
	{
//		pw.println("quit");
		pw.writeBytes("quit\r\n");
		rbuf=getResponse();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return;
}
}