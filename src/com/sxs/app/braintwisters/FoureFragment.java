package com.sxs.app.braintwisters;

import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.common.BraintwisterAdapter;
import com.sxs.app.data.BraintwisterVO;
import com.sxs.app.data.UserDataModel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FoureFragment extends BaseFragment {
	
	@ViewInject(R.id.lv_bt_store_list)         ListView listView;
	@ViewInject(R.id.tv_no_data) 			private TextView textView;
	private BraintwisterAdapter adapter;
	private List<BraintwisterVO> listdata;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState); 

    } 
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = getInjectedView(inflater,R.layout.activity_store);
        return view;  
    }
	@Override
	public void onResume(){
		super.onResume();
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
				}
			}
        });
	}
	private void initdata(){
		listdata = UserDataModel.instance().mgr.queryQuestionList("",true);
		if(listdata == null || listdata.size() == 0){
			textView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.INVISIBLE);
			return;
		}else{
			textView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
		adapter.setListData(listdata);
		adapter.notifyDataSetInvalidated();
		listView.scrollTo(0, 0);
	}
}
