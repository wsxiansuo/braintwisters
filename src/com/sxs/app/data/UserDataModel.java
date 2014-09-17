package com.sxs.app.data;

import java.util.ArrayList;
import java.util.List;


public class UserDataModel {
	
	public DBManager mgr;
	private List<BraintwisterVO> alllistdata;
	/**
	 * @return the alllistdata
	 */
	public List<BraintwisterVO> getSearchlistdata(String key) {
		if(alllistdata == null){
			alllistdata = mgr.querySearchList("");
		}
		if(key.isEmpty()){
			return alllistdata;
		}else{
			List<BraintwisterVO> list= new ArrayList<BraintwisterVO>();
			for (BraintwisterVO item : alllistdata) {
				if(item.question.indexOf(key) > -1 || item.answer.indexOf(key) > -1){
					list.add(item);
				}
			}
			return list;
		}
	}
	
	
	private static UserDataModel _instance;
	public static UserDataModel instance()
	{
		if(_instance == null)
		{
			_instance = new UserDataModel();
		}
		return _instance;
	}
}
