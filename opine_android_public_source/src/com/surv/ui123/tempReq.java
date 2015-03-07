package com.surv.ui123;

class tempReq {
	String vcr;
	String ts;

	public tempReq() {

	}

	public tempReq(String vcr, String ts) {
		this.vcr = vcr;
		this.ts = ts;
	}

	public void setVcr(String vcr) {
		this.vcr = vcr;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVcr() {
		return this.vcr;
	}

	public String getTs() {
		return this.ts;
	}

}