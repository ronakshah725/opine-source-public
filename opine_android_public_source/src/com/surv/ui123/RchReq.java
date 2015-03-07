package com.surv.ui123;

class RchReq {
	int amt;
	String serv;
	String phno;
	String ts;

	public RchReq() {

	}

	public RchReq(int amt,String serv, String phno, String ts) {
		this.amt=amt;
		this.serv=serv;
		this.phno=phno;
		this.ts=ts;}
	
	public void setAmt(int amt) {
		this.amt=amt;
	}
	
	public void setServ(String serv)
	{
		this.serv=serv;
	}
	
	public void setPhno(String phno)
	{
		this.phno=phno;
	}

		
	public void setTs(String ts) {
		this.ts=ts;
	}

	
	public String getTs() {
		return this.ts;
	}

	public int getAmt() {
		return this.amt;
	}
	
	public String getServ()
	{
		return this.serv;
	}
	
	public String getPhno()
	{
		return this.phno;
	}
	
}