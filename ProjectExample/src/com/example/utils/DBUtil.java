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
	 * 添加历史
	 * @param data 		数据
	 * @return	如果数据库未打开或插入出错则返回false，如果插入成功返回true
	 */
	public boolean addHistory(float heartRate,int[] data)
	{
		if(null==mDatabase||!mDatabase.isOpen()||mDatabase.isReadOnly())
		{
			Log.e("Database Error", "未正确打开数据库");
			return false;
		}
		Calendar date=Calendar.getInstance();
		Object[] args={date.getTimeInMillis(),heartRate,data};
		String sql="insert into"+TABLE_HISTORY+"(time,heartrate,data) "+"VALUES(?,?,?)";
		mDatabase.execSQL(sql,args);
		return true;
	}
	/**
	 * 获取所有历史数据
	 * @return List<History> 类型的数据，History包含了该记录的日期Date,心率值heartRate和数据Data
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
	 * 删除历史数据
	 * @param date	该历史数据的日期
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
