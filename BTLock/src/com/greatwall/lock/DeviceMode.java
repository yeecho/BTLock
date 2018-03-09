package com.greatwall.lock;

public class DeviceMode {

	private String name;
	private String password;
	private String type;
	private String deviceName;
	private String deviceAddress;
	
	public DeviceMode() {
		
	}
	public DeviceMode(String name, String password, String type, String deviceName, String deviceAddress) {
		super();
		this.name = name;
		this.password = password;
		this.type = type;
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
}
