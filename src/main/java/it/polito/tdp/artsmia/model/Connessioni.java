package it.polito.tdp.artsmia.model;

public class Connessioni implements Comparable<Connessioni>{
	
	private Artist artista1;
	private Artist artista2;
	private Integer peso;
	
	public Connessioni(Artist artista1, Artist artista2, Integer peso) {
		this.artista1 = artista1;
		this.artista2 = artista2;
		this.peso = peso;
	}

	public Artist getArtista1() {
		return artista1;
	}

	public void setArtista1(Artist artista1) {
		this.artista1 = artista1;
	}

	public Artist getArtista2() {
		return artista2;
	}

	public void setArtista2(Artist artista2) {
		this.artista2 = artista2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return artista1.toString() + " - " + artista2.toString() + ", esposizioni comuni=" + peso + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artista1 == null) ? 0 : artista1.hashCode());
		result = prime * result + ((artista2 == null) ? 0 : artista2.hashCode());
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
		Connessioni other = (Connessioni) obj;
		if (artista1 == null) {
			if (other.artista1 != null)
				return false;
		} else if (!artista1.equals(other.artista1))
			return false;
		if (artista2 == null) {
			if (other.artista2 != null)
				return false;
		} else if (!artista2.equals(other.artista2))
			return false;
		return true;
	}

	@Override
	public int compareTo(Connessioni other) {
		return -this.peso.compareTo(other.peso);
	}

}
