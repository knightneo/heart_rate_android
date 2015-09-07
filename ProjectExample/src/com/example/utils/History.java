package com.example.utils;

import java.util.Date;

public class History {
	private Date time;
	private float heartRate;
	private byte[] data;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public float getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(float heartRate) {
		this.heartRate = heartRate;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
