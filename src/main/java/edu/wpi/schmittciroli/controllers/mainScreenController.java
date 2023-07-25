package edu.wpi.schmittciroli.controllers;

import edu.wpi.schmittciroli.Server_v2;
import edu.wpi.schmittciroli.model.Device;
import edu.wpi.schmittciroli.model.Subtitle;
import edu.wpi.schmittciroli.model.Theater;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//#c4c4c4, #767b91
public class mainScreenController implements Initializable{

	private Server_v2 server;
	@FXML
	private VBox theaterInfoVBox; //Vbox for displaying all theaters + info on left hand side

	@FXML
	private VBox deviceInfoVBox; //VBox for displaying connected devices + info on right hand side.

	@FXML
	private MFXButton qrGenBTN; //BTN for movieList
	@FXML
	private Label movieTitleLBL; //Label for movie Title
	@FXML
	private MFXButton subManagementBTN; //BTN for subManagement

	@FXML
	private Label deviceCountLBL; //Label for device count on Bottom
	@FXML
	private MFXButton play_Pause_BTN; //play/pause button
	@FXML
	private MFXButton increaseMS_BTN;
	@FXML
	private MFXButton decreaseMS_BTN;

	@FXML
	private Label fileDisplayLBL;

	@FXML
	private Label subtitleLanguageDisplayLBL;

	@FXML
	private Label currentRuntimeLBL;

	@FXML
	private Label finalRuntimeLBL;
	private Subtitle activeSub;

	@FXML
	private ImageView displayImage;

	private boolean isPaused = true;

	private List<Theater> theaters(){
		List<Theater> ls = new ArrayList<>();
		Theater th_B = new Theater(1, "Napoleon Dynamite");
		ls.add(th_B);
		return ls;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		movieTitleLBL.setId("movieTitleLBL"); //Sets ID of label for CSS styling
		theaterInfoVBox.setId("infoVBox");
		deviceInfoVBox.setId("infoVBox");
		setDeviceCountLBL(0);

		//For runtime incrementing in background. Runs on another tread to avoid hanging the main application thread.
		RuntimeTask timerTask = new RuntimeTask(this, isPaused);
		Thread timerThread = new Thread(timerTask);
		timerThread.setDaemon(true);
		timerThread.start();

		/*
		For Loading Theater Info List. TODO This is temporary Data in need of backend. Just for Demo!
		 */
		List<Theater> theaters = new ArrayList<Theater>(theaters());
		for(int i = 0; i < theaters.size(); i++){
			FXMLLoader theaterFXMLLoader = new FXMLLoader(this.getClass().getResource("theaterItem.fxml"));

			try{
				HBox indvTheaterHBox = theaterFXMLLoader.load();
				theaterItemController indvTheaterController = theaterFXMLLoader.getController();
				indvTheaterController.setData(theaters.get(i));
				theaterInfoVBox.getChildren().add(indvTheaterHBox);
			}catch(IOException e){
				e.printStackTrace();
			}
		}


		qrGenBTN.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("qrGenView.fxml"));
				Parent root1 = null;
				try {
					root1 = (Parent) fxmlLoader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				//Get controller instance to pass in server data to properly generate the QR Code on popup page

				qrGenController controllerInstance = fxmlLoader.getController();
				controllerInstance.setData(server.getServerInfo()); //Send data to QR page
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
			}
		});

		subManagementBTN.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("subtitleManagementView.fxml"));
				Parent root1 = null;
				try {
					root1 = (Parent) fxmlLoader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				subtitleManagementController subtitleManagementController = fxmlLoader.getController();
				// Pass the mainScreenController to the popup window controller
				subtitleManagementController.setMainScreenController(mainScreenController.this);

				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
			}
		});

		play_Pause_BTN.setOnMouseClicked(event -> {
			//Send play/pause code to devices
			changePlayPause();
			server.sendDataToClients("pp");

		});

		increaseMS_BTN.setOnMouseClicked(event -> {
			server.sendDataToClients("inc");
		});

		decreaseMS_BTN.setOnMouseClicked(event -> {
			server.sendDataToClients("dec");
		});

	}

	public void addDeviceToList(Device d){
		FXMLLoader deviceFXMLLoader = new FXMLLoader(this.getClass().getResource("deviceItem.fxml"));

		try{
			HBox indvDeviceHBox = deviceFXMLLoader.load();
			deviceItemController indvDeviceController = deviceFXMLLoader.getController();
			indvDeviceController.setData(d);
			deviceInfoVBox.getChildren().add(indvDeviceHBox);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/*
	Allows for setting of Device Counter.
	Takes in String of Int
	 */
	public void setDeviceCountLBL(int dC) {
		this.deviceCountLBL.setText(String.valueOf(dC));
	}

	public void setServer(Server_v2 s){
		this.server = s;
	}

	public void setMovieTitleLBL(String str){
		this.movieTitleLBL.setText(str);
	}

	public void setFileDisplayLBL(String str){
		this.fileDisplayLBL.setText(str);
	}

	public void setCurrentRuntimeLBL(String str){
		this.currentRuntimeLBL.setText(str);
	}

	public void setFinalRuntimeLBL(String str){
		this.finalRuntimeLBL.setText(str);
	}

	public void setSubtitleLanguageDisplayLBL(String str){
		this.subtitleLanguageDisplayLBL.setText(str);
	}

	public void changePlayPause(){
		if(isPaused){
			this.play_Pause_BTN.setText("Pause");
			isPaused = false;
		}else{
			this.play_Pause_BTN.setText("Play");
			isPaused = true;
		}
	}

	/**
	 * Listener for Selecting Subtitle. Will set params on mainscreen controller
	 * @param sub Subtitle info being set as active.
	 */
	public void setActiveSub(Subtitle sub){
		this.activeSub = sub;
		setFileDisplayLBL(sub.getFileLoc());
		setSubtitleLanguageDisplayLBL(sub.getLanguage());
		setFinalRuntimeLBL(sub.getRuntime());
		this.displayImage.setImage(sub.getMovieImage());
	}


	public void updateTimerValue() {
		Platform.runLater(() -> {
			if(!isPaused){
				int hour = Integer.parseInt(currentRuntimeLBL.getText().substring(0,2)); // get the hour value as an integer
				int minute = Integer.parseInt(currentRuntimeLBL.getText().substring(3,5));
				int second = Integer.parseInt(currentRuntimeLBL.getText().substring(6));

				second += 1; //runs each second
				//always append 0 to hour

				if(minute == 59){
					hour +=1;
					minute = 0;
				}
				if(second == 59){
					minute += 1;
					second = 0;
				}

				currentRuntimeLBL.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
			}
		});
	}
}
