package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Connessioni;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	this.txtResult.clear();
    	
    	String ruolo = this.boxRuolo.getValue();
    	if(!ruolo.equals(this.model.getRuolo())) {
    		this.txtResult.appendText("Devi prima creare un nuovo GRAFO!!");
    		return;
    	}

    	List<Connessioni> result = this.model.getConnessioni();
    	
    	this.txtResult.appendText("Artisti connessi: \n\n");
    	Integer i = 1;
    	for(Connessioni c : result) {
    		this.txtResult.appendText(""+i+". "+c.toString());
    		i++;
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Integer ID;
    	try {
    		ID = Integer.parseInt(this.txtArtista.getText());
    	}
    	catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire un numero INTERO!!");
    		return;
    	}
    	
    	if(!this.model.IDPresente(ID)) {
    		this.txtResult.appendText("L'artista inserito non esiste o non fa parte del grafo, inserisci un altro ID");
    		return;
    	}
    	
    	this.model.trovaPercorso(ID);
    	List<Artist> percorsoBest = this.model.getPercorsoBest();
    	Integer peso = this.model.getNumEsposizioniCondivise();

    	this.txtResult.appendText("Percorso migliore trovato per "+peso+" esposizioni condivise: \n\n");
    	for(Artist a : percorsoBest) 
    		this.txtResult.appendText("- "+a.toString()+" \n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	String ruolo = this.boxRuolo.getValue();
    	if(ruolo==null) {
    		this.txtResult.appendText("Devi prima scegliere un RUOLO!!");
    		return;
    	}
    	
    	String result = this.model.creaGrafo(ruolo);
    	this.txtResult.appendText(result);

    	this.btnArtistiConnessi.setDisable(false);
    	this.btnCalcolaPercorso.setDisable(false);
    }

    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
		this.boxRuolo.getItems().addAll(this.model.getAllRoles());
	}
}

