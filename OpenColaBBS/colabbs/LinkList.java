package colabbs;

import java.net.*;
import java.io.*;
import java.util.*;

public class LinkList
{
	private LinkType first = null;

	public void additem(LinkType lastitem, LinkType nextitem, Object d)
	{
		LinkType newlink = new LinkType();
		newlink.obj = d;
		if (first == null)
		{
			first = newlink;
			newlink.next = newlink;
			newlink.last = newlink;
		}
		else
		{
			newlink.last = lastitem;
			newlink.next = nextitem;
			lastitem.next = newlink;
			nextitem.last = newlink;
		}
	}
	public Object del1()
	{
		Object temp = null;

		if (first == null)
			return null;
		else
			temp = first.obj;
		first.last.next = first.next;
		first.next.last = first.last;
		if (first == first.next)
			first = null;
		else
			first = first.next;
		return temp;
	}
	public Object delitem(LinkType node)
	{
		if (first == null)
			return null;
		node.last.next = node.next;
		node.next.last = node.last;
		if (first == node)
		{
			if (first == first.next)
				first = null;
			else
				first = first.next;
		}
		return node.obj;
	}
	public LinkType GetBase()
	{
		return first;
	}
	public LinkType Goto(short Index)
	{
		short i;
		LinkType ptr = first;

		for(i = 0;i<Index;i++)
			ptr = ptr.next;
		return ptr;
	}
	public void ladditem(Object d)
	{
		LinkType newlink = new LinkType();
		newlink.obj = d;
		if (first == null)
		{
			first = newlink;
			newlink.next = newlink;
			newlink.last = newlink;
		}
		else
		{
			newlink.last = first.last;
			newlink.next = first;
			newlink.last.next = newlink;
			first.last = newlink;
			first = newlink;
		}
	}
	public void radditem(Object d)
	{
		LinkType newlink = new LinkType();
		newlink.obj = d;
		if (first == null)
		{
			first = newlink;
			newlink.next = newlink;
			newlink.last = newlink;
		}
		else
		{
			newlink.next = first;
			newlink.last = first.last;
			first.last = newlink;
			newlink.last.next = newlink;
		}
	}
}