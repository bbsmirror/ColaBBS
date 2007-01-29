package colabbs;

import java.net.*;
import java.io.*;
import java.util.*;

public final class Perm
{
	public final static int Basic=0x1;			//���v�O
	public final static int Chat=0x2;			//�i�J��ѫ�
	public final static int Page=0x4;			//�I�s�L�H���
	public final static int Post=0x8;			//�o���
	public final static int LoginOK=0x10;		//�ϥΪ̸�ƥ��T
	public final static int DenyPost=0x20;		//�T��o���
	public final static int Cloak=0x40;			//�����N
	public final static int SeeCloak=0x80;		//�ݨ��Ԫ�
	public final static int Forever=0x100;		//�b���ä[�O�d
	public final static int EditINI=0x200;		//�s��t���ɮ�
	public final static int BM=0x400;			//�O�D
	public final static int Account=0x800;		//�b���޲z��
	public final static int ChatCloak=0x1000;	//��ѫ�����
	public final static int OVote=0x2000;		//�벼�޲z��
	public final static int SYSOP=0x4000;		//�t�κ��@�޲z��
	public final static int PostMask=0x8000;	//�s������(�Q�װϱM��);���|�Q�۰ʽ�U��
	public final static int Announce=0x10000;	//��ذ��`��
	public final static int OBoards=0x20000;	//�Q�װ��`��
	public final static int ACBoard=0x40000;	//���ʬݪ��`��
	public final static int Unuse1=0x80000;		//�S���v��1
	public final static int Unuse2=0x100000;	//�S���v��2
	public final static int Unuse3=0x200000;	//�S���v��3
	public final static int Unuse4=0x400000;	//�S���v��4
	public final static int Unuse5=0x800000;	//�S���v��5
	public final static int Unuse6=0x1000000;	//�S���v��6
	public final static int Unuse7=0x2000000;	//�S���v��7
	public final static int Unuse8=0x4000000;	//�S���v��8
	public final static int Unuse9=0x8000000;	//�S���v��9
	public final static int Unuse10=0x10000000;	//�S���v��10
	public final static int Unuse11=0x20000000;	//�S���v��11
	public final static int Unuse12=0x40000000;	//�S���v��12
	public final static int Unuse13=0x80000000;	//�S���v��13

	public final static int StrToPerm(String str)
	{
		if (str.equals("Basic"))
			return 0x1;
		if (str.equals("Chat"))
			return 0x2;
		if (str.equals("Page"))
			return 0x4;
		if (str.equals("Post"))
			return 0x8;
		if (str.equals("LoginOK"))
			return 0x10;
		if (str.equals("DenyPost"))
			return 0x20;
		if (str.equals("Cloak"))
			return 0x40;
		if (str.equals("SeeCloak"))
			return 0x80;
		if (str.equals("Forever"))
			return 0x100;
		if (str.equals("EditINI"))
			return 0x200;
		if (str.equals("BM"))
			return 0x400;
		if (str.equals("Account"))
			return 0x800;
		if (str.equals("ChatCloak"))
			return 0x1000;
		if (str.equals("OVote"))
			return 0x2000;
		if (str.equals("SYSOP"))
			return 0x4000;
		if (str.equals("PostMask"))
			return 0x8000;
		if (str.equals("Announce"))
			return 0x10000;
		if (str.equals("OBoards"))
			return 0x20000;
		if (str.equals("ACBoard"))
			return 0x40000;
		if (str.equals("Unuse1"))
			return 0x80000;
		if (str.equals("Unuse2"))
			return 0x100000;
		if (str.equals("Unuse3"))
			return 0x200000;
		if (str.equals("Unuse4"))
			return 0x400000;
		if (str.equals("Unuse5"))
			return 0x800000;
		if (str.equals("Unuse6"))
			return 0x1000000;
		if (str.equals("Unuse7"))
			return 0x2000000;
		if (str.equals("Unuse8"))
			return 0x4000000;
		if (str.equals("Unuse9"))
			return 0x8000000;
		if (str.equals("Unuse10"))
			return 0x10000000;
		if (str.equals("Unuse11"))
			return 0x20000000;
		if (str.equals("Unuse12"))
			return 0x40000000;
		if (str.equals("Unuse13"))
			return 0x80000000;
		return 0;
	}
}