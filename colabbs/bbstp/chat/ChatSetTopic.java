package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleReply;
import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatSetTopic extends MultiModuleCmdAdapter implements MultiModuleReply
{
	public String newTopic=null;
public ChatSetTopic() {
	super();
}
public ChatSetTopic(String theTopic)
{
	newTopic=theTopic;
}
}