package com.handgold.pjdc.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.ui.Controller.VideoController;
import com.handgold.pjdc.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/30.
 */
public class PopupGameVideoView extends RelativeLayout {

    private RoundedImageView mVideoImage = null;

    private ImageView mCloseImage = null;

    private ImageView mPlayImage = null;

    private VideoView mVideoView = null;

    private FrameLayout mPopupVideoViewFramelayout = null;

    private VideoController mVideoController = null;

    private boolean showPlayIcon = true;

    private int mCurrPos = 0;

    public PopupGameVideoView(Context context) {
        super(context);
    }

    public PopupGameVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupGameVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mVideoImage = (RoundedImageView) findViewById(R.id.video_pic);
        mCloseImage = (ImageView) findViewById(R.id.iv_popup_close);
        mPlayImage = (ImageView) findViewById(R.id.iv_popup_play);

        mVideoView = (VideoView) findViewById(R.id.popup_videoview);

        mPopupVideoViewFramelayout = (FrameLayout) findViewById(R.id.popup_videoview_framelayout);

        mVideoImage.setOnClickListener(mOnClickListener);
        mCloseImage.setOnClickListener(mOnClickListener);
        mPlayImage.setOnClickListener(mOnClickListener);
        mPopupVideoViewFramelayout.setOnClickListener(mOnClickListener);
        initData(null);
    }

    private VideoController.OnVideoControllerListener mOnVideoControllerListener = new VideoController.OnVideoControllerListener() {
        @Override
        public void onCompletion() {
            showPlayIcon = true;
            togglePlayVideo();

            // 视频播放完毕，统计完毕的次数
            HashMap<String,String> map = new HashMap<String, String>();
            if (mVideoController != null) {
                map.put("video_uri", mVideoController.getUri().toString());
                map.put("total_length", CommonUtils.formatTimeLength(mVideoController.getTotalLength() / 1000));
                if (mListener != null) {
                    mListener.onVideoCompletion(map);
                }
            }
            exitView();
        }

        @Override
        public void onError() {
            if (mVideoController != null) {
                mVideoController.stop();
            }
            showPlayIcon = true;
            togglePlayVideo();
        }

        @Override
        public void onPause(int pos) {
            showPlayIcon = true;
            togglePlayVideo();
            mCurrPos = pos;
        }

        @Override
        public void onStop() {
            showPlayIcon = true;
            togglePlayVideo();
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == mPlayImage || v == mVideoImage) {
                showPlayIcon = false;
                togglePlayVideo();
                if (mVideoController == null) {
                    mVideoController = new VideoController();
                    mVideoController.setVideoView(mVideoView);
                    Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.produce);
//                    Uri uri2 = Uri.parse(mMenuData.getVideoUrl());
                    mVideoController.setUri(uri);
                    mVideoController.setOnVideoControllerListener(mOnVideoControllerListener);
                    mVideoController.play(0);
                } else {
                    mVideoController.play(mCurrPos);
                }
            } else if (v == mCloseImage) {

                // 点击关闭按钮，统计出用户的播放时长
                Map<String, String> map_value = new HashMap<String, String>();
                if (mVideoController != null) {
                    map_value.put("video_uri", mVideoController.getUri().toString());
                    map_value.put("total_length", CommonUtils.formatTimeLength(mVideoController.getTotalLength() / 1000));
                    map_value.put("curr_position", CommonUtils.formatTimeLength(mVideoController.getCurrPos() / 1000));
                    if (mListener != null) {
                        mListener.onCloseBtnEvent(map_value, mVideoController.getCurrPos());
                    }
                }
                exitView();
            }/* else if (v == mPopupVideoViewFramelayout) {
                if (mVideoController != null) {
                    mVideoController.pause();
                }
            }*/
        }
    };

    /**
     * 切换显示视频播放的UI
     */
    private void togglePlayVideo() {
        mPlayImage.setVisibility(showPlayIcon ? VISIBLE : GONE);
        mVideoImage.setVisibility(showPlayIcon ? VISIBLE : GONE);
        mPopupVideoViewFramelayout.setVisibility(showPlayIcon ? GONE : VISIBLE);
    }

    /**
     * 退出该View
     */
    public void exitView() {
        if (getAnimation() != null) {
            return;
        }

        if (mVideoController != null) {
            mVideoController.stop();
            // 修复从别的activity返回到该界面的activity时，视频会再次播放的bug
            mVideoView.setVideoURI(null);
            mVideoController = null;
        }
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) getParent()).setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 初始化数据
     *
     * @param mUri
     */
    public void initData(Uri mUri) {

        showPlayIcon = false;
        togglePlayVideo();
        if (mVideoController == null) {
            mVideoController = new VideoController();
            mVideoController.setVideoView(mVideoView);
            Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.produce);
//                    Uri uri2 = Uri.parse(mMenuData.getVideoUrl());
            mVideoController.setUri(uri);
            mVideoController.setOnVideoControllerListener(mOnVideoControllerListener);
            mVideoController.play(0);
        }
    }

    /**
     * 判断该View是否正在显示
     *
     * @return
     */
    public boolean isVisible() {
        return (((ViewGroup) getParent()).getVisibility() == VISIBLE);
    }

    private OnPopGameVideoViewListener mListener = null;

    public void setOnPopGameVideoViewListener(OnPopGameVideoViewListener listener) {
        this.mListener = listener;
    }

    public interface OnPopGameVideoViewListener{
        void onCloseBtnEvent(Map<String, String> map_value, int curPos);

        void onVideoCompletion(Map<String, String> map);
    }
}
