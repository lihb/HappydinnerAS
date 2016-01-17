package com.handgold.pjdc.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

public class SystemMediaPlayer implements IVideoPlayer {

    private VideoView mPlayer;

    public SystemMediaPlayer(VideoView videoView) {
        super();
        mPlayer = videoView;
    }

    @Override
    public void release() {
        mPlayer = null;
    }

    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void setSurfaceView(SurfaceView videoView) {
        // mPlayer.set
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        try {
            mPlayer.setVideoURI(uri);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareAsync() {
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void stop() {
        mPlayer.stopPlayback();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void resume() {
        mPlayer.start();
    }

    @Override
    public void seekTo(long msec) {
        mPlayer.seekTo((int) msec);
    }

    @Override
    public void setOnPreparedListener(final OnPreparedListener listener) {
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (listener != null) {
                    listener.onPrepared(SystemMediaPlayer.this);
                }
            }
        });
    }

    @Override
    public void setOnCompletionListener(final OnCompletionListener listener) {
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                if (listener != null) {
                    listener.onCompletion(SystemMediaPlayer.this);
                }
            }
        });
    }

    @Override
    public void setOnErrorListener(final OnErrorListener listener) {
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (listener != null) {
                    return listener.onError(SystemMediaPlayer.this, what, extra);
                }
                return false;
            }
        });
    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    public void setAudioStreamType(int streamType) {
    }

}
