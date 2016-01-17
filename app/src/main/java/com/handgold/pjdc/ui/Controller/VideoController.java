package com.handgold.pjdc.ui.Controller;

import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;


/**
 * Created by Administrator on 2015/9/30.
 */
public class VideoController {

    /**
     * 用来播放的videoView
     */
    private VideoView mVideoView;

    /**
     * 播放的文件地址，在线的或者本地的
     */
    private Uri mUri;

    public VideoView getVideoView() {
        return mVideoView;
    }

    public void setVideoView(VideoView videoView) {
        mVideoView = videoView;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    /**
     * 开始播放
     * @param msec 播放的位置
     */
    public void play(int msec) {
        mVideoView.setVideoURI(mUri);
        mVideoView.seekTo(msec);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mOnVideoControllerListener != null) {
                    mOnVideoControllerListener.onCompletion();
                }
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mOnVideoControllerListener != null) {
                    mOnVideoControllerListener.onError();
                }
                return false;
            }
        });
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            if (mOnVideoControllerListener != null) {
                mOnVideoControllerListener.onPause(mVideoView.getCurrentPosition());
            }
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
            if (mOnVideoControllerListener != null) {
                mOnVideoControllerListener.onStop();
            }
        }
    }

    /**
     * 获取当前播放位置
     */
    public int getCurrPos() {
        int curPos = 0;
        if (mVideoView != null) {
            curPos = mVideoView.getCurrentPosition();
        }
        return curPos;
    }

    /**
     * 获取视频总时长
     */
    public int getTotalLength() {
        int length = 0;
        if (mVideoView != null) {
            length = mVideoView.getDuration();
        }
        return length;
    }

    private OnVideoControllerListener mOnVideoControllerListener = null;

    public void setOnVideoControllerListener(OnVideoControllerListener listener) {
        mOnVideoControllerListener = listener;
    }


    public interface OnVideoControllerListener{
        void onCompletion();
        void onError();
        void onPause(int pos);
        void onStop();
    }
}
