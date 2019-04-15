package it.polito.tdp.meteo.bean;

import java.util.LinkedList;
import java.util.List;
import java.time.*;

public class Citta {

	private String nome;
	private List<Rilevamento> rilevamenti;
	private int giorniConsecutivi = 0;
	private int giorniTotali = 0;
	
	public Citta(String nome) {
		this.nome = nome;
		rilevamenti = new LinkedList<Rilevamento>();
	}
	
	public Citta(String nome, List<Rilevamento> rilevamenti) {
		this.nome = nome;
		this.rilevamenti = rilevamenti;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getGiorniConsecutivi() {
		return giorniConsecutivi;
	}

	public int getGiorniTotali() {
		return giorniTotali;
	}

	public void incrementaGiorniConsecutivi() {
		this.giorniConsecutivi++;
	}
	
	
	public void incrementaGiorniTotali() {
		this.giorniTotali++;
	}
	
	public List<Rilevamento> getRilevamenti() {
		return rilevamenti;
	}

	public void setRilevamenti(List<Rilevamento> rilevamenti) {
		this.rilevamenti = rilevamenti;
	}
	
	
	public double getUmiditaPerGiorno (int mese, int giorno) {
		for (Rilevamento r : rilevamenti) {
			if ( r.getData().getMonthValue() == mese && r.getData().getDayOfMonth() == giorno) {
				return r.getUmidita();
			}
		}
		return 0.0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citta other = (Citta) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	public String stampati() {
		String s = "";
		for (Rilevamento r : this.rilevamenti) {
			s+= r.toString()+" ";
		}
		return s;
	}

	public void resetGiorniConsecutivi() {
		this.giorniConsecutivi = 0;
	}

	public void decrementaGiorniTotali() {
		this.giorniTotali--;
	}

	
	
}
