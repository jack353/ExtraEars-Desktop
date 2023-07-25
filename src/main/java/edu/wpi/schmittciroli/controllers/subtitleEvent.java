package edu.wpi.schmittciroli.controllers;

import edu.wpi.schmittciroli.model.Subtitle;

public class subtitleEvent {
	private final Subtitle sub;

	public subtitleEvent(Subtitle sub){
		this.sub = sub;
	}

	public Subtitle getSub(){
		return this.sub;
	}


}
