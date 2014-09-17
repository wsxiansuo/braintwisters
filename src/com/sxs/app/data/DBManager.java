package com.sxs.app.data;

import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map;
  
import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.sqlite.SQLiteDatabase;  
import android.text.TextUtils;
import android.util.Log;
  
public class DBManager {   
	
    private SQLiteDatabase db;  
    private Context mContext;
    public DBManager(Context context) {  
    	mContext = context;
        db = (new DBHelper(context)).openDatabase();  
    } 
    
    public void upDatabase(){
    	db = (new DBHelper(mContext)).updateDatabase();  
    }

    public List<Map<String, String>> queryPoemList(String type_id) {   
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
        Cursor c = db.query("sxs_poem_list", null, "typeid like ?", new String[]{"%*"+type_id+"*%"} ,null, null, null); 
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("title"))); 
            map.put("content", c.getString(c.getColumnIndex("content"))); 
            map.put("translate", c.getString(c.getColumnIndex("translate")));
            map.put("author", c.getString(c.getColumnIndex("author")));
            i++;
            map.put("num", i+""); 
            listData.add(map);  
        }  
        c.close();  
        //Log.i("count:", type_id + " -- : -- " + listData.size());
        return listData;  
    }  
    
    public List<Map<String, String>> queryAuthorsList() {  
        List<Map<String, String>> listData = new ArrayList<Map<String, String>>();   
        Cursor c = db.rawQuery("SELECT * FROM sxs_authors", null); 
        int i = 0;
        while (c.moveToNext()) {
        	Map<String, String> map = new HashMap<String, String>();  
            map.put("id", c.getString(c.getColumnIndex("id"))); 
            map.put("title", c.getString(c.getColumnIndex("name"))); 
            map.put("content", c.getString(c.getColumnIndex("detail"))); 
            map.put("author", c.getString(c.getColumnIndex("date")));  
            i++;
            map.put("num", i+""); 
            listData.add(map);  
        }  
        c.close();  
        return listData;  
    } 

    /**
     * 根据typeId获取列表 或 获取收藏列表
     * @param key
     * @return
     */
    public List<BraintwisterVO> queryQuestionList(String type_id,Boolean isTag) {   
        List<BraintwisterVO> listData = new ArrayList<BraintwisterVO>();
        Cursor c = null;
        if(isTag){
        	c = db.rawQuery("SELECT * FROM bt_list where isTag=?", new String[]{"1"});  
        }else if(type_id != ""){
        	c = db.rawQuery("SELECT * FROM bt_list where type_id=?", new String[]{type_id});  
        }else{
        	c = db.rawQuery("SELECT * FROM bt_list",null);
        }
        int i = 0;
        while (c.moveToNext()) {
        	BraintwisterVO map = new BraintwisterVO();  
            map.id = c.getInt(c.getColumnIndex("id")); 
            map.question = c.getString(c.getColumnIndex("question")); 
            map.answer = "答案："+c.getString(c.getColumnIndex("answer")); 
            map.type_id = c.getString(c.getColumnIndex("type_id"));
            map.isTag =  c.getInt(c.getColumnIndex("isTag")) == 1;
            i++;
//            map.num = i+"、";
            listData.add(map);  
        }  
        c.close();
        return listData;  
    } 
    /**
     * 根据typeId获取列表 或 获取收藏列表
     * @param key
     * @return
     */
    public List<BraintwisterVO> querySearchList(String key) {   
        List<BraintwisterVO> listData = new ArrayList<BraintwisterVO>();
        Cursor c = null;
        if(TextUtils.isEmpty(key)){
        	c = db.rawQuery("SELECT * FROM bt_list", null);  
        }else{
        	c = db.query("bt_list", null, "question like ? or answer like ?", new String[]{"%"+key+"%","%"+key+"%"} ,null, null, null);
        }
        while (c.moveToNext()) {
        	BraintwisterVO map = new BraintwisterVO();  
            map.id = c.getInt(c.getColumnIndex("id")); 
            map.question = c.getString(c.getColumnIndex("question")); 
            map.answer = "答案："+c.getString(c.getColumnIndex("answer")); 
            map.type_id = c.getString(c.getColumnIndex("type_id"));
            map.isTag =  c.getInt(c.getColumnIndex("isTag")) == 1;
            listData.add(map);  
        }  
        c.close();
        return listData;  
    } 
    /**
     * 根据首字母获取分类
     * @param key
     * @return
     */
    public List<MenuItemVO> queryTypeList() {   
        List<MenuItemVO> listData = new ArrayList<MenuItemVO>();
        Cursor c = db.rawQuery("SELECT * FROM bt_types", null);  
        int i = 0;
        while (c.moveToNext()) {
        	MenuItemVO map = new MenuItemVO();  
            map.id = c.getInt(c.getColumnIndex("id")); 
            map.type = c.getString(c.getColumnIndex("type")); 
            listData.add(map);  
        }  
        c.close();
        return listData;  
    } 

    /**
     * 根据首字母获取分类
     * @param key
     * @return
     */
    public int getDbVersion() {   
    	int result = 1;
        Cursor c = db.rawQuery("SELECT * FROM bt_version", null);
        while (c.moveToNext()) {
        	result = c.getInt(c.getColumnIndex("version"));
        }  
        c.close();  
        return result;  
    } 
    /**
     * 根据首字母获取分类
     * @param key
     * @return
     */
    public void updateQuestionState(String id, Boolean state) {   
    	ContentValues cv = new ContentValues();  
        cv.put("isTag", state ? "1":"0");  
        //更新数据  
        db.update("bt_list", cv, "id = ?", new String[]{id});  
    } 
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  