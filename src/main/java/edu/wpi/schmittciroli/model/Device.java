package edu.wpi.schmittciroli.model;

public class Device {
	private String deviceInfo;

	public Device(String deviceInfo){
		this.deviceInfo = "device-iphone-" + deviceInfo;
	}

	public String getInfo(){
		return this.deviceInfo;
	}

	public void setInfo(String info){
		this.deviceInfo = info;
	}
}
