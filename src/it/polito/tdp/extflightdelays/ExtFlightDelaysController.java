/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Tratta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;
	private boolean graphCreated = false;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML // fx:id="voliMinimo"
	private TextField voliMinimo; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizza"
	private Button btnAnalizza; // Value injected by FXMLLoader

	@FXML // fx:id="cmbBoxAeroportoPartenza"
	private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

	@FXML // fx:id="btnAeroportiConnessi"
	private Button btnAeroportiConnessi; // Value injected by FXMLLoader

	@FXML // fx:id="numeroOreTxtInput"
	private TextField numeroOreTxtInput; // Value injected by FXMLLoader

	@FXML // fx:id="btnOttimizza"
	private Button btnOttimizza; // Value injected by FXMLLoader

	@FXML
	void doAnalizzaAeroporti(ActionEvent event) {
		txtResult.clear();
		if (voliMinimo.getText() == "") {
			txtResult.setText("Errore, selezionare un numero minimo di voli.");
		} else {
			try {
				int flights = Integer.parseInt(voliMinimo.getText());
				model.createGraph(flights);
				this.graphCreated = true;
			} catch (NumberFormatException e) {
				txtResult.setText("Errore, inserisci un numero corretto.");
			}
		}
	}

	@FXML
	void doCalcolaAeroportiConnessi(ActionEvent event) {
		txtResult.clear();
		if (cmbBoxAeroportoPartenza.getValue() == null) {
			txtResult.setText("Errore, seleziona un'aeroporto");
		} else {
			Airport selected = cmbBoxAeroportoPartenza.getValue();
			if (this.graphCreated) {
				if (model.getGraph().vertexSet().contains(selected)) {
					List<Airport> neighbours = new ArrayList<>(model.getNeighbours(selected));
					List<Tratta> tratte = new ArrayList<>();
					txtResult.appendText("Selezionato aeroporto " + selected.getAirportName() + ".\n");
					for (Airport a : neighbours) {
						double weight = model.getWeight(selected, a);
						Tratta t = new Tratta(selected, a, weight);
						tratte.add(t);
					}
					Collections.sort(tratte);
					for (Tratta t : tratte) {
						txtResult.appendText(t.toString() + "\n");
					}
				} else
					txtResult.setText(
							"Errore, l'aeroporto selezionato non effettua il numero di voli precedentemente inserito.");
			} else
				txtResult.setText(
						"Errore, devi selezionare un numero minimo di voli prima di poter calcolare aeroporti connessi!");
		}
	}

	@FXML
	void doCercaItinerario(ActionEvent event) {

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert voliMinimo != null : "fx:id=\"voliMinimo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert numeroOreTxtInput != null : "fx:id=\"numeroOreTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
		assert btnOttimizza != null : "fx:id=\"btnOttimizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
		cmbBoxAeroportoPartenza.getItems().addAll(model.getAllAirports());

	}
}
