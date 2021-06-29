package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
	
	private Integer ID;
	private String name;
	private List<Integer> esibizioniPerRuolo;
	
	public Artist(Integer iD, String name) {
		ID = iD;
		this.name = name;
		this.esibizioniPerRuolo = new ArrayList<>();
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getEsibizioniPerRuolo() {
		return esibizioniPerRuolo;
	}

	public void addEsibizioniPerRuolo(Integer esibizione) {
		this.esibizioniPerRuolo.add(esibizione);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
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
		Artist other = (Artist) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ID+" "+name;
	}

}
