package com.sxs.app.braintwisters;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.sxs.app.common.BaseActivity;

public class BaseFragment extends Fragment {
	public BaseActivity getBaseActivity(){
		return (BaseActivity)getActivity();
	}
	
	private View rootView;//缓存Fragment view
	
	protected View getInjectedView(LayoutInflater inflater,int res){
//		if( rootView == null ){  
            rootView=inflater.inflate(res, null);  
            ViewUtils.inject(this,rootView);
//        }
    	//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
    	ViewGroup parent = (ViewGroup) rootView.getParent();  
    	if (parent != null) {  
    		parent.removeView(rootView);  
    	} 
        return rootView;
	}
}
