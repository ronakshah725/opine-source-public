package com.surv.ui123;


class SaveDemo {

	String zip;
	String mar;
	String edu;
	String occ;
	String sal;

	// Empty constructor
	public SaveDemo() {

	}

	// constructor
	public SaveDemo(String zip, String mar, String edu,String occ, String sal) {
		this.zip=zip;
		this.mar=mar;
		this.edu=edu;
		this.occ = occ;
		this.sal = sal;
	}

	void setmar(String mar)
	{
		this.mar=mar;
	}
	void setEdu(String edu)
	{
		this.edu=edu;
	}
void setzip(String zip)
{
	this.zip=zip;
}
void setOcc(String occ)
{
	this.occ=occ;
}
void setSal(String sal)
{
	this.sal=sal;
}


String getZip()
{
	return this.zip;
}
String getMar()
{
	return this.mar;
}
String getEdu()
{
	return this.edu;
}

String getOcc()
{
	return this.occ;
}
String getSal()
{
	return this.sal;
}

}