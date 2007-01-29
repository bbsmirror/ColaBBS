package colabbs.UTILS.FILE;

import java.io.*;

import colabbs.UTILS.*;
 
public final class DelTree extends Thread 
{
	String Path;
	
	public DelTree(String name) 
	{
		super();
		
		Path=name;
		this.start();
	}

	public DelTree(String name, boolean fork)
	{
		super();
		
		Path=name;
		
		if (fork)
			this.start();
		else
			DelDirectory(Path);
	}

	private final void DelDirectory(String DirName) 
	{
		int tries;
		File Path1 = new File(DirName);
		
		if(Path1.exists())
		{
			if(Path1.isDirectory())
			{
				int i;
				String dirs[]=Path1.list();
				
				for(i=0;i<dirs.length;i++)
					DelDirectory(DirName+dirs[i]);
			}
			tries=0;
			while(tries<10&&Path1.exists()&&!Path1.delete())
			{
				TIME.Delay(100);
				tries++;
			}	
		}		
	}

	public static void main(String args[]) 
	{
		if(args.length>=1)
		{
			System.out.println("Starting Del");
			new DelTree(args[0]);
		}	
		return;
	}
	public void run ( ) 
	{
		DelDirectory(Path);
		return;
	}
}