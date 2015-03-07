package com.surv.ui123;

class pollsWithin {

	// private variables
	String id;
	String qid;
	String ques;
	int nrpl;
	String ops;
	String res;
	int icreated;

	// Empty constructor
	public pollsWithin() {

	}

	// constructor
	public pollsWithin(String id, String qid, String ques,int nrpl, String ops, String res,int icreated) {
		this.id = id;
		this.qid = qid;
		this.ques = ques;
		this.ops=ops;
		this.nrpl = nrpl;
		this.res=res;
		this.icreated=icreated;
	}

	public void setId(String id) {
		this.id = id;
	}

	String getId() {
		return this.id;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	String getQid() {
		return this.qid;
	}

	public void setQues(String ques) {
		this.ques = ques;
	}

	String getQues() {
		return this.ques;
	}

	public void setNrpl(int nrpl) {
		this.nrpl = nrpl;
	}

	int getNrpl() {
		return this.nrpl;
	}

	public void setOps(String ops) {
		this.ops = ops;
	}

	String getOps() {
		return this.ops;
	}

	public void setRes(String res) {
		this.res = res;
	}

	String getRes() {
		return this.res;
	}
	int getIcreated()
	{
		return this.icreated;
	}
	void setIcreated(int n)
	{
		this.icreated=n;
	}

}