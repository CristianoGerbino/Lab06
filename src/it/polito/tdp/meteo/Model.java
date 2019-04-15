package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private List<Citta> listaCitta;
	private Double costo_best;
	private List<SimpleCity> best;
	int ric;

	public Model() {
		MeteoDAO dao = new MeteoDAO();
		listaCitta = dao.getAllCitta();
		
		for (Citta c : listaCitta) {
			c.setRilevamenti(dao.getRilevamentiByCitta(c.getNome()));
		}
	}

	public String getUmiditaMedia(int mese) {
		MeteoDAO dao = new MeteoDAO();
		String s = "";
		for (Citta c : listaCitta) {
			s+= c.getNome()+" "+dao.getAvgRilevamentiLocalitaMese(mese, c.getNome())+"\n";
		}
		return s;
	}

	public String trovaSequenza(int mese) {
		String s = "";
		best = new ArrayList<SimpleCity>();
		costo_best = Double.MAX_VALUE;
		
		List<SimpleCity> parziale = new LinkedList<SimpleCity>();
		cerca(parziale, 0, mese);
		System.err.println(ric +"\n");
		for (SimpleCity c : best) {
			s += c.toString()+"\n";
		}
		return s;
	}

	
	
	
	private void cerca(List<SimpleCity> parziale, int L, int mese) {
		ric++;
		
		//Il livello è la posizione delle città nella listaCitta
		SimpleCity temp = null;
		
		//Casi terminali:
		if (parziale.size() == 15) {
			if (punteggioSoluzione (parziale) >= costo_best) {
				return;
			}
			else {
				best = new LinkedList<SimpleCity>(parziale);
				return;
			}
		}
		
		if (L == listaCitta.size()) {  //Ho esaurito le citta da aggiungere 
			//L = 0;
			return;
		}
		
		//In questo caso dobbiamo riempire la lista generando sotto-problemi
			Citta c = listaCitta.get(L);
			
			if (c.getGiorniTotali() <6) {
				//Se i giorni totali sono inferiori posso:
				
				// 1) Rimanere nella stessa città se non ho trascorso 3 giorni consecutivi
				if  (c.getGiorniConsecutivi() <3) {
					temp = new SimpleCity (c.getNome(), 
							c.getUmiditaPerGiorno(mese, parziale.size()+1));
					parziale.add(temp);
					c.incrementaGiorniConsecutivi();
					c.incrementaGiorniTotali();
					this.cerca(parziale, L, mese);
					//parziale.remove(temp);
					//c.decrementaGiorniTotali();
				}	
				
				// 2) Se ho trascorso già 3 giorni:
				else { 
				
				//-Posso rimanere nella citta in cui sono
				
				c.resetGiorniConsecutivi();
				temp = new SimpleCity (c.getNome(), 
						c.getUmiditaPerGiorno(mese, parziale.size()+1));
				parziale.add(temp);
				c.incrementaGiorniConsecutivi();
				c.incrementaGiorniTotali();
				this.cerca(parziale, L, mese);
				//parziale.remove(temp);
				//c.decrementaGiorniTotali();
				
				//-Posso cambiare città
				c.resetGiorniConsecutivi();
				cerca(parziale, L+1, mese);
				}
			} //Se i giorni totali sono maggiori o uguali di 6:
			else {
			//Devo cambiare citta
				c.resetGiorniConsecutivi();
				cerca(parziale, L+1, mese);
				}
			
			if (parziale.size() == 0) {
				
			}
			
			else if (parziale.size() == 1) {
				parziale.remove(0);
				c.decrementaGiorniTotali();
			}
			
			else if (parziale.size() >1) {
				parziale.remove(parziale.size()-1);
				c.decrementaGiorniTotali();
			}
		}
		

	private Double punteggioSoluzione(List<SimpleCity> sol) {
		double somma = sol.get(0).getCosto();
		
		for (int i = 1; i<sol.size(); i++) {
			
			if (sol.get(i).getNome().equals(sol.get(i-1).getNome() )) {
				somma = somma + sol.get(i).getCosto();
			}
			else
				somma = somma +sol.get(i).getCosto() +100;
		}
			
		return somma;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
