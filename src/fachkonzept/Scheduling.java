package fachkonzept;

import java.util.ArrayList;

public class Scheduling
{
	private int zeitscheibe;
	private int umschaltzeit;
	
	private int indexZuletztBearbeiteterProzess; 
	private Prozess[] dieProzesse;
	private ArrayList<Bearbeitung> dieBearbeitungen;
	
	public Scheduling(int pZeitscheibe, int pUmschaltzeit, ArrayList<Prozess> pProzesse) throws Exception
	{
		if(pZeitscheibe <= 0)
		{
			throw new Exception("Zeitscheibe muss mindestens 1 betragen!");
		}
		
		if(pUmschaltzeit < 0)
		{
			throw new Exception("Umschaltzeit darf nicht negativ sein!");
		}
		
		dieProzesse = new Prozess[pProzesse.size()];

		for (int i = 0; i < dieProzesse.length; i++)
		{
				dieProzesse[i] = new Prozess(pProzesse.get(i).liesName(),
						pProzesse.get(i).liesAnkunftszeit(),
						pProzesse.get(i).liesAusfuehrdauer(),
						pProzesse.get(i).liesPrioritaet());
		}
		
		zeitscheibe = pZeitscheibe;
		umschaltzeit = pUmschaltzeit;
		
		dieBearbeitungen = new ArrayList<>(); //Arraylist, da nicht klar, wie viele entstehen werden
		 indexZuletztBearbeiteterProzess = -1; //noch kein Prozess wurdee bearbeitet
	}
	
	public double berechneVerweilzeitDurchschnitt()
	{
		double wertGesamt = 0;
		
		for(int i = 0; i < dieProzesse.length; i++)
		{
			wertGesamt += dieProzesse[i].berechneVerweilzeit();
		}
		
		return wertGesamt / dieProzesse.length;
	}
	
	public double berechneWartezeitDurchschnitt()
	{
		double wertGesamt = 0;
		
		for(int i = 0; i < dieProzesse.length; i++)
		{
			wertGesamt += dieProzesse[i].berechneWartezeit();
		}
		
		return wertGesamt / dieProzesse.length;
	}
	
	public void fuehreSchedulingDurch()
	{
		int zeitpunkt = 0; 
		
		do
		{
			if(aktiveProzesseVorhanden(zeitpunkt))
			{
				//mindestens ein Prozess ist bereit für Bearbeitung
				int indexBearbeitung;
				int PrMax = wertHoechstePrioritaet(zeitpunkt);
				ArrayList<Integer> index_PrMax = indexDerProzesseMitPrioritaet(PrMax, zeitpunkt);
				
				if(index_PrMax.size() == 1)
				{
					//nur 1 Prozess hat die höchste Priorität
					indexBearbeitung = index_PrMax.get(0);
				}
				else
				{
					//mehrere Prozesse haben höchste Priorität
					//es muss nach längster Wartezeit gesucht werden
					int wartezeitMax = wertLaengsteWartezeit(index_PrMax, zeitpunkt);
					ArrayList<Integer> index_WartezeitMax = indexDerProzesseMitWartezeit(index_PrMax, wartezeitMax, zeitpunkt);
					
					//auch wenn noch mehrere Prozesse mit höchster Wartezeit vorhanden sind
					//wird nun der erste aus der Liste genommen
					
					indexBearbeitung = index_WartezeitMax.get(0);
					
				}
				
				zeitpunkt = weißeBearbeitungZu(indexBearbeitung, zeitpunkt);
				//Methode gibt zeitpunkt nach Bearbeitung zurück und führt Bearbeitung durch
			}
			else
			{
				//im Moment ist kein Prozess aktiv, Zeit vergeht weiter
				zeitpunkt++;
			}
		}while(!alleProzesseBeendet());
	}
	
	private boolean alleProzesseBeendet()
	{
		for(int i = 0; i < dieProzesse.length; i++)
		{
			if(!dieProzesse[i].istBeendet())
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean aktiveProzesseVorhanden(int pZeitpunkt)
	{
		for(int i = 0; i < dieProzesse.length; i++)
		{
			if(dieProzesse[i].istProzessAktiv(pZeitpunkt))
			{
				return true;
			}
		}
		return false;
	}
	
	private int wertHoechstePrioritaet(int pZeitpunkt)
	{
		//Methode findet heraus, welchen Wert die höchste Priorität hat
		int prMax = 1;
		
		for(int i = 0; i < dieProzesse.length; i++)
		{
			if(dieProzesse[i].istProzessAktiv(pZeitpunkt) && dieProzesse[i].liesPrioritaet() > prMax)
			{
				prMax = dieProzesse[i].liesPrioritaet();
			}
		}
		
		return prMax;
	}
	
	private ArrayList<Integer> indexDerProzesseMitPrioritaet(int pPrioritaet, int pZeitpunkt)
	{
		ArrayList<Integer> liste_index = new ArrayList<>();
		
		for(int i = 0; i < dieProzesse.length; i++)
		{
			if(dieProzesse[i].istProzessAktiv(pZeitpunkt) && dieProzesse[i].liesPrioritaet() == pPrioritaet)
			{
				liste_index.add(i);
			}
		}
		
		return liste_index;
	}
	
	private int wertLaengsteWartezeit(ArrayList<Integer> pIndex, int pZeitpunkt)
	{
		int wartezeitMax = 0;
		
		for(int i = 0; i < pIndex.size(); i++)
		{
			if(dieProzesse[pIndex.get(i)].berechneWartezeitBisher(pZeitpunkt) >= wartezeitMax)
			{
				wartezeitMax = dieProzesse[pIndex.get(i)].berechneWartezeitBisher(pZeitpunkt);
			}
		}
		
		return wartezeitMax;
	}
	
	private ArrayList<Integer> indexDerProzesseMitWartezeit(ArrayList<Integer> pIndex, int pWartezeit, int pZeitpunkt)
	{
		ArrayList<Integer> liste_index = new ArrayList<>();
		
		for(int i = 0; i < pIndex.size(); i++)
		{
			if(dieProzesse[pIndex.get(i)].berechneWartezeitBisher(pZeitpunkt) == pWartezeit)
			{
				liste_index.add(pIndex.get(i));
			}
		}
		
		return liste_index;
	}
	
	private int weißeBearbeitungZu(int pIndex, int pZeitpunkt)
	{
		int zeitpunktNeu = pZeitpunkt;
		
		if(indexZuletztBearbeiteterProzess != pIndex)
		{
			//es benötigt Umschaltzeit, wenn der zuletzt bearbeitete Prozess ein anderer war oder
			//noch kein Prozess bearbeitet wurde (-1)
			zeitpunktNeu += umschaltzeit;
			indexZuletztBearbeiteterProzess = pIndex; //Wert wird überschrieben
		}
		
		int bearbeitungszeit;
		
		if(volleZeitscheibeBenoetigt(dieProzesse[pIndex].liesRestZeit()))
		{
			bearbeitungszeit = zeitscheibe;
		}
		else
		{
			bearbeitungszeit = dieProzesse[pIndex].liesRestZeit();
		}
			
		dieBearbeitungen.add(new Bearbeitung(zeitpunktNeu, bearbeitungszeit, pIndex));
		dieProzesse[pIndex].bearbeiteProzess(bearbeitungszeit, zeitpunktNeu);
		zeitpunktNeu += bearbeitungszeit;
			
		return zeitpunktNeu;
	}
	
	private boolean volleZeitscheibeBenoetigt(int pRestzeit)
	{
		if(pRestzeit >= zeitscheibe)
		{
			return true;
		}
		return false;
	}
	
	public Prozess[] liesProzesse()
	{
		return dieProzesse;
	}
	
	public ArrayList<Bearbeitung> liesBearbeitungen()
	{
		return dieBearbeitungen;
	}
	
}
