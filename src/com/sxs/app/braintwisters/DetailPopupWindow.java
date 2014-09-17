package com.sxs.app.braintwisters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.PopupWindow;

import com.sxs.app.common.BaseActionBar;
  
public class DetailPopupWindow extends PopupWindow {  
  

    private View mMenuView; 
    private WebView webView;  
    private BaseActionBar actionBar;
    
    public DetailPopupWindow(Activity context) {  
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.popup_layout, null);  
        webView = (WebView) mMenuView.findViewById(R.id.wv_shuoming_webview);  
        actionBar = (BaseActionBar)mMenuView.findViewById(R.id.ab_detail_activity);  
        actionBar.setRightBtnOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();  
			}
		});
       
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.mystyle);  
        //实例化一个ColorDrawable颜色为半透明  
//        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
//        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
//        mMenuView.setOnTouchListener(new OnTouchListener() {  
//              
//            public boolean onTouch(View v, MotionEvent event) {  
//                return true;  
//            }  
//        });  
//  
    }  
    
    public void loadUrl(String url,String title){
    	actionBar.setTitle(title);
    	webView.loadUrl(url);
    }
  
}  