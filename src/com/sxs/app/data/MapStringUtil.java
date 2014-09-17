package com.sxs.app.data;

public class MapStringUtil { 
	
	public static int getRandomNum(int count){
		int value = (int) (Math.random() * (count - 1));
		return value;
	}
	public static String getStr(Object str){
		
		if(str != null)
		{
			return str.toString();
		}
		return "";
	}
	public static int stringFind(String source, String target){
       int number = 0;
       int i = 0;
       while((i=source.indexOf(target, i))!=-1) {
           number++;
           i++;
       }
       return number;
    }
	public static String getTimeStr(long time)
	{
		time = time < 0 ? 0 :time;
		long allmm = time / 1000;
		long mm = allmm % 60;
		long ff = allmm/60;
		return (ff < 10 ? "0"+ff : ff) +":"+ (mm < 10 ? "0"+mm : mm);
	}
	
	

}

