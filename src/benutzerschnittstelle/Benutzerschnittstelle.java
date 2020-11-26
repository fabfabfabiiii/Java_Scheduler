package benutzerschnittstelle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import fachkonzept.Bearbeitung;
import fachkonzept.Prozess;
import steuerung.Steuerung;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Benutzerschnittstelle extends JFrame
{
	private Steuerung dieSteuerung;

	private JPanel contentPane;

	private JTable tabelle_Auswahl;

	private String[] spaltenNamen = {"Prozess", "Ankunftszeit", "Ausführdauer",
			"Priorität"};
	private Object[][] daten = {{"A", 0, 150, 4},
			{"B", 50, 200, 3}, {null, null, null, null},
			{null, null, null, null}, {null, null, null, null},
			{null, null, null, null}, {null, null, null, null},
			{null, null, null, null}, {null, null, null, null},
			{null, null, null, null}};

	private JLabel lblProzessname;
	private JLabel lblAnkunftszeit;
	private JLabel lblAusfuehrdauer;
	private JLabel lblPrioritaet;
	private JLabel lblZeitscheibe;
	private JLabel lblUmschaltzeit;

	private JTextField txtZeitscheibe;
	private JTextField txtUmschaltzeit;
	private JButton btnAuswerten;

	private JLabel[] lblNamenProzesse;
	private ArrayList<JLabel> lblBalken;
	private JLabel[] lblSkala;

	private JButton btnPressLinks;
	private JButton btnPressRechts;
	private JButton btnWertEingabe;
	
	private int index; // gibt an auf welche Seite des Diagramms angezeigt wird
	private int indexMax; //maximaler index, welches die letzte Seite des Diagramms darstellt

	private JTable tableAuswertung;

	public Benutzerschnittstelle(Steuerung pSteuerung)
	{
		setResizable(false);
		setTitle("Scheduler");
		dieSteuerung = pSteuerung;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 410, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

	}

	public void ladeBenutzerschnittstelleEingabe()
	{
		resetFenster();
		setBounds(100, 100, 410, 300);
		
		tabelle_Auswahl = new JTable(daten, spaltenNamen);
		tabelle_Auswahl.setBounds(0, 16, 400, 160);

		tabelle_Auswahl.getColumnModel().getColumn(0).setResizable(false);
		tabelle_Auswahl.getColumnModel().getColumn(1).setResizable(false);
		tabelle_Auswahl.getColumnModel().getColumn(2).setResizable(false);
		tabelle_Auswahl.getColumnModel().getColumn(3).setResizable(false);

		contentPane.add(tabelle_Auswahl);

		lblProzessname = new JLabel("Prozessname");
		lblProzessname.setBounds(0, 0, 100, 16);
		contentPane.add(lblProzessname);

		lblAnkunftszeit = new JLabel("Ankunftszeit");
		lblAnkunftszeit.setBounds(100, 0, 100, 16);
		contentPane.add(lblAnkunftszeit);

		lblAusfuehrdauer = new JLabel("Ausführdauer");
		lblAusfuehrdauer.setBounds(200, 0, 100, 16);
		contentPane.add(lblAusfuehrdauer);

		lblPrioritaet = new JLabel("Priorität");
		lblPrioritaet.setBounds(300, 0, 100, 16);
		contentPane.add(lblPrioritaet);

		lblZeitscheibe = new JLabel("Zeitscheibe");
		lblZeitscheibe.setBounds(0, 200, 100, 20);
		contentPane.add(lblZeitscheibe);

		lblUmschaltzeit = new JLabel("Umschaltzeit");
		lblUmschaltzeit.setBounds(0, 240, 100, 20);
		contentPane.add(lblUmschaltzeit);

		txtZeitscheibe = new JTextField("10");
		txtZeitscheibe.setBounds(120, 200, 100, 20);
		contentPane.add(txtZeitscheibe);
		txtZeitscheibe.setColumns(10);

		txtUmschaltzeit = new JTextField("0");
		txtUmschaltzeit.setBounds(120, 240, 100, 20);
		contentPane.add(txtUmschaltzeit);
		txtUmschaltzeit.setColumns(10);

		btnAuswerten = new JButton("Auswerten");
		btnAuswerten.setBounds(240, 220, 160, 20);
		btnAuswerten.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clickAuswerten();
			}
		});

		contentPane.add(btnAuswerten);

		setVisible(true);
	}

	private void clickAuswerten()
	{
		ArrayList<Prozess> dieProzesse_dyn = new ArrayList<>();

		boolean zeileVorhanden; // wert sagt, ob gesuchte Zeile vorhanden ist
		for (int i = 0; i < 10; i++)
		{
			zeileVorhanden = true;
			try
			{
				// wenn eine Exception ausgelöst wird oder der String aus keinen
				// Zeichen besteht, ist Zeile nicht vorhanden
				if (tabelle_Auswahl.getValueAt(i, 0).toString().length() == 0)
				{
					zeileVorhanden = false;
				}

			}
			catch (NullPointerException e)
			{
				// kein Wert in der Zeile, deshalb ist die Zeile nicht vorhanden
				zeileVorhanden = false;
			}

			if (zeileVorhanden)
			{
				// schreibt den Prozess bei vorhandener Zeile in Liste
				String prozessname = tabelle_Auswahl.getValueAt(i, 0)
						.toString();
				int ankunftszeit = Integer
						.parseInt(tabelle_Auswahl.getValueAt(i, 1).toString());
				int ausfuehrdauer = Integer
						.parseInt(tabelle_Auswahl.getValueAt(i, 2).toString());
				int prioritaet = Integer
						.parseInt(tabelle_Auswahl.getValueAt(i, 3).toString());

				try
				{
					dieProzesse_dyn.add(new Prozess(prozessname, ankunftszeit,
							ausfuehrdauer, prioritaet));
				}
				catch (Exception e)
				{
					zeigeMeldung(e.getMessage());
					return;
				}
			}

		}

		int zeitscheibe = Integer.parseInt(txtZeitscheibe.getText());
		int umschaltzeit = Integer.parseInt(txtUmschaltzeit.getText());

		if (dieProzesse_dyn.size() == 0)
		{
			zeigeMeldung("Es muss mindestens ein Prozess erstellt werden!");
			return;
		}

		dieSteuerung.starteScheduling(dieProzesse_dyn, umschaltzeit,
				zeitscheibe);
	}

	public void zeigeMeldung(String pMeldung)
	{
		JOptionPane.showMessageDialog(null, pMeldung, "Fehler!",
				JOptionPane.INFORMATION_MESSAGE, null);
	}

	private void resetFenster()
	{
		setVisible(false);
		contentPane.removeAll();
	}

	public void ladeBenutzerschnittstelleAuswertung(Prozess[] pProzesse,
			ArrayList<Bearbeitung> pBearbeitung, double pVerweil, double pWarte)
	{
		resetFenster();

		setBounds(100, 100, 600, 380);

		index = 0;
		indexMax = indexMax(pBearbeitung);

		lblNamenProzesse = new JLabel[pProzesse.length];

		for (int i = 0; i < lblNamenProzesse.length; i++)
		{
			lblNamenProzesse[i] = new JLabel(pProzesse[i].liesName());
			lblNamenProzesse[i].setBounds(0, 30 + (i * 20), 50, 20);
			contentPane.add(lblNamenProzesse[i]);
		}

		lblBalken = new ArrayList<>();

		for (int i = 0; i < pBearbeitung.size(); i++)
		{
			if (pBearbeitung.get(i).liesStartpunkt() < (100 + 100 * index)
					&& pBearbeitung.get(i).liesStartpunkt() >= (100 * index))
			{
				// gehört in Diagramm
				lblBalken.add(new JLabel(""));

				int startpunkt = pBearbeitung.get(i).liesStartpunkt()
						- (100 * index);
				startpunkt *= 5;
				int endpunkt;
				if ((pBearbeitung.get(i).liesStartpunkt()
						+ pBearbeitung.get(i).liesDauer()) > (100
								+ 100 * index))
				{
					// Diagramm passt nicht ganz rein
					endpunkt = 500;
				}
				else
				{
					endpunkt = (pBearbeitung.get(i).liesStartpunkt()
							+ pBearbeitung.get(i).liesDauer()) - (100 * index);
					endpunkt *= 5;
				}

				lblBalken.get(lblBalken.size() - 1).setBounds(50 + startpunkt,
						30 + (20 * pBearbeitung.get(i).liesIndexProzess()),
						endpunkt - startpunkt, 20);
				lblBalken.get(lblBalken.size() - 1).setOpaque(true);
				lblBalken.get(lblBalken.size() - 1).setBackground(Color.BLUE);
				contentPane.add(lblBalken.get(lblBalken.size() - 1));
			}
			else
			{
				int zahl = (index * 100) + 1; // erste Zeit im Diagramm
				if (zahl > pBearbeitung.get(i).liesStartpunkt()
						&& zahl <= (pBearbeitung.get(i).liesStartpunkt()
								+ pBearbeitung.get(i).liesDauer()))
				{
					lblBalken.add(new JLabel(""));
					//bearbeitung int noch im Diagramm vorhanden
					int startpunkt = 0; //logischerweise wird Bearbeitung sofort weiter geführt
					
					int endpunkt;
					if ((pBearbeitung.get(i).liesStartpunkt()
							+ pBearbeitung.get(i).liesDauer()) > (100
									+ 100 * index))
					{
						// Diagramm passt nicht ganz rein
						endpunkt = 500;
					}
					else
					{
						endpunkt = (pBearbeitung.get(i).liesStartpunkt()
								+ pBearbeitung.get(i).liesDauer()) - (100 * index);
						endpunkt *= 5;
					}
					
					lblBalken.get(lblBalken.size() - 1).setBounds(50 + startpunkt,
							30 + (20 * pBearbeitung.get(i).liesIndexProzess()),
							endpunkt - startpunkt, 20);
					lblBalken.get(lblBalken.size() - 1).setOpaque(true);
					lblBalken.get(lblBalken.size() - 1).setBackground(Color.BLUE);
					contentPane.add(lblBalken.get(lblBalken.size() - 1));
					
				}
			}
		}

		lblSkala = new JLabel[10];

		for (int i = 0; i < lblSkala.length; i++)
		{
			lblSkala[i] = new JLabel(Integer.toString(10 + (10 * i)));
			lblSkala[i].setBounds(90 + 50 * i, 0, 30, 30);
			contentPane.add(lblSkala[i]);
		}

		btnPressLinks = new JButton("<--");
		btnPressLinks.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clickLinks(pBearbeitung);
			}
		});
		btnPressLinks.setBounds(100, 230, 200, 20);
		contentPane.add(btnPressLinks);

		btnPressRechts = new JButton("-->");
		btnPressRechts.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clickRechts(pBearbeitung);
			}
		});
		btnPressRechts.setBounds(300, 230, 200, 20);
		contentPane.add(btnPressRechts);

		String[] columnName = new String[pProzesse.length + 2];

		for (int i = 0; i < columnName.length; i++)
		{
			columnName[i] = new String("");
			// es ist egal, was drin steht, da es eh nicht angezeigt wird
		}

		DecimalFormat df = new DecimalFormat("#0.00");

		String[] zeile1 = new String[columnName.length];
		zeile1[0] = new String("Prozess");

		String[] zeile2 = new String[columnName.length];
		zeile2[0] = new String("Verweilzeit");

		String[] zeile3 = new String[columnName.length];
		zeile3[0] = new String("Wartezeit");

		String[] zeile4 = new String[columnName.length];
		zeile4[0] = new String("Endzeit");

		for (int i = 0; i < pProzesse.length; i++)
		{
			zeile1[i + 1] = new String(pProzesse[i].liesName());
			zeile2[i + 1] = new String(
					df.format(pProzesse[i].berechneVerweilzeit()));
			zeile3[i + 1] = new String(
					df.format(pProzesse[i].berechneWartezeit()));
			zeile4[i + 1] = new String(
					df.format(pProzesse[i].liesProzessende()));
		}

		int indexLetzter = zeile1.length - 1;

		zeile1[indexLetzter] = new String("Durchschnitt");
		zeile2[indexLetzter] = new String(df.format(pVerweil));
		zeile3[indexLetzter] = new String(df.format(pWarte));
		zeile4[indexLetzter] = new String(" ");

		String[][] tabelleDaten = {zeile1, zeile2, zeile3, zeile4};

		tableAuswertung = new JTable(tabelleDaten, columnName);
		tableAuswertung.setBounds(0, 250, 600, 64);

		tableAuswertung.setDefaultEditor(Object.class, null); // Werte in
																// Tabelle nicht
																// editierbar
		contentPane.add(tableAuswertung);
		
		
		btnWertEingabe = new JButton("neue Werte eingeben");
		btnWertEingabe.setBounds(100, 320, 400, 30);
		btnWertEingabe.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clickNeueWerteEingeben();
			}
		});
		
		contentPane.add(btnWertEingabe);
		
		
		setVisible(true);
	}

	private void clickLinks(ArrayList<Bearbeitung> pBearbeitung)
	{
		if (index > 0)
		{
			index--;
			maleDiagramm(pBearbeitung);
		}
	}

	private void clickRechts(ArrayList<Bearbeitung> pBearbeitung)
	{
		if (index < indexMax)
		{
			index++;
			maleDiagramm(pBearbeitung);
		}
	}

	private int indexMax(ArrayList<Bearbeitung> pBearbeitung)
	{
		int endpunkt = pBearbeitung.get(pBearbeitung.size() - 1)
				.liesStartpunkt()
				+ pBearbeitung.get(pBearbeitung.size() - 1).liesDauer();
		int z = 0;

		do
		{
			endpunkt -= 100;
			if (endpunkt > 0)
			{
				z++;
			}
		} while (endpunkt > 0);

		return z;
	}

	private void maleDiagramm(ArrayList<Bearbeitung> pBearbeitung)
	{
		setVisible(false);

		for (int i = 0; i < lblBalken.size(); i++)
		{
			contentPane.remove(lblBalken.get(i));
		}

		lblBalken = new ArrayList<>();

		for (int i = 0; i < pBearbeitung.size(); i++)
		{
			if (pBearbeitung.get(i).liesStartpunkt() < (100 + 100 * index)
					&& pBearbeitung.get(i).liesStartpunkt() >= (100 * index))
			{
				// gehört in Diagramm
				lblBalken.add(new JLabel(""));

				int startpunkt = pBearbeitung.get(i).liesStartpunkt()
						- (100 * index);
				startpunkt *= 5;
				int endpunkt;
				if ((pBearbeitung.get(i).liesStartpunkt()
						+ pBearbeitung.get(i).liesDauer()) > (100
								+ 100 * index))
				{
					// Diagramm passt nicht ganz rein
					endpunkt = 500;
				}
				else
				{
					endpunkt = (pBearbeitung.get(i).liesStartpunkt()
							+ pBearbeitung.get(i).liesDauer()) - (100 * index);
					endpunkt *= 5;
				}

				lblBalken.get(lblBalken.size() - 1).setBounds(50 + startpunkt,
						30 + (20 * pBearbeitung.get(i).liesIndexProzess()),
						endpunkt - startpunkt, 20);
				lblBalken.get(lblBalken.size() - 1).setOpaque(true);
				lblBalken.get(lblBalken.size() - 1).setBackground(Color.BLUE);
				contentPane.add(lblBalken.get(lblBalken.size() - 1));
			}
			else
			{
				int zahl = (index * 100) + 1; // erste Zeit im Diagramm
				if (zahl > pBearbeitung.get(i).liesStartpunkt()
						&& zahl <= (pBearbeitung.get(i).liesStartpunkt()
								+ pBearbeitung.get(i).liesDauer()))
				{
					//bearbeitung int noch im Diagramm vorhanden
					lblBalken.add(new JLabel(""));
					int startpunkt = 0; //logischerweise wird Bearbeitung sofort weiter geführt
					
					int endpunkt;
					if ((pBearbeitung.get(i).liesStartpunkt()
							+ pBearbeitung.get(i).liesDauer()) > (100
									+ 100 * index))
					{
						// Diagramm passt nicht ganz rein
						endpunkt = 500;
					}
					else
					{
						endpunkt = (pBearbeitung.get(i).liesStartpunkt()
								+ pBearbeitung.get(i).liesDauer()) - (100 * index);
						endpunkt *= 5;
					}
					
					lblBalken.get(lblBalken.size() - 1).setBounds(50 + startpunkt,
							30 + (20 * pBearbeitung.get(i).liesIndexProzess()),
							endpunkt - startpunkt, 20);
					lblBalken.get(lblBalken.size() - 1).setOpaque(true);
					lblBalken.get(lblBalken.size() - 1).setBackground(Color.BLUE);
					contentPane.add(lblBalken.get(lblBalken.size() - 1));
					
				}
			}
		}

		for (int i = 0; i < lblSkala.length; i++)
		{
			contentPane.remove(lblSkala[i]);
		}

		lblSkala = new JLabel[10];

		for (int i = 0; i < lblSkala.length; i++)
		{
			lblSkala[i] = new JLabel(
					Integer.toString(10 + (10 * i) + (100 * index)));
			lblSkala[i].setBounds(90 + 50 * i, 0, 30, 30);
			contentPane.add(lblSkala[i]);
		}

		setVisible(true);
	}
	
	private void clickNeueWerteEingeben()
	{
		dieSteuerung.werteNeuEingeben();
	}

}
