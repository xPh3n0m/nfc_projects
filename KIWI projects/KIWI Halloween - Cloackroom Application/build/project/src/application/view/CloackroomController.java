package application.view;

import application.Main;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kw.nfc.communication.ConnectDB;
import kw.nfc.communication.ReadCloackNumber;
import kw.nfc.communication.Utility;
import kw.nfc.communication.WriteNewCloackId;

public class CloackroomController {

    @FXML
    private Button startReadingButton;
    @FXML
    private Button stopReadingButton;
    @FXML
    private Label setCloackNumberLabel;
    @FXML
    private TextField cloackNumberTextField;
    @FXML
    private Button confirmWriteCloackNumber;
    @FXML
    private Button cancelWriteCloackNumber;
    @FXML
    private TextField actionsTextField;
    
    private ReadCloackNumber readCloackNumber;
    
    private WriteNewCloackId wnci;
    
    // Reference to the main application.
    private Main mainApp;
    
    private ConnectDB connDB;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public CloackroomController() {
    	readCloackNumber = null;
    	wnci = null;
    	connDB = new ConnectDB();
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	cloackNumberTextField.setText("0");
    	stopReadingButton.setDisable(true);
    	cancelWriteCloackNumber.setDisable(true);
    	actionsTextField.setEditable(false);
    	connDB.connect();
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    public void readCloackNumber() {
    	readCloackNumber = new ReadCloackNumber();
    	readCloackNumber.setConnDB(connDB);
    	stopReadingButton.setDisable(false);
    	startReadingButton.setDisable(true);
    		
    	readCloackNumber.setOnSucceeded(
    			new EventHandler<WorkerStateEvent>() {

    		    @Override
    		    public void handle(WorkerStateEvent t) {
    		    	int cid = (int) t.getSource().getValue();
    		    	if(cid <= 0) {
    		    		setCloackNumberLabel.setText("--");
    		    		actionsTextField.setText("No cloack number available");
    		    	} else {
    		    		setCloackNumberLabel.setText(cid + "");
    		    		actionsTextField.setText("Number succesfully read");
    		    	}
    		    	readCloackNumber();
    		    }
    		});
    		
    	readCloackNumber.setOnFailed(
    				new EventHandler<WorkerStateEvent>() {

    			    @Override
    			    public void handle(WorkerStateEvent t) {
    			    	actionsTextField.setText("Failed, please try again");
    			    	resetRead();
    			    }
    		});
    		
    	readCloackNumber.setOnCancelled(
    				new EventHandler<WorkerStateEvent>() {

    			    @Override
    			    public void handle(WorkerStateEvent t) {
    			    	actionsTextField.setText("Order cancelled");
        		    	stopReadingButton.setDisable(true);
        		    	startReadingButton.setDisable(false);
    			    	resetRead();
    			    }
    		});
    		
    		actionsTextField.setText("Place wristband on reader");
    		readCloackNumber.start();
    	}

    public void cancelReadHandler() {
    	readCloackNumber.stopTask();
    	readCloackNumber.cancel();
    	resetRead();
    }
    
    public void confirmWriteCloackNumber() {
    	if(readCloackNumber != null) {
    		cancelReadHandler();
    	}
    	confirmWriteCloackNumber.setDisable(true);
    	cancelWriteCloackNumber.setDisable(false);
    	
    	int cid = new Integer(cloackNumberTextField.getText());
    	
    	wnci = new WriteNewCloackId(cid);
    	wnci.setConnDB(connDB);
    	
    	wnci.setOnSucceeded(
    			new EventHandler<WorkerStateEvent>() {

    		    @Override
    		    public void handle(WorkerStateEvent t) {
    		    	int cid = (int) t.getSource().getValue();
    		    	if(cid == -1) {
    		    		actionsTextField.setText("Error");
    		    	} else {
    		    		setCloackNumberLabel.setText(cid + "");
    		    		actionsTextField.setText("Number succesfully written");
    		    		
    		    	}
    		    	resetWrite();
    		    }
    		});
    		
    	wnci.setOnFailed(
    				new EventHandler<WorkerStateEvent>() {

    			    @Override
    			    public void handle(WorkerStateEvent t) {
    			    	actionsTextField.setText("Failed, please try again");
    			    	resetWrite();
    			    }
    		});
    		
    	wnci.setOnCancelled(
    				new EventHandler<WorkerStateEvent>() {

    			    @Override
    			    public void handle(WorkerStateEvent t) {
    			    	actionsTextField.setText("Order cancelled");
    			    	resetWrite();
    			    }

    		});
    		
    		actionsTextField.setText("Place wristband on reader");
    		wnci.start();
    }
    
    public void cancelWriteHandler() {
    	wnci.stopTask();
    	wnci.cancel();
    	resetRead();
    }
    
    public void resetRead() {
    	stopReadingButton.setDisable(true);
    	startReadingButton.setDisable(false);
    }

	private void resetWrite() {
		// TODO Auto-generated method stub
    	confirmWriteCloackNumber.setDisable(false);
    	cancelWriteCloackNumber.setDisable(true);
	}
}
