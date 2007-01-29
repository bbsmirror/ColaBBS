package colabbs.DATA.APLASMID;

import colabbs.*;

public class ActivePlasmidItem
{
	public String PlasmidClassName;
	public Class PlasmidClass;
	
	public ActivePlasmidItem(String ClassName) throws Exception
	{
		PlasmidClassName = ClassName;
		PlasmidClass = Class.forName(ClassName);
	}

	public int Go(BBSUser who) throws Exception
	{
		Class ClassArgu[] = new Class[] {BBSUser.class};
		Object MethodArgu[] = new Object[] {who};
		Object result = PlasmidClass.getMethod("PlasmidGo", ClassArgu).invoke(PlasmidClass.newInstance(), MethodArgu);
		return ((Integer)result).intValue();
	}

	public int Go(BBSUser who, String Param) throws Exception
	{
		Class ClassArgu[] = new Class[] {BBSUser.class, Param.getClass()};
		Object MethodArgu[] = new Object[] {who, Param};
		Object result = PlasmidClass.getMethod("PlasmidGo", ClassArgu).invoke(PlasmidClass.newInstance(), MethodArgu);
		return ((Integer)result).intValue();
	}
}