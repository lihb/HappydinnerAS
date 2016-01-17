package com.handgold.pjdc.services;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 视频播放接口
 *
 * @author Administrator
 */
public interface IVideoPlayer {

    void release();

    long getCurrentPosition();

    long getDuration();

    boolean isPlaying();

    int getVideoWidth();

    int getVideoHeight();

    void setSurfaceView(SurfaceView videoView);

    void setDisplay(SurfaceHolder sh);

    void setDataSource(Context context, Uri uri);

    void prepareAsync();

    void start();

    void stop();

    void pause();

    void resume();

    void seekTo(long msec);

    void setOnPreparedListener(IVideoPlayer.OnPreparedListener listener);

    void setOnCompletionListener(IVideoPlayer.OnCompletionListener listener);

    void setOnErrorListener(IVideoPlayer.OnErrorListener listener);

    public interface OnPreparedListener {
        void onPrepared(IVideoPlayer mp);
    }

    public interface OnCompletionListener {
        void onCompletion(IVideoPlayer mp);
    }

    public interface OnErrorListener {
        boolean onError(IVideoPlayer mp, int what, int extra);
    }
}
