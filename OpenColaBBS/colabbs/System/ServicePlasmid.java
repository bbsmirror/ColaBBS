package colabbs.System;

public abstract class ServicePlasmid extends Thread 
{
	public int PID = -1;
	public boolean Init()
	{
		return true;
	}
	
	public boolean Start()
	{
		return true;
	}
	
	public boolean Restart()
	{
		return true;
	}	
	
	public boolean Stop()
	{
		return true;
	}	
	
	public boolean Suspend()
	{
		return true;
	}
	
	public boolean ReloadSetting()
	{
		return true;
	}
	
	public boolean isSupportRestart()
	{
		return false;
	}
	
	public boolean isSupportStop()
	{
		return false;
	}
			
	public boolean isSupportSuspend()
	{
		return false;
	}
	
	public boolean isSupportReloadSetting()
	{
		return false;
	}
	
	public abstract boolean Body();
	
	public void run()
	{
		Body();
	}
}
