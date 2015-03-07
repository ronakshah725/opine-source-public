package com.surv.ui123;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragment extends Fragment {

	int mCurrentPage;
	RadioGroup rg;
	RadioGroup rg1;
	int which, which1, i = 0, res[] = null, l = 0;
	RatingBar rb;
	DatePicker dp;
	EditText et, et1;
	TableLayout tl;
	int g = 0;
	static View v;
	final Calendar c = Calendar.getInstance();

	int maxYear = c.get(Calendar.YEAR) + 100; // this year ( 2011 ) - 20 = 1991
	int maxMonth = c.get(Calendar.MONTH);
	int maxDay = c.get(Calendar.DAY_OF_MONTH);

	int minYear = 1900;
	int minMonth = 0; // january
	int minDay = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_page", 0);

	}

	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// changeStatus();

		if (mCurrentPage != FragQues.pagerAdapter.getCount()) {
			v = inflater.inflate(R.layout.fragment_layout, container, false);
			TextView tv = (TextView) v.findViewById(R.id.tv);
			TextView tv1 = (TextView) v.findViewById(R.id.cn);
			TextView tv2 = (TextView) v.findViewById(R.id.si);
			tv1.setText(MainActivity.curr.getCompNm());
			tv2.setText(MainActivity.curr.getId());
			tv.setText(FragQues.allQuests.get(mCurrentPage - 1).getQuest());
			TableRow r = (TableRow) v.findViewById(R.id.tableRow2);
			r.removeAllViews();

			int t = FragQues.allQuests.get(mCurrentPage - 1).getTyp();
			final int spm = FragQues.allQuests.get(mCurrentPage - 1).getSpm();	
			Log.w("The Qno is",""+FragQues.allQuests.get(mCurrentPage - 1).getQno());
			int qno=FragQues.allQuests.get(mCurrentPage - 1).getQno();
			Log.w("The spm is","Qno"+ FragQues.allQuests.get(mCurrentPage - 1).getSpm()+":"+spm);
			switch (t) {
			case 1:
				for (int j = 0; j < FragQues.allYn.size(); j++) {
					{
						if (FragQues.allYn.get(j).getQno() == qno)
						{	Log.w("Setting which",""+which);	
							which = j;						
						break;
						}
					}
				}
				Log.w("Which=",""+which);
				rg = new RadioGroup(getActivity());
				RadioButton r1 = new RadioButton(getActivity());
				r1.setText("Yes");
				r1.setId(1);
				if (FragQues.allYn.get(which).getRes() == 1)
					r1.setChecked(true);
				rg.addView(r1, 0);
				r1 = new RadioButton(getActivity());
				r1.setText("No");
				r1.setId(2);
				if (FragQues.allYn.get(which).getRes() == 2)
					r1.setChecked(true);
				rg.addView(r1, 1);
				r.addView(rg);
Log.w("Setting Response",""+FragQues.allYn.get(which).getRes());
				rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						FragQues.allYn.get(which).setRes(checkedId);
						MainActivity.db.updateResponse_yn(FragQues.allYn
								.get(which));
						Log.w("Added response YN","Qno:"+FragQues.allYn.get(which).getQno()+" which:"+which+" res:"+FragQues.allYn.get(which).getRes());
					}
				});
				break;

			case 2:
				rg1 = new RadioGroup(getActivity());
				for (int j = 0; j < FragQues.allOpRad.size(); j++) {
					if (FragQues.allOpRad.get(j).getQno() ==qno) {
						which = j;
						String s[] = FragQues.allOpRad.get(j).getOp();
						for (int i = 0; i < s.length; i++) {
							RadioButton r2 = new RadioButton(getActivity());
							r2.setId(i + 1);
							r2.setText(s[i]);
							if (FragQues.allOpRad.get(which).getRes() == (i + 1))
								r2.setChecked(true);
							rg1.addView(r2, i);
						}
						r.addView(rg1);
						break;
					}
				}
				rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						FragQues.allOpRad.get(which).setRes(checkedId);
						Log.w("Spam Detection Check!",""+checkedId+","+spm);
						if(checkedId==spm)
						{
							FragQues.allQuests.get(mCurrentPage - 1).setSpmd(1);	
							Log.w("Spam Detected!",""+checkedId+"="+spm);
						}
						else
						{
							FragQues.allQuests.get(mCurrentPage - 1).setSpmd(0);							
						}
						MainActivity.db.updateOptions_Radio(FragQues.allOpRad
								.get(which));
						
						Log.w("Added response Radio ",""+FragQues.allOpRad.get(which).getRes());
						 MainActivity.db.updateQuestions(FragQues.allQuests
								  .get(mCurrentPage - 1));
					}
				});
				break;

			case 3:
				tl = (TableLayout) v.findViewById(R.id.tableLayout1);
				String s[] = null;
				CheckBox cbx[] = null;
				for (int j = 0; j < FragQues.allOpChB.size(); j++) {
					if (FragQues.allOpChB.get(j).getQno() == (mCurrentPage)) {
						which = j;
						s = FragQues.allOpChB.get(j).getOp();

						res = FragQues.allOpChB.get(j).getRes();

						cbx = new CheckBox[s.length];
						for (i = 0; i < s.length; i++) {
							TableRow tr = new TableRow(getActivity());
							cbx[i] = new CheckBox(getActivity());
							cbx[i].setId(i);
							cbx[i].setText(s[i]);
							if (res[i] == 1) {
								cbx[i].setChecked(true);							
							}
							tr.addView(cbx[i]);
							tl.addView(tr);
						}

						break;
					}
				}

				if (s != null) {
					for (i = 0; i < s.length; i++) {
						cbx[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								Log.w("Spam Detection Check!",""+buttonView.getId()+","+spm);
								FragQues.allQuests.get(mCurrentPage - 1).setSpmd(0);
								if (isChecked) {
									res[buttonView.getId()] = 1;

								} else
									res[buttonView.getId()] = 0;
								Log.w("Checkbox Response Now", ""+Arrays.toString(res));
								if(buttonView.getId()==(spm-1)&&isChecked)
								{
									FragQues.allQuests.get(mCurrentPage - 1).setSpmd(1);
									Log.w("Spam Detected",buttonView.getId()+"="+spm);
								}
								if(buttonView.getId()==(spm-1)&&!isChecked)
								{
									FragQues.allQuests.get(mCurrentPage - 1).setSpmd(0);
									Log.w("Spam Resolved",buttonView.getId()+"="+spm);
								}
								MainActivity.db.updateQuestions(FragQues.allQuests
										  .get(mCurrentPage - 1));
								FragQues.allOpChB.get(which).setRes(res);
								MainActivity.db
										.updateOptions_Checkbox(FragQues.allOpChB
												.get(which));
								Log.w("Added Response OpChB"," "+FragQues.allOpChB.get(which).getRes());
								/*
								 * FragQues.allQuests.get(mCurrentPage - 1)
								 * .setG(0); int k; for (k = 0; k < res.length;
								 * k++) {
								 * 
								 * if (res[k] != 0) { FragQues.allQuests
								 * .get(mCurrentPage - 1).setG(1); break; } }
								 * 
								 * if (k == res.length)
								 * FragQues.allQuests.get(mCurrentPage - 1)
								 * .setG(0); MainActivity.db
								 * .updateQuestions(FragQues.allQuests
								 * .get(mCurrentPage - 1)); changeStatus();
								 */

							}
						});

					}
				}

				break;
			case 4:
				for (int j = 0; j < FragQues.allRat.size(); j++) {
					if (FragQues.allRat.get(j).getQno() == (mCurrentPage)) {
						which = j;
						rb = new RatingBar(getActivity());
						rb.setNumStars(5);
						rb.setRating(FragQues.allRat.get(which).getRes());
						rb.setStepSize((float) 1.0);
						rb.setId(j);
						r.addView(rb);
						break;
					}

				}

				rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar, float res,
							boolean fromUser) {

						FragQues.allRat.get(which).setRes((int) res);
						MainActivity.db.updateResponse_Rating(FragQues.allRat
								.get(which));
						Log.w("Added Response Rating",""+FragQues.allRat
								.get(which).getRes());
						/*
						 * if (FragQues.allQuests.get(mCurrentPage - 1).getG()
						 * == 0) { FragQues.allQuests.get(mCurrentPage -
						 * 1).setG(1);
						 * MainActivity.db.updateQuestions(FragQues.allQuests
						 * .get(mCurrentPage - 1)); changeStatus(); }
						 */
					}
				});
				break;
			case 5:

				for (int j = 0; j < FragQues.allFb.size(); j++) {
					if (FragQues.allFb.get(j).getQno() == (mCurrentPage)) {
						which = j;
						et = new EditText(getActivity());
						// et.setLayoutParams(new LinearLayout.LayoutParams(
						// GlobVars.screenwidth,100 ,
						// 1f));
						et.setMinWidth(GlobVars.screenwidth);
						et.setMaxWidth(GlobVars.screenheight);
						et.setLines(8);

						InputFilter[] FilterArray = new InputFilter[2];
						FilterArray[0] = new InputFilter.LengthFilter(25);

						FilterArray[1] = new InputFilter() {

							@Override
							public CharSequence filter(CharSequence source,
									int start, int end, Spanned dest,
									int dstart, int dend) {

								for (int i = start; i < end; i++) {
									if (!Character.isLetterOrDigit(source
											.charAt(i))
											&& source.charAt(i) != '.'
											&& source.charAt(i) != ','
											&& source.charAt(i) != ' ') {
										return "";
									}
								}
								return null;
							}
						};

						et.setFilters(FilterArray);
						et.setLayoutParams(new TableRow.LayoutParams(200, 150));
						et.setHint("Enter Your Response Here");
						et.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_FLAG_MULTI_LINE
								| InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
						et.setSingleLine(false);
						et.setHorizontalScrollBarEnabled(false);
						et.setGravity(Gravity.TOP | Gravity.LEFT);
						if (!"".equals(FragQues.allFb.get(j).getRes()))
							et.setText(FragQues.allFb.get(j).getRes());
						et.setVerticalScrollBarEnabled(true);
						et.setImeOptions(EditorInfo.IME_ACTION_DONE);
						tl = (TableLayout) v.findViewById(R.id.tableLayout1);
						tl.setShrinkAllColumns(true);
						r.addView(et);
						break;
					}
				}

				et.addTextChangedListener(new TextWatcher() {
					@Override
					public void afterTextChanged(Editable s) {
						FragQues.allQuests.get(mCurrentPage - 1).setG(0);
						FragQues.allFb.get(which).setRes(s.toString().trim());
						MainActivity.db.updateResponse_FeedBack(FragQues.allFb
								.get(which));
						Log.w("Added Response Fedback","Res:"+FragQues.allFb
								.get(which).getRes());
						/*
						 * if (!s.toString().trim().equals("")) {
						 * 
						 * FragQues.allQuests.get(mCurrentPage - 1).setG(1);
						 * 
						 * } else { FragQues.allQuests.get(mCurrentPage -
						 * 1).setG(0);
						 * 
						 * } MainActivity.db.updateQuestions(FragQues.allQuests
						 * .get(mCurrentPage - 1)); changeStatus();
						 */
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}
				});

				break;
			case 6:
				l = 0;
				et1 = new EditText(getActivity());
				et1.setVisibility(View.INVISIBLE);
				String txt = "";
				rg1 = new RadioGroup(getActivity());
				RadioButton r2 = null;
				for (int j = 0; j < FragQues.allOpRadC.size(); j++) {
					if (FragQues.allOpRadC.get(j).getQno() == (mCurrentPage)) {
						which = j;
						String sx[] = FragQues.allOpRadC.get(j).getOp();
						l = sx.length;
						for (int i = 0; i <= sx.length; i++) {
							r2 = new RadioButton(getActivity());
							r2.setId(i + 1);
							if (i == sx.length) {
								r2.setText("Other");
							} else {
								r2.setText(sx[i]);
							}
							if (FragQues.allOpRadC.get(which).getRes() == (i + 1)) {
								r2.setChecked(true);
								if (FragQues.allOpRadC.get(which).getRes() == (l + 1)) {
									// et1.setEnabled(true);
									et1.setVisibility(View.VISIBLE);
									for (int k = 0; k < FragQues.allOther
											.size(); k++) {
										if (FragQues.allOther.get(k).getQno() == (mCurrentPage)) {
											txt = FragQues.allOther.get(k)
													.getAns();
											which1 = k;
											break;
										}
									}

								}
							}

							rg1.addView(r2, i);

						}
						r.addView(rg1);
						break;
					}
				}

				InputFilter[] FilterArray = new InputFilter[2];
				FilterArray[0] = new InputFilter.LengthFilter(20);

				FilterArray[1] = new InputFilter() {

					@Override
					public CharSequence filter(CharSequence source, int start,
							int end, Spanned dest, int dstart, int dend) {

						for (int i = start; i < end; i++) {
							if (!Character.isLetterOrDigit(source.charAt(i))) {
								return "";
							}
						}
						return null;
					}
				};

				et1.setFilters(FilterArray);
				et1.setInputType(InputType.TYPE_CLASS_TEXT);
				et1.setSingleLine(true);
				et1.setHorizontalScrollBarEnabled(false);
				et1.setGravity(Gravity.TOP | Gravity.LEFT);
				if ("".equals(txt)) {
					et1.setHint("Please Specify");
				} else {
					et1.setText(txt);
				}
				et1.addTextChangedListener(new TextWatcher() {
					@Override
					public void afterTextChanged(Editable s) {
						FragQues.allQuests.get(mCurrentPage - 1).setG(0);
						FragQues.allOther.get(which1).setAns(
								(s.toString().trim()));
						MainActivity.db.updateOtherans(FragQues.allOther
								.get(which1));
						Log.w("Added Other", "Other:" + s);
						/*
						 * if (!"".trim().equals(s.toString())) {
						 * 
						 * FragQues.allQuests .get(mCurrentPage - 1).setG(1);
						 * 
						 * } else { FragQues.allQuests .get(mCurrentPage -
						 * 1).setG(0);
						 * 
						 * } changeStatus();
						 */
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}
				});
				// et1.setLayoutParams(new LayoutParams(180, 28));
				r.addView(et1);

				rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						FragQues.allOpRadC.get(which).setRes(checkedId);
						
						if (checkedId == (l + 1)) {
							FragQues.allQuests.get(mCurrentPage - 1).setG(0);
							// et1.setEnabled(true);
							et1.setHint("Please Specify");
							et1.setVisibility(View.VISIBLE);

						} else {
							FragQues.allQuests.get(mCurrentPage - 1).setG(1);
							et1.setText("");
							FragQues.allOther.get(which1).setAns("");
							MainActivity.db.updateOtherans(FragQues.allOther
									.get(which1));
							// et1.setEnabled(false);
							et1.setVisibility(View.INVISIBLE);
						}
						FragQues.allOpRadC.get(which).setRes(checkedId);
						Log.w("Spam Detection Check ",checkedId+","+spm);
						if(checkedId==spm)
						{
							FragQues.allQuests.get(mCurrentPage - 1).setSpmd(1);
							Log.w("Spam Detected",checkedId+"="+spm);
						}
						else
						{
							FragQues.allQuests.get(mCurrentPage - 1).setSpmd(0);
						}
						MainActivity.db.updateQuestions(FragQues.allQuests
								  .get(mCurrentPage - 1));
						Log.w("Added OpRadC", "" + checkedId);
						MainActivity.db.updateOptions_RadioC(FragQues.allOpRadC
								.get(which));
						/*
						 * MainActivity.db.updateQuestions(FragQues.allQuests
						 * .get(mCurrentPage - 1)); changeStatus();
						 */
					}
				});

				break;
			case 7:
				/*
				 * FragQues.allQuests.get(mCurrentPage - 1).setG(1);
				 * MainActivity.db.updateQuestions(FragQues.allQuests
				 * .get(mCurrentPage - 1));
				 */
				// changeStatus();
				dp = new DatePicker(getActivity());
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= 11) {
					try {
						Method m = dp.getClass().getMethod(
								"setCalendarViewShown", boolean.class);
						m.invoke(dp, false);
					} catch (Exception e) {
					} // eat exception in our case
				}
				for (int j = 0; j < FragQues.allDts.size(); j++) {
					if (FragQues.allDts.get(j).getQno() == (mCurrentPage)) {
						which = j;
					}
				}
				r.addView(dp);

				dp.init(maxYear, maxMonth, maxDay, new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (year < minYear) {
							view.updateDate(minYear, minMonth, minDay);
							year = minYear;
							monthOfYear = minMonth;
							dayOfMonth = minDay;

						}
						if (monthOfYear < minMonth && year == minYear) {
							view.updateDate(minYear, minMonth, minDay);
							year = minYear;
							monthOfYear = minMonth;
							dayOfMonth = minDay;
						}

						if (dayOfMonth < minDay && year == minYear
								&& monthOfYear == minMonth) {
							view.updateDate(minYear, minMonth, minDay);
							year = minYear;
							monthOfYear = minMonth;
							dayOfMonth = minDay;
						}

						if (year > maxYear) {
							view.updateDate(maxYear, maxMonth, maxDay);
							year = maxYear;
							monthOfYear = maxMonth;
							dayOfMonth = maxDay;
						}

						if (monthOfYear > maxMonth && year == maxYear) {
							view.updateDate(maxYear, maxMonth, maxDay);
							year = maxYear;
							monthOfYear = maxMonth;
							dayOfMonth = maxDay;
						}

						if (dayOfMonth > maxDay && year == maxYear
								&& monthOfYear == maxMonth) {
							view.updateDate(maxYear, maxMonth, maxDay);
							year = maxYear;
							monthOfYear = maxMonth;
							dayOfMonth = maxDay;

						}
						String dt = "" + year + "," + monthOfYear + ","
								+ dayOfMonth;
					
						FragQues.allDts.get(which).setAns(dt);
						int c = MainActivity.db.updateDateans(FragQues.allDts
								.get(which));
						Log.w("Added Date", FragQues.allDts.get(which)
								.getAns());

					}
				});

				if (!"".equals(FragQues.allDts.get(which).getAns())) {
					String a[] = FragQues.allDts.get(which).getAns().split(",");
					Log.w("Fetched", "" + Arrays.toString(a));
					dp.updateDate(Integer.parseInt(a[0]),
							Integer.parseInt(a[1]), Integer.parseInt(a[2]));

				} else {
					final Calendar c = Calendar.getInstance();
					int mYear = c.get(Calendar.YEAR);
					int mMonth = c.get(Calendar.MONTH);
					int mDay = c.get(Calendar.DAY_OF_MONTH);
					dp.updateDate(mYear, mMonth, mDay);
					String dt = "" + mYear + "," + mMonth + "," + mDay;
					FragQues.allDts.get(which).setAns(dt);
					MainActivity.db.updateDateans(FragQues.allDts.get(which));
				}
				break;

			default:
				break;
			}
		}

		else {
			v = inflater.inflate(R.layout.lastp, container, false);
			TextView tv1 = (TextView) v.findViewById(R.id.cn1);
			TextView tv2 = (TextView) v.findViewById(R.id.si1);
			tv1.setText(MainActivity.curr.getCompNm());
			tv2.setText(MainActivity.curr.getId());
			Button b = (Button) v.findViewById(R.id.submitAll);

			CheckBox cb = (CheckBox) v.findViewById(R.id.decl);
			cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					Button b = (Button) v.findViewById(R.id.submitAll);
					if (arg1) {
						if (b != null)
							b.setEnabled(true);
					} else {
						if (b != null)
							b.setEnabled(false);
					}

				}
			});

			b.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(v.getContext())
							.setTitle("Submit Survey")
							.setMessage(
									"Are You Sure You Want to submit the Survey?")
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Log.w("Stat 3", "yess");

											Log.w("Conven", MainActivity.curr
													.getConven());
											String conven[] = (MainActivity.curr
													.getConven()).split("~#%&");
											String ques[] = conven[0]
													.split(",");
											String types[] = conven[1]
													.split(",");
											Log.w("Conven",
													Arrays.toString(ques)
															+ " "
															+ Arrays.toString(types));
											String incomp = "";
											for (int i = 0; i < ques.length; i++) {
												switch (Integer
														.parseInt(types[i])) {
												case 1:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	Integer.parseInt(types[i])))
														incomp = incomp + ","
																+ ques[i];
													break;
												case 2:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	Integer.parseInt(types[i])))
														incomp = incomp + ","
																+ ques[i];
													break;
												case 3:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	Integer.parseInt(types[i])))
														incomp = incomp + ","
																+ ques[i];
													break;
												case 4:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	Integer.parseInt(types[i])))
														incomp = incomp + ","
																+ ques[i];
													break;
												case 5:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	Integer.parseInt(types[i])))
														incomp = incomp + ","
																+ ques[i];
													break;
												case 6:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	(Integer.parseInt(types[i]))))
														incomp = incomp + ","
																+ ques[i];
													break;
												case 7:
													if (!MainActivity.db
															.answered(
																	MainActivity.curr
																			.getId(),
																	Integer.parseInt(ques[i]),
																	(Integer.parseInt(types[i]))))
														incomp = incomp + ","
																+ ques[i];
													break;

												}
											}
											if (incomp.length() > 0) {
												incomp = incomp.substring(1);
											}
											if ("".equals(incomp)) {
												
												String spmd="";
												for(int m=0;m<FragQues.allQuests.size();m++)
												{
													if(FragQues.allQuests.get(m).getSpmd()==1)
														spmd=spmd+","+(m+1);
												}
												
												if(!"".equals(spmd))
												{
												spmd=spmd.substring(1);
												}
												Log.w("Spam Detected in ",""+spmd);
												if(!"".equals(spmd))
												{
													final Dialog dialg = new Dialog(
															MainActivity.champu);
													((Dialog) dialg)
															.setContentView(R.layout.speclsurv);
													dialg.findViewById(R.id.rel1)
															.setVisibility(
																	View.INVISIBLE);
													dialg.findViewById(R.id.rel2)
															.setVisibility(
																	View.VISIBLE);
													TextView et = (TextView) dialg
															.findViewById(R.id.ack1);
													String text = "You seem to be Spamming! "															
															+ "\nPlease check your responses to the following questions:\n"+spmd;
													et.setText(text);
													Button dialogButton = (Button) dialg
															.findViewById(R.id.ok);
													// if button is clicked, close
													// the custom dialog
													dialogButton
															.setOnClickListener(new OnClickListener() {
																@Override
																public void onClick(
																		View v) {
																	dialg.dismiss();
																}
															});
													dialg.show();
												}
												else
												{
												MainActivity.curr.setSts(3);
												Log.w("Get Satus",
														""
																+ MainActivity.curr
																		.getSts());
												MainActivity.db
														.updatetilesTable(MainActivity.curr);
												MainActivity.fm.popBackStack();
												Toast.makeText(
														MainActivity.champu,
														"Responses Saved",
														Toast.LENGTH_LONG)
														.show();
												boolean bl = isOnline();
												if (bl) {
													Log.w("Attempting Upload",
															"gh");
													Intent in = new Intent(
															MainActivity.champu,
															SometimesServiceU.class);
													MainActivity.champu
															.startService(in);
												} else {
													Log.w("else mein ",
															"else mein");
												}
												}
											} else {
												final Dialog dialg = new Dialog(
														MainActivity.champu);
												((Dialog) dialg)
														.setContentView(R.layout.speclsurv);
												dialg.findViewById(R.id.rel1)
														.setVisibility(
																View.INVISIBLE);
												dialg.findViewById(R.id.rel2)
														.setVisibility(
																View.VISIBLE);
												TextView et = (TextView) dialg
														.findViewById(R.id.ack1);
												String text = "The following questions have not been filled: \n"
														+ incomp
														+ "\nSurvey can be submitted once all non-optional questions have been filled.";
												et.setText(text);
												Button dialogButton = (Button) dialg
														.findViewById(R.id.ok);
												// if button is clicked, close
												// the custom dialog
												dialogButton
														.setOnClickListener(new OnClickListener() {
															@Override
															public void onClick(
																	View v) {
																dialg.dismiss();
															}
														});
												dialg.show();
											}
										}
									}).setNegativeButton("No", null).show();
				}
			});

		}

		return v;
	}

	/*
	 * static int changeStatus() {
	 * 
	 * int k; for (k = 0; k < FragQues.allQuests.size(); k++) { Log.w("G??? ", k
	 * + "  " + FragQues.allQuests.get(k).getG()); if
	 * (FragQues.allQuests.get(k).getIsop() == 0 &&
	 * FragQues.allQuests.get(k).getG() == 0) {
	 * 
	 * MainActivity.curr.setSts(1); break; } } if (k ==
	 * FragQues.allQuests.size()) MainActivity.curr.setSts(2); Log.w("" +
	 * MainActivity.curr.getSts(), "Status Now");
	 * MainActivity.db.updatetilesTable(MainActivity.curr); try { Button b =
	 * (Button) v.findViewById(R.id.submitAll); if (MainActivity.curr.getSts()
	 * == 2) { b.setEnabled(true); Log.w("Status en", "hhehe"); } else {
	 * b.setEnabled(false); } } catch (NullPointerException e) {
	 * Log.w("null Pointer", "aaya"); } return MainActivity.curr.getSts();
	 * 
	 * }
	 */

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) MainActivity.champu
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

}