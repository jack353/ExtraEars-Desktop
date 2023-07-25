package edu.wpi.schmittciroli.controllers;

import edu.wpi.schmittciroli.model.Theater;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class theaterItemController implements Initializable {

	@FXML
	private HBox theaterIndvHBox;

	@FXML
	private Button manageTheaterBtn;

	@FXML
	private Label movieInfoLabel;

	@FXML
	private Label theaterInfoLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		theaterIndvHBox.setId("theaterIndvHBox");
		theaterIndvHBox.getStylesheets().add("edu/wpi/schmittciroli/controllers/styles/individualTheaterHBoxStyle.css");
	}

	public void setData(Theater theater){
		movieInfoLabel.setText(theater.getCurrentMovie());
		theaterInfoLabel.setText(Integer.toString(theater.getId()));
	}

}
