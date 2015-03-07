package com.surv.ui123;

class Otherans {
	String _id;
	int _qno;
	String _ans;	

	public Otherans() {

	}

	public Otherans(String _id, int _qno, String _ans) {
		this._id = _id;
		this._qno = _qno;
		this._ans = _ans;		
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public void setQno(int _qno) {
		this._qno = _qno;
	}

	
	public String getId() {
		return this._id;
	}

	public int getQno() {
		return this._qno;
	}

	
	public String getAns() {
		return this._ans;
	}
	public void setAns(String _ans) {
	this._ans=_ans;
	}

}