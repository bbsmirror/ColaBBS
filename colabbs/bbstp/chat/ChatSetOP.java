package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatSetOP extends MultiModuleCmdAdapter
{
	public String newOP=null;
public ChatSetOP() {
	super();
}
public ChatSetOP(String theOP)
{
	newOP=theOP;
}
}