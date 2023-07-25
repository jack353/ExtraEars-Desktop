package edu.wpi.schmittciroli.controllers;

import javafx.concurrent.Task;

public class RuntimeTask extends Task<Void> {
	private final mainScreenController msC;

	private boolean isPaused;

	public RuntimeTask(mainScreenController msC, boolean isPaused) {
		this.msC = msC;
		this.isPaused = isPaused;
	}


	@Override
	protected Void call() throws Exception {
		while (true) {
				msC.updateTimerValue();
				// Wait for one second before updating the timer again
				Thread.sleep(1000);
		}//do nothign
	}

	public void setState(boolean state){
		this.isPaused = state;
	}
}
