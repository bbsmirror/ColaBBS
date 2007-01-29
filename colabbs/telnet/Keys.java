package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

//於本程式有許多地方用Utils.Cut的可以改成clrtoeol比較快!!
//這也是telnet才有

public class Keys
{
	public final static int BackSpace = 8;
	public final static int Tab = 9;
	public final static int Esc = 27;
	public final static int Enter = 13;
	public final static int Space = 32;
	public final static int Up = 0x0101;
	public final static int Down = 0x0102;
	public final static int Right = 0x0103;
	public final static int Left = 0x0104;
	public final static int F1 = 0x0105;
	public final static int F2 = 0x0106;
	public final static int F3 = 0x0107;
	public final static int F4 = 0x0108;
	public final static int F5 = 0x0109;
	public final static int F6 = 0x010a;
	public final static int F7 = 0x010b;
	public final static int F8 = 0x010c;
	public final static int F9 = 0x010d;
	public final static int F10 = 0x010e;
	public final static int F11 = 0x010f;
	public final static int F12 = 0x0110;
	public final static int Home = 0x0201;
	public final static int Ins = 0x0202;
	public final static int Del = 0x0203;
	public final static int End = 0x0204;
	public final static int PgUp = 0x0205;
	public final static int PgDn = 0x0206;
}