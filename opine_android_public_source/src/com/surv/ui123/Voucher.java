package com.surv.ui123;

class Voucher {
	String _id;
	String _vcrnm;	
	String _desc;
	Integer _cost;
	Integer _chk;

	public Voucher() {

	}

	public Voucher(String _id, String _vcrnm, String _desc,
			Integer _cost, Integer _chk) {
		this._id = _id;
		this._vcrnm = _vcrnm;		
		this._desc = _desc;
		this._cost = _cost;
		this._chk = _chk;
	}

	public String getId() {
		return this._id;
	}

	public int getCost() {
		return this._cost;
	}

	public String getVcrnm() {
		return this._vcrnm;
	}

	public int getChk() {
		return this._chk;
	}

	public String getDesc() {
		return this._desc;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public void setCost(int _cost) {
		this._cost = _cost;
	}

	public void setChk(int _chk) {
		this._chk = _chk;
	}

	public void setVcrnm(String _vcrnm) {
		this._vcrnm = _vcrnm;
	}

	public void setDesc(String _desc) {
		this._desc = _desc;
	}

	public void toggleChecked() {
		if (this._chk == 1) {
			this._chk = 0;
		} else {
			this._chk = 1;
		}
	}
}