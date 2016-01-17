package com.handgold.pjdc.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import com.handgold.pjdc.util.DLog;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.Executor;

public class BaseActivity extends Activity{

	private static final String USER_LOGIN_BUNDLE_KEY = "userLogin";
	private static final String USER_INFO_BUNDLE_KEY = "userInfo";
	private long mInTime;
	private long mOutTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/*UserInfo userInfo = MyAppContext.userInfo;
		if (userInfo != null) {
			// 保存用户信息
			String[] infoArray = { userInfo._loginName,
					String.valueOf(userInfo._available),
					String.valueOf(userInfo._capacity),
					String.valueOf(userInfo._maxFilesize) };
			outState.putStringArray(USER_INFO_BUNDLE_KEY, infoArray);
		}
		Session session = SessionManager.get().getCurSession();
		if (session != null) {
			// 保存会话信息
			String[] infoArray = { session.getAccount(),
					session.getSessionKey(), session.getSessionSecret(),
					String.valueOf(session.getKeepAlive()) };
			outState.putStringArray(USER_LOGIN_BUNDLE_KEY, infoArray);
		}*/
		DLog.d(getClass().getSimpleName(), "onSaveInstanceState()");
	}

	protected void loadSaveInstanceState(Bundle savedInstanceState) {
		/*UserInfo userInfo = MyAppContext.userInfo;

		String[] infoArray = savedInstanceState
				.getStringArray(USER_LOGIN_BUNDLE_KEY);
		if (infoArray != null) {
			// 恢复会话信息
			SessionManager.get().setCurSession(
					new Session(infoArray[0], infoArray[1], infoArray[2],
							Integer.parseInt(infoArray[3])));
		}

		String[] userInfoArray = savedInstanceState
				.getStringArray(USER_INFO_BUNDLE_KEY);
		if (userInfoArray != null) {
			// 恢复用户信息
			userInfo._loginName = userInfoArray[0];
			userInfo._available = Long.parseLong(userInfoArray[1]);
			userInfo._capacity = Long.parseLong(userInfoArray[2]);
			userInfo._maxFilesize = Long.parseLong(userInfoArray[3]);
		}*/

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			loadSaveInstanceState(savedInstanceState);
		}
		mInTime = System.currentTimeMillis()/1000;
		Log.i(getClass().getSimpleName(), "in time ------>> " + mInTime);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		initScreenDeminsion();
		// MenuItem menuItem = (MenuItem)this.findViewById(4);
		// this.getMenuInflater().
		DLog.d(getClass().getSimpleName(), "onCreate()");
	}

	// 获取和设置屏幕分辨率
	private void initScreenDeminsion() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyAppContext.screenW = dm.widthPixels;
		MyAppContext.screenH = dm.heightPixels;
		MyAppContext.screenDensity = dm.density;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		// 按返回的退出，自动从堆栈中退出
		// ApplicationEx application = (ApplicationEx) getApplication();
		// application.getActivityManager().popActivity(this);
	}

	@Override
	public void onDestroy() {
		mOutTime = System.currentTimeMillis()/1000;
		DLog.i(getClass().getSimpleName(), "out time ------>> "+mOutTime);
		super.onDestroy();
		/*mAutoCancelController.clean();
		mAutoCancelController = null;*/
		DLog.d(getClass().getSimpleName(), "OnDestroy()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		DLog.d(getClass().getSimpleName(), "OnPause()");
		/*ETransAgent.onPause(this);
		if (isFinishing()) {
			mAutoCancelController.clean();
		}*/
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		DLog.d(getClass().getSimpleName(), "OnResume()");
		//启动锁屏
/*		if(SetGesturePasswordActivity.isLockScreen){
			Intent intent = new Intent(this, SetGesturePasswordActivity.class);
			// 打开新的Activity
			startActivity(intent);
			SetGesturePasswordActivity.isLockScreen = false;
			
		}*/
		MobclickAgent.onResume(this);
	}

	public Executor getMainExecutor() {
		return ((ApplicationEx) getApplication()).getMainExecutor();

	}
	/**
	 * 实时操作，立即需要有响应的顺序执行器
	 * @return
	 */
	public Executor getJITExcutor() {
	    return ((ApplicationEx) getApplication()).getJITExcutor();
	}

	public Executor getSerialExecutor() {
		return ((ApplicationEx) getApplication()).getSerialExecutor();
	}

	public Executor getTransferExecutor() {
		return ((ApplicationEx) getApplication()).getTransferExecutor();
	}

	public Executor getNoTransferExcutor() {
		return ((ApplicationEx) getApplication()).getNoTransferExcutor();
	}
	
	public Executor getPicExcutor(){
		return ((ApplicationEx) getApplication()).getPicExcutor();
	}
	
	/**
     * 图片大图浏览任务执行器
     */
	public Executor getPictureDisplayExecutor(){
	    return ((ApplicationEx) getApplication()).getPictureDisplayExecutor();
	}

//	public void autoCancel(Cancellable task) {
//		mAutoCancelController.add(task);
//	}

//	public void removeAutoCancel(Cancellable task) {
//		mAutoCancelController.remove(task);
//	}

//	public AutoCancelController getAutoCancelController() {
//		return mAutoCancelController;
//	}

//	private AutoCancelController mAutoCancelController = new AutoCancelController();
	
	/**获得activity界面停留时间*/
	public String getStayTime(){
		DLog.i(getClass().getSimpleName(), "mOutTime - mInTime >>>" + mOutTime + "-"+mInTime +"=" + (mOutTime-mInTime));
		return String.valueOf(mOutTime - mInTime);
	}
	
	
	private float lastX;
	
	private float lastY;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastX = ev.getX();
			lastY = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			float  scrollX = ev.getX() - lastX;
			float  scrollY = ev.getY() - lastY;
			
			if(Math.abs(scrollY) < MyAppContext.screenH/10){
				if( scrollX >= 0.0 && Math.abs(scrollX) > MyAppContext.screenW/4
						&& onScrollRight(scrollX)){//向右
					return true;
				}else if(scrollX <= 0.0 && Math.abs(scrollX) > MyAppContext.screenW/4
						&& onScrollLeft(scrollX)){//向左
					return true;
				}
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	/**
	 * 向左滑动
	 * @param scrollX 滑动的距离
	 * @return true 消耗此次事件
	 */
	protected boolean onScrollRight(float scrollX){
		return false;
	}
	/**
	 * 向右滑动
	 * @param scrollX 滑动的距离
	 * @return true 消耗此次事件
	 */
	protected boolean onScrollLeft(float scrollX){
		return false;
	}
}
