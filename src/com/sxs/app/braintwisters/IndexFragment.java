package com.sxs.app.braintwisters;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BaseActionBar;
import com.sxs.app.common.BraintwisterAdapter;
import com.sxs.app.common.ListViewAdapter;
import com.sxs.app.common.OnListPageChangeListener;
import com.sxs.app.common.PageControl;
import com.sxs.app.data.BraintwisterVO;
import com.sxs.app.data.MenuItemVO;
import com.sxs.app.data.UserDataModel;
import com.zhaoxufeng.leftsliderlayout.lib.LeftSliderLayout;

public class IndexFragment extends BaseFragment implements LeftSliderLayout.OnLeftSliderLayoutStateListener,OnListPageChangeListener, View.OnClickListener{
	
	@ViewInject(R.id.lv_bt_list)         ListView listView;
	@ViewInject(R.id.page_control)       PageControl pageControl;
	@ViewInject(R.id.ab_index_activity)  BaseActionBar actionBar;
	
	private String currType = "9";
	private String currTitle = "大集锦";
	private BraintwisterAdapter adapter;
	private List<BraintwisterVO> alllistdata;
	private List<BraintwisterVO> listdata;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);
        mOpenActionBar.setTitle(currTitle);
		onHander();
		adapter = new BraintwisterAdapter(getActivity());
		listView.setAdapter(adapter);
		initdata();
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BraintwisterVO item = (BraintwisterVO) listView.getItemAtPosition(arg2);
				ImageView img = (ImageView) arg1.findViewById(R.id.bt_tag_iv);
				if(img != null && item != null){
					item.isTag = !item.isTag;
					UserDataModel.instance().mgr.updateQuestionState(item.id +"",item.isTag);
					img.setImageResource(item.isTag ? R.drawable.star_full:R.drawable.star_empty);
					getBaseActivity().showToast(item.isTag?"成功添加收藏！":"成功去除收藏！");
				}
			}
        });
    } 
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = getInjectedView(inflater,R.layout.activity_index);
//		actionBar.setLeftBtnOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		actionBar.setRightBtnOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getBaseActivity(), SearchActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});
        return view;  
    }
	@Override
	public void onResume(){
		super.onResume();
	}
	private void initdata(){
		alllistdata = UserDataModel.instance().mgr.queryQuestionList(currType,false);
		if(alllistdata == null)return;
		pageControl.setPageChangeListener(this);  
        pageControl.initPageShow(alllistdata.size());  
	}
	
	
	@ViewInject(R.id.main_slider_layout)  LeftSliderLayout leftSliderLayout;
    @ViewInject(R.id.ab_index_activity)   BaseActionBar mOpenActionBar;
    @ViewInject(R.id.listTab)             ListView mListView;
    private List<MenuItemVO> mDataList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void onHander() {
        bindView();
        mDataList = UserDataModel.instance().mgr.queryTypeList();
        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(),mDataList) ;
        mListView.setAdapter(listViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	openLeftSlider(false);
            	MenuItemVO item = mDataList.get(i);
            	currType = item.id + "";
            	currTitle = item.type;
            	mOpenActionBar.setTitle(currTitle);
            	initdata();
            }
        });
    }

    @Override
    public void onClick(View view) {
    	openLeftSlider(false);
    }

    @Override
    public void OnLeftSliderLayoutStateChanged(boolean bIsOpen) {

        if (bIsOpen) {
//            Toast.makeText(this, "LeftSliderLayout is open!", Toast.LENGTH_SHORT).show();
//            Log.d(TAG," leftsilder is open")  ;
        } else {
           // Toast.makeText(this, "LeftSliderLayout is close!", Toast.LENGTH_SHORT).show();
//            Log.d(TAG," leftsilder is close")  ;
        }

    }

    @Override
    public boolean OnLeftSliderLayoutInterceptTouch(MotionEvent ev) {

        return false;
    }

    private  void bindView(){
        leftSliderLayout.setOnLeftSliderLayoutListener(this);
        mOpenActionBar.setLeftBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leftSliderLayout.isOpen()) {
                    leftSliderLayout.close();
                } else {
                    leftSliderLayout.open();
                }

            }
        });

    }

    public  void openLeftSlider(boolean isToOpen){
        if(isToOpen)    {
            leftSliderLayout.open();
        }else {
            leftSliderLayout.close();
        }

    }


    public  void enableSlider(boolean isEnable)    {
         if(isEnable)  {
              leftSliderLayout.enableSlide(true);
         } else {
             leftSliderLayout.enableSlide(false);
         }
    }

	@Override
	public void pageChanged(int curPage, int numPerPage) {
		if(alllistdata != null && alllistdata.size() > 0){
			int last = curPage * numPerPage;
			if(last > alllistdata.size())last = alllistdata.size();
			listdata = alllistdata.subList((curPage - 1) * numPerPage, last);
			adapter.setListData(listdata);
			adapter.notifyDataSetInvalidated();
			listView.scrollTo(0, 0);
		}
	}

	
}
