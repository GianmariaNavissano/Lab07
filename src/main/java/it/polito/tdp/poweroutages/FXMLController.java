/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    @FXML
    void doRun(ActionEvent event) {
    	if(this.txtYears!=null && this.txtHours!=null) {
    		this.txtResult.clear();
    		int maxYears;
    		int maxHours;
    		Nerc nerc;
    		try {
    			maxYears = Integer.parseInt(this.txtYears.getText());
    			maxHours = Integer.parseInt(this.txtHours.getText());
    			nerc = this.cmbNerc.getValue();
    		//	System.out.println("Nuovo nerc: "+nerc.getValue()+"\n");
    			
    			List<PowerOutage> powerOutages = this.model.getWorstCase(nerc, maxHours, maxYears);
    			if(powerOutages == null) 
    				this.txtResult.setText("Errore nella ricorsione");
    			else {
    				for(PowerOutage po : powerOutages) {
    					this.txtResult.appendText(po.toString()+"\n");
    				}
    			}
    			
    		} catch(NumberFormatException e) {
    			this.txtResult.setText("Inserire valori numerici per anni ed ore");
    			throw new RuntimeException("Inserire valori numerici per anni ed ore", e);
    		}
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
        
    }
    
    public void setNerc() {
    	List<Nerc> nerc = this.model.getNercList();
    	for(Nerc n : nerc) {
    		this.cmbNerc.getItems().add(n);
    	}
    }
    
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
