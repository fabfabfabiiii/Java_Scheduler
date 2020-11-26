package fachkonzept;

public class Bearbeitung
{
	private int startpunkt;
	private int dauer;
	private int indexProzess;
	
	public Bearbeitung(int pStartpunkt, int pDauer, int pIndexProzess)
	{
		startpunkt = pStartpunkt;
		dauer = pDauer;
		indexProzess = pIndexProzess;
	}
	
	public int liesStartpunkt()
	{
		return startpunkt;
	}
	
	public int liesDauer()
	{
		return dauer;
	}
	
	public int liesIndexProzess()
	{
		return indexProzess;
	}
}
