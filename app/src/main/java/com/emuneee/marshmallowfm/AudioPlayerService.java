package com.emuneee.marshmallowfm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;

public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String SESSION_TAG = "mmFM";

    private MediaSession mMediaSession;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private int mPlaybackState = PlaybackState.STATE_NONE;

    public class ServiceBinder extends Binder {

        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }

    private MediaSession.Callback mMediaSessionCallback = new MediaSession.Callback() {

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {

            try {
                switch (mPlaybackState) {
                    case PlaybackState.STATE_PLAYING:
                    case PlaybackState.STATE_PAUSED:
                        mMediaPlayer.reset();
                        mMediaPlayer.setDataSource(AudioPlayerService.this, uri);
                        mMediaPlayer.prepare();
                        break;
                    case PlaybackState.STATE_NONE:
                        mMediaPlayer.setDataSource(AudioPlayerService.this, uri);
                        mMediaPlayer.prepare();
                        break;

                }
            } catch (IOException e) {

            }

        }

        @Override
        public void onPlay() {
            switch (mPlaybackState) {
                case PlaybackState.STATE_PAUSED:
                    mMediaPlayer.start();
                    break;

            }
        }

        @Override
        public void onPause() {
            switch (mPlaybackState) {
                case PlaybackState.STATE_PLAYING:
                    mMediaPlayer.pause();
                    break;

            }
        }

        @Override
        public void onRewind() {
            switch (mPlaybackState) {
                case PlaybackState.STATE_PLAYING:
                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 10000);
                    break;

            }
        }

        @Override
        public void onFastForward() {
            switch (mPlaybackState) {
                case PlaybackState.STATE_PLAYING:
                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 10000);
                    break;

            }
        }
    };

    public AudioPlayerService() {
    }

    public MediaSession.Token getMediaSessionToken() {
        return mMediaSession.getSessionToken();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 1) set up media session and media session callback
        mMediaSession = new MediaSession(this, SESSION_TAG);
        mMediaSession.setCallback(mMediaSessionCallback);
        mMediaSession.setActive(true);

        // 2) get instance to AudioManager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // 3) create our media player
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mMediaSession.release();
    }
}
