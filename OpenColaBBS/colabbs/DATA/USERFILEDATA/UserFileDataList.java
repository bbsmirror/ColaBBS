package colabbs.DATA.USERFILEDATA;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.DATA.PROPERTIES.*;
import colabbs.UTILS.FILE.*;

public class UserFileDataList
{
	public static int CACHE_SIZE = 10;
	public Hashtable OnlineList = new Hashtable();
	public Vector CacheList = new Vector();
	
	public Hashtable passlist;
	
	public String PassFile = new String();
	
	public UserFileDataList()
	{
		PassFile = ColaServer.INI.BBSHome + Consts.PassFile;
	}

	public UserFileDataList(String xPassFile)
	{
		PassFile = xPassFile;
	}
	
	public Enumeration getPassList()
	{
		return passlist.elements();
	}
	
	public boolean exist(String id)
	{
		return passlist.containsKey(id.trim().toLowerCase());
	}
	
	public PassItem getPass(String id)
	{
		return (PassItem)passlist.get(id.trim().toLowerCase());
	}
	
	private void removePass(String id)
	{
		passlist.remove(id.trim().toLowerCase());
	}
	
	private void removePass(PassItem pi)
	{
		passlist.remove(pi.IDItem.trim().toLowerCase());
	}
	
	public synchronized UserFileData newUser(String id, String password)
	{
		String pb = ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(id.charAt(0)) + File.separator + id +  File.separator;
		File PathBuf = new File(pb);
		if (PathBuf.exists())
			new DelTree(pb, false);
		PathBuf.mkdirs();
		
		pb = ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(id.charAt(0)) + File.separator + id +  File.separator; 
		PathBuf = new File(pb);
		if (PathBuf.exists())
			new DelTree(pb, false);
		PathBuf.mkdirs();
		
		UserFileData UFD = new UserFileData();
		UFD.ID = id;
		UFD.PassWord = password;
		UFD.Perm = ColaServer.INI.OrgPerm;
		newUFD(UFD);
		
		return UFD;
	}
	
	public synchronized void deleteUser(String id)
	{
		deleteUser(getPass(id));
	}
	
	public synchronized void deleteUser(PassItem pi)
	{
		if (pi == null)
			return;
		
		removePass(pi.IDItem);
		
		UserFileData UFD = get(pi.uid);
		UFD.ID = "\0" + UFD.ID;
		save(UFD);
		clearCache(UFD);
		
		//沒有清掉使用者好友關係, 之後要補上
		/*FriendList.removemeAll("friends", pi.IDItem);
		FriendList.removemeAll("blacks", pi.IDItem);*/
		//GroupList.removemeAll(pi.IDItem);
		
		String pb = ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(pi.IDItem.charAt(0)) + File.separator + pi.IDItem +  File.separator;
		File PathBuf = new File(pb);
		if (PathBuf.exists())
			new DelTree(pb, false);
		pb = ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(pi.IDItem.charAt(0)) + File.separator + pi.IDItem +  File.separator; 
		PathBuf = new File(pb);
		if (PathBuf.exists())
			new DelTree(pb, false);
	}
	
	public int users()
	{
		return passlist.size();
	}

	public boolean LoadFile(String FileName)	
	{
		PassFile = FileName;
		return LoadFile();
	}
	
	public boolean LoadFile()
	{
		File F = new File(PassFile);

		if(F.exists())
		{
			int i;
			byte buf[] = new byte[Consts.IDLen + 2 + Consts.PassLen];
			int L = (int)(F.length() / Consts.UserLen);
			String id, pw;
			RandomAccessFile raf = null;
			
			try
			{
				raf = new RandomAccessFile(F, "r");
				
				passlist = new Hashtable(L);
				for (i = 0; i < L; i++)
				{
					raf.seek(Consts.UserLen * i);
					raf.read(buf);
					id = (new String(buf, 0, 0, Consts.IDLen)).trim();
					if (id.length() != 0 && buf[0] != 0 && buf[0] != 1)
					{
						pw = (new String(buf, 0, Consts.IDLen + 2, Consts.PassLen)).trim();
						passlist.put(id.toLowerCase(), new PassItem(i, id, pw));
					}
				}				
				raf.close();
				raf = null;
			}
			catch (Exception e)
			{
				ColaServer.logfile.WriteException("UserFileDataList.LoadFile:", e);
				try
				{
					ColaServer.ErrorMsg = e.getMessage();
					if (raf != null)
						raf.close();
					return false;
				}
				catch (Exception e1)
				{
					ColaServer.logfile.WriteException("UserFileDataList.LoadFile:", e1);
					return false;
				}
			}
		}
		else
			passlist = new Hashtable();
		
		return true;
	}
	
	public void clearCache()
	{
		CacheList.removeAllElements();
	}
	
	public void clearCache(UserFileData ufd)
	{
		CacheList.removeElement(ufd.ID.toLowerCase());
	}
	
	public boolean isFull()
	{
		return (ColaServer.UFDList.users() >= ColaServer.INI.MaxUsers);
	}
	
	public synchronized boolean removeUFD(String id)
	{
		return removeUFD(getPass(id).uid, id);
	}
	
	public synchronized boolean removeUFD(UserFileData ufd)
	{
		return removeUFD(ufd.uid, ufd.ID);
	}
	
	private synchronized boolean removeUFD(int uid, String id)
	{
		byte buf[] = new byte[Consts.IDLen + 2];
		int i;
		RandomAccessFile raf = null;
		
		for (i = 0; i < Consts.IDLen + 2; i++)
			buf[i] = 0;
		
		try
		{
			raf = new RandomAccessFile(PassFile, "rw");
			raf.seek(Consts.UserLen * uid);
			raf.write(buf);
			raf.close();
			raf = null;
			
			passlist.remove(id.toLowerCase());
			
			return true;
		}
		catch (Exception e)
		{
			ColaServer.logfile.WriteException("移除使用者資料檔錯誤", e);
		}
		finally
		{
			try
			{
				if (raf != null)
					raf.close();
			}
			catch (Exception e)
			{
				ColaServer.logfile.WriteException("移除使用者資料檔錯誤", e);
			}
		}		
		return false;
	}
	
	public synchronized int newUFD(UserFileData ufd)
	{
		byte buf[] = new byte[Consts.IDLen + 2];
		int i, L;
		RandomAccessFile raf = null;
		String id;
		
		try
		{
			raf = new RandomAccessFile(PassFile, "rw");
			L = (int)(raf.length() / Consts.UserLen);
			for (i = 0; i < L; i++)
			{
				raf.seek(Consts.UserLen * i);
				raf.read(buf);
				id = new String(buf, 0, 0, Consts.IDLen).trim();
				//if (id.length() == 0)
				if (id.length() == 0 || buf[0] == 0 || buf[0] == 1)
					break;
			}
			raf.close();
			raf = null;
			
			ufd.uid = i;
			save(ufd);
			
			passlist.put(ufd.ID.toLowerCase(), new PassItem(ufd));
			
			return i;
		}
		catch (Exception e)
		{
			ColaServer.logfile.WriteException("新增使用者資料檔錯誤", e);
		}
		finally
		{
			try
			{
				if (raf != null)
					raf.close();
			}
			catch (Exception e)
			{
				ColaServer.logfile.WriteException("新增使用者資料檔錯誤", e);
			}
		}		
		return -1;
	}

	public UserFileData login(UserFileData ufd)
	{		
		if (!OnlineList.containsKey(ufd.ID.toLowerCase()))
		{
			OnlineList.put(ufd.ID.toLowerCase(), ufd);
		}
		
		ufd.logins++;
		
		return ufd;
	}
	
	public void changerPassWord(UserFileData ufd, String NewPassWord)
	{
		ufd.PassWord = NewPassWord;
		getPass(ufd.ID).PassWordItem = NewPassWord;
		save(ufd);
	}
	
	public void changerPassWord(String id, String NewPassWord)
	{
		UserFileData ufd = get(id);
		ufd.PassWord = NewPassWord;
		getPass(id).PassWordItem = NewPassWord;
		save(ufd);
	}
	
	public UserFileData login(String id)
	{
		UserFileData ufd = null;
		if (OnlineList.containsKey(id.toLowerCase()))
		{
			ufd = (UserFileData)(OnlineList.get(id.toLowerCase()));
		}
		else
		{
			ufd = get(id);
			OnlineList.put(id.toLowerCase(), ufd);
		}
		
		ufd.logins++;
		
		return ufd;
	}
	
	public void logout(String id)
	{
		UserFileData ufd = null;
		if (OnlineList.containsKey(id.toLowerCase()))
		{
			ufd = (UserFileData)(OnlineList.get(id.toLowerCase()));
		}
		else
			return;
		
		ufd.logins--;
		save(ufd);
		if (ufd.logins == 0)
		{
			OnlineList.remove(id.toLowerCase());		
			ufd = null;
		}
	}
	
/*	public UserFileData get(String id, boolean throughCache)
	{
		if (throughCache)
			return get(id);
		else
			return get(getPass(id).uid);
	}*/
	
	public UserFileData get(String id)
	{
		if (OnlineList.containsKey(id.toLowerCase()))
			return ((UserFileData)OnlineList.get(id.toLowerCase()));
		
		UserFileData ufd;
		
		//可以改用 indexOf 
		Enumeration e = CacheList.elements();
		while (e.hasMoreElements())
		{
			ufd = (UserFileData)e.nextElement();
			if (ufd.ID.equalsIgnoreCase(id))
				return ufd;
		}
		
		PassItem pi = ColaServer.UFDList.getPass(id);
		if (pi == null)
			return null;
		ufd = get(pi.uid);
		
		CacheList.insertElementAt(ufd, 0);
		if (CacheList.size() > CACHE_SIZE)
			CacheList.setSize(CACHE_SIZE);
		
		return ufd;
	}
	
	public UserFileData get(int uid)
	{
		byte buf[] = new byte[Consts.UserLen];
		RandomAccessFile raf = null;

		try
		{
			raf = new RandomAccessFile(PassFile, "r");
			raf.seek(uid * Consts.UserLen);
			raf.read(buf);
			raf.close();
			return new UserFileData(uid, buf);
		}
		catch (Exception e)
		{
			ColaServer.logfile.WriteException("讀取使用者 " + uid + " 資料檔錯誤", e);
		}
		finally
		{
			try
			{
				if (raf != null)
					raf.close();
			}
			catch (Exception e)
			{
				ColaServer.logfile.WriteException("讀取使用者 " + uid + " 資料檔錯誤", e);
			}
		}		
		return null;
	}
	
	public void save(String id)
	{
		UserFileData ufd = null;
		
		if (OnlineList.containsKey(id.toLowerCase()))
			ufd = (UserFileData)OnlineList.get(id.toLowerCase());
		else
		{
			Enumeration e = CacheList.elements();
			while (e.hasMoreElements())
			{
				ufd = (UserFileData)e.nextElement();
				if (ufd.ID.equalsIgnoreCase(id))
					break;
			}
		}
		
		save(ufd.uid, ufd);
	}
	
	public void save(UserFileData ufd)
	{
		save(ufd.uid, ufd);
	}
	
	public synchronized void save(int uid, UserFileData ufd)
	{
		byte buf[] = ufd.toFileBuf();
		RandomAccessFile raf = null;

		try
		{
			raf = new RandomAccessFile(PassFile, "rw");
			raf.seek(uid * Consts.UserLen);
			raf.write(buf);
			raf.close();
			
			passlist.put(ufd.ID.toLowerCase(), new PassItem(ufd));
			
			return;
		}
		catch (Exception e)
		{
			ColaServer.logfile.WriteException("寫入使用者 " + uid + " 資料檔錯誤", e);
		}
		finally
		{
			try
			{
				if (raf != null)
					raf.close();
			}
			catch (Exception e)
			{
				ColaServer.logfile.WriteException("寫入使用者 " + uid + " 資料檔錯誤", e);
			}
		}		
		return;
	}

	ColaProperties getProperties(String Who, String PropertiesName, ColaProperties default_cp)
	{
		String F = ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(Who.charAt(0)) + File.separator + Who + File.separator + PropertiesName + ".PRO";
		ColaProperties cp = (new ColaProperties(default_cp));
		cp.LoadFile(PropertiesName);
		return cp;
	}
}
