package colabbs.WW;

public class NewsArticle extends Object
{
	public String poster = null; //Modified by yhwu
	public String title = null;
	public String date = null;
	public String Site = null;
	public String MIME = null;
	public StringBuffer ArticleBody = null;
	
	public NewsArticle()
	{
		String poster = "";
		String title = "";
		String date = "";
		String Site = "";
		String MIME = "";
		StringBuffer ArticleBody = new StringBuffer();
	}
	public NewsArticle(String P, String T, String D, String S, String M, StringBuffer A/* Modified by yhwu*/)
	{
		poster = P;
		title = T;
		date = D;
		Site = S;
		MIME = M;
		ArticleBody = A;
	}
	public void writeByte(int buf)
	{
		ArticleBody.append((char)buf); //Modified by yhwu
	}
}