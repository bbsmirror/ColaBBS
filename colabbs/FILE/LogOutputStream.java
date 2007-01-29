package colabbs.FILE;

import java.io.*;

/**
 * OutputStream 和 LogFile 之間的橋樑,
 * 可以用在 System.setErr 上, 將標準錯誤輸出導到某個 LogFile 上.
 */
public class LogOutputStream extends OutputStream
{
	private LogFile logfile = null;
	
	public LogOutputStream()
	{
		
	}
	
	public LogOutputStream(LogFile lf)
	{
		logfile = lf;
	}
	
	public void close()
	{
		logfile.Close();
		logfile = null;
	}
	
	public void flush()
	{
	}
	
	public void write(byte[] buf)
	{
		String tmp = (new String(buf));
		if (!tmp.equals("\n") && !tmp.equals("\r\n"))
			logfile.Write(tmp);
	}
	
	public void write(byte[] buf, int b, int e)
	{
		String tmp = new String(buf, b, e);
		if (!tmp.equals("\n") && !tmp.equals("\r\n"))
			logfile.Write(tmp);
	}
	
	public void write(int i)
	{
		String tmp = String.valueOf(i);
		if (!tmp.equals("\n") && !tmp.equals("\r\n"))
			logfile.Write(tmp);
	}
}
