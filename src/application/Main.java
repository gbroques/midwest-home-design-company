// G Roques EO1
package application;

import java.io.IOException;

import application.Main;
import application.model.Country;
import application.model.European;
import application.model.House;
import application.model.Modern;
import application.model.Mountain;
import application.model.Southwest;
import application.model.Traditional;
import application.model.Victorian;
import application.view.HouseCustomizeController;
import application.view.HouseOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Houses.
     */
    private ObservableList<House> houses = FXCollections.observableArrayList();


    /**
     * Constructor
     */
    public Main() {
    	houses.add(new Traditional());
    	houses.add(new Modern());
    	houses.add(new European());
    	houses.add(new Southwest());
    	houses.add(new Mountain());
    	houses.add(new Victorian());
    	houses.add(new Country());
    }
    
    /**
     * Returns the data as an observable list of Houses. 
     * @return
     */
    public ObservableList<House> getHouses() {
        return houses;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Midwest Home Design Company");

        initRootLayout();

        showHouseOverview();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the house overview inside the root layout.
     */
    public void showHouseOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/HouseOverview.fxml"));
            AnchorPane houseOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(houseOverview);

            // Give the controller access to the main app.
            HouseOverviewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setChangeListeners();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens a dialog to edit details for the specified house. If the user
     * clicks OK, the changes are saved into the provided house object and true
     * is returned.
     * 
     * @param house The house object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showHouseCustomizeDialog(House house) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/HouseCustomizeDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Customize House");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the house into the controller.
            HouseCustomizeController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setHouse(house);
            controller.setChangeListeners();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


	public static void main(String[] args) {
		launch(args);
	}
}
