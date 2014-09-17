package com.sxs.app.braintwisters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sxs.app.data.BraintwisterVO;
import com.sxs.app.data.MapStringUtil;
import com.sxs.app.data.UserDataModel;

public class SecondFragment extends BaseFragment {
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState); 
        setIsShow(false);
        listdata = UserDataModel.instance().mgr.queryQuestionList("",false);
        refresh(null);
        isFirst = true;
    } 
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		View view = getInjectedView(inflater,R.layout.activity_second);
        return view;  
    }
	@ViewInject(R.id.rl_top_btn_group)           RelativeLayout  topRL;
	@ViewInject(R.id.second_question_tv)         TextView   questionTV;
	@ViewInject(R.id.second_answer_tv)           TextView   answerTV;
	@ViewInject(R.id.btn_new_timu)				Button     refreshBtn;
	@ViewInject(R.id.btn_look_result)			Button	   resultBtn;
	
	@ViewInject(R.id.btn_stop)					Button     stopBtn;
	@ViewInject(R.id.btn_next)					Button     nextBtn;
	
	@ViewInject(R.id.tv_xiangdaole)             TextView   xiangdaoTV;
	@ViewInject(R.id.tv_title)                  TextView   titleTV;
	@ViewInject(R.id.tv_every_time)             TextView   itemTimeTV;
	@ViewInject(R.id.tv_total_time)             TextView   totalTimeTV;
	
	@ViewInject(R.id.iv_shoucang)               ImageView   imageIV;
	@ViewInject(R.id.sv_question_view)          ScrollView questionSV;
	@ViewInject(R.id.rl_test_score)				LinearLayout scoreLL;
	
	@ViewInject(R.id.tv_result_score)             TextView   rtScoreTV;
	@ViewInject(R.id.tv_result_short_time)        TextView   rtShortTV;
	@ViewInject(R.id.tv_result_long_time)         TextView   rtLongTV;
	@ViewInject(R.id.tv_result_total_time)        TextView   rtTotalTV;
	
	
	private static int count = 10;
	private int currIndex = 0;
    private Boolean isShow = false;
    private Boolean isFirst = false;
    private BraintwisterVO item;
    private List<BraintwisterVO> listdata;
    private List<BraintwisterVO> myGameList;
    
    private long timepoint;
    private long itempoint;
    
    private TimeCount itemTime;
    private TimeCount totalTime;
    private long timeArr[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    private int  result[] = {0,0,0,0,0,0,0,0,0,0};
    
    /**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
		handerShow();
	}
    

	@Override  
    public void onResume() {  
        super.onResume();
        if(!isShow && !isFirst){
        	if(itemTime != null)
            {
        		itemTime = new TimeCount(itempoint, 1000,false);//构造CountDownTimer对象
        		itemTime.start();
            }
        	if(totalTime != null){
        		totalTime = new TimeCount(timepoint, 1000,true);
        		totalTime.start();
        	}
        }
        isFirst = false;
    }
	@Override 
	public void onPause() {   
		super.onPause();   
//	        Log.d("aaa", "onPause");   
	    }  
	 @Override
     public void onStop() {
         super.onStop();
//         Log.i("aaa", "onStop");
    	 if(itemTime != null){
    		 itemTime.cancel();
    	 }
    	 if(totalTime != null){
    		 totalTime.cancel();
    	 }
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
	@OnClick(R.id.btn_new_timu)
	public void refresh(View v){
		if(itemTime != null){
        	itemTime.cancel();
        	itemTime = null;
        }
        if(totalTime != null){
    		totalTime.cancel();
    		totalTime = null;
    	}
		if(listdata != null){
        	currIndex = 0;
        	timeArr = new long[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        	result  = new int[]{0,0,0,0,0,0,0,0,0,0};
        	myGameList = new ArrayList<BraintwisterVO>();
        	for(int i=0; i<count;i++){
        		int p = MapStringUtil.getRandomNum(listdata.size());
        		myGameList.add(listdata.get(p));
        	}
        	changeQuestion();
        	if(totalTime != null){
        		totalTime.cancel();
        	}
        	totalTime = new TimeCount(10 * 60000, 1000, true);
        	totalTime.start();
        }
	}
	 @OnClick(R.id.btn_look_result)
	 public void lookClick(View v){
		 setIsShow(!isShow);
		 if(totalTime != null){
			 totalTime.onFinish();
		 }
		 currIndex = 0;
		 changeQuestion();
	 }
	 @OnClick(R.id.btn_stop)
	 public void stopClick(View v){
		 if(!isShow){
			 if(item != null){
				 getBaseActivity().shareMessage(item.question);
			 }
		 }else{
			 result[currIndex] = 1;
			 currIndex++;
			 changeQuestion();
		 }
	 }
	 
	 @OnClick(R.id.btn_next)
	 public void nextClick(View v){
		 if(!isShow){
			 if(itemTime != null){
	        	itemTime.onFinish();
	        	itemTime.cancel();
	        	itemTime = null;
	         }
			 currIndex++;
			 changeQuestion();
		 }else{
			 result[currIndex] = 0;
			 currIndex++;
			 changeQuestion();
		 }
	 }
	 
	 @OnClick(R.id.btn_test_again)
	 public void testAgainClick(View v){
		 questionSV.setVisibility(View.VISIBLE);
		 scoreLL.setVisibility(View.GONE);
		 setIsShow(!isShow);
		 refresh(null);
	 }
	 
    private void changeQuestion(){
    	if(currIndex < count){
    		titleTV.setText("第  "+(currIndex + 1)+"/" + count + "  题");
            item = myGameList.get(currIndex);
            questionTV.setText(item.question);
            if(isShow){
            	answerTV.setText(item.answer);
            	imageIV.setImageResource(item.isTag?R.drawable.star_full:R.drawable.star_empty);
            }else{
            	itemTime = new TimeCount(60000, 1000 ,false);
            	itemTime.start();
            }
    	}else if(isShow){//去计算结果
   		 	questionSV.setVisibility(View.GONE);
   		 	scoreLL.setVisibility(View.VISIBLE);
   		 	int score = 0;
   		 	long maxMM = 0;
   		 	long minMM = 0;
   		 	for(int i = 0;i < result.length;i++)
   		 	{
   		 		long timesc = timeArr[i];
   		 		if(timesc > 0 && maxMM < timesc){
   		 			maxMM = timesc;
   		 		}
   		 		if(timesc > 0 && minMM > timesc){
   		 			minMM = timesc;
   		 		}
   		 		if(result[i] != 0){
   		 			if(timesc < 0){
   		 			}
   		 			else if(timesc <= 10000){
   		 				score += 10;
   		 			}else if(timesc <= 20000){
   		 				score += 9;
   		 			}else if(timesc <= 30000){
   		 				score += 8;
   		 			}else if(timesc <= 40000){
   		 				score += 7;
   		 			}else if(timesc <= 50000){
   		 				score += 6;
   		 			}
   		 		}
   		 	}
   		 	rtScoreTV.setText("本次测试成绩:"+ score +"分");
   		 	rtShortTV.setText("最短思考时间:" + MapStringUtil.getTimeStr(minMM));
   		 	rtLongTV.setText("最长思考时间:" + MapStringUtil.getTimeStr(maxMM));
   		 	rtTotalTV.setText("测试所用时间:" + MapStringUtil.getTimeStr(600000 - timepoint));
   		 	
    	}else{
    		titleTV.setText("题目已全部回答完毕");
    		if(itemTime != null){
    			itemTime.onFinish();
    			itemTime.cancel();
    			itemTime = null;
    		}
    		if(totalTime != null){
    			totalTime.onFinish();
    			totalTime.cancel();
    			totalTime = null;
    		}
    		changeTextColor(2,0);
    	}

    }
	    
    private void handerShow(){
    	if(isShow){
    		topRL.setVisibility(View.GONE);
    		xiangdaoTV.setVisibility(View.VISIBLE);
    		answerTV.setVisibility(View.VISIBLE);
    		stopBtn.setText("我猜对了");
    		nextBtn.setText("我猜错了");
    		totalTimeTV.setVisibility(View.GONE);
    		imageIV.setVisibility(View.VISIBLE);
    	}else{
    		topRL.setVisibility(View.VISIBLE);
    		xiangdaoTV.setVisibility(View.GONE);
    		answerTV.setVisibility(View.GONE);
    		stopBtn.setText("分享朋友");
    		nextBtn.setText("下一题目");
    		totalTimeTV.setVisibility(View.VISIBLE);
    		imageIV.setVisibility(View.GONE);	
    	}
    }
    /**
     * 
     * @param mode 0:total 1:item 2:over
     * @param time
     */
    private void changeTextColor(int mode,long time){
    	int id = 0;
    	if(mode == 0){
    		id = time <= 60000 ? R.color.color_red:R.color.color_green;
    		totalTimeTV.setTextColor(getActivity().getResources().getColor(id));
    	}else if(mode == 1){
    		id = time <= 10000 ? R.color.color_red:R.color.color_green;
    		itemTimeTV.setTextColor(getActivity().getResources().getColor(id));
    	}else{
    		itemTimeTV.setText("00:00");
    		itemTimeTV.setTextColor(getActivity().getResources().getColor(R.color.color_mid_gray));
    	}
    }
    
	class TimeCount extends CountDownTimer {
		private Boolean totalMode = false;
		public TimeCount(long millisInFuture, long countDownInterval, Boolean mode) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
			totalMode = mode;
			if(totalMode){
				totalTimeTV.setText(MapStringUtil.getTimeStr(millisInFuture));
				changeTextColor(0,millisInFuture);
			}else{
				itemTimeTV.setText(MapStringUtil.getTimeStr(millisInFuture));
				changeTextColor(1,millisInFuture);
			}
		}
		@Override
		public void onFinish() {//计时完毕时触发
			if(totalMode){
				
			}else{
				timeArr[currIndex] = 60000 - itempoint;
				itemTimeTV.setText(MapStringUtil.getTimeStr(itempoint - 1000));
				changeTextColor(1,itempoint);
			}
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			if(totalMode){
				timepoint = millisUntilFinished;
				totalTimeTV.setText(MapStringUtil.getTimeStr(millisUntilFinished));
				changeTextColor(0,timepoint);
			}else{
				itempoint = millisUntilFinished;
				itemTimeTV.setText(MapStringUtil.getTimeStr(millisUntilFinished));
				changeTextColor(1,itempoint);
			}
		}
	}
}
