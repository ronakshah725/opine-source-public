package com.surv.ui123;

class pollCats {

	String id;
	String catNm;
	int nrpl;

	// Empty constructor
	public pollCats() {

	}
	public pollCats(String id, String catNm, int nrpl) {

		this.id=id;
		this.catNm=catNm;
		this.nrpl=nrpl;
	}

	public void setId(String id)
	{
		this.id=id;
	}
	
	public String getId()
	{
		return id;
	}
	// constructor
	public pollCats(String catNm) {
		this.catNm = catNm;
	}

	public String getCats() {
		return this.catNm;
	}

	public void setCats(String cat) {
		this.catNm = cat;
	}

	public int getnrpl() {
		// TODO Auto-generated method stub
		return this.nrpl;
	}
	public void setnrpl(int num)
	{
		nrpl=num;
	}

}