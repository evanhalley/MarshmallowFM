package com.emuneee.marshmallowfm;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private MediaController mMediaController;

    private MediaController.Callback mMediaControllerCallback = new MediaController.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackState state) {

            switch (state.getState()) {
                case PlaybackState.STATE_PLAYING:
                    break;
                case PlaybackState.STATE_PAUSED:
                    break;
                case PlaybackState.STATE_FAST_FORWARDING:
                    break;
                case PlaybackState.STATE_REWINDING:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.play:
                if (mMediaController.getPlaybackState().getState() == PlaybackState.STATE_PLAYING) {
                    mMediaController.getTransportControls().pause();
                } else if(mMediaController.getPlaybackState().getState() == PlaybackState.STATE_PAUSED){
                    mMediaController.getTransportControls().play();
                } else {
                    Uri uri = Uri.parse("http://c.espnradio.com/s:5L8r1/audio/2580146/pti_2015-10-13-180346.64k.mp3");
                    mMediaController.getTransportControls().playFromUri(uri, null);
                }
                break;
            case R.id.rewind:
                mMediaController.getTransportControls().rewind();
                break;
            case R.id.forward:
                mMediaController.getTransportControls().fastForward();
                break;
        }

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        if (service instanceof AudioPlayerService.ServiceBinder) {
            mMediaController = new MediaController(MainActivity.this,
                    ((AudioPlayerService.ServiceBinder) service).getService().getMediaSessionToken());
            mMediaController.registerCallback(mMediaControllerCallback);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
