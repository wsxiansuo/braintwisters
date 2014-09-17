package com.sxs.app.braintwisters;

import java.util.ArrayList;
import java.util.HashMap;

import pop.doujin.android.DPManager;
import pop.doujin.android.pop.PopManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseActivity;
import com.sxs.app.data.DBManager;
import com.sxs.app.data.UserDataModel;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import drop.doujin.android.DDManager;
import drop.doujin.android.rp.DropManager;

public class MainActivity extends BaseActivity {
	
	public static final int TYPE_INDEX_TAB = 0;
	public static final int TYPE_SECOND_TAB = 1;
	public static final int TYPE_THREE_TAB = 2;
	
	public static final int TYPE_INTERACTIVE_TAB = 3;
	private int currentTab = TYPE_INDEX_TAB;
	
//	private NavigateHelper navigateHelper;
	 // 点击菜单选项的常量Id   
    private static final int menu1 = 0;  
    private static final int menu2 = 1;  
    private static final int menu3 = 2;  
    private AlertDialog menuDialog;// menu菜单Dialog
    private GridView menuGrid;
    private View menuView;
    /** 菜单图片 **/
	private int[] menu_image_array = { R.drawable.menu_help,
			R.drawable.menu_about, R.drawable.menu_sharepage};
	/** 菜单文字 **/
	private String[] menu_name_array = { "用户帮助", "关于软件", "推荐"};
    
	private DetailPopupWindow popupWindow;
    
    
	//定义数组来存放Fragment界面  
    private Class fragmentArray[] = {IndexFragment.class,SecondFragment.class,ThreeFragment.class,FoureFragment.class};  
    //定义数组来存放按钮图片  
    private int mImageViewArray[] = {R.drawable.icon_tab_1,R.drawable.icon_tab_2,R.drawable.icon_tab_3,R.drawable.icon_tab_4};  
    //Tab选项卡的文字  
    private String mTextviewArray[] = {"急转弯", "猜一猜" , "摇一摇", "收藏夹"};
    @ViewInject(R.id.th_main)	private FragmentTabHost tabHost;
    @ViewInject(R.id.fl_main)   private FrameLayout     frameLayout;
    
    private boolean mode = false;//true:测试 false发布
	private int dbVersion = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this); 
		initComponent();
        configTabs(); 
        UserDataModel.instance().mgr = new DBManager(MainActivity.this);
        if(UserDataModel.instance().mgr.getDbVersion() < dbVersion){
    		UserDataModel.instance().mgr.upDatabase();  
    		Log.i("update", "数据库执行了更新---------------");
    	}
        initOptionsMenu();
        DDManager.getInstance(this).init("2b61dc54be73ae6866d936ba04757a98", mode);
        DDManager.getInstance(this).initLayout("#41cec6", 0);
        DropManager.getInstance(this).start(this);
        
        DPManager.getInstance(this).init("2b61dc54be73ae6866d936ba04757a98", mode);
        PopManager.getInstance(this).start(this,1);
//        DJManager.getInstance(this).init("2b61dc54be73ae6866d936ba04757a98", mode);
//        DJPushManager.startDoujinPush(this);
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run(){
//            	
//            }
//        }, 1000);
	}
	private void initOptionsMenu(){
		menuView = View.inflate(this, R.layout.gridview_menu, null);
		// 创建AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				menuDialog.dismiss();
				switch (arg2) {
				case menu1:  
		        	popWithUrl("file:///android_asset/help.html",menu_name_array[0]);
		            break;  
		        case menu2:  
		        	popWithUrl("file:///android_asset/about.html",menu_name_array[1]);
		            break;  
		        case menu3:  
		        	shareMessage("啦啦啦！大家一起来玩脑筋急转弯啦！");  
		            break;  
		        }  
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}
	
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// 返回为true 则显示系统menu
	}
    private void popWithUrl(String url,String title){
    	if(popupWindow == null){
    		popupWindow = new DetailPopupWindow(MainActivity.this);  
    	}
    	popupWindow.loadUrl(url,title);//file:///android_asset/www/index.html
        //显示窗口  
    	popupWindow.showAtLocation(frameLayout, Gravity.CENTER, 0, 0); //设置layout在PopupWindow中显示的位置  
    }
    //初始化组件
	private void initComponent() {
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_main); 
	}

	private void configTabs(){
		int count = fragmentArray.length;  
		final ArrayList<View> tabs = new ArrayList<View>();
		for(int i = 0; i < count; i++){    
			//为每一个Tab按钮设置图标、文字和内容  
			tabs.add(getTabItemView(i));
			TabSpec tabSpec = tabHost.newTabSpec(mTextviewArray[i]).setIndicator(tabs.get(i));
			//将Tab按钮添加进Tab选项卡中  
			tabHost.addTab(tabSpec, fragmentArray[i], null);  
			//设置Tab按钮的背景  
			tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.sl_nav_main);  
			tabHost.getTabWidget().getChildAt(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View tab) {
					int index = tabs.indexOf(tab);
					realNavigateToTab(index);
				}
			});
		}  
	}
	private void realNavigateToTab(int index){
		currentTab = index;
		tabHost.setCurrentTab(index);
	}
	private View getTabItemView(int index){  
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);  
	      
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab_item_icon);  
        imageView.setImageResource(mImageViewArray[index]);  
          
        TextView textView = (TextView) view.findViewById(R.id.tv_tab_item_title);          
        textView.setText(mTextviewArray[index]);  
      
        return view;   
    }  
	 


		@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event)  {
	        switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:     
	            DropManager.getInstance(MainActivity.this).show(MainActivity.this);
	            break;
	        default:
	            break;
	        }
	        return false;    
	    }

		
		@Override
		public void onSaveInstanceState(Bundle savedInstanceState){
			super.onSaveInstanceState(savedInstanceState);
//			Log.v(TAG, "MainAcitivity 将要被销毁");
			// 这里什么都不做，不要调用父类，不然重新启动会报错
		}
		@Override
		public void onResume() {
	    	super.onResume();
	    	MobclickAgent.onResume(this);
	    }
		@Override
		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
		}
		@Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //应用的最后一个Activity关闭时应释放DB  
	        UserDataModel.instance().mgr.closeDB();  
	    }
}
