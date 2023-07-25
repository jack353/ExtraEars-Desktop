package edu.wpi.schmittciroli.controllers;

import edu.wpi.schmittciroli.model.Subtitle;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class subtitleItemController implements Initializable {
	private subtitleEventListenerI listenerI;
	@FXML
	private HBox subsIndvHBox;

	@FXML
	private Label titleLabel;

	@FXML
	private Label languageLabel;

	@FXML
	private MFXButton selectBTN;

	private Subtitle linkedSub;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		subsIndvHBox.setId("subsIndvHBox");
		subsIndvHBox.getStylesheets().add("edu/wpi/schmittciroli/controllers/styles/individualSubHBoxStyle.css");

		selectBTN.setOnMouseClicked(event -> {
			subtitleEvent eventC = new subtitleEvent(linkedSub);
			if(listenerI != null){
				listenerI.onSubtitleEvent(eventC);
			}
		});
	}

	public void setEventListener(subtitleEventListenerI listenerI){
		this.listenerI = listenerI;
	}

	public void setData(Subtitle sub){
		this.linkedSub = sub;
		titleLabel.setText(sub.getMovieTitle());
		languageLabel.setText(sub.getLanguage());
	}



}
