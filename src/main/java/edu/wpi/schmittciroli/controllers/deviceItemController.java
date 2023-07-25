package edu.wpi.schmittciroli.controllers;

import edu.wpi.schmittciroli.model.Device;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class deviceItemController implements Initializable {

	@FXML
	private HBox deviceInfoHBox;
	@FXML
	private ImageView connectionStatusImg;
	@FXML
	private ImageView deviceImg;

	@FXML
	private Label deviceInfo;


	public void initialize(URL location, ResourceBundle resources) {
		deviceInfoHBox.setId("deviceIndvHBox");
		deviceInfoHBox.getStylesheets().add("edu/wpi/schmittciroli/controllers/styles/individualDeviceHBoxStyle.css");
	}

	public void setData(Device device){
		deviceInfo.setText(device.getInfo());
	}

	public ImageView getConnectionStatusImg() {
		return connectionStatusImg;
	}

	public void setConnectionStatusImg(ImageView connectionStatusImg) {
		this.connectionStatusImg = connectionStatusImg;
	}

	public ImageView getDeviceImg() {
		return deviceImg;
	}

	public void setDeviceImg(ImageView deviceImg) {
		this.deviceImg = deviceImg;
	}

	public Label getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(Label deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
