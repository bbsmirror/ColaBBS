package colabbs.net;

import java.io.*;
import java.net.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public abstract class NetworkServer implements java.lang.Cloneable, java.lang.Runnable
{
	public Socket clientSocket = null;
	private Thread serverInstance = null;
	private ServerSocket serverSocket = null;
	public OutputStream os = null;
	public InputStream is = null;
	public NetworkServer()
	{
		clientSocket = null;
	}
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public void close()
		throws IOException
	{
		if(clientSocket!=null)
		{
			clientSocket.close();
			clientSocket = null;
		}
		if(is!=null)
		{
			is.close();
			is = null;
		}
		if(os!=null)
		{
			os.close();
			os = null;
		}
	}
	public abstract void doService() throws IOException;
	public boolean isClientOpen()
	{
		return clientSocket != null;
	}
	public final void run()
	{
		if(serverSocket != null)
		{
//			Thread.currentThread().setPriority(10);
			try
			{
				while(true)
				{
					Socket socket = serverSocket.accept();
					NetworkServer networkserver = (NetworkServer)clone();
					networkserver.serverSocket = null;
					networkserver.clientSocket = socket;
					(new Thread(networkserver)).start();
				}
			}
			catch(Exception e)
			{
				System.out.print("Server error:\r\n");
				e.printStackTrace();
			}
			try
			{
				serverSocket.close();
			}
			catch(IOException e) {}
		}
		else
		{
			try
			{
//				os = new BufferedOutputStream(clientSocket.getOutputStream());
//				is = new BufferedInputStream(clientSocket.getInputStream());
				os = clientSocket.getOutputStream();
				is = clientSocket.getInputStream();
				doService();
			}
			catch(Exception exception1) {}
			try
			{
				close();
			}
			catch(IOException ioexception) {}
		}
	}
	public final void startServer(int i,String ThreadName)
		throws IOException
	{
		serverSocket = new ServerSocket(i, 50);
		serverInstance = new Thread(this,ThreadName);
		serverInstance.start();
	}
	public final void startServer(int i,InetAddress addr,String ThreadName)
		throws IOException
	{
		serverSocket = new ServerSocket(i, 50,addr);
		serverInstance = new Thread(this,ThreadName);
		serverInstance.start();
	}
}