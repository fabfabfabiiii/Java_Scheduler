package fachkonzept;

public class Prozess
{
	private String name;
	
	private int ankunftszeit;
	private int ausfuehrdauer;
	private int prioritaet;
	private int prozessende;
	
	private int restZeit;
	
	private int zeitpunktAnfangWarten;
	
	private boolean istBeendet;
	
	public Prozess(String pName, int pAnkunftszeit, int pAusfuehrdauer, int pPrioritaet) throws Exception
	{
		if(pAnkunftszeit < 0)
		{
			throw new Exception("Ankunftszeit darf nicht negativ sein");
		}
		
		if(pAusfuehrdauer <= 0)
		{
			throw new Exception("Ausfuehrdauer muss mindestens 1 sein");
		}
		
		if(pPrioritaet < 1)
		{
			throw new Exception("Priorität muss mindestens 1 sein");
		}
		
		name = pName;
		ankunftszeit = pAnkunftszeit;
		ausfuehrdauer = pAusfuehrdauer;
		prioritaet = pPrioritaet;
		
		restZeit = ausfuehrdauer; //zu Beginn entspricht die Restzeit der Ausfuehrdauer
		istBeendet = false;
		
		zeitpunktAnfangWarten = -1; //wert noch unbeschrieben
	}
	
	private void beendeProzess(int pZeit)
	{
		prozessende = pZeit;
		istBeendet = true;
	}
	
	public void bearbeiteProzess(int pLaenge, int pStartpunkt)
	{
		restZeit = restZeit - pLaenge;
		zeitpunktAnfangWarten = pStartpunkt + pLaenge;
		verringerePrioritaet();
		//nach Beenden der Bearbeitung wartet der Prozess
		
		if(restZeit == 0)
		{
			beendeProzess(pStartpunkt + pLaenge);
		}
	}
	
	public boolean istProzessAktiv(int pZeit)
	{
		//Prozess aktiv wenn Zeitpunkt größer/gleich ist als ankunftszeit und Prozess noch nicht beendet ist
		if(ankunftszeit <= pZeit && !istBeendet)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public int berechneVerweilzeit()
	{
		int verweilzeit = prozessende - ankunftszeit;
		return verweilzeit;
	}
	
	public int berechneWartezeit()
	{
		int wartezeit = berechneVerweilzeit() - ausfuehrdauer;
		return wartezeit;
	}
	
	private void verringerePrioritaet()
	{
		//priorität wird verringert, wenn sie noch nicht am geirngsten (1) ist
		if(prioritaet > 1)
		{
			prioritaet--;
		}
	}
	
	public String liesName()
	{
		return name;
	}
	
	public int liesAnkunftszeit()
	{
		return ankunftszeit;
	}
	
	public int liesAusfuehrdauer()
	{
		return ausfuehrdauer;
	}
	public int liesRestZeit()
	{
		return restZeit;
	}
	
	public int liesPrioritaet()
	{
		return prioritaet;
	}
	
	public int liesProzessende()
	{
		return prozessende;
	}
	
	public boolean istBeendet()
	{
		return istBeendet;
	}
	
	public int berechneWartezeitBisher(int pZeitpunkt)
	{
		//Wert sagt die Zeit, wie lange der Prozess schon auf Bearbeitung wartet
		if(zeitpunktAnfangWarten == -1)
		{
			//wenn Wert noch nicht beschrieben, wartet der Prozess seit Beginn der Freischaltung
			zeitpunktAnfangWarten = ankunftszeit;
		}
		
		return pZeitpunkt - zeitpunktAnfangWarten;
	}
	
}
