package colabbs.bbstp.chat;

import java.io.*;
import java.util.*;

import colabbs.bbstp.MultiModuleReplyAdapter;
public class ChatActions extends MultiModuleReplyAdapter
{
	public Hashtable Verb1_1=null,Verb1_2=null,Verb2=null,Verb3=null;
public ChatActions() {
	super();
}
public ChatActions(Hashtable theVerb1_1, Hashtable theVerb1_2, Hashtable theVerb2, Hashtable theVerb3)
{
	Verb1_1=theVerb1_1;
	Verb1_2=theVerb1_2;
	Verb2=theVerb2;
	Verb3=theVerb3;
}
}