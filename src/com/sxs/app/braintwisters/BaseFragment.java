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
	
	private View rootView;//����Fragment view
	
	protected View getInjectedView(LayoutInflater inflater,int res){
//		if( rootView == null ){  
            rootView=inflater.inflate(res, null);  
            ViewUtils.inject(this,rootView);
//        }
    	//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���  
    	ViewGroup parent = (ViewGroup) rootView.getParent();  
    	if (parent != null) {  
    		parent.removeView(rootView);  
    	} 
        return rootView;
	}
}
