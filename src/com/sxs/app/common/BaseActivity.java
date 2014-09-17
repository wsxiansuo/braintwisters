package com.sxs.app.common;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 说明： 基础activity
 * 作者： ztian
 * 创建时间：2013-11-4 下午3:51:23
 */

public class BaseActivity extends FragmentActivity {
	
	private String shareUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.sxs.app.braintwisters";
//	private String shareUrl = "";
	@Override
	 protected void onCreate(Bundle savedInstanceState){
	      super.onCreate(savedInstanceState);
	 }
	@Override
	protected void onDestroy(){
	    super.onDestroy();
	    SnsManager.getInstance().distroyActivity();
	}
	@Override
	protected void onPause(){
		super.onPause();
	}
	

	// 登陆后返回
	@Override 
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		UMSsoHandler ssoHandler = SnsManager.getInstance().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
    }
		
	@Override
	protected void onResume() {
    	super.onResume();
    	SnsManager.getInstance().setSnsActivity(this);
		SnsManager.getInstance().configPlatforms(shareUrl);
    }
	
	@Override
	protected void onStop(){
		super.onStop();
	}

	public void shareMessage(String content){
		if(content.length() > 100){
			content = content.substring(0, 100) + "..." + shareUrl;
		}
		SnsManager.getInstance().openShare(content);
	}
	
	
	public void showToast(String tip){
		Toast t = Toast.makeText(BaseActivity.this, tip, Toast.LENGTH_SHORT);
		t.show();
	}
	public void showLongToast(String tip){
		Toast t = Toast.makeText(BaseActivity.this, tip, Toast.LENGTH_LONG);
		t.show();
	}
}
