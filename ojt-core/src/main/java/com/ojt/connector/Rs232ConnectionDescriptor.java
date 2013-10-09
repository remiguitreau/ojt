package com.ojt.connector;

public class Rs232ConnectionDescriptor implements ConnectionDescriptor {
	private String portName = "COM1";

	private int baudRate = 9600;

	private int dataBits = 8;

	private int stopBits = 1;

	private int parity = 0;

	public Rs232ConnectionDescriptor() {
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(final int baudRate) {
		this.baudRate = baudRate;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(final String name) {
		this.portName = name;
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(final int dataBits) {
		this.dataBits = dataBits;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(final int parity) {
		this.parity = parity;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(final int stopBits) {
		this.stopBits = stopBits;
	}

}
