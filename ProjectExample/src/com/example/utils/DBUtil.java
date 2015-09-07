package com.example.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBUtil extends SQLiteOpenHelper{
	private static final int VERSION=1;
	
	private static final String DATABASE_NAME="project.db";
	private static final String TABLE_USER=" user ";
	private static final String TABLE_HISTORY=" history ";
	
	private SQLiteDatabase mDatabase;
	public DBUtil(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create="create table "+TABLE_HISTORY+"(_id integer primary key autoincrement,time long,heartrate float,data blob)"+'\n';
		db.execSQL(create);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	public void open()
	{
		this.mDatabase=getReadableDatabase();
	}
	/**
	 * �����ʷ
	 * @param data 		����
	 * @return	������ݿ�δ�򿪻��������򷵻�false���������ɹ�����true
	 */
	public boolean addHistory(float heartRate,int[] data)
	{
		if(null==mDatabase||!mDatabase.isOpen()||mDatabase.isReadOnly())
		{
			Log.e("Database Error", "δ��ȷ�����ݿ�");
			return false;
		}
		Calendar date=Calendar.getInstance();
		Object[] args={date.getTimeInMillis(),heartRate,data};
		String sql="insert into"+TABLE_HISTORY+"(time,heartrate,data) "+"VALUES(?,?,?)";
		mDatabase.execSQL(sql,args);
		return true;
	}
	/**
	 * ��ȡ������ʷ����
	 * @return List<History> ���͵����ݣ�History�����˸ü�¼������Date,����ֵheartRate������Data
	 */
	public List<History> getHistory()
	{
		String[] columns={"time","heartrate","data"};
		Cursor cursor=mDatabase.query(TABLE_HISTORY, columns, null, null, null, null, null, null);
		List<History> list=new ArrayList<History>();
		while(!cursor.isAfterLast()&&!cursor.isLast())
		{
			cursor.moveToNext();
			History his=new History();
			Date date=new Date(cursor.getLong(0));
			his.setTime(date);
			his.setHeartRate(cursor.getFloat(1));
			his.setData(cursor.getBlob(2));
			list.add(his);
		}
		return list;
	}
	/**
	 * ɾ����ʷ����
	 * @param date	����ʷ���ݵ�����
	 */
	public void deleteHistory(Date date)
	{
		String where="time like ?";
		String[] args={Long.toString(date.getTime())};
		mDatabase.delete(TABLE_HISTORY, where, args);
	}
	
	
	
	public void deleteDatabase(Context context)
	{
		if(null==mDatabase)
		{
			return;
		}
		if(this.mDatabase.isOpen())
			mDatabase.close();
		context.deleteDatabase(DATABASE_NAME);
		
	}
	public void close()
	{
		if(mDatabase.isOpen())
			mDatabase.close();
	}
	public boolean isOpen()
	{
		return mDatabase!=null&&mDatabase.isOpen();
	}
}
