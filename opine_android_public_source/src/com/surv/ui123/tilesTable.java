package com.surv.ui123;

import android.util.Log;

class tilesTable {

	// private variables
	String _id;
	String _compNm;
	int _nodl;
	int _pts;
	int _sts;
	int _no;
	String _conven;

	// Empty constructor
	public tilesTable() {

	}

	// constructor
	public tilesTable(String _id, String _compNm, int _nodl, int _pts, int _sts, int _no,String _conven) {
		this._compNm = _compNm;
		this._pts = _pts;
		this._nodl = _nodl;
		this._id = _id;
		this._sts = _sts;
		this._no = _no;
		this._conven=_conven;

		Log.w("Added Count", "" + this._no);
	}

	public int getSts() {
		return this._sts;
	}

	public String getId() {
		return this._id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getCompNm() {
		return this._compNm;
	}

	public void setCompNm(String compNm) {
		this._compNm = compNm;
	}

	public int getPts() {
		return this._pts;
	}

	public void setPts(int pts) {
		this._pts = pts;
	}

	public String getConven()
	{
		return this._conven;
	}
	public void setConven(String conven)
	{
		this._conven=conven;
	}
	public void setNodl(int nodl) {
		this._nodl = nodl;
	}

	public int getNodl() {
		return this._nodl;
	}

	public void setSts(int s) {
		this._sts = s;
	}

	public int getNo() {
		return _no;
	}

	public void setNo(int n) {
		this._no = n;
	}

}