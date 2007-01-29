package colabbs.System;

public class ServicePlasmid_Item
{
	public String Plasmid_Name = "";
	public Class Plasmid_Class = null;
	public ServicePlasmid Plasmid_Object = null;
	
	public ServicePlasmid_Item(String name)
	{
		Plasmid_Name = name;
		try
		{
			Plasmid_Class = Class.forName(name);
			Plasmid_Object = (ServicePlasmid)Plasmid_Class.newInstance();
		}
		catch (Exception e)
		{
			Plasmid_Class = null;
			Plasmid_Object = null;
		}
	}
	
	public boolean init()
	{
		return Plasmid_Object.Init();
	}
	
	public void reloadSetting()
	{
		Plasmid_Object.ReloadSetting();
	}
	
	public boolean start()
	{
		if (Plasmid_Object.Start())
			Plasmid_Object.start();
		else
			return false;
		return true;
	}
	
	public boolean stop()
	{
		if (Plasmid_Object.Stop())
			Plasmid_Object.stop();
		else
			return false;
		return true;
	}
	
	public boolean suspend()
	{
		if (Plasmid_Object.Suspend())
			Plasmid_Object.suspend();
		else
			return false;
		return true;
	}
	
	public boolean restart()
	{
		if (Plasmid_Object.Restart())
		{
			Plasmid_Object.stop();
			Plasmid_Object.start();
		}
		else
			return false;
		return true;
	}
}
