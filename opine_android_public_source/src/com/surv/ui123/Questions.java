package com.surv.ui123;

class Questions {
	String _id;
	int _qno;
	String _quest;
	int _typ;
	int _g;
	int _isOp;
	int _spm;
	int _spmd;

	public Questions() {

	}

	public Questions(String _id, int _qno, String _quest, int _typ, int _isOp,
			int _g, int _spm) {
		this._id = _id;
		this._qno = _qno;
		this._quest = _quest;
		this._typ = _typ;
		this._isOp = _isOp;
		this._g = _g;
		this._spm=_spm;
		this._spmd=0;
	}

	public String getId() {
		return this._id;
	}

	public int getQno() {
		return this._qno;
	}

	public String getQuest() {
		return this._quest;
	}

	public int getTyp() {
		return this._typ;
	}

	public void setQno(int _qno) {
		this._qno = _qno;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public void setQuest(String _quest) {
		this._quest = _quest;
	}

	public void setTyp(int _typ) {
		this._typ = _typ;
	}

	public void setIsop(int i) {
		this._isOp = i;
	}

	public int getIsop() {
		return this._isOp;
	}

	public int getG() {
		return this._g;
	}

	public void setG(int _g) {
		this._g = _g;
	}
	public int getSpm() {
		return this._spm;
	}

	public void setSpm(int _spm) {
		this._spm = _spm;
	}
	public int getSpmd() {
		return this._spmd;
	}

	public void setSpmd(int _spmd) {
		this._spmd = _spmd;
	}
}