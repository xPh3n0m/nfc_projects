package epfl.tnyfeler.eventmanager.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateEventSceneController implements Initializable {
	
	private Stage prevStage;
	
	@FXML
	private TextField eventNameTextField;
	
	@FXML
	private Button createEventButton;
	
	public void setPrevStage(Stage stage) {
		prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Opens the create event page
     * @throws IOException 
     */
    public void createNewEvent(ActionEvent event) throws IOException {
    	Stage stage; 
        Parent root;
        if(event.getSource()==createEventButton){
           //get reference to the button's stage         
           stage=(Stage) createEventButton.getScene().getWindow();
           //load up OTHER FXML document
           root = FXMLLoader.load(getClass().getResource("EventManagerHomepage.fxml"));
           //create a new scene with root and set the stage
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
         }
    }

}
