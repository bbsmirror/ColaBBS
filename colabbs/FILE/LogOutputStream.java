package colabbs.FILE;

import java.io.*;

/**
 * OutputStream �M LogFile ����������,
 * �i�H�Φb System.setErr �W, �N�зǿ��~��X�ɨ�Y�� LogFile �W.
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
