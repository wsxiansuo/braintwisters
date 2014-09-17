package com.sxs.app.braintwisters;

import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sxs.app.common.BaseActivity;
import com.sxs.app.data.BraintwisterVO;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.data.UserDataModel;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreeFragment extends BaseFragment {
	
	@ViewInject(R.id.three_question_tv)         TextView   questionTV;
	@ViewInject(R.id.three_answer_tv)           TextView   answerTV;
	@ViewInject(R.id.btn_share)					Button     shareBtn;
	@ViewInject(R.id.btn_look_result)			Button	   lookBtn;
	@ViewInject(R.id.iv_shoucang)			    ImageView  imageIV;
	
	private SensorManager sensorManager;  
    private Vibrator vibrator;
    private Boolean isShow = false;
    private BraintwisterVO item;
  
    /**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
		handerShow();
	}

	private static final String TAG = "TestSensorActivity";  
    private static final int SENSOR_SHAKE = 10;  
    
    private List<BraintwisterVO> listdata;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState); 
        listdata = UserDataModel.instance().mgr.queryQuestionList("",false);
        if(listdata != null){
        	changeQuestion();
        	sensorManager = (SensorManager)(getActivity()).getSystemService(Context.SENSOR_SERVICE);  
            vibrator = (Vibrator) (getActivity()).getSystemService(Context.VIBRATOR_SERVICE);  
            if (sensorManager != null) {// ע�������  
                sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);  
                // ��һ��������Listener���ڶ������������ô��������ͣ�����������ֵ��ȡ��������Ϣ��Ƶ��  
            }  
        }
    } 
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = getInjectedView(inflater,R.layout.activity_three);
        return view;  
    }
	@Override  
    public void onResume() {  
        super.onResume(); 
    }  
	 @Override  
    public void onPause() {  
        super.onPause();  
        if (sensorManager != null) {// ȡ��������  
            sensorManager.unregisterListener(sensorEventListener);  
        }  
    }  
	 @OnClick(R.id.btn_share)
	 public void shareClick(View v){
		 if(item != null){
			 getBaseActivity().shareMessage(item.question);
		 }
	 }
	 @OnClick(R.id.btn_look_result)
	 public void lookClick(View v){
		 setIsShow(!isShow);
	 }
	 @OnClick(R.id.iv_shoucang)
	 public void shoucangClick(View v){
		 if(item != null){
			 item.isTag = !item.isTag;
			 imageIV.setImageResource(item.isTag?R.drawable.star_full:R.drawable.star_empty);
			 UserDataModel.instance().mgr.updateQuestionState(item.id + "", item.isTag);
			 getBaseActivity().showToast(item.isTag?"�ɹ�����ղأ�":"�ɹ�ȥ���ղأ�");
		 }
	 }
	  
	    /** 
	     * ������Ӧ���� 
	     */  
	    private SensorEventListener sensorEventListener = new SensorEventListener() {  
	  
	        @Override  
	        public void onSensorChanged(SensorEvent event) {  
	            // ��������Ϣ�ı�ʱִ�и÷���  
	        	int sensorType = event.sensor.getType();  
	            //values[0]:X�ᣬvalues[1]��Y�ᣬvalues[2]��Z��  
	            float[] values = event.values;  
	            if (sensorType == Sensor.TYPE_ACCELEROMETER)  
	            {  
	                if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math  
	                        .abs(values[2]) > 17))  
	                {  
	                    //ҡ���ֻ����ٰ�������ʾ~~  
	                    vibrator.vibrate(500);  
	                    changeQuestion();
	                }  
	            }  
	        }  
	  
	        @Override  
	        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
	  
	        }  
	    };  
	    private void changeQuestion(){
	    	setIsShow(false);
	    	int p = MapStringUtil.getRandomNum(listdata.size());
            item = listdata.get(p);
            listdata.remove(p);
            questionTV.setText(item.question);
        	answerTV.setText(item.answer);
        	imageIV.setImageResource(item.isTag?R.drawable.star_full:R.drawable.star_empty);
	    }
	    
	    private void handerShow(){
	    	if(isShow){
	    		lookBtn.setText("���ش�");
	    		answerTV.setVisibility(View.VISIBLE);
	    		
	    	}else{
	    		lookBtn.setText("�鿴��");
	    		answerTV.setVisibility(View.GONE);
	    	}
	    }
	   
}
