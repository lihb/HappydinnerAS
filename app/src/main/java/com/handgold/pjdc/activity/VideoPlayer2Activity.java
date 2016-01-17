package com.handgold.pjdc.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.BaseActivity;
import com.handgold.pjdc.services.IVideoPlayer;
import com.handgold.pjdc.services.SystemMediaPlayer;
import com.handgold.pjdc.util.CommonUtils;
import com.handgold.pjdc.util.DLog;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/3/13
 */

public class VideoPlayer2Activity extends BaseActivity implements IVideoPlayer.OnCompletionListener, IVideoPlayer.OnPreparedListener, IVideoPlayer.OnErrorListener {

    private static final String TAG = "VideoPlayer2Activity";

    private static final int sDefaultTimeout = 5000;

    private static final int SHOW_PROGRESS = 11;

    private static final int FADE_OUT = 10;

    private static final float STEP_PROGRESS = 10f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快

    private static final int GESTURE_MODIFY_PROGRESS = 1;

    private static final int GESTURE_MODIFY_VOLUME = 2;

    @InjectView(R.id.tv_video_close)
    TextView mTvVideoClose;

    @InjectView(R.id.video_playorpause)
    ImageView mPlayorPause;

    @InjectView(R.id.video_newSeekbar)
    SeekBar mCurrenctSeekBar;

    @InjectView(R.id.surfaceviewPlayer)
    SurfaceView mSurface;

    @InjectView(R.id.surfaceFramePlayer)
    FrameLayout mSurfaceFrame;

    @InjectView(R.id.video_file_txt)
    TextView mFileNameTv;

    @InjectView(R.id.vodeo_hasplayedtime_txt)
    TextView mCurrentTime;

    @InjectView(R.id.video_endtime_txt)
    TextView mEndTime;

    @InjectView(R.id.video_top_control)
    RelativeLayout mTopControl;

    @InjectView(R.id.video_bottom_control)
    LinearLayout mBottomControl;

    private AudioManager mAudioMgr;

    private SurfaceHolder mSurfaceHolder;

    private SystemMediaPlayer mVideoPlayer;

    // 系统最大声音
    private int mMaxVolume;

    // 当前声音大小
    private int mVolume = -1;

    // 当前亮度
    private float mBrightness = -1f;

    private boolean mDragging;

    private boolean mShowing;

    private GestureDetector mGestureDetector;

    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志

    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量

    private Intent mIntent;

    private Dialog dialog;

    private String mVideoUrl; //播放地址

    /**
     * 视频名字
     */
    private String mFileNameStr = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_player2);
        ButterKnife.inject(this);
        mIntent = getIntent();

        mVideoUrl = mIntent.getStringExtra("videoUrl");

        Uri uri = null;

        uri = Uri.parse(mVideoUrl);
        DLog.i(TAG, "uri: " + uri.toString());


        initView();
        initFocus();
        requestAudioFocus();


        try {
            mVideoPlayer.setDataSource(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mVideoPlayer.prepareAsync();

    }

    private void initView() {

        mFileNameStr = mIntent.getStringExtra("fileName");
        mFileNameTv.setText(mFileNameStr);

        mTvVideoClose.setOnClickListener(mOnClickListener);
        mPlayorPause.setOnClickListener(mOnClickListener);

        mSurfaceHolder = mSurface.getHolder();


        mSurfaceFrame.setDrawingCacheEnabled(false);

        // video player
        mVideoPlayer = new SystemMediaPlayer((VideoView) mSurface);
        DLog.i(TAG, "new MediaPlayer();");


        // set callback
        mVideoPlayer.setOnPreparedListener(this);
        mVideoPlayer.setOnCompletionListener(this);
        mVideoPlayer.setOnErrorListener(this);

        mSurface.setOnClickListener(mOnClickListener);
        mSurfaceFrame.setOnClickListener(mOnClickListener);

        mSurfaceFrame.setOnTouchListener(mOnTouchListener);
        mSurface.setLongClickable(true);
        mSurfaceFrame.setLongClickable(true);

        mCurrenctSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        mGestureDetector.setIsLongpressEnabled(true);

    }

    // 按钮的监听事件
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_video_close: // 关闭
                    setResult(RESULT_OK);
                    finish();
                    break;
                case R.id.video_playorpause: // 开始、暂停
                    doPauseResume();
                    break;
                default:
                    break;
            }

        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            DLog.i(TAG, "mOnTouchListener");
            mGestureDetector.onTouchEvent(event);
            return true; // 一定要返回true
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hideControllerView();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    // seekbar事件
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        int newProgress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            newProgress = progress;
            if (mCurrentTime != null)
                mCurrentTime.setText(millisToString(progress, false));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            showControllerView(sDefaultTimeout);
            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            DLog.d(TAG, "SeekBar onStopTrackingTouch()");
            mDragging = false;
            showControllerView(sDefaultTimeout);
            seekTo(newProgress);
        }
    };


    /**
     * 系统音频变化监听
     */
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = null;

    @Override
    public void onCompletion(IVideoPlayer videoPlayer) {
        finish();
    }

    @Override
    public boolean onError(IVideoPlayer mp, int what, int extra) {
        dialog = new Dialog(VideoPlayer2Activity.this);
        dialog.setTitle("播放错误");
       /* dialog.setonsetOnPositiveClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                VideoPlayer2Activity.this.dialog.dismiss();
                finish();
            }
        });
        dialog.show();*/
        return true;
    }

    @Override
    public void onPrepared(IVideoPlayer videoPlayer) {
        mPlayorPause.setImageResource(R.drawable.controller_pause_selector);

        int videoWidth = videoPlayer.getVideoWidth();
        int videoHeight = videoPlayer.getVideoHeight();
        if (videoHeight != 0 && videoWidth != 0) {
            videoPlayer.start();
        }
    }

    /**
     * 滑动改变声音大小
     *
     * @param distanceY
     */
    private void onVolumeSlide(float distanceY) {
        if (distanceY >= CommonUtils.dip2px(VideoPlayer2Activity.this, STEP_PROGRESS)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
            if (mVolume < mMaxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                mVolume++;
            }
        } else if (distanceY <= -CommonUtils.dip2px(VideoPlayer2Activity.this, STEP_PROGRESS)) {// 音量调小
            if (mVolume > 0) {
                mVolume--;
                if (mVolume == 0) {// 静音
                    Toast.makeText(this, "静音", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (mAudioMgr != null) {
            mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
        }
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurface.requestFocus();
        start();
        showControllerView(sDefaultTimeout);
        MobclickAgent.onResume(this);
    }

    /**
     * 显示或隐藏控制栏开关
     */
    private void toggleControlsVisiblity() {
        DLog.i(TAG, "toggleControlsVisiblity()");
        if (mShowing) {
            hideControllerView();
        } else {
            showControllerView(sDefaultTimeout);
        }

    }

    /**
     * 显示控制栏
     */
    private void showControllerView(int timeout) {
        DLog.i(TAG, "showControllerView()");
        if (mVideoPlayer != null) {
            if (mVideoPlayer.getDuration() <= 0) {
                mCurrenctSeekBar.setEnabled(false);
            } else {
                mCurrenctSeekBar.setEnabled(true);
            }
        }
        if (!mShowing) {
            setProgress();
            if (mPlayorPause != null) {
                mPlayorPause.requestFocus();
            }
            if (mTopControl != null) {
                mTopControl.setVisibility(View.VISIBLE);
            }
            if (mBottomControl != null) {
                mBottomControl.setVisibility(View.VISIBLE);
            }
            mShowing = true;
        }
        updatePausePlay(isPlaying());

        // cause the progress bar to be updated even if mShowing
        // was already true. This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }

    }

    /**
     * 隐藏控制栏
     */
    private void hideControllerView() {
        if (mShowing) {
            mTopControl.setVisibility(View.GONE);
            mBottomControl.setVisibility(View.GONE);
            mHandler.removeMessages(SHOW_PROGRESS);
            mShowing = false;
        }
    }

    /**
     * 设置视频的播放进度
     *
     * @return
     */
    private int setProgress() {
        DLog.i(TAG, "setProgress()");
        if (mVideoPlayer == null || mDragging) {
            return 0;
        }
        int position = (int) getCurrentPosition();
        int duration = (int) getDuration();
        DLog.i(TAG, "duration = " + duration);
        DLog.i(TAG, "position = " + position);
        if (mCurrenctSeekBar != null) {
            if (duration > 0) {
                mCurrenctSeekBar.setMax(duration);
                mCurrenctSeekBar.setProgress(position);
            } else {
                mCurrenctSeekBar.setProgress(0);
            }
        }

        if (mEndTime != null)
            mEndTime.setText(millisToString(duration, false));
        if (mCurrentTime != null)
            mCurrentTime.setText(millisToString(position, false));

        return position;
    }

    /**
     * 获取视频当前播放进度
     *
     * @return
     */
    private long getCurrentPosition() {
        if (mVideoPlayer != null) {
            long currentPosition = mVideoPlayer.getCurrentPosition();
            return currentPosition;
        }
        return 0;
    }

    /**
     * 获取视频总长度
     *
     * @return
     */
    private long getDuration() {
        if (mVideoPlayer != null) {
            long length = mVideoPlayer.getDuration();
            return length;
        }
        return 0;
    }

    /**
     * 切换暂停开始状态
     */
    private void doPauseResume() {
        if (isPlaying()) {
            updatePausePlay(false);
            pause();
        } else {
            updatePausePlay(true);
            resume();
        }
    }

    /**
     * 切换按钮状态，播放或者暂停
     */
    private void updatePausePlay(boolean isPlaying) {
        if (mPlayorPause == null) {
            return;
        }
        if (isPlaying) {
            mPlayorPause.setImageResource(R.drawable.controller_pause_selector);
        } else {
            mPlayorPause.setImageResource(R.drawable.controller_play_selector);
        }
    }

    private boolean isPlaying() {
        if (mVideoPlayer != null) {
            return mVideoPlayer.isPlaying();
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoPlayer != null) {
            mVideoPlayer.stop();
        }
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
        dialog = null;
    }

    /**
     * 开始播放
     */
    private void start() {
        requestAudioFocus();
        if (mVideoPlayer != null) {
            mVideoPlayer.start();
            mVideoPlayer.seekTo(0);
        }
    }

    private void seekTo(int msec) {
        if (mVideoPlayer != null) {
            mVideoPlayer.seekTo(msec);
        }
    }

    /**
     * 暂停
     */
    private void pause() {
        if (mVideoPlayer != null) {
            abandonAudioFocus();
            mVideoPlayer.pause();
        }
    }

    /**
     * 播放（暂停后播放）
     */
    private void resume() {
        if (mVideoPlayer != null) {
            mVideoPlayer.start();
        }
    }

    /**
     * 获取系统音频接口
     */
    @SuppressLint("NewApi")
    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }
        if (mAudioMgr == null)
            mAudioMgr = (AudioManager) ApplicationEx.instance.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (mAudioMgr != null) {
            DLog.i(TAG, "Request audio focus");
            int ret =
                    mAudioMgr.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN);
            if (ret != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                DLog.i(TAG, "request audio focus fail. " + ret);
            }
        }

    }

    /**
     * 解绑系统音频接口
     */
    @SuppressLint("NewApi")
    private void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }
        if (mAudioMgr != null) {

            DLog.i(TAG, "Abandon audio focus");

            mAudioMgr.abandonAudioFocus(mAudioFocusChangeListener);

            mAudioMgr = null;
        }
    }

    @SuppressLint("NewApi")
    private void initFocus() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
            mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    DLog.i(TAG, "focusChange------>>" + focusChange);
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS || focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        DLog.i(TAG, "audio focus loss");
                        if (isPlaying()) {
                            pause();
                        }
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        DLog.i(TAG, "audio focus gain");
                    }
                }
            };
        }
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            firstScroll = true;
            return super.onDown(e);
        }

        /**
         * 单击屏幕事件
         *
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            toggleControlsVisiblity();
            return super.onSingleTapConfirmed(e);
        }

        /**
         * 滑动调节音量、屏幕亮度事件
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                // 横向的距离变化大则调整进度，纵向的变化大则调整音量
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                } else {
                    GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                }
            }
            // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
            if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
                showControllerView(sDefaultTimeout);
                int position = (int) getCurrentPosition();
                int duration = (int) getDuration();
                if (position <= 0 || duration <= 0) {
                    return false;
                }
                // distanceX=lastScrollPositionX-currentScrollPositionX，因此为正时是快进
                if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                    if (distanceX >= CommonUtils.dip2px(VideoPlayer2Activity.this, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
                        if (position > 3 * 1000) {// 避免为负
                            position -= 3 * 1000;// scroll方法执行一次快退3秒
                        } else {
                            position = 3 * 1000;
                        }
                        seekTo(position); // 快退到position处
                    } else if (distanceX <= -CommonUtils.dip2px(VideoPlayer2Activity.this, STEP_PROGRESS)) {// 快进
                        if (position < getDuration() - 16 * 1000) {// 避免超过总时长
                            position += 3 * 1000;// scroll执行一次快进3秒
                        } else {
                            position = duration - 10 * 1000;
                        }
                        seekTo(position); // 快进到position处
                    }
                }

                if (mCurrentTime != null)
                    mCurrentTime.setText(millisToString(position, false));
                if (mCurrenctSeekBar != null) {
                    mCurrenctSeekBar.setMax(duration);
                    mCurrenctSeekBar.setProgress(position);
                }

            }
            // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
            else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
                if (mAudioMgr != null) {
                    mVolume = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
                }
                if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                    float mOldX = e1.getX(), mOldY = e1.getY();
                    int y = (int) e2.getRawY();
                    Display disp = getWindowManager().getDefaultDisplay();
                    int windowWidth = disp.getWidth();
                    int windowHeight = disp.getHeight();

                    if (mOldX > windowWidth * 1.0 / 2) {// 右边滑动调节声音
                        onVolumeSlide(distanceY);
                    } else if (mOldX < windowWidth / 2.0) {// 左边滑动调节亮度
                        onBrightnessSlide((mOldY - y) / windowHeight);
                    }
                }

            }

            firstScroll = false;// 第一次scroll执行完成，修改标志
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private static String millisToString(long millis, boolean text) {
        boolean negative = millis < 0L;
        millis = Math.abs(millis);
        millis /= 1000L;
        int sec = (int) (millis % 60L);
        millis /= 60L;
        int min = (int) (millis % 60L);
        millis /= 60L;
        int hours = (int) millis;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        String time;
        if (text) {
            if (millis > 0L) {
                time = (negative ? "-" : "") + hours + "h" + format.format((long) min) + "min";
            } else if (min > 0) {
                time = (negative ? "-" : "") + min + "min";
            } else {
                time = (negative ? "-" : "") + sec + "s";
            }
        } else if (millis > 0L) {
            time = (negative ? "-" : "") + hours + ":" + format.format((long) min) + ":" + format.format((long) sec);
        } else {
            time = (negative ? "-" : "") + min + ":" + format.format((long) sec);
        }

        return time;
    }

}
