package com.surv.ui123;

import android.util.Log;

class logData {

	// private variables
	String logd;
	String dt;

	// Empty constructor
	public logData() {

	}

	// constructor
	public logData(String logd, String dt) {
		this.logd = logd;
		this.dt = dt;
	}

	public void setLogd(String logd) {

		this.logd = logd;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getLogd() {
		return logd;
	}

	public String getDt() {
		return dt;
	}
}