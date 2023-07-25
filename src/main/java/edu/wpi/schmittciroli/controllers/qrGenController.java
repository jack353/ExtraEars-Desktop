package edu.wpi.schmittciroli.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class qrGenController implements Initializable {
	@FXML
	private ImageView qrCodeDisplayIMG;

	@FXML
	private MFXButton generateQRBTN;

	@FXML
	private MFXButton saveQRBTN;

	private String info;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		generateQRBTN.setOnMouseClicked(event -> {
			try {
				qrCodeDisplayIMG.setImage(generateQRImg());
			} catch (WriterException e) {
				throw new RuntimeException(e);
			}
		});

		saveQRBTN.setOnMouseClicked(event -> {
			saveQRCode();
		});
	}

	/**
	 * Sets data for proper QR code generation
	 * @param info Server info
	 */
	public void setData(String info){
		this.info = info;
	}

	/**
	 * Saves QR Code to local machine
	 */
	public void saveQRCode(){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter for image files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(generateQRImg(), null), "png", file);
			} catch (IOException | WriterException e) {
				// handle exception
			}
		}
	}

	/**
	 * Generates QR Code Image
	 */
	public Image generateQRImg() throws WriterException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix;
		String qrCodeText = this.info;
		int width = 250;
		int height = 250;
		try {
			bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();

			// Set background color to white
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, width, height);

			// Set QR code color to black
			graphics.setColor(Color.BLACK);

			// Write QR code to image
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (bitMatrix.get(x, y)) {
						graphics.fillRect(x, y, 1, 1);
					}
				}
			}

			return SwingFXUtils.toFXImage(image, null);
		} catch (WriterException e) {
			throw e;
		}
	}
}

