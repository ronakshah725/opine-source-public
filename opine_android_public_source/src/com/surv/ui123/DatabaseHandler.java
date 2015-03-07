package com.surv.ui123;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	private static int srchCnt = 0;
	private static int srchCntV = 0;
	// Database Name
	private static final String DATABASE_NAME = "survManager";

	// Contacts table name
	private static final String TABLE_NAME = "tilesInfo";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CONTACTS_TABLE = "CREATE TABLE tilesInfo ('_id' TEXT PRIMARY KEY,'_compNm' TEXT, '_nodl' INTEGER, '_pts' INTEGER, '_sts' INTEGER, '_no' INTEGER,'_conven' TEXT)";
		String CREATE_VOUCHERS = "CREATE TABLE Vouchers('_id' TEXT, '_vcrnm' TEXT, '_desc' TEXT, '_cost' INTEGER, '_chk' INTEGER)";
		String CREATE_VIRTUAL_TABLE = "CREATE VIRTUAL TABLE vt USING fts3('_id', 'cn1', 'ns1', 'pt1','sts1','no1','conven1')";
		String CREATE_QUESTION_TABLE = "CREATE TABLE Questions ('_id' TEXT, '_qno' INTEGER, '_quest' TEXT, '_typ' INTEGER, '_isOp' INTEGER,'_g' INTEGER,'_spm' INTEGER, '_spmd' INTEGER)";
		String CREATE_RESPONSE_YN = "CREATE TABLE Response_yn('_id' TEXT, '_qno' INTEGER, '_res' INTEGER)";
		String CREATE_OPTIONS_RADIO = "CREATE TABLE Options_Radio('_id' TEXT,'_qno' INTEGER, '_op' TEXT, '_res' INTEGER)";
		String CREATE_OPTIONS_RADIOC = "CREATE TABLE Options_RadioC('_id' TEXT,'_qno' INTEGER, '_op' TEXT, '_res' INTEGER)";
		String CREATE_OPTIONS_CHECKBOX = "CREATE TABLE Options_Checkbox('_id' TEXT, '_qno' INTEGER, '_op' TEXT, '_res' TEXT)";
		String CREATE_RESPONSE_RATING = "CREATE TABLE Response_Rating('_id' TEXT, '_qno' INTEGER, '_res' INTEGER)";
		String CREATE_RESPONSE_FEEDBACK = "CREATE TABLE Response_FeedBack('_id' TEXT, '_qno' INTEGER, '_res' TEXT)";
		String CREATE_REDEEMED_VCR = "CREATE TABLE Redeem('vcr' TEXT, 'ts' TEXT)";
		String CREATE_OTHERANS = "CREATE TABLE Otherans('_id' TEXT, '_qno' INTEGER, '_ans' TEXT)";
		String CREATE_DATEANS = "CREATE TABLE Dateans('_id' TEXT, '_qno' INTEGER, '_ans' TEXT)";
		String CREATE_RECHARGE_REQ = "CREATE TABLE Recharge('amt' INTEGER, 'serv' TEXT, 'phno' TEXT, 'ts' TEXT)";
		String CREATE_LOGDATA = "CREATE TABLE LogData('logd' TEXT, 'dt' TEXT)";
		String CREATE_POLLCATS = "CREATE TABLE pollCats('id' TEXT, 'catnm' TEXT, 'nrpl' INTEGER )";
		String CREATE_SaveDemo = "CREATE TABLE savedemo('zip' TEXT, 'mar' TEXT, 'edu' TEXT, 'occ' TEXT, 'sal' TEXT )";
		String CREATE_CURRPOLLS = "CREATE TABLE pollsWithin('id' TEXT, 'qid' TEXT, 'ques' TEXT, 'nrpl' INTEGER, 'ops' TEXT, 'res' TEXT, 'icreated' INTEGER)";
		try {
			db.execSQL(CREATE_CONTACTS_TABLE);
			db.execSQL(CREATE_VIRTUAL_TABLE);
			db.execSQL(CREATE_QUESTION_TABLE);
			db.execSQL(CREATE_RESPONSE_YN);
			db.execSQL(CREATE_OPTIONS_RADIO);
			db.execSQL(CREATE_OPTIONS_RADIOC);
			db.execSQL(CREATE_OPTIONS_CHECKBOX);
			db.execSQL(CREATE_RESPONSE_RATING);
			db.execSQL(CREATE_RESPONSE_FEEDBACK);
			db.execSQL(CREATE_VOUCHERS);
			db.execSQL(CREATE_REDEEMED_VCR);
			db.execSQL(CREATE_RECHARGE_REQ);
			db.execSQL(CREATE_OTHERANS);
			db.execSQL(CREATE_DATEANS);
			db.execSQL(CREATE_LOGDATA);
			db.execSQL(CREATE_POLLCATS);
			db.execSQL(CREATE_CURRPOLLS);
			db.execSQL(CREATE_SaveDemo);
		} catch (Exception e) {
			Log.w("Exc", "" + e);
		}

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS vt");

		// Create tables again
		onCreate(db);

	}

	void addVrcR(String vcr, String ts) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Redeem WHERE vcr='" + vcr
				+ "' AND ts='" + ts + "'";

		Cursor cursor = MainActivity.sdb.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("vcr", vcr);
			values.put("ts", ts);
			Log.w("Inserting into redeem", vcr + " " + ts);
			MainActivity.sdb.insert("Redeem", null, values);
		}

		cursor.close();
		// db.close(); // Closing database connection
	}

	int getVcrRcnt() {
		String sql = "SELECT * FROM Redeem";
		Cursor cursor = SometimesServiceV.sld.rawQuery(sql, null);
		int cnt = cursor.getCount();
		return cnt;
	}

	String[] getAllSurvIds() {

		String selectQuery = "SELECT _id FROM tilesInfo";

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		String ids[] = new String[cursor.getCount()];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				ids[i] = "";
				ids[i] = cursor.getString(0);
				i++;
			} while (cursor.moveToNext());
		}
		cursor.close();
		return ids;
	}

	String[] getAllVoucIds() {

		String selectQuery = "SELECT _id FROM Vouchers";

		Cursor cursor = SometimesServiceDV.sld.rawQuery(selectQuery, null);
		String ids[] = new String[cursor.getCount()];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				ids[i] = "";
				ids[i] = cursor.getString(0);
				i++;
			} while (cursor.moveToNext());
		}
		cursor.close();
		return ids;
	}

	int getVCost(String s) {
		String selectQuery = "SELECT _cost FROM vouchers WHERE _id='" + s + "'";

		Cursor cursor = MainActivity.sdb.rawQuery(selectQuery, null);
		int c = 0;
		if (cursor.getCount() == 0) {
			c = Integer.parseInt(cursor.getString(0));
		}

		cursor.close();
		return c;
	}

	public List<tempReq> getAllTempR() {
		List<tempReq> allTemp = new ArrayList<tempReq>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = SometimesServiceV.sld.query("Redeem", new String[] {
				"vcr", "ts" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				tempReq t = new tempReq();
				t.setTs(cursor.getString(1));
				t.setVcr(cursor.getString(0));
				allTemp.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allTemp;
	}

	void setsrchcnt(int c) {
		srchCnt = c;
	}

	void addRechReq(int amt, String serv, String phno, String ts) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Recharge WHERE amt=" + amt
				+ " AND serv='" + serv + "' AND phno='" + phno + "' AND ts='"
				+ ts + "'";

		Cursor cursor = MainActivity.sdb.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("amt", amt);
			values.put("serv", serv);
			values.put("phno", phno);
			values.put("ts", ts);
			MainActivity.sdb.insert("Recharge", null, values);
		}

		cursor.close();
		// db.close(); // Closing database connection
	}

	int getRchRcnt() {
		String sql = "SELECT * FROM Recharge";
		Cursor cursor = SometimesServiceR.sld.rawQuery(sql, null);
		int cnt = cursor.getCount();
		return cnt;
	}

	public int getCntPollswithin() {
		String sql = "SELECT * FROM pollsWithin";
		Cursor cursor = MainActivity.sdb.rawQuery(sql, null);
		int cnt = cursor.getCount();
		return cnt;
	}

	public List<RchReq> getRchTempR() {
		List<RchReq> allTemp = new ArrayList<RchReq>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Recharge", new String[] {
				"amt", "serv", "phno", "ts" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				RchReq t = new RchReq();
				t.setAmt(Integer.parseInt(cursor.getString(0)));
				t.setServ(cursor.getString(1));
				t.setPhno(cursor.getString(2));
				t.setTs(cursor.getString(3));
				allTemp.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allTemp;
	}

	// Adding new tile
	void addNewTile(tilesTable tile) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE _id = '"
				+ tile.getId() + "'";
		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", tile.getId());
			values.put("_compNm", tile.getCompNm()); // Contact Name
			values.put("_nodl", tile.getNodl()); // Contact Phone
			values.put("_pts", tile.getPts());
			values.put("_sts", tile.getSts());
			values.put("_no", tile.getNo());
			values.put("_conven", tile.getConven());
			ContentValues values1 = new ContentValues();
			values1.put("_id", tile.getId());
			values1.put("cn1", tile.getCompNm()); // Contact Name
			values1.put("ns1", tile.getNodl()); // Contact Phone
			values1.put("pt1", tile.getPts());
			values1.put("sts1", tile.getSts());
			values1.put("no1", tile.getNo());
			values1.put("conven1", tile.getConven());
			GCMIntentService.sld.insert("vt", null, values1);
			GCMIntentService.sld.insert(TABLE_NAME, null, values);

		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addTile(tilesTable tile) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE _id = '"
				+ tile.getId() + "'";
		Cursor cursor = null;

		cursor = SometimesService.sld.rawQuery(selectQuery, null);

		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", tile.getId());
			values.put("_compNm", tile.getCompNm()); // Contact Name
			values.put("_nodl", tile.getNodl()); // Contact Phone
			values.put("_pts", tile.getPts());
			values.put("_sts", tile.getSts());
			values.put("_no", tile.getNo());
			values.put("_conven", tile.getConven());

			ContentValues values1 = new ContentValues();
			values1.put("_id", tile.getId());
			values1.put("cn1", tile.getCompNm()); // Contact Name
			values1.put("ns1", tile.getNodl()); // Contact Phone
			values1.put("pt1", tile.getPts());
			values1.put("sts1", tile.getSts());
			values1.put("no1", tile.getNo());
			values1.put("conven1", tile.getConven());
			SometimesService.sld.insert("vt", null, values1);
			SometimesService.sld.insert(TABLE_NAME, null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	// Adding new Question
	void addQuestion(Questions q) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Questions WHERE _id = '"
				+ q.getId() + "' AND _qno = " + q.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", q.getId());
			values.put("_qno", q.getQno()); // Contact Name
			values.put("_quest", q.getQuest()); // Contact Phone
			values.put("_typ", q.getTyp());
			values.put("_isOp", q.getIsop());
			values.put("_g", q.getG());
			values.put("_spm", q.getSpm());
			values.put("_spmd", 0);
			SometimesService.sld.insert("Questions", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addPollCat(pollCats q) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM pollCats WHERE id = '" + q.getId()
				+ "' AND catnm = '" + q.getCats() + "' AND nrpl = '"
				+ q.getnrpl() + "'";

		Cursor cursor = MainActivity.sdb.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("id", q.getId());
			values.put("catnm", q.getCats()); // Contact Name
			values.put("nrpl", q.getnrpl()); // Contact Phone
			MainActivity.sdb.insert("pollCats", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addLog(logData q) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM LogData WHERE logd = '"
				+ q.getLogd() + "' AND dt = '" + q.getDt() + "'";

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("logd", q.getLogd());
			values.put("dt", q.getDt()); // Contact Name
			GCMIntentService.sld.insert("LogData", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addLogM(logData q) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM LogData WHERE logd = '"
				+ q.getLogd() + "' AND dt = '" + q.getDt() + "'";

		Cursor cursor = MainActivity.sdb.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("logd", q.getLogd());
			values.put("dt", q.getDt()); // Contact Name
			MainActivity.sdb.insert("LogData", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewQuestion(Questions q) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Questions WHERE _id = '"
				+ q.getId() + "' AND _qno = " + q.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", q.getId());
			values.put("_qno", q.getQno()); // Contact Name
			values.put("_quest", q.getQuest()); // Contact Phone
			values.put("_typ", q.getTyp());
			values.put("_isOp", q.getIsop());
			values.put("_g", q.getG());
			values.put("_spm", q.getSpm());
			values.put("_spmd", 0);
			GCMIntentService.sld.insert("Questions", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addResponse_yn(Response_yn r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Response_yn WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_res", r.getRes());
			SometimesService.sld.insert("Response_yn", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewResponse_yn(Response_yn r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Response_yn WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_res", r.getRes());
			GCMIntentService.sld.insert("Response_yn", null, values);
		}
		Log.w("New Response added",
				r.getId() + " " + r.getQno() + " " + r.getRes());
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addOptions_Radio(Options_Radio r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Options_Radio WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			StringBuilder builder = new StringBuilder();
			for (String s : r.getOp()) {
				builder.append(s + "~#%&");
			}
			values.put("_op", builder.toString());
			values.put("_res", r.getRes());
			SometimesService.sld.insert("Options_Radio", null, values);
			Log.w("New Options Radio added", r.getId() + " " + r.getQno() + " "
					+ builder.toString());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addOptions_RadioC(Options_RadioC r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Options_Radio WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			StringBuilder builder = new StringBuilder();
			for (String s : r.getOp()) {
				builder.append(s + "~#%&");
			}
			values.put("_op", builder.toString());
			values.put("_res", r.getRes());
			SometimesService.sld.insert("Options_RadioC", null, values);
			Log.w("New Options RadioC added", r.getId() + " " + r.getQno()
					+ " " + builder.toString());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addOtherans(Otherans r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Otherans WHERE _id = '" + r.getId()
				+ "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_ans", r.getAns());
			SometimesService.sld.insert("Otherans", null, values);
			Log.w("New Options RadioC added", r.getId() + " " + r.getQno()
					+ " " + r.getAns());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addDateans(Dateans r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Dateans WHERE _id = '" + r.getId()
				+ "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_ans", r.getAns());
			SometimesService.sld.insert("Dateans", null, values);
			Log.w("New Dateans added",
					r.getId() + " " + r.getQno() + " " + r.getAns());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewOptions_Radio(Options_Radio r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Options_Radio WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			StringBuilder builder = new StringBuilder();
			for (String s : r.getOp()) {
				builder.append(s + "~#%&");
			}
			values.put("_op", builder.toString());
			values.put("_res", r.getRes());
			GCMIntentService.sld.insert("Options_Radio", null, values);
			Log.w("New Options Radio added", r.getId() + " " + r.getQno() + " "
					+ builder.toString());
		}

		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewOptions_RadioC(Options_RadioC r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Options_RadioC WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			StringBuilder builder = new StringBuilder();
			for (String s : r.getOp()) {
				builder.append(s + "~#%&");
			}
			values.put("_op", builder.toString());
			values.put("_res", r.getRes());
			GCMIntentService.sld.insert("Options_RadioC", null, values);
			Log.w("New Options Radio added", r.getId() + " " + r.getQno() + " "
					+ builder.toString());
		}

		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewOtherans(Otherans r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Otherans WHERE _id = '" + r.getId()
				+ "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_ans", r.getAns());
			GCMIntentService.sld.insert("Otherans", null, values);
			Log.w("New Otherans",
					r.getId() + " " + r.getQno() + " " + r.getAns());
		}

		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewDateans(Dateans r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Dateans WHERE _id = '" + r.getId()
				+ "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_ans", r.getAns());
			GCMIntentService.sld.insert("Dateans", null, values);
			Log.w("New Dateans",
					r.getId() + " " + r.getQno() + " " + r.getAns());
		}

		cursor.close();
		// db.close(); // Closing database connection
	}

	boolean answered(String id, int qno, int typ) {
		Cursor cursor = null;
		boolean ret = true;
		switch (typ) {
		case 1:
			cursor = MainActivity.sdb.query("Response_yn",
					new String[] { "_res" }, "_id= '" + id + "' AND _qno="
							+ qno, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ Integer.parseInt(cursor.getString(0)));
					if (Integer.parseInt(cursor.getString(0)) == 0)
						ret = false;
				} while (cursor.moveToNext());
			}
			break;

		case 2:
			cursor = MainActivity.sdb.query("Options_Radio",
					new String[] { "_res" }, "_id= '" + id + "' AND _qno="
							+ qno, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ Integer.parseInt(cursor.getString(0)));
					if (Integer.parseInt(cursor.getString(0)) == 0)
						ret = false;
				} while (cursor.moveToNext());
			}
			break;

		case 3:
			cursor = MainActivity.sdb.query("Options_Checkbox",
					new String[] { "_res" }, "_id= '" + id + "' AND _qno="
							+ qno, null, null, null, null);
			String resa = "";
			if (cursor.moveToFirst()) {
				do {
					resa = cursor.getString(0);
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ cursor.getString(0));
					Log.w("Intially resa", resa);
					resa = resa.substring(1, resa.length() - 1);
					Log.w("then resa", resa);
					String reax[] = resa.split(",");
					int ar[] = new int[reax.length];
					if ((Arrays.toString(ar)).equals(cursor.getString(0)))
						ret = false;
				} while (cursor.moveToNext());
			}
			break;
		case 4:
			cursor = MainActivity.sdb.query("Response_Rating",
					new String[] { "_res" }, "_id= '" + id + "' AND _qno="
							+ qno, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ Integer.parseInt(cursor.getString(0)));
					if (Integer.parseInt(cursor.getString(0)) == 0)
						ret = false;
				} while (cursor.moveToNext());
			}
			break;
		case 5:
			cursor = MainActivity.sdb.query("Response_FeedBack",
					new String[] { "_res" }, "_id= '" + id + "' AND _qno="
							+ qno, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ cursor.getString(0));
					if ("".equals(cursor.getString(0)))
						ret = false;
				} while (cursor.moveToNext());
			}
			break;

		case 6:
			cursor = MainActivity.sdb.query("Options_RadioC", new String[] {
					"_res", "_op" }, "_id= '" + id + "' AND _qno=" + qno, null,
					null, null, null);
			Cursor cursor1 = MainActivity.sdb.query("Otherans",
					new String[] { "_ans" }, "_id= '" + id + "' AND _qno="
							+ qno, null, null, null, null);
			String ans = null;
			if (cursor1.moveToFirst()) {
				do {
					Log.w("Checking if all filled Other", qno + " " + typ + " "
							+ cursor1.getString(0));
					ans = cursor1.getString(0);
				} while (cursor1.moveToNext());
			}
			cursor1.close();
			Log.w("This is the other ans", "ans:" + ans);
			if (cursor.moveToFirst()) {
				do {
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ cursor.getString(0));
					String s[] = cursor.getString(1).split("~#%&");
					if (Integer.parseInt(cursor.getString(0)) == 0)
						ret = false;
					else if (Integer.parseInt(cursor.getString(0)) == (s.length + 1)) {
						if ("".equals(ans))
							ret = false;
					}
				} while (cursor.moveToNext());
			}
			break;
		case 7:
			cursor = MainActivity.sdb
					.query("Dateans", new String[] { "_ans" }, "_id= '" + id
							+ "' AND _qno=" + qno, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					Log.w("Checking if all filled", qno + " " + typ + " "
							+ cursor.getString(0));
					if ("".equals(cursor.getString(0)))
						ret = false;
				} while (cursor.moveToNext());
			}
			break;
		}

		cursor.close();
		return ret;
	}

	void addVoucher(Voucher r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Vouchers WHERE _id = '" + r.getId()
				+ "'";
		Cursor cursor = SometimesServiceDV.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_vcrnm", r.getVcrnm());
			values.put("_desc", r.getDesc());
			values.put("_cost", r.getCost());
			values.put("_chk", r.getChk());
			SometimesServiceDV.sld.insert("Vouchers", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewVoucher(Voucher r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Vouchers WHERE _id = '" + r.getId()
				+ "'";
		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_vcrnm", r.getVcrnm());
			values.put("_desc", r.getDesc());
			values.put("_cost", r.getCost());
			values.put("_chk", r.getChk());
			GCMIntentService.sld.insert("Vouchers", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addOptions_Checkbox(Options_Checkbox r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Options_Checkbox WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			StringBuilder builder = new StringBuilder();
			for (String s : r.getOp()) {
				builder.append(s + "~#%&");
			}
			values.put("_op", builder.toString());

			values.put("_res", Arrays.toString(r.getRes()));
			SometimesService.sld.insert("Options_Checkbox", null, values);
			Log.w("New Options Checkbox added", r.getId() + " " + r.getQno()
					+ " " + builder.toString());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addResponse_Rating(Response_Rating r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Response_Rating WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_res", r.getRes());
			SometimesService.sld.insert("Response_Rating", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewOptions_Checkbox(Options_Checkbox r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Options_Checkbox WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			StringBuilder builder = new StringBuilder();
			for (String s : r.getOp()) {
				builder.append(s + "~#%&");
			}
			values.put("_op", builder.toString());

			values.put("_res", Arrays.toString(r.getRes()));
			GCMIntentService.sld.insert("Options_Checkbox", null, values);
			Log.w("New Options Checkbox added", r.getId() + " " + r.getQno()
					+ " " + builder.toString());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewResponse_Rating(Response_Rating r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Response_Rating WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_res", r.getRes());
			GCMIntentService.sld.insert("Response_Rating", null, values);
			Log.w("New Options Rating",
					r.getId() + " " + r.getQno() + " " + r.getRes());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	tilesTable getTile(String _id) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM tilesInfo WHERE _id = '" + _id
				+ "'";

		Cursor cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);
		tilesTable c = new tilesTable();

		if (cursor.moveToFirst()) {
			do {

				c.setId(cursor.getString(0));
				c.setCompNm(cursor.getString(1));
				c.setNodl(Integer.parseInt(cursor.getString(2)));
				c.setPts(Integer.parseInt(cursor.getString(3)));
				c.setSts(Integer.parseInt(cursor.getString(4)));
				c.setNo(Integer.parseInt(cursor.getString(5)));
				c.setConven(cursor.getString(6));
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return c;
	}

	tilesTable getTilegcm(String _id) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM tilesInfo WHERE _id = '" + _id
				+ "'";

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		tilesTable c = new tilesTable();

		if (cursor.moveToFirst()) {
			do {

				c.setId(cursor.getString(0));
				c.setCompNm(cursor.getString(1));
				c.setNodl(Integer.parseInt(cursor.getString(2)));
				c.setPts(Integer.parseInt(cursor.getString(3)));
				c.setSts(Integer.parseInt(cursor.getString(4)));
				c.setNo(Integer.parseInt(cursor.getString(5)));
				c.setConven(cursor.getString(6));
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return c;
	}

	void addResponse_FeedBack(Response_FeedBack r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Response_FeedBack WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_res", r.getRes());
			SometimesService.sld.insert("Response_FeedBack", null, values);
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	void addNewResponse_FeedBack(Response_FeedBack r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM Response_FeedBack WHERE _id = '"
				+ r.getId() + "' AND _qno = " + r.getQno();

		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {

			ContentValues values = new ContentValues();
			values.put("_id", r.getId());
			values.put("_qno", r.getQno());
			values.put("_res", r.getRes());
			GCMIntentService.sld.insert("Response_FeedBack", null, values);
			Log.w("New Response Feedback", r.getId() + " " + r.getQno() + " "
					+ r.getRes());
		}
		cursor.close();
		// db.close(); // Closing database connection
	}

	public int updatetilesTable(tilesTable t) {
		// SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.w("Uodated tile", t.getId() + "   " + t.getNo());
		values.put("_id", t.getId());
		values.put("_compNm", t.getCompNm());
		values.put("_nodl", "" + t.getNodl());
		values.put("_pts", "" + t.getPts());
		values.put("_sts", "" + t.getSts());
		values.put("_no", "" + t.getNo());
		values.put("_conven", "" + t.getConven());
		ContentValues values1 = new ContentValues();

		values1.put("_id", t.getId());
		values1.put("cn1", t.getCompNm()); // Contact Name
		values1.put("ns1", t.getNodl()); // Contact Phone
		values1.put("pt1", t.getPts());
		values1.put("sts1", t.getSts());
		values1.put("no1", t.getNo());
		values1.put("conven1", t.getConven());
		int c = 0;
		try {
			MainActivity.sdb.update("tilesInfo", values, "_id=?",
					new String[] { t.getId() });
			c = MainActivity.sdb.update("vt", values1, "_id=?",
					new String[] { t.getId() });
		} catch (IllegalStateException e) {
			SometimesServiceU.sld.update("tilesInfo", values, "_id=?",
					new String[] { t.getId() });
			c = SometimesServiceU.sld.update("vt", values1, "_id=?",
					new String[] { t.getId() });
		}

		// db.close();
		return c;
	}

	public int updatetilesTablegcm(tilesTable t) {
		// SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.w("Uodated tile", t.getId() + "   " + t.getNo());
		int c = 0;
		if (t.getId() != null) {
			Log.w("Updating Nodl", "");
			values.put("_id", t.getId());
			values.put("_compNm", t.getCompNm());
			values.put("_nodl", "" + t.getNodl());
			values.put("_pts", "" + t.getPts());
			values.put("_sts", "" + t.getSts());
			values.put("_no", "" + t.getNo());
			values.put("_conven", "" + t.getConven());

			ContentValues values1 = new ContentValues();

			values1.put("_id", t.getId());
			values1.put("cn1", t.getCompNm()); // Contact Name
			values1.put("ns1", t.getNodl()); // Contact Phone
			values1.put("pt1", t.getPts());
			values1.put("sts1", t.getSts());
			values1.put("no1", t.getNo());
			values1.put("conven1", t.getConven());

			try {
				GCMIntentService.sld.update("tilesInfo", values, "_id=?",
						new String[] { t.getId() });
				c = GCMIntentService.sld.update("vt", values1, "_id=?",
						new String[] { t.getId() });
			} catch (IllegalStateException e) {
				GCMIntentService.sld.update("tilesInfo", values, "_id=?",
						new String[] { t.getId() });
				c = GCMIntentService.sld.update("vt", values1, "_id=?",
						new String[] { t.getId() });
			}
		}

		// db.close();
		return c;
	}

	public int updateQuestions(Questions q) {
		// SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_id", q.getId());
		values.put("_qno", q.getQno()); // Contact Name
		values.put("_quest", q.getQuest()); // Contact Phone
		values.put("_typ", q.getTyp());
		values.put("_isOp", q.getIsop());
		values.put("_g", q.getG());
		values.put("_spm", q.getSpm());
		values.put("_spmd", q.getSpmd());
		int c = MainActivity.sdb
				.update("Questions", values, "_id=? AND _qno=?", new String[] {
						q.getId(), "" + q.getQno() });
		// db.close();

		return c;
	}

	public int updateVouchers(Voucher r) {
		// SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_id", r.getId());
		values.put("_vcrnm", r.getVcrnm());
		values.put("_desc", r.getDesc());
		values.put("_cost", r.getCost());
		values.put("_chk", r.getChk());
		;

		int c = MainActivity.sdb.update("Vouchers", values, "_id=?",
				new String[] { r.getId() });
		// db.close();
		return c;
	}

	public int updateResponse_yn(Response_yn r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_id", r.getId());
		values.put("_qno", r.getQno());
		values.put("_res", r.getRes());
		int c = MainActivity.sdb
				.update("Response_yn", values, "_id=? AND _qno=?",
						new String[] { r.getId(), "" + r.getQno() });
		// db.close();
		Log.w("Add response", "Yn Response Added is: " + r.getRes());
		return c;
	}

	public int updateOptions_Radio(Options_Radio r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_res", r.getRes());
		int c = MainActivity.sdb
				.update("Options_Radio", values, "_id=? AND _qno=?",
						new String[] { r.getId(), "" + r.getQno() });
		// updating row
		// db.close();
		return c;
	}

	public int updateOptions_RadioC(Options_RadioC r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_res", r.getRes());
		int c = MainActivity.sdb
				.update("Options_RadioC", values, "_id=? AND _qno=?",
						new String[] { r.getId(), "" + r.getQno() });
		// updating row
		// db.close();
		return c;
	}

	public int updateOtherans(Otherans r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_ans", r.getAns());
		int c = MainActivity.sdb.update("Otherans", values, "_id=? AND _qno=?",
				new String[] { r.getId(), "" + r.getQno() });
		// updating row
		// db.close();
		return c;
	}

	public int updateDateans(Dateans r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_ans", r.getAns());
		int c = MainActivity.sdb.update("Dateans", values, "_id=? AND _qno=?",
				new String[] { r.getId(), "" + r.getQno() });
		// updating row
		// db.close();
		return c;
	}

	public int updateOptions_Checkbox(Options_Checkbox r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_res", Arrays.toString(r.getRes()));
		int c = MainActivity.sdb
				.update("Options_Checkbox", values, "_id=? AND _qno=?",
						new String[] { r.getId(), "" + r.getQno() });
		// updating row
		// db.close();
		return c;
	}

	public int updateResponse_Rating(Response_Rating r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_res", r.getRes());
		int c = MainActivity.sdb
				.update("Response_Rating", values, "_id=? AND _qno=?",
						new String[] { r.getId(), "" + r.getQno() });
		// db.close();
		// updating row
		return c;
	}

	public int updateResponse_FeedBack(Response_FeedBack r) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("_res", r.getRes());
		int c = MainActivity.sdb
				.update("Response_FeedBack", values, "_id=? AND _qno=?",
						new String[] { r.getId(), "" + r.getQno() });
		// db.close();
		// updating row
		return c;

	}

	// Getting single contact
	/*
	 * Contact getContact(int id) { SQLiteDatabase db =
	 * this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_NAME,
	 * KEY_PH_NO }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null,
	 * null, null, null); if (cursor != null) cursor.moveToFirst();
	 * 
	 * Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
	 * cursor.getString(1), cursor.getString(2)); // return contact return
	 * contact; }
	 */

	public List<tilesTable> getAllTiles(int op) {
		List<tilesTable> allTiles = new ArrayList<tilesTable>();
		// SQLiteDatabase db = this.getWritableDatabase();

		String or = "";
		if (op == 1) {
			or = "_id ASC";
		}

		if (op == 2) {
			or = "_compNm ASC";
		}

		if (op == 3) {
			or = "_nodl ASC";
		}

		if (op == 4) {
			or = "_pts DESC";
		}

		Cursor cursor = MainActivity.sdb.query(TABLE_NAME, new String[] {
				"_id", "_compNm", "_nodl", "_pts", "_sts", "_no", "_conven" },
				"_sts <> 3 AND _sts <> 4", null, null, null, or);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				tilesTable t = new tilesTable();
				t.setId(cursor.getString(0));
				t.setCompNm(cursor.getString(1));
				t.setNodl(Integer.parseInt(cursor.getString(2)));
				t.setPts(Integer.parseInt(cursor.getString(3)));
				t.setSts(Integer.parseInt(cursor.getString(4)));
				t.setNo(Integer.parseInt(cursor.getString(5)));
				t.setConven(cursor.getString(6));

				allTiles.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();

		Cursor cursor1 = MainActivity.sdb.query(TABLE_NAME, new String[] {
				"_id", "_compNm", "_nodl", "_pts", "_sts", "_no", "_conven" },
				"_sts = 3 OR _sts = 4", null, null, null, or);

		// looping through all rows and adding to list
		if (cursor1.moveToFirst()) {
			do {

				tilesTable t = new tilesTable();

				t.setId(cursor1.getString(0));
				t.setCompNm(cursor1.getString(1));
				t.setNodl(Integer.parseInt(cursor1.getString(2)));
				t.setPts(Integer.parseInt(cursor1.getString(3)));
				t.setSts(Integer.parseInt(cursor1.getString(4)));
				t.setNo(Integer.parseInt(cursor1.getString(5)));
				t.setConven(cursor1.getString(6));
				allTiles.add(t);
			} while (cursor1.moveToNext());
		}
		cursor1.close();
		// db.close();
		// return contact list

		return allTiles;
	}

	public List<String> getSubmittingTiles() {
		List<String> allTiles = new ArrayList<String>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = SometimesServiceU.sld.query(TABLE_NAME,
				new String[] { "_id" }, "_sts=?", new String[] { "3" }, null,
				null, null);

		if (cursor.moveToFirst()) {
			do {

				allTiles.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();

		return allTiles;
	}

	public List<QueTyp> getAllQus(String _id) {
		List<QueTyp> allTiles = new ArrayList<QueTyp>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = SometimesServiceU.sld.query("Questions", new String[] {
				"_qno", "_typ" }, "_id=?", new String[] { _id }, null, null,
				null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				QueTyp q = new QueTyp();
				q.setQno(Integer.parseInt(cursor.getString(0)));
				q.setTyp(Integer.parseInt(cursor.getString(1)));
				allTiles.add(q);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		// return contact list

		return allTiles;
	}

	public List<logData> getAllLogData() {
		List<logData> allTiles = new ArrayList<logData>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb
				.rawQuery("SELECT * FROM LogData", null);

		Log.w("Data base retrieved count", "" + cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				logData q = new logData();
				q.setLogd(cursor.getString(0));
				q.setDt(cursor.getString(1));
				allTiles.add(q);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		// return contact list

		return allTiles;
	}

	public List<Questions> getQuestions(String _id) {
		List<Questions> allQuests = new ArrayList<Questions>();
		// Select All Query

		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Questions",
				new String[] { "_id", "_qno", "_quest", "_typ", "_isOp", "_g",
						"_spm", "_spmd" }, "_id= '" + _id + "'", null, null,
				null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				Questions t = new Questions();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				t.setQuest(cursor.getString(2));
				t.setTyp(Integer.parseInt(cursor.getString(3)));
				t.setIsop(Integer.parseInt(cursor.getString(4)));
				t.setG(Integer.parseInt(cursor.getString(5).trim()));
				t.setSpm(Integer.parseInt(cursor.getString(6)));
				t.setSpmd(Integer.parseInt(cursor.getString(7)));
				allQuests.add(t);
			} while (cursor.moveToNext());
		}

		cursor.close();
		// db.close();
		// return contact list

		return allQuests;
	}

	public List<pollCats> getAllCats() {
		List<pollCats> allCats = new ArrayList<pollCats>();
		// Select All Query

		// SQLiteDatabase db = this.getWritableDatabase();
		String SQL = "SELECT * FROM pollCats";
		Cursor cursor = MainActivity.sdb.rawQuery(SQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				pollCats t = new pollCats();
				t.setId(cursor.getString(0));
				t.setCats(cursor.getString(1));
				t.setnrpl(Integer.parseInt(cursor.getString(2)));
				Log.w("Setting Poll Cate",
						cursor.getString(0) + " " + cursor.getString(1) + " "
								+ cursor.getString(2));
				allCats.add(t);
			} while (cursor.moveToNext());
		}

		cursor.close();
		// db.close();
		// return contact list

		return allCats;
	}

	public List<Voucher> getAllVouchers() {
		List<Voucher> allVouchers = new ArrayList<Voucher>();
		// Select All Query

		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Vouchers", new String[] {
				"_id", "_vcrnm", "_desc", "_cost", "_chk" }, null, null, null,
				null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				Voucher t = new Voucher();
				t.setId(cursor.getString(0));
				t.setVcrnm(cursor.getString(1));
				t.setDesc(cursor.getString(2));
				t.setCost(Integer.parseInt(cursor.getString(3)));
				t.setChk(Integer.parseInt(cursor.getString(4).trim()));
				allVouchers.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		// return contact list

		return allVouchers;
	}

	public List<Response_yn> getAllYn(String _id) {
		List<Response_yn> allYn = new ArrayList<Response_yn>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Response_yn", new String[] {
				"_id", "_qno", "_res" }, "_id= '" + _id + "'", null, null,
				null, null);

		if (cursor.moveToFirst()) {
			do {
				Response_yn t = new Response_yn();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				t.setRes(Integer.parseInt(cursor.getString(2)));
				allYn.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allYn;
	}

	public String getResponseS(String _id, int _qno, int _typ) {
		Log.w("Get Response", _id + " " + _qno + " " + _typ);
		// SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		String g = null;
		if (_typ == 3) {
			String selectQuery = "SELECT _res FROM Options_Checkbox WHERE _id = '"
					+ _id + "' AND _qno = " + _qno;

			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					g = cursor.getString(0);

				} while (cursor.moveToNext());
			}

		}

		if (_typ == 5) {
			String selectQuery = "SELECT _res FROM Response_FeedBack WHERE _id = '"
					+ _id + "' AND _qno = " + _qno;

			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					g = cursor.getString(0);

				} while (cursor.moveToNext());
			}

		}

		if (_typ == 7) {
			String selectQuery = "SELECT _ans FROM Dateans WHERE _id = '" + _id
					+ "' AND _qno = " + _qno;

			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					g = cursor.getString(0);

				} while (cursor.moveToNext());
			}

		}
		cursor.close();
		// db.close();
		return g;
	}

	public String getResponseO(String _id, int _qno) {
		Log.w("Get Response", _id + " " + _qno);
		// SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		String g = null;

		String selectQuery = "SELECT _ans FROM Otherans WHERE _id = '" + _id
				+ "' AND _qno = " + _qno;

		cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				g = cursor.getString(0);

			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return g;
	}

	public int getResponseI(String _id, int _qno, int _typ) // TODO Make switch
															// case
	{
		Log.w("Get Responese", _id + " " + _qno + " " + _typ);
		// SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		int g = 0;
		if (_typ == 1) {
			String selectQuery = "SELECT _res FROM Response_yn WHERE _id = '"
					+ _id + "' AND _qno = " + _qno;
			// String selectQuery = "SELECT _res FROM Response_yn WHERE _id = '"
			// + _id+"'";
			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					g = Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}

		}

		if (_typ == 2) {
			String selectQuery = "SELECT _res FROM Options_Radio WHERE _id = '"
					+ _id + "' AND _qno = " + _qno;

			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					g = Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}

		}

		if (_typ == 4) {
			String selectQuery = "SELECT _res FROM Response_Rating WHERE _id = '"
					+ _id + "' AND _qno = " + _qno;

			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					g = Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}
		}
		if (_typ == 6) {
			String selectQuery = "SELECT _res FROM Options_RadioC WHERE _id = '"
					+ _id + "' AND _qno = " + _qno;

			cursor = SometimesServiceU.sld.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					g = Integer.parseInt(cursor.getString(0));

				} while (cursor.moveToNext());
			}
		}

		cursor.close();
		// MainActivity.sdb.close();
		return g;
	}

	public List<Response_FeedBack> getAllFb(String _id) {
		List<Response_FeedBack> allFb = new ArrayList<Response_FeedBack>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Response_FeedBack",
				new String[] { "_id", "_qno", "_res" }, "_id= '" + _id + "'",
				null, null, null, null);

		if (cursor.moveToFirst()) {
			do {

				Response_FeedBack t = new Response_FeedBack();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				t.setRes(cursor.getString(2));
				allFb.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allFb;
	}

	public List<Options_Radio> getAllOpRad(String _id) {
		List<Options_Radio> allOpRad = new ArrayList<Options_Radio>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Options_Radio", new String[] {
				"_id", "_qno", "_op", "_res" }, "_id= '" + _id + "'", null,
				null, null, null);

		if (cursor.moveToFirst()) {
			do {

				Options_Radio t = new Options_Radio();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				String s[] = cursor.getString(2).split("~#%&");
				t.setOp(s);
				t.setRes(Integer.parseInt(cursor.getString(3)));
				allOpRad.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allOpRad;
	}

	public List<Options_RadioC> getAllOpRadC(String _id) {
		List<Options_RadioC> allOpRad = new ArrayList<Options_RadioC>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Options_RadioC", new String[] {
				"_id", "_qno", "_op", "_res" }, "_id= '" + _id + "'", null,
				null, null, null);

		if (cursor.moveToFirst()) {
			do {

				Options_RadioC t = new Options_RadioC();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				String s[] = cursor.getString(2).split("~#%&");
				t.setOp(s);
				t.setRes(Integer.parseInt(cursor.getString(3)));
				allOpRad.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allOpRad;
	}

	public List<Otherans> getAllOtherans(String _id) {
		List<Otherans> allOp = new ArrayList<Otherans>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Otherans", new String[] {
				"_id", "_qno", "_ans" }, "_id= '" + _id + "'", null, null,
				null, null);

		if (cursor.moveToFirst()) {
			do {

				Otherans t = new Otherans();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				t.setAns(cursor.getString(2));
				allOp.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allOp;
	}

	public List<Dateans> getAllDateans(String _id) {
		List<Dateans> allOp = new ArrayList<Dateans>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Dateans", new String[] { "_id",
				"_qno", "_ans" }, "_id= '" + _id + "'", null, null, null, null);

		if (cursor.moveToFirst()) {
			do {

				Dateans t = new Dateans();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				t.setAns(cursor.getString(2));
				allOp.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allOp;
	}

	public List<Options_Checkbox> getAllOpChB(String _id) {
		List<Options_Checkbox> allOpChB = new ArrayList<Options_Checkbox>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Options_Checkbox",
				new String[] { "_id", "_qno", "_op", "_res" }, "_id= '" + _id
						+ "'", null, null, null, null);

		if (cursor.moveToFirst()) {
			do {

				Options_Checkbox t = new Options_Checkbox();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				String s[] = cursor.getString(2).split("~#%&");
				Log.w("Sab kuch",
						"test" + cursor.getString(0) + " "
								+ cursor.getString(1) + " "
								+ cursor.getString(2) + " "
								+ cursor.getString(3) + " " + s.length);
				t.setOp(s);
				String s1[] = cursor.getString(3).split(",");
				s1[0] = "" + s1[0].charAt(1);
				s1[s1.length - 1] = ""
						+ (("" + s1[s1.length - 1]).trim()).charAt(0);
				int c[] = new int[s1.length];
				for (int i = 0; i < c.length; i++) {
					c[i] = Integer.parseInt(s1[i].trim());
				}
				t.setRes(c);

				allOpChB.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allOpChB;
	}

	public List<Response_Rating> getAllRat(String _id) {
		List<Response_Rating> allRat = new ArrayList<Response_Rating>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Response_Rating", new String[] {
				"_id", "_qno", "_res" }, "_id= '" + _id + "'", null, null,
				null, null);

		if (cursor.moveToFirst()) {
			do {

				Response_Rating t = new Response_Rating();
				t.setId(cursor.getString(0));
				t.setQno(Integer.parseInt(cursor.getString(1)));
				t.setRes(Integer.parseInt(cursor.getString(2)));

				allRat.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// db.close();
		return allRat;
	}

	/*
	 * // Updating single contact public int updateTile(tilesTable t) {
	 * SQLiteDatabase db = this.getWritableDatabase();
	 * 
	 * ContentValues values = new ContentValues(); values.put(KEY_compNm,
	 * t.getCompNm()); values.put(KEY_nodl, t.getNodl()); values.put(KEY_pts,
	 * t.getPts()); values.put(KEY_im, t.getIm());
	 * 
	 * values.put(KEY_id, t.getId());
	 * 
	 * // updating row return db.update(TABLE_NAME, values, KEY_id + " = ?", new
	 * String[] { String.valueOf(t.getId()) }); }
	 */

	// Deleting single contact

	public void deletetab(String tab, String _id) {
		// SQLiteDatabase db = this.getWritableDatabase();
		GCMIntentService.sld.delete(tab, "_id = ?", new String[] { _id });
		// db.close();
	}

	public int isetcnt() {
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = MainActivity.sdb.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		srchCnt = cnt;
		return cnt;
	}

	public int isetcntV() {
		String countQuery = "SELECT * FROM Vouchers";
		Cursor cursor = MainActivity.sdb.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		srchCntV = cnt;
		return cnt;
	}

	// Getting contacts Count
	public int getCnt() {

		return srchCnt;
	}

	public void getSearch(String q) {

		Log.w("Search ", "Called");
		// String countQuery =
		// "SELECT  * FROM "+TABLE_NAME+" WHERE _id LIKE '%"+q+"%'"+" OR _compNm LIKE '%"+q+"%'";
		String countQuery = "SELECT * FROM vt WHERE _id LIKE '%" + q + "%'";
		// SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = MainActivity.sdb.rawQuery(countQuery, null);

		countQuery = "SELECT * FROM vt WHERE cn1 LIKE '%" + q
				+ "%' AND cn1 NOT IN (SELECT cn1 FROM vt WHERE _id LIKE '%" + q
				+ "%')";
		Cursor cursor1 = MainActivity.sdb.rawQuery(countQuery, null);

		srchCnt = cursor.getCount() + cursor1.getCount();
		Log.w("Search Count", srchCnt + "");
		if (cursor.getCount() != 0) {
			if (cursor.moveToFirst()) {
				do {
					tilesTable t = new tilesTable();
					t.setId(cursor.getString(0));
					t.setCompNm(cursor.getString(1));
					t.setNodl(Integer.parseInt(cursor.getString(2)));
					t.setPts(Integer.parseInt(cursor.getString(3)));
					t.setSts(Integer.parseInt(cursor.getString(4)));
					t.setNo(Integer.parseInt(cursor.getString(5)));
					t.setConven(cursor.getString(6));
					MainActivity.allTiles.add(t);
				} while (cursor.moveToNext());
			}
		}

		if (cursor1.getCount() != 0) {
			if (cursor1.moveToFirst()) {
				do {
					tilesTable t = new tilesTable();
					t.setId(cursor1.getString(0));
					t.setCompNm(cursor1.getString(1));
					t.setNodl(Integer.parseInt(cursor1.getString(2)));
					t.setPts(Integer.parseInt(cursor1.getString(3)));
					t.setSts(Integer.parseInt(cursor1.getString(4)));
					t.setNo(Integer.parseInt(cursor1.getString(5)));
					t.setConven(cursor1.getString(6));
					MainActivity.allTiles.add(t);
				} while (cursor1.moveToNext());
			}
		}

		cursor1.close();
		cursor.close();
		// db.close();
	}

	public void getSearchV(String q) {

		Log.w("Search ", "Called");
		// String countQuery =
		// "SELECT  * FROM "+TABLE_NAME+" WHERE _id LIKE '%"+q+"%'"+" OR _compNm LIKE '%"+q+"%'";
		String countQuery = "SELECT * FROM Vouchers WHERE _vcrnm LIKE '%" + q
				+ "%'";
		// SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = MainActivity.sdb.rawQuery(countQuery, null);

		srchCntV = cursor.getCount();
		Log.w("Search Count", srchCnt + "");
		if (cursor.getCount() != 0) {
			if (cursor.moveToFirst()) {
				do {
					Voucher t = new Voucher();
					t.setId(cursor.getString(0));
					t.setVcrnm(cursor.getString(1));
					t.setDesc(cursor.getString(2));
					t.setCost(Integer.parseInt(cursor.getString(3)));
					t.setChk(Integer.parseInt(cursor.getString(4).trim()));
					MainActivity.allVouchers.add(t);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		// db.close();
	}

	public int getSrchCntV() {
		// SQLiteDatabase db = this.getReadableDatabase();
		int c = srchCntV;
		// db.close();
		return c;
	}

	public void setSrchCntV(int c) {
		// SQLiteDatabase db = this.getReadableDatabase();
		srchCntV = c;
		// db.close();
	}

	public int getSrchCnt() {
		// SQLiteDatabase db = this.getReadableDatabase();
		int c = srchCnt;
		// db.close();
		return c;
	}

	public void setSrchCnt(int c) {
		// SQLiteDatabase db = this.getReadableDatabase();
		srchCnt = c;
		// db.close();
	}

	public boolean enableSbmtBtn(String _id) {
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("Questions",
				new String[] { "_qno " }, "_id= '" + _id + "'"
						+ " AND _isOp=0 AND _g=0", null, null, null, null);
		Log.w("grrrr", _id + "  " + cursor.getCount());
		boolean g;
		if (cursor.getCount() == 0)
			g = true;
		else
			g = false;

		cursor.close();
		// db.close();
		return g;
	}

	public void deleteSub() {
		List<String> del = new ArrayList<String>();
		// SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = MainActivity.sdb.query("tilesInfo",
				new String[] { "_id" }, "_sts = 4", null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				del.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();

		for (int i = 0; i < del.size(); i++) {
			MainActivity.sdb.delete("tilesInfo", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Questions", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Response_yn", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Response_Rating", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Response_FeedBack", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Options_Radio", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Options_RadioC", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Otherans", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Dateans", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("Options_Checkbox", "_id = ?",
					new String[] { del.get(i) });
			MainActivity.sdb.delete("vt", "_id = ?",
					new String[] { del.get(i) });
		}

	}

	public void delTemp(String s1, String s2) {
		SometimesServiceV.sld.delete("Redeem", "vcr=? AND ts=?", new String[] {
				s1, s2 });
	}

	public void delRch(int amt, String serv, String phno, String ts) {
		SometimesServiceR.sld.delete("Recharge",
				"amt=? AND serv=? AND phno=? AND ts=?", new String[] {
						"" + amt, serv, phno, ts });
	}

	public List<pollsWithin> getAllPollsWithin() {
		List<pollsWithin> allPolls = new ArrayList<pollsWithin>();
		String SQL = "SELECT * FROM pollsWithin";
		Cursor cursor = MainActivity.sdb.rawQuery(SQL, null);
		if (cursor.moveToFirst()) {
			do {
				pollsWithin t = new pollsWithin();
				t.setId(cursor.getString(0));
				t.setQid(cursor.getString(1));
				t.setQues(cursor.getString(2));
				t.setNrpl(Integer.parseInt(cursor.getString(3)));
				t.setOps(cursor.getString(4));
				t.setRes(cursor.getString(5));
				t.setIcreated(Integer.parseInt(cursor.getString(6)));
				Log.w("Setting Polls Ques", "Poll Ques: " + cursor.getString(0)
						+ " " + cursor.getString(1) + " " + cursor.getString(2));
				allPolls.add(t);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return allPolls;
	}

	void addPolls(pollsWithin p) {
		Log.w("called", "Added Poll" + p.getQid());
		String selectQuery = "SELECT * FROM pollsWithin WHERE qid='"
				+ p.getId() + "'";
		Cursor cursor = SometimesServiceOR.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("id", p.getId());
			values.put("qid", p.getQid());
			values.put("ques", p.getQues());
			values.put("nrpl", p.getNrpl());
			values.put("ops", p.getOps());
			values.put("res", p.getRes());
			values.put("icreated", p.getIcreated());
			Log.w("Added Poll", "Added Poll" + p.getQid());
			SometimesServiceOR.sld.insert("pollsWithin", null, values);
		}
		cursor.close();
	}

	void addPollsn(pollsWithin p) {
		String selectQuery = "SELECT * FROM pollsWithin WHERE qid='"
				+ p.getId() + "'";
		Cursor cursor = SometimesServiceNP.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("id", p.getId());
			values.put("qid", p.getQid());
			values.put("ques", p.getQues());
			values.put("nrpl", p.getNrpl());
			values.put("ops", p.getOps());
			values.put("res", p.getRes());
			values.put("icreated", p.getIcreated());
			Log.w("Added Poll", "Added poll " + p.getQid());
			SometimesServiceNP.sld.insert("pollsWithin", null, values);
		}
		cursor.close();
	}

	void addPollinit(pollsWithin p) {
		String selectQuery = "SELECT * FROM pollsWithin WHERE qid='"
				+ p.getId() + "'";
		Cursor cursor = SometimesService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("id", p.getId());
			values.put("qid", p.getQid());
			values.put("ques", p.getQues());
			values.put("nrpl", p.getNrpl());
			values.put("ops", p.getOps());
			values.put("res", p.getRes());
			values.put("icreated", p.getIcreated());
			Log.w("Added Poll", "Added poll " + p.getQid());
			SometimesService.sld.insert("pollsWithin", null, values);
		}
		cursor.close();
	}

	void addPollsnd(pollsWithin p) {
		String selectQuery = "SELECT * FROM pollsWithin WHERE qid='"
				+ p.getId() + "'";
		Cursor cursor = GCMIntentService.sld.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("id", p.getId());
			values.put("qid", p.getQid());
			values.put("ques", p.getQues());
			values.put("nrpl", p.getNrpl());
			values.put("ops", p.getOps());
			values.put("res", p.getRes());
			values.put("icreated", p.getIcreated());
			Log.w("Added Poll", "Added poll " + p.getQid());
			GCMIntentService.sld.insert("pollsWithin", null, values);
		}
		cursor.close();
	}

	public int updatePolls(String qid, String res, int nrpl) {
		ContentValues values = new ContentValues();
		values.put("res", res);
		values.put("nrpl", nrpl);
		Log.w("Updated Poll", "" + res);
		int c = GCMIntentService.sld.update("pollsWithin", values, "qid=?",
				new String[] { qid });
		return c;
	}

	public void deletepoll(String qid) {
		// SQLiteDatabase db = this.getWritableDatabase();
		SometimesServiceDP.sld.delete("pollsWithin", "qid = ?",
				new String[] { qid });
		// db.close();
	}

	public void saveDemo() {

		ContentValues values = new ContentValues();

		Log.w("Demo init", "by main");
		values.put("zip", "");
		values.put("mar", "Single");
		values.put("edu", "Some college");
		values.put("occ", "Student");
		values.put("sal", "Nil");
		MainActivity.sdb.insert("savedemo", null, values);

	}

	public SaveDemo getDemo() {
		String selectQuery = "SELECT * FROM savedemo";

		Cursor cursor = SometimesServiceSD1.sld.rawQuery(selectQuery, null);
		SaveDemo sd = new SaveDemo();

		if (cursor.moveToFirst()) {
			do {

				sd.setzip(cursor.getString(0));
				sd.setmar(cursor.getString(1));
				sd.setEdu(cursor.getString(2));
				sd.setOcc(cursor.getString(3));
				sd.setSal(cursor.getString(4));
				Log.w("Retriveing for broadcasr", "" + cursor.getString(0)
						+ " " + cursor.getString(1) + " " + cursor.getString(2)
						+ " " + cursor.getString(3) + " " + cursor.getString(4));
			} while (cursor.moveToNext());
		}

		cursor.close();
		return sd;
	}

	public SaveDemo getDemo1() {
		String selectQuery = "SELECT * FROM savedemo";

		Cursor cursor = MainActivity.sdb.rawQuery(selectQuery, null);
		SaveDemo sd = new SaveDemo();

		if (cursor.moveToFirst()) {
			do {
				sd.setzip(cursor.getString(0));
				sd.setmar(cursor.getString(1));
				sd.setEdu(cursor.getString(2));
				sd.setOcc(cursor.getString(3));
				sd.setSal(cursor.getString(4));
				Log.w("Retriveing for display", "" + cursor.getString(0) + " "
						+ cursor.getString(1) + " " + cursor.getString(2) + " "
						+ cursor.getString(3) + " " + cursor.getString(4));
			} while (cursor.moveToNext());
		}

		cursor.close();
		return sd;

	}

	public int updatesaveDemo(String zip, String mar, String edu, String occ,
			String sal) {
		// SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("zip", zip);
		values.put("mar", mar);
		values.put("edu", edu);
		values.put("occ", occ);
		values.put("sal", sal);
		SometimesServiceSaveDemo.sld.delete("savedemo", null, null);
		SometimesServiceSaveDemo.sld.insert("savedemo", null, values);
		return 1;
	}
}
