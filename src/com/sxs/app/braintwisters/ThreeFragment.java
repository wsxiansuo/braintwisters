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
            if (sensorManager != null) {// 注册监听器  
                sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);  
                // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率  
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
        if (sensorManager != null) {// 取消监听器  
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
			 getBaseActivity().showToast(item.isTag?"成功添加收藏！":"成功去除收藏！");
		 }
	 }
	  
	    /** 
	     * 重力感应监听 
	     */  
	    private SensorEventListener sensorEventListener = new SensorEventListener() {  
	  
	        @Override  
	        public void onSensorChanged(SensorEvent event) {  
	            // 传感器信息改变时执行该方法  
	        	int sensorType = event.sensor.getType();  
	            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴  
	            float[] values = event.values;  
	            if (sensorType == Sensor.TYPE_ACCELEROMETER)  
	            {  
	                if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math  
	                        .abs(values[2]) > 17))  
	                {  
	                    //摇动手机后，再伴随震动提示~~  
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
	    		lookBtn.setText("隐藏答案");
	    		answerTV.setVisibility(View.VISIBLE);
	    		
	    	}else{
	    		lookBtn.setText("查看答案");
	    		answerTV.setVisibility(View.GONE);
	    	}
	    }
	   
}
