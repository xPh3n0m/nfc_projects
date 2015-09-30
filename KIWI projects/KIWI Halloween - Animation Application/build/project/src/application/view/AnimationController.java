package application.view;

import java.sql.SQLException;

import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import kw.nfc.communication.ConnectDB;
import kw.nfc.communication.Utility;
import kw.nfc.communication.NewAnimation;

public class AnimationController {
	@FXML
    private Label titleLabel;

    @FXML
    private AnchorPane shotPane;
    @FXML
    private AnchorPane prizePane;
    @FXML
    private Label errorTextField;
    
    @FXML
    private AnchorPane wheelPane;
    
    private NewAnimation newAnimation;
    
    private ConnectDB connDB;
    
    // Reference to the main application.
    private Main mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public AnimationController() {
    	newAnimation = new NewAnimation();
    	connDB = new ConnectDB();
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	shotPane.setVisible(false);
    	prizePane.setVisible(false);
    	wheelPane.setVisible(false);
    	
    	titleLabel.setText("KIWI Animation Application");
    	
    	try {
			connDB.connect();
		} catch (SQLException e) {
			errorTextField.setText("Error connecting to the Database. Contact Admin");
			e.printStackTrace();
		}
    	
    	startButtonHandler();
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    public void startButtonHandler() {
    	Utility.CANCELED = false;
    	
    	newAnimation = new NewAnimation();
    	newAnimation.setConnDB(connDB);
    	
    	newAnimation.setOnSucceeded(
    			new EventHandler<WorkerStateEvent>() {

    		    @Override
    		    public void handle(WorkerStateEvent t) {
    		    	titleLabel.setText(t.getSource().getValue() + ",");
    		    	int gid = newAnimation.getGid();
    		    	int k = (int) (200*Math.random());
    		    	System.out.println(k);
    		    	if(k < 30) {
    		    		shotPane.setVisible(false);
    		    		prizePane.setVisible(false);
    		    		wheelPane.setVisible(true);
    		    		try {
							connDB.insertAnimation(gid, Utility.ANIMATION_WHEEL);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    		    	} else if(k >= 30 && k < 50) {
    		    		shotPane.setVisible(false);
    		    		prizePane.setVisible(true);
    		    		wheelPane.setVisible(false);
    		    		try {
							connDB.insertAnimation(gid, Utility.ANIMATION_PRIZE);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    		    	} else {
    		    		shotPane.setVisible(true);
    		    		prizePane.setVisible(false);
    		    		wheelPane.setVisible(false);
    		    		try {
							connDB.insertAnimation(gid, Utility.ANIMATION_SHOT);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    		    	}
    		    	
    		    	startButtonHandler();
    		    }
    		});
    		
    	newAnimation.setOnFailed(
    				new EventHandler<WorkerStateEvent>() {

    			    @Override
    			    public void handle(WorkerStateEvent t) {
    			    	errorTextField.setText("Card/DB Error. Contact Admin");
    			    	reset();
    			    }
    		});
    		
    	newAnimation.setOnCancelled(
    				new EventHandler<WorkerStateEvent>() {

    			    @Override
    			    public void handle(WorkerStateEvent t) {
    			    	reset();
    			    }
    		});
    		
    		//actionsTextField.setText("Place wristband on reader");
    		newAnimation.start();
    }
    
    public void cancelOrderHandler() {
    	newAnimation.stopTask();
    	newAnimation.cancel();
    	reset();
    }
    
    public void reset() {
    	
    	shotPane.setVisible(false);
    	prizePane.setVisible(false);
    	wheelPane.setVisible(false);
    	
    	titleLabel.setText("KIWI Animation Application");
    	
    	try {
			connDB.reconnect();
			
			startButtonHandler();
		} catch (SQLException e) {
			errorTextField.setText("Card/DB Error. Contact Admin");
			e.printStackTrace();
		}
    	
    	
    }
}
