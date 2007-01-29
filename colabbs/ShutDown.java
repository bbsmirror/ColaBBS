package colabbs;

import java.io.*;
import java.lang.reflect.*;

public final class ShutDown extends Thread
{
	private int mode = 0;

	public ShutDown(int modebuf)
	{
		super(ColaServer.userGroup, "ShutDown");
		mode = modebuf;
	}

	//討厭的一段程式碼, 為了重開電腦用的
	Method SDM = null;
	Object[] SDM_PARAM = null;
	public ShutDown(int modebuf, Method sdm, Object[] sdm_param)
	{
		super(ColaServer.userGroup,"ShutDown");
		SDM = sdm;
		SDM_PARAM = sdm_param;
		mode=modebuf;
	}
	//
	
	public void run()
	{
		File NoteFile;
		int i,t = ColaServer.count+ColaServer.clientcount;
		
		System.out.println("Waiting for shutdown....");
		
		if (ColaServer.TelnetServer != null)
			ColaServer.TelnetServer.stop();
		else
			System.exit(1);
		
		for(i = 0; i < t; i++)
		{
			if (ColaServer.BBSUsers[i] != null)
			{
				ColaServer.BBSUsers[i].stop();
				if (ColaServer.BBSUsers[i].isAlive())
				{
					try
					{
						ColaServer.BBSUsers[i].join();
					}
					catch(InterruptedException e){}
				}
				if (ColaServer.BBSUsers[i].UserPassItem != null)
					ColaServer.BBSUsers[i].UFD.brclist.SaveFile();
					//ColaServer.BBSUsers[i].SaveBrc();
			}
		}

		File RegListFile;
		if ((RegListFile = new File(ColaServer.INI.BBSHome, "MailCheckList")).exists())
			RegListFile.delete();

		//ColaServer.BBSlog.flush();
		ColaServer.BBSlog.Close();
		ColaServer.logfile.Close();
		
		//討厭的一段程式碼, 為了重開電腦用的
		try
		{
			if (SDM != null)
				SDM.invoke(null, SDM_PARAM);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
		}
		//
		
		System.out.println("OK!");
		System.exit(mode);
	}
}