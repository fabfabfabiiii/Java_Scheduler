package steuerung;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSpinnerUI;

import benutzerschnittstelle.Benutzerschnittstelle;
import fachkonzept.Bearbeitung;
import fachkonzept.Prozess;
import fachkonzept.Scheduling;

public class Steuerung
{

	private Benutzerschnittstelle dieBenutzerschnittstelle;
	private Scheduling dasScheduling;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Steuerung dieSteuerung = new Steuerung();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public Steuerung()
	{
		dieBenutzerschnittstelle = new Benutzerschnittstelle(this);
		dieBenutzerschnittstelle.ladeBenutzerschnittstelleEingabe();

	}

	public void starteScheduling(ArrayList<Prozess> pProzesse,
			int pUmschaltzeit, int pZeitscheibe)
	{
		try
		{
			dasScheduling = new Scheduling(pZeitscheibe, pUmschaltzeit,
					pProzesse);
		}
		catch (Exception e)
		{
			dieBenutzerschnittstelle.zeigeMeldung(e.getMessage());
		}

		dasScheduling.fuehreSchedulingDurch();

		dieBenutzerschnittstelle.ladeBenutzerschnittstelleAuswertung(
				dasScheduling.liesProzesse(), dasScheduling.liesBearbeitungen(),
				dasScheduling.berechneVerweilzeitDurchschnitt(),
				dasScheduling.berechneWartezeitDurchschnitt());
	}
	
	public void werteNeuEingeben()
	{
		dieBenutzerschnittstelle.ladeBenutzerschnittstelleEingabe();
	}
}
