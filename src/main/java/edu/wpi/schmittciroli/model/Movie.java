package edu.wpi.schmittciroli.model;

public class Movie {
	private String title;
	private String runtime;
	private String director;


	public Movie(String title, String runtime, String director){
		this.title = title;
		this.runtime = runtime;
		this.director = director;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}
}
