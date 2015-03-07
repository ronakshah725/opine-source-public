package com.surv.ui123;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolder {
	public TextView vcrnm;
	public TextView desc;
	public TextView cost;
	public CheckBox chk;
	public LinearLayout vouc;
	public TextView vid;
	public Button bdesc;

	public void setVcrnm(TextView vcrnm) {
		this.vcrnm = vcrnm;
	}

	public void setDesc(TextView desc) {
		this.desc = desc;
	}

	public void setCost(TextView cost) {
		this.cost = cost;
	}
	public void setVid(TextView vid) {
		this.vid = vid;
	}

	public void setChk(CheckBox chk) {
		this.chk = chk;
	}

	public void setVouc(LinearLayout vouc) {
		this.vouc = vouc;
	}

	public TextView getVcrnm() {
		return this.vcrnm;
	}

	public TextView getDesc() {
		return this.desc;
	}

	public TextView getVid() {
		return this.vid;
	}
	
	public Button getBdesc() {
		return this.bdesc;
	}
	
	public void setBdesc(Button bdesc)
	{
		this.bdesc=bdesc;
	}
	public TextView getCost() {
		return this.cost;
	}

	public CheckBox getChk() {
		return this.chk;
	}

	public LinearLayout getVouc() {
		return this.vouc;
	}
}
