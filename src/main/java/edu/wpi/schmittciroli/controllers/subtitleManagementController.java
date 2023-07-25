package edu.wpi.schmittciroli.controllers;

import edu.wpi.schmittciroli.model.Subtitle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class subtitleManagementController implements Initializable, subtitleEventListenerI{
	@FXML
	private VBox subtitleListVBox;

	private subtitleEventListenerI listenerI;
	private mainScreenController mainScreenController;
	@FXML
	private HBox headerHBox;

	//TODO temporary for demo
	private List<Subtitle> subs(){
		List<Subtitle> ls = new ArrayList<>();
		//Subtitle sw = new Subtitle("Star Wars", "ENG", "2:21:00");
		Subtitle nd = new Subtitle("Napoleon Dynamite", "ENG", "1:22:00");
		//ls.add(sw);
		ls.add(nd);
		return ls;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headerHBox.setId("headerHBox");
		subtitleListVBox.setId("subtitleListVBox");
		headerHBox.getStylesheets().add("edu/wpi/schmittciroli/controllers/styles/movieListHeaderStyle.css");

		//FXML for loading at runtime
		List<Subtitle> subs = new ArrayList<Subtitle>(subs());
		for(int i = 0; i < subs.size(); i++){
			FXMLLoader subsFXMLLoader = new FXMLLoader(this.getClass().getResource("subtitleItem.fxml"));
			try{
				HBox indvSubHBox = subsFXMLLoader.load();
				subtitleItemController indvSubsController = subsFXMLLoader.getController();
				indvSubsController.setEventListener(this);
				indvSubsController.setData(subs.get(i));
				subtitleListVBox.getChildren().add(indvSubHBox);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public void setMainScreenController(mainScreenController msC){
		this.mainScreenController = msC;
	}

	@Override
	public void onSubtitleEvent(subtitleEvent event) {
		this.mainScreenController.setActiveSub(event.getSub());
		this.mainScreenController.setMovieTitleLBL(event.getSub().getMovieTitle());
	}

}
