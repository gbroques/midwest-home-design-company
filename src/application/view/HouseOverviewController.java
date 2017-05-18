package application.view;

import java.text.DecimalFormat;

import application.Main;
import application.model.House;
import application.util.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class HouseOverviewController {
	
    // Reference to the main application.
    private Main mainApp;
    
    @FXML
    private ChoiceBox<House> styleChoiceBox;
    @FXML
    private TextField areaMinTextField;
    @FXML
    private TextField areaMaxTextField;
    @FXML
    private TextField numOfBedroomsTextField;
    @FXML
    private TextField numOfBathroomsTextField;
    @FXML
    private Button clearButton;
    @FXML
    private Button searchButton;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
	public HouseOverviewController() {
	}
	
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
	
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        
        ObservableList<House> houses = mainApp.getHouses();
        
        styleChoiceBox.setItems(houses);
        
        // Set default value for choice box
        styleChoiceBox.setValue(houses.get(0));
        showHouseDetails(houses.get(0));
        
        // Disable Clear button when form is empty.
        BooleanBinding formIsEmpty = Bindings.and(
        	styleChoiceBox.valueProperty().isNull(),
        	areaMinTextField.textProperty().isEmpty())
        	.and(areaMaxTextField.textProperty().isEmpty())
        	.and(numOfBedroomsTextField.textProperty().isEmpty())
        	.and(numOfBathroomsTextField.textProperty().isEmpty()
        );
        
        clearButton.disableProperty().bind(formIsEmpty);
        
        BooleanBinding oneFieldIsEmpty = Bindings.or(
            styleChoiceBox.valueProperty().isNull(),
            areaMinTextField.textProperty().isEmpty())
            .or(areaMaxTextField.textProperty().isEmpty())
            .or(numOfBedroomsTextField.textProperty().isEmpty())
            .or(numOfBathroomsTextField.textProperty().isEmpty()
        );
        
        searchButton.disableProperty().bind(oneFieldIsEmpty);
    }
    
    /**
     * Set change listeners to each field in the form.
     */
    public void setChangeListeners() {
        styleChoiceBox.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> showHouseDetails(newValue));
        areaMinTextField.textProperty().addListener(
            	(observable, oldValue, newValue) -> handleTextFieldChange(areaMinTextField, oldValue, newValue));
        areaMaxTextField.textProperty().addListener(
            	(observable, oldValue, newValue) -> handleTextFieldChange(areaMaxTextField, oldValue ,newValue));
        numOfBedroomsTextField.textProperty().addListener(
            	(observable, oldValue, newValue) -> handleTextFieldChange(numOfBedroomsTextField, oldValue, newValue));
        numOfBathroomsTextField.textProperty().addListener(
            	(observable, oldValue, newValue) -> handleTextFieldChange(numOfBathroomsTextField, oldValue, newValue));
    }
    
    /**
     * Ensure input is valid decimal numbers.
     * 
     * @param textfield The textfield being typed in.
     * @param oldValue The old value of the text field.
     * @param newValue The new value of the text field.
     */
    private void handleTextFieldChange(TextField textfield, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
        	textfield.setText(newValue.replaceAll("[^\\d.]", ""));
        }
        
        if (Util.countOccurrences(newValue, '.') >= 2) {
        	textfield.setText(oldValue);
        }
    }
    
    /**
     * Fills all text fields to show details about the house.
     * If the specified house is null, all text fields are cleared.
     * 
     * @param House or null
     */
    private void showHouseDetails(House house) {
        if (house != null) {
            // Fill the text fields with info from the house object.
        	DecimalFormat removeTrailingZeroes = new DecimalFormat("0.#####");
        	areaMinTextField.setText(removeTrailingZeroes.format(house.getArea()));
        	areaMaxTextField.setText(removeTrailingZeroes.format(house.getArea()));
        	numOfBedroomsTextField.setText(removeTrailingZeroes.format(house.getBedrooms()));
        	numOfBathroomsTextField.setText(removeTrailingZeroes.format(house.getBathrooms()));
        } else {
        	areaMinTextField.setText("");
        	areaMaxTextField.setText("");
        	numOfBedroomsTextField.setText("");
        	numOfBathroomsTextField.setText("");
        }
    }
    
    /**
     * Called when the user clicks the search button. Opens a dialog to customize
     * details about the house.
     * @throws CloneNotSupportedException 
     */
    @FXML
    private void handleSearch() throws CloneNotSupportedException {
    	House selectedHouse = (House) styleChoiceBox.getSelectionModel().getSelectedItem().clone();
    	selectedHouse.setBedrooms(Double.parseDouble((numOfBedroomsTextField.getText())));
    	selectedHouse.setBathrooms(Double.parseDouble((numOfBathroomsTextField.getText())));
    	selectedHouse.setArea(2000);
    	
        mainApp.showHouseCustomizeDialog(selectedHouse);
    }
    

    /**
     * Called when the user clicks the "Clear" button.
     */
    @FXML
    private void handleClear() {
    	showHouseDetails(null);
    	styleChoiceBox.setValue(null);
    }
    
    /**
     * Called when the user clicks the "Finish" button.
     */
    @FXML
    private void handleFinish() {
    	mainApp.getPrimaryStage().close();
    }


}
