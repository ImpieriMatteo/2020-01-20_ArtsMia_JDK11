package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private SimpleWeightedGraph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	private String ruolo;
	private List<Artist> percorsoBest;
	private Integer numEsposizioniCondivise;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
	}
	
	public String creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		this.ruolo = ruolo;
		
		this.dao.getVertex(ruolo, this.idMap);
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//this.dao.getEsibizioniArtistaPerRuolo(ruolo, this.idMap);
		
		/*
		for(Artist a1 : this.grafo.vertexSet()) {
			
			for(Artist a2 : this.grafo.vertexSet()) {
				
				if(!this.grafo.containsEdge(a1, a2) && !a1.equals(a2)) {
					
					Integer peso = 0;
					for(Integer i1 : a1.getEsibizioniPerRuolo()) {
						
						for(Integer i2 : a2.getEsibizioniPerRuolo()) {
							
							if(i1.equals(i2))
								peso++;
						}
					}
					
					if(peso!=0) 
						Graphs.addEdgeWithVertices(this.grafo, a1, a2, peso);
				}
			}
		}
		*/
		
		for(Connessioni c : this.dao.getEdges(ruolo, idMap)) 
			Graphs.addEdgeWithVertices(this.grafo, c.getArtista1(), c.getArtista2(), c.getPeso());
		
		return String.format("GRAFO CREATO!!\n\n#VERTICI: %s\n#ARCHI: %s\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public List<String> getAllRoles() {
		return this.dao.getAllRoles();
	}
	
	public String getRuolo() {
		return this.ruolo;
	}

	public List<Connessioni> getConnessioni() {
		List<Connessioni> result = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet())
			result.add(new Connessioni(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e)));
		
		Collections.sort(result);
		return result;
	}

	public boolean IDPresente(Integer iD) {
		
		if(this.grafo.vertexSet().contains(this.idMap.get(iD)))
			return true;
		else
			return false;
	}

	public void trovaPercorso(Integer iD) {
		this.numEsposizioniCondivise = 0;
		this.percorsoBest = new ArrayList<>();
		
		List<Artist> parziale = new ArrayList<>();
		parziale.add(this.idMap.get(iD));
		
		this.calcolaPercorso(parziale, 0);
	}
	
	private void calcolaPercorso(List<Artist> parziale, Integer espoCondivise) {
		
		if(parziale.size()>this.percorsoBest.size()) {
			this.percorsoBest = new ArrayList<>(parziale);
			this.numEsposizioniCondivise = espoCondivise;
		}
		
		for(Artist a : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			if(!parziale.contains(a)) {

				Integer peso = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(a, parziale.get(parziale.size()-1)));

				if(espoCondivise==0) {

					espoCondivise = peso;
					parziale.add(a);

					this.calcolaPercorso(parziale, espoCondivise);

					espoCondivise = 0;
					parziale.remove(parziale.size()-1);
				}
				else if(espoCondivise==peso) {

					parziale.add(a);

					this.calcolaPercorso(parziale, espoCondivise);

					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}

	public List<Artist> getPercorsoBest(){
		return this.percorsoBest;
	}
	
	public Integer getNumEsposizioniCondivise(){
		return this.numEsposizioniCondivise;
	}

}
