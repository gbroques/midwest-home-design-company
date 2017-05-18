package application.view;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import application.Main;
import application.model.House;
import application.util.Util;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HouseCustomizeController {
    // Reference to the main application.
    private Main mainApp;
    
    @FXML
    private ImageView houseImageView;
    @FXML
    private Label statusLabel;
    @FXML
    private ChoiceBox<House> styleChoiceBox;
    @FXML
    private TextField totalAreaTextField;
    @FXML
    private TextField bedroomsTextField;
    @FXML
    private TextField bathroomsTextField;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Button printButton;
    @FXML
    private Button closeButton;
    
    private Stage dialogStage;
    private House house;
    private boolean okClicked = false;
    
	public HouseCustomizeController() {
	}
	
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     * @throws FileNotFoundException 
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    /**
     * Adds change listeners to each field in the form.
     */
    public void setChangeListeners() {
        styleChoiceBox.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> handleChoiceBoxChange(newValue));
        totalAreaTextField.textProperty().addListener(
        	(observable, oldValue, newValue) -> handleTextFieldChange(totalAreaTextField, oldValue, newValue));
        bedroomsTextField.textProperty().addListener(
        	(observable, oldValue, newValue) -> handleTextFieldChange(bedroomsTextField, oldValue ,newValue));
        bathroomsTextField.textProperty().addListener(
        	(observable, oldValue, newValue) -> handleTextFieldChange(bathroomsTextField, oldValue, newValue));
    }
    
    /**
     * Change the house style and recalculate the total cost.
     * @param selectedHouse
     * @throws CloneNotSupportedException 
     */
    private void handleChoiceBoxChange(House selectedHouse) {
    	this.house = selectedHouse;
    	styleChoiceBox.setValue(selectedHouse);
    	House customHouse = null;
		try {
			customHouse = getCustomHouse(selectedHouse);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
    	boolean houseIsTemplate = houseIsTemplate(customHouse);
    	setStatusLabelText(houseIsTemplate);
    	setHouseImageView(houseIsTemplate);
    	setTotalCostLabel();
    }
    
    private House getCustomHouse(House selectedHouse) throws CloneNotSupportedException {
    	House customHouse = (House) selectedHouse.clone();
    	customHouse.setBedrooms(Double.parseDouble((bedroomsTextField.getText())));
    	customHouse.setBathrooms(Double.parseDouble((bathroomsTextField.getText())));
    	customHouse.setArea(Double.parseDouble((totalAreaTextField.getText())));
    	return customHouse;
    }
    
    /**
     * When the user changes the value of the text field:
     * 1. Ensure input is valid decimal numbers.
     * 2. Update image and status label.
     * 3. Recalculate the total cost of the house.
     * 
     * @param textfield The textfield being typed in.
     * @param oldValue The old value of the text field.
     * @param newValue The new value of the text field.
     */
    private void handleTextFieldChange(TextField textfield, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
        	textfield.setText(newValue.replaceAll("[^\\d.]", ""));
        }
        
        if (Util.countOccurrences(newValue, '.') >= 2 || newValue.equals(".")) {
        	textfield.setText(oldValue);
        	return;
        }
        
        if (!newValue.isEmpty()) {
        	House customHouse = null;
    		try {
    			customHouse = getCustomHouse(styleChoiceBox.getSelectionModel().getSelectedItem());
    		} catch (CloneNotSupportedException e) {
    			e.printStackTrace();
    		}
        	boolean houseIsTemplate = houseIsTemplate(customHouse);
        	setStatusLabelText(houseIsTemplate);
        	setHouseImageView(houseIsTemplate);
        	setTotalCostLabel();
        } else {
        	houseImageView.setImage(null);
        	statusLabel.setText("");
        	totalCostLabel.setText("");
        }
    }
    
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Sets the house to be customized in the dialog.
     * 
     * @param house
     */
    public void setHouse(House house) {  
        showHouseDetails(house);
    }
    
    /**
     * Fills all form fields to show details about the house.
     * If the specified house is null, all text fields are cleared.
     * 
     * @param House or null
     */
    private void showHouseDetails(House house) {
        if (house != null) {
        	setStyleChoiceBox(house);
        	boolean houseIsTemplate = houseIsTemplate(house);
        	setStatusLabelText(houseIsTemplate);
        	setHouseImageView(houseIsTemplate);
        	
            // Fill the text fields with info from the house object.
        	DecimalFormat removeTrailingZeroes = new DecimalFormat("0.#####");
        	totalAreaTextField.setText(removeTrailingZeroes.format(this.house.getArea()));
        	bedroomsTextField.setText(removeTrailingZeroes.format(house.getBedrooms()));
        	bathroomsTextField.setText(removeTrailingZeroes.format(house.getBathrooms()));
        	setTotalCostLabel();
        } else {
        	totalAreaTextField.setText("");
        	bedroomsTextField.setText("");
        	bathroomsTextField.setText("");
        }
    }
    
    /**
     * Sets the value of the status label depending on whether the house is a template.
     * @param houseIsTemplate
     */
    private void setStatusLabelText(boolean houseIsTemplate) {
    	if (houseIsTemplate) {
    		statusLabel.setText("We have just what you're looking for!");
            return;
    	}
    	
		statusLabel.setText("We'll be more than happy to build that for ya!");
		return;
    }
    
    /**
     * Changes the image of the house depending on whether the house is a template.
     * @param houseIsTemplate
     */
    private void setHouseImageView(boolean houseIsTemplate) {
    	if (houseIsTemplate) {
            loadImage(house.getStyle());
            return;
    	}
    	
        loadImage("Custom");
		return;
    }
    
    private void setStyleChoiceBox(House house) {
    	ObservableList<House> houses = this.mainApp.getHouses();
        styleChoiceBox.setItems(houses);
        
        int i;
        for (i = 0; i < houses.size(); i++) {
        	if (houses.get(i).getStyle() == house.getStyle()) {
        		break;
        	}
        }
    	styleChoiceBox.setValue(houses.get(i));
        this.house = houses.get(i);
    }
    
    /**
     * Checks if a given house is equal to one of template houses.
     * 
     * @param house
     * @return True if template.
     */
    private boolean houseIsTemplate(House house) {
    	ObservableList<House> houses = this.mainApp.getHouses();
    	
    	for (House template : houses) {
        	if (house.compareTo(template) == 0) {
        		return true;
        	}
    	}
    	
		return false;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Called when the user clicks the "Close" button.
     */
    @FXML
    private void handleClose() {
        dialogStage.close();
    }
    
    /**
     * Loads the image based upon the style of the house.
     * @param style
     */
    private void loadImage(String style) {
    	switch (style) {
    		case "Traditional":
    			houseImageView.setImage(new Image("traditional-house.jpg"));
    			return;
    		case "Modern":
    			houseImageView.setImage(new Image("modern-house.jpg"));
    			return;
    		case "European":
    			houseImageView.setImage(new Image("european-house.jpg"));
    			return;
    		case "Southwest":
    			houseImageView.setImage(new Image("southwest-house.jpg"));
    			return;
    		case "Mountain":
    			houseImageView.setImage(new Image("mountain-house.jpg"));
    			return;
    		case "Victorian":
    			houseImageView.setImage(new Image("victorian-house.jpg"));
    			return;
    		case "Country":
    			houseImageView.setImage(new Image("country-house.jpg"));
    			return;
    		case "Custom":
    			houseImageView.setImage(new Image("custom-house.jpg"));
    			return;
    	}
    }
    
    /**
     * Displays the total cost to the user.
     */
    private void setTotalCostLabel() {
    	totalCostLabel.setText(getCost());
    }
    
    /**
     * Calculates the total cost of the house.
     */
    private String getCost() {
    	Map<String, Double> customizations = new HashMap<>();
    	customizations.put("numOfBedrooms", Double.parseDouble((bedroomsTextField.getText())));
    	customizations.put("numOfBathrooms", Double.parseDouble((bathroomsTextField.getText())));
    	customizations.put("area", Double.parseDouble((totalAreaTextField.getText())));
    	
    	NumberFormat formatter = NumberFormat.getCurrencyInstance();

    	return formatter.format(house.getCost(customizations));
    }
    
    
    /**
     * Prints out the user's receipt to the terminal.
     */
    @FXML
    private void handlePrint() {
    	System.out.println("\n");
    	String thankYou = "Thanks for shopping with the Midwest Home Design Company!";
    	System.out.println(thankYou);
    	for (char c : thankYou.toCharArray()) System.out.print("=");
    	System.out.println("\n\nStyle: " + styleChoiceBox.getSelectionModel().getSelectedItem());
    	System.out.println("Total Area: " + totalAreaTextField.getText());
    	System.out.println("Bedrooms: " + bedroomsTextField.getText());
    	System.out.println("Bathrooms: " + bathroomsTextField.getText());
    	System.out.println("\nTotal Cost: " + getCost());
    }
}
