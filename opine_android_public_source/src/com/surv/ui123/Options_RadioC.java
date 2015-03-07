package com.surv.ui123;

class Options_RadioC {
	String _id;
	int _qno;
	String[] _op;
	int _res;

	public Options_RadioC() {

	}

	public Options_RadioC(String _id, int _qno, String[] _op, int _res) {
		this._id = _id;
		this._qno = _qno;
		this._op = new String[_op.length];
		System.arraycopy(_op, 0, this._op, 0, _op.length);
		this._res = _res;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public void setQno(int _qno) {
		this._qno = _qno;
	}

	public void setOp(String[] _op) {
		this._op = new String[_op.length];
		for (int i = 0; i < _op.length; i++) {
			this._op[i] = "";
		}
		System.arraycopy(_op, 0, this._op, 0, _op.length);
	}

	public void setRes(int _res) {
		this._res = _res;
	}

	public String getId() {
		return this._id;
	}

	public int getQno() {
		return this._qno;
	}

	public String[] getOp() {
		return this._op;
	}

	public int getRes() {
		return this._res;
	}

}