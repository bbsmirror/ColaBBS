package colabbs.record;

import java.io.*;
import java.util.*;

import colabbs.Consts;
/**
 * default is 'not' readonly, programmer needs handle synchronize by his own while calling nonstatic method if needMonitor retrun true!
 */
public class RecordHandler
{
	//	private boolean readonly=false;
	//	private Object lockObject=null;
	private RecordType rt=null;
	//	private String Path=null,RecordFileName=null;

	private RandomAccessFile DirFile=null;

	public RecordHandler()
	{
		super();
		//	lockObject=new Object();
	}

	public RecordHandler(RecordType thert, String thePath, String theRecordFileName)
	{
		//	lockObject=new Object();
		rt=thert;
		//	Path=thePath;
		//	RecordFileName=theRecordFileName;
		if(thePath.charAt(thePath.length()-1)!=File.separatorChar)
			thePath+=File.separator;
		try
		{
			DirFile=new RandomAccessFile(thePath+theRecordFileName,"rw");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public RecordHandler(RecordType thert, String thePath, String theRecordFileName,boolean thereadonly)
	{
		//	lockObject=new Object();
		rt=thert;
		//	Path=thePath;
		//	RecordFileName=theRecordFileName;
		//	readonly=thereadonly;
		if(thePath.charAt(thePath.length()-1)!=File.separatorChar)
			thePath+=File.separator;
		try
		{
			if(thereadonly)
				DirFile=new RandomAccessFile(thePath+theRecordFileName,"r");
			else
				DirFile=new RandomAccessFile(thePath+theRecordFileName,"rw");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public RecordHandler(String thePath, String theRecordFileName)
	{
		//	lockObject=new Object();
		//	Path=thePath;
		//	RecordFileName=theRecordFileName;
		if(thePath.charAt(thePath.length()-1)!=File.separatorChar)
			thePath+=File.separator;
		try
		{
			DirFile=new RandomAccessFile(thePath+theRecordFileName,"rw");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public RecordHandler(String thePath, String theRecordFileName,boolean thereadonly)
	{
		//	lockObject=new Object();
		//	Path=thePath;
		//	RecordFileName=theRecordFileName;
		//	readonly=thereadonly;
		if(thePath.charAt(thePath.length()-1)!=File.separatorChar)
			thePath+=File.separator;
		try
		{
			if(thereadonly)
				DirFile=new RandomAccessFile(thePath+theRecordFileName,"r");
			else
				DirFile=new RandomAccessFile(thePath+theRecordFileName,"rw");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void append(byte[] b)
	{
		try
		{
			DirFile.seek(DirFile.length());
			DirFile.write(b);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void append(RecordType t)
	{
		append(t.getRecordBytes());
	}

	protected static void append(Object lockObj, byte[] b, String Path, String RecordFileName)
	{
		RandomAccessFile DirFile=null;
		
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		synchronized(lockObj)
		{
			try
			{
				DirFile=new RandomAccessFile(Path+RecordFileName,"rw");
				DirFile.seek(DirFile.length());
				DirFile.write(b);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(DirFile!=null)
					{
						DirFile.close();
						DirFile=null;
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void append(Object lockObj, RecordType t, String Path, String RecordFileName)
	{
		append(lockObj,t.getRecordBytes(),Path,RecordFileName);
	}

	public synchronized void close() 
	{
		try
		{
			if(DirFile!=null)
			{
				DirFile.close();
				DirFile=null;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void compress(Object lockObj, RecordType t, String Path, String RecordFileName)
	{
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		// Version 1, get less performance but more safe!
		synchronized(lockObj)
		{
			File DelFile=null,DirFile=new File(Path,RecordFileName),LockFile=new File(Path,RecordFileName+Consts.Deleted);
			RandomAccessFile Source=null,Target=null;
			
			if(DirFile.exists()&&LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Record file and lock file both exist, but delete lock file failure!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete the lock file by hand. (Maybe delete record file and rename lock file to record file is more correct.)");
			}
			
			if(DirFile.exists()&&!DirFile.renameTo(LockFile))
			{
				System.err.println("Can't rename record file to lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and renamd record file to lock file by hand.");
			}
			
			try
			{
				Source=new RandomAccessFile(LockFile,"r");
				Target=new RandomAccessFile(DirFile,"rw");
				int itemsize=t.getSize();
				long slen=Source.length();
				byte readbuf[]=new byte[itemsize];

				for(int i=0;i<(int)(Source.length()/itemsize);i++)
				{
					Source.read(readbuf);
					t.setRecord(readbuf);
					if(t.isDeleted())
					{
						String delname=t.deleteBody();
						if(delname!=null)
						{
							DelFile=new File(Path,delname);
							
							if(DelFile.exists()&&!DelFile.delete())
							{
								System.err.println("Can't delete body file!");
								System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
							}	
						}
					}
					else
						Target.write(readbuf);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				if(LockFile.exists()&&!DirFile.exists()&&!LockFile.renameTo(DirFile))
				{
					System.err.println("Warning! Can't rename lock file to record file!");
					System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and rename lock file to record file by hand.");
				}	
			}
			finally
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Warning! Can't delete lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
			}	
		}
	}

	public static void delete(Object lockObj,RecordType t,String Path,String RecordFileName)
	{
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		// Version 1, get less performance but more safe!
		synchronized(lockObj)
		{
			File DelFile=null,DirFile=new File(Path,RecordFileName),LockFile=new File(Path,RecordFileName+Consts.Deleted);
			RandomAccessFile Source=null,Target=null;
			RecordType rt;
			
			rt=(RecordType)t.clone();
			rt.setRecord((byte [])null);
			
			if(DirFile.exists()&&LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Record file and lock file both exist, but delete lock file failure!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete the lock file by hand. (Maybe delete record file and rename lock file to record file is more correct.)");
			}
			
			if(DirFile.exists()&&!DirFile.renameTo(LockFile))
			{
				System.err.println("Can't rename record file to lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and renamd record file to lock file by hand.");
			}
			
			try
			{
				Source=new RandomAccessFile(LockFile,"r");
				Target=new RandomAccessFile(DirFile,"rw");
				int itemsize=t.getSize();
				long slen=Source.length()/itemsize;
				byte readbuf[]=new byte[itemsize];

				for(int i=0;i<slen;i++)
				{
					Source.read(readbuf);
					rt.setRecord(readbuf);
					if(rt.isRangeDeletible()&&rt.equals(t))
					{
						String delname=rt.deleteBody();
						if(delname!=null)
						{
							DelFile=new File(Path,delname);
							
							if(DelFile.exists()&&!DelFile.delete())
							{
								System.err.println("Can't delete body file!");
								System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
							}	
						}
					}
					else
					{
						Target.write(readbuf);
					}
				}
			}
			catch(Exception e) //original is IOException
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
				if(LockFile.exists())
				{
					if(!DirFile.delete())
					{
						System.err.println("Can't delete record file while Exception!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete record file by hand.");
					}
					else if(!LockFile.renameTo(DirFile))
					{
						System.err.println("Warning! Can't rename lock file to record file!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and rename lock file to record file by hand.");
					}
				}
			}
			finally
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Warning! Can't delete lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
			}	
		}
	}

	public static void delete(Object lockObj, RecordType t, String Path, String RecordFileName, int x)
	{
		deleteRange(lockObj,t,Path,RecordFileName,x,x, true);
	}

	public static void deleteRange(Object lockObj,RecordType t,String Path,String RecordFileName,int start, int end)
	{
		deleteRange(lockObj, t, Path, RecordFileName, start, end, false);
	}

	public static void deleteRange(Object lockObj,RecordType t,String Path,String RecordFileName,int start, int end, boolean EnforceDelete)
	{
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		// Version 1, get less performance but more safe!
		synchronized(lockObj)
		{
			File DelFile=null,DirFile=new File(Path,RecordFileName),LockFile=new File(Path,RecordFileName+Consts.Deleted);
			RandomAccessFile Source=null,Target=null;
			
			if(DirFile.exists()&&LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Record file and lock file both exist, but delete lock file failure!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete the lock file by hand. (Maybe delete record file and rename lock file to record file is more correct.)");
			}
			
			if(DirFile.exists()&&!DirFile.renameTo(LockFile))
			{
				System.err.println("Can't rename record file to lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and renamd record file to lock file by hand.");
			}
			
			try
			{
				Source=new RandomAccessFile(LockFile,"r");
				Target=new RandomAccessFile(DirFile,"rw");
				int itemsize=t.getSize();
				long slen=Source.length();
				byte readbuf[]=new byte[itemsize],readbuf1[]=new byte[start*itemsize],readbuf2[]=new byte[(int)(slen-((end+1)*itemsize))];

				if(readbuf1.length!=0)
				{
					Source.read(readbuf1);
					Target.write(readbuf1);
				}

				for(int i=start;i<=end;i++)
				{
					Source.read(readbuf);
					t.setRecord(readbuf);
					String delname=t.deleteBody();
					if(delname!=null)
					{
						if (t.isRangeDeletible() || EnforceDelete)
						{
							DelFile=new File(Path,delname);
							
							if(DelFile.exists()&&!DelFile.delete())
							{
								System.err.println("Can't delete body file!");
								System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
							}
						}
						else
							Target.write(readbuf);
					}
				}
				
				Source.read(readbuf2);
				Target.write(readbuf2);
			}
			catch(Exception e) //original is IOException
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
				if(LockFile.exists())
				{
					if(!DirFile.delete())
					{
						System.err.println("Can't delete record file while Exception!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete record file by hand.");
					}
					else if(!LockFile.renameTo(DirFile))
					{
						System.err.println("Warning! Can't rename lock file to record file!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and rename lock file to record file by hand.");
					}
				}
			}
			finally
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Warning! Can't delete lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
			}	
		}
	}

	/**
	 * 批次砍信, 由傳入的 linklist 決定要保留哪些檔案
	 * 傳入的 linklist 每個 element 是由 int[2] 構成
	 * 意思是要保留的起始值和結束值
	 */
	public static void deleteBatchReserve(Object lockObj, RecordType t, String Path, String RecordFileName, Vector reservelist, boolean EnforceDelete)
	{
		Enumeration list = reservelist.elements();
		if (!list.hasMoreElements())
			return;
		int reserverange[];
		
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		// Version 1, get less performance but more safe!
		synchronized(lockObj)
		{
			File DelFile=null,DirFile=new File(Path,RecordFileName),LockFile=new File(Path,RecordFileName+Consts.Deleted);
			RandomAccessFile Source=null,Target=null;
			
			if(DirFile.exists()&&LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Record file and lock file both exist, but delete lock file failure!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete the lock file by hand. (Maybe delete record file and rename lock file to record file is more correct.)");
			}
			
			if(DirFile.exists()&&!DirFile.renameTo(LockFile))
			{
				System.err.println("Can't rename record file to lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and renamd record file to lock file by hand.");
			}
			
			try
			{
				Source = new RandomAccessFile(LockFile, "r");
				Target = new RandomAccessFile(DirFile, "rw");
				
				int itemsize = t.getSize();
				long slen = Source.length();
				int articles = (int)(slen / itemsize);
				byte readbufs[];
				byte readbufd[] = new byte[itemsize];
				int lastremove = 0;
				while(list.hasMoreElements())
				{
					reserverange = (int[])list.nextElement();
					if (reserverange[0] >= articles)
						break;
					if (reserverange[1] >= articles)
						reserverange[1] = articles - 1;
					
					//Source.seek(itemsize * (reserverange[0] - lastremove + 1));
					for (int i = lastremove; i < reserverange[0]; i++)
					{
						Source.read(readbufd);
						t.setRecord(readbufd);
						String delname = t.deleteBody();
						if (delname != null)
						{
							if (t.isRangeDeletible() || EnforceDelete)
							{
								DelFile = new File(Path,delname);
								
								if(DelFile.exists() && !DelFile.delete())
								{
									System.err.println("Can't delete body file!");
									System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
								}
							}
							else
								Target.write(readbufd);
						}
					}
					
					readbufs = new byte[itemsize * (reserverange[1] - reserverange[0] + 1)];
					Source.read(readbufs);
					Target.write(readbufs);
					
					lastremove = reserverange[1] + 1;
					if (lastremove >= articles)
						break;
				}

				for (int i = lastremove; i < articles; i++)
				{
					Source.read(readbufd);
					t.setRecord(readbufd);
					String delname = t.deleteBody();
					if (delname != null)
					{
						if (t.isRangeDeletible() || EnforceDelete)
						{
							DelFile = new File(Path,delname);
							
							if(DelFile.exists() && !DelFile.delete())
							{
								System.err.println("Can't delete body file!");
								System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
							}
						}
						else
							Target.write(readbufd);
					}
				}						
			}
			catch(Exception e) //original is IOException
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
				if(LockFile.exists())
				{
					if(!DirFile.delete())
					{
						System.err.println("Can't delete record file while Exception!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete record file by hand.");
					}
					else if(!LockFile.renameTo(DirFile))
					{
						System.err.println("Warning! Can't rename lock file to record file!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and rename lock file to record file by hand.");
					}
				}
			}
			finally
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Warning! Can't delete lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
			}	
		}
	}

	/**
	 * 批次砍信, 由傳入的 linklist 決定要砍掉哪些檔案
	 * 傳入的 linklist 每個 element 是由 int[2] 構成
	 * 意思是要砍掉的起始值和結束值
	 */
	public static void deleteBatchRemove(Object lockObj, RecordType t, String Path, String RecordFileName, Vector deletelist, boolean EnforceDelete)
	{
		Enumeration list = deletelist.elements();
		if (!list.hasMoreElements())
			return;
		int delrange[];
		
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		// Version 1, get less performance but more safe!
		synchronized(lockObj)
		{
			File DelFile=null,DirFile=new File(Path,RecordFileName),LockFile=new File(Path,RecordFileName+Consts.Deleted);
			RandomAccessFile Source=null,Target=null;
			
			if(DirFile.exists()&&LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Record file and lock file both exist, but delete lock file failure!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete the lock file by hand. (Maybe delete record file and rename lock file to record file is more correct.)");
			}
			
			if(DirFile.exists()&&!DirFile.renameTo(LockFile))
			{
				System.err.println("Can't rename record file to lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and renamd record file to lock file by hand.");
			}
			
			try
			{
				Source = new RandomAccessFile(LockFile, "r");
				Target = new RandomAccessFile(DirFile, "rw");
				
				int itemsize = t.getSize();
				long slen = Source.length();
				int articles = (int)(slen / itemsize);
				byte readbufs[];
				byte readbufd[] = new byte[itemsize];
				int lastsave = 0;
				while(list.hasMoreElements())
				{
					delrange = (int[])list.nextElement();
					if (delrange[0] >= articles)
						break;
					if (delrange[1] >= articles)
						delrange[1] = articles - 1;
					
					readbufs = new byte[itemsize * (delrange[0] - lastsave)];
					Source.read(readbufs);
					Target.write(readbufs);
					
					for (int i = delrange[0]; i <= delrange[1]; i++)
					{
						Source.read(readbufd);
						t.setRecord(readbufd);
						String delname = t.deleteBody();
						if (delname != null)
						{
							if (t.isRangeDeletible() || EnforceDelete)
							{
								DelFile = new File(Path,delname);
								
								if(DelFile.exists() && !DelFile.delete())
								{
									System.err.println("Can't delete body file!");
									System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
								}
							}
							else
								Target.write(readbufd);
						}
					}
					lastsave = delrange[1] + 1;
					if (lastsave >= articles)
						break;
					//Source.seek(itemsize * lastsave);
				}
				if (lastsave < articles)
				{
					readbufs = new byte[itemsize * (articles - lastsave)];
					Source.read(readbufs);
					Target.write(readbufs);
				}
				
				/*int itemsize=t.getSize();
				long slen=Source.length();
				byte readbuf[]=new byte[itemsize],readbuf1[]=new byte[start*itemsize],readbuf2[]=new byte[(int)(slen-((end+1)*itemsize))];

				if(readbuf1.length!=0)
				{
				Source.read(readbuf1);
				Target.write(readbuf1);
				}

				for(int i=start;i<=end;i++)
				{
				Source.read(readbuf);
				t.setRecord(readbuf);
				String delname=t.deleteBody();
				if(delname!=null)
				{
				if (t.isRangeDeletible() || EnforceDelete)
				{
				DelFile=new File(Path,delname);
				
				if(DelFile.exists()&&!DelFile.delete())
				{
				System.err.println("Can't delete body file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
				}
				}
				else
				Target.write(readbuf);
				}
				}
				
				Source.read(readbuf2);
				Target.write(readbuf2);*/
			}
			catch(Exception e) //original is IOException
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
				if(LockFile.exists())
				{
					if(!DirFile.delete())
					{
						System.err.println("Can't delete record file while Exception!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete record file by hand.");
					}
					else if(!LockFile.renameTo(DirFile))
					{
						System.err.println("Warning! Can't rename lock file to record file!");
						System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and rename lock file to record file by hand.");
					}
				}
			}
			finally
			{
				try
				{
					if(Target!=null)
					{
						Target.close();
						Target=null;
					}	
					if(Source!=null)
					{
						Source.close();
						Source=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(LockFile.exists()&&!LockFile.delete())
			{
				System.err.println("Warning! Can't delete lock file!");
				System.err.println("Maybe monitor not handle correctly. Please check your plasmids, and delete it by hand.");
			}	
		}
	}

	protected byte[] get(int x)
	{
		byte readbuf[]=new byte[rt.getSize()];
		
		try
		{
			DirFile.seek(x*rt.getSize());
			if(DirFile.read(readbuf)<=0)
				return null;
			return readbuf;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected byte[] get(int x, RecordType t)
	{
		byte readbuf[]=new byte[t.getSize()];
		
		try
		{
			DirFile.seek(x*t.getSize());
			if(DirFile.read(readbuf)<=0)
				return null;
			return readbuf;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected static byte[] get(int x, Object lockObj, int size, String Path, String RecordFileName)
	{
		byte b[]=new byte[size];
		RandomAccessFile DirFile=null;
		
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		synchronized(lockObj)
		{
			try
			{
				DirFile=new RandomAccessFile(Path+RecordFileName,"r");
				DirFile.seek(x*size);
				DirFile.read(b);
				return b;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(DirFile!=null)
					{
						DirFile.close();
						DirFile=null;
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public RecordType getRecord(int x)
	{
		RecordType tmp=(RecordType)rt.clone();
		tmp.setRecord(get(x));
		return tmp;
	}

	public boolean getRecord(int x, RecordType t)
	{
		byte tmp[]=get(x,t);
		if(tmp!=null)
		{
			t.setRecord(tmp);
			return true;
		}
		return false;
	}

	public static boolean getRecord(int x, Object lockObj, RecordType t, String Path, String RecordFileName)
	{
		byte tmp[]=get(x,lockObj,t.getSize(),Path,RecordFileName);
		if(tmp!=null)
		{
			t.setRecord(tmp);
			return true;
		}
		return false;
	}

	public static void moveRecord(Object theObj, RecordType t, String thePath, String theFileName, int from, int to)
	{
		if(from==to)
			return;
		if(thePath.charAt(thePath.length()-1)!=File.separatorChar)
			thePath+=File.separator;
		synchronized(theObj)
		{
			RandomAccessFile DirFile=null;
			
			try
			{
				byte keep[]=new byte[t.getSize()],buf[];
				DirFile=new RandomAccessFile(thePath+theFileName,"rw");
				if(from>to)
				{
					buf=new byte[(from-to)*t.getSize()];
					DirFile.seek(to*t.getSize());
					DirFile.read(buf);
					DirFile.read(keep);
					DirFile.seek(to*t.getSize());
					DirFile.write(keep);
					DirFile.write(buf);
				}
				else
				{
					buf=new byte[(to-from)*t.getSize()];
					DirFile.seek(from*t.getSize());
					DirFile.read(keep);
					DirFile.read(buf);
					DirFile.seek(from*t.getSize());
					DirFile.write(buf);
					DirFile.write(keep);
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
					if(DirFile!=null)
					{
						DirFile.close();
						DirFile=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	protected byte[] next()
	{
		byte readbuf[]=new byte[rt.getSize()];
		
		try
		{
			if(DirFile.read(readbuf)<=0)
				return null;
			return readbuf;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected byte[] next(RecordType t)
	{
		byte readbuf[]=new byte[t.getSize()];
		
		try
		{
			if(DirFile.read(readbuf)<=0)
				return null;
			return readbuf;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public RecordType nextElement()
	{
		RecordType tmp=(RecordType)rt.clone();
		tmp.setRecord(next());
		return tmp;
	}

	public boolean nextElement(RecordType t)
	{
		byte tmp[]=next(t);
		if(tmp!=null)
		{
			t.setRecord(tmp);
			return true;
		}
		return false;
	}

	public long recordNumber(RecordType t)
	{
		try
		{
			if(DirFile!=null)
				return DirFile.length()/t.getSize()-1;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	public static long recordNumber(RecordType rt, String Path, String RecordFileName)
	{
		File rec=new File(Path,RecordFileName);
		if(!rec.exists())
			return -1;
		return rec.length()/rt.getSize()-1;
	}

	public void setIndex(int x, RecordType t)
	{
		try
		{
			if(DirFile!=null)
				DirFile.seek(x*t.getSize());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void setRecordType(RecordType thert)
	{
		rt=thert;
	}

	protected void update(int x, byte[] b)
	{
		try
		{
			DirFile.seek(x*b.length);
			DirFile.write(b);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void update(int index, RecordType t)
	{
		update(index,t.getRecordBytes());
	}

	protected static void update(int index, Object lockObj, byte[] b, String Path, String RecordFileName)
	{
		RandomAccessFile DirFile=null;
		
		if(Path.charAt(Path.length()-1)!=File.separatorChar)
			Path+=File.separator;
		synchronized(lockObj)
		{
			try
			{
				DirFile=new RandomAccessFile(Path+RecordFileName,"rw");
				DirFile.seek(index*b.length);
				DirFile.write(b);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(DirFile!=null)
					{
						DirFile.close();
						DirFile=null;
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void update(int index, Object lockObj, RecordType t, String Path, String RecordFileName)
	{
		update(index,lockObj,t.getRecordBytes(),Path,RecordFileName);
	}
}