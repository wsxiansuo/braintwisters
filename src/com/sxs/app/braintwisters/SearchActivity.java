package com.sxs.app.braintwisters;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BraintwisterAdapter;
import com.sxs.app.common.OnListPageChangeListener;
import com.sxs.app.common.PageControl;
import com.sxs.app.data.BraintwisterVO;
import com.sxs.app.data.UserDataModel;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener,OnListPageChangeListener{
	@ViewInject(R.id.lv_bt_list)         ListView listView;
	@ViewInject(R.id.page_control)       PageControl pageControl;
	@ViewInject(R.id.search_view) 			private SearchView searchView; 
	@ViewInject(R.id.tv_no_data) 			private TextView textView; 
	
	private BraintwisterAdapter adapter;
	private List<BraintwisterVO> alllistdata;
	private List<BraintwisterVO> listdata;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		ViewUtils.inject(this);
        initConment();
	}

	
	public void initConment() {
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);
		adapter = new BraintwisterAdapter(this);
		listView.setAdapter(adapter);
		searchForData("");
    }  
	private void searchForData(String key){
		alllistdata = UserDataModel.instance().getSearchlistdata(key);
		if(alllistdata == null || alllistdata.size() == 0){
			listView.setVisibility(View.INVISIBLE);
			pageControl.setVisibility(View.INVISIBLE);
			textView.setVisibility(View.VISIBLE);
			return;
		}else if(alllistdata.size() <= pageControl.numPerPage){
			listView.setVisibility(View.VISIBLE);
			pageControl.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
		}else{
			listView.setVisibility(View.VISIBLE);
			pageControl.setVisibility(View.VISIBLE);
			textView.setVisibility(View.GONE);
		}
		pageControl.setPageChangeListener(this);  
        pageControl.initPageShow(alllistdata.size());  
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
					Toast.makeText(getApplicationContext(), item.isTag?"成功添加收藏！":"成功去除收藏！", Toast.LENGTH_SHORT).show();
				}
			}
        });
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
	
	 @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //应用的最后一个Activity关闭时应释放DB  
    }


	 @Override
	 public boolean onQueryTextChange(String newText) {
		  if (TextUtils.isEmpty(newText)) {
		   // Clear the text filter.
			  searchForData("");
		  } else {
		   // Sets the initial value for the text filter.
			  searchForData(newText);
		  }
		  return false;
	 }

	 @Override
	 public boolean onQueryTextSubmit(String query) {
	  // TODO Auto-generated method stub
		 Log.i("A -- ",query);
	  return false;
	 }
}
