package com.surv.ui123;

class Response_FeedBack {
	String _id;
	int _qno;
	String _res;

	public Response_FeedBack() {

	}

	public Response_FeedBack(String _id, int _qno, String _res) {
		this._id = _id;
		this._qno = _qno;
		this._res = _res;

	}

	public void setId(String _id) {
		this._id = _id;
	}

	public void setQno(int _qno) {
		this._qno = _qno;
	}

	public void setRes(String _res) {
		this._res = _res;
	}

	public String getId() {
		return this._id;
	}

	public int getQno() {
		return this._qno;
	}

	public String getRes() {
		return this._res;
	}

}