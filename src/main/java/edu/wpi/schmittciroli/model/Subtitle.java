package edu.wpi.schmittciroli.model;

import javafx.scene.image.Image;

import java.net.URL;

public class Subtitle {
	private String movieTitle;
	private String language; //Conventional naming for langs, ENG, SP, GER, etc...

	private String fileLoc;

	private Image img;
	private String runtime;

	public Subtitle(String movieTitle, String language, String runtime){
		this.movieTitle = movieTitle;
		this.language = language;
		this.fileLoc = generateFileLoc();
		this.runtime = runtime;
		this.img = setMovieImage();
	}

	private Image setMovieImage() {
		return new Image(Subtitle.class.getResource("images/" + getMovieTitle().replaceAll("\\s", "") + "_IMG.png").toString());
	}

	public Image getMovieImage(){
		return this.img;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFileLoc(){
		return this.fileLoc;
	}

	/*
	For Generating the filenaming scheme for easy fetching. All subs have the same naming scheme
	 */
	private String generateFileLoc(){
		return "Subtitles/" + getMovieTitle().replaceAll("\\s", "") + "_" + getLanguage() + ".srt";
	}

	public String getRuntime() {
		return this.runtime;
	}
}
