package com.xikang.rr;

public class RRInterval{
	private boolean isHrValueEnable = false;
	
	public void setEcgValue (int value) {
		isHrValueEnable = setValue(value);
	}
	
	public boolean getHrState() {
		return this.isHrValueEnable;
	}
	
	private native boolean setValue(int amp);
	
	public native float getRR_Value();
	
	static {
        System.loadLibrary("ecgAnalysis");
    }
}