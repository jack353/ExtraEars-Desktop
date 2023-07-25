package edu.wpi.schmittciroli.model;

public class Theater {
	private int id;
	private String currentMovie;

	public Theater(int id, String currentMovie){
		this.id = id;
		this.currentMovie = currentMovie;
	}

	public String getCurrentMovie() {
		return currentMovie;
	}

	public void setCurrentMovie(String currentMovie) {
		this.currentMovie = currentMovie;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
