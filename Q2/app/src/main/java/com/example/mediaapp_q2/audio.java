package com.example.mediaapp_q2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class audio extends Fragment {
    MediaPlayer mediaPlayer;
    Uri audio_uri;
    boolean isPlaying = false;
    ImageButton btn_audio_play;
    ImageButton btn_audio_next;
    ImageButton btn_audio_restart;
    SeekBar seek_bar_audio;
    TextView tv_current_time;
    TextView audio_time;
    Handler handler = new Handler();
    int PICK_AUDIO = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio, container, false);
        btn_audio_play = view.findViewById(R.id.btn_video_play);
        btn_audio_next = view.findViewById(R.id.btn_video_next);
        btn_audio_restart = view.findViewById(R.id.btn_video_restart);
        seek_bar_audio = view.findViewById(R.id.seek_bar_video);
        tv_current_time = view.findViewById(R.id.tv_current_time);
        audio_time = view.findViewById(R.id.video_time);
        ImageButton fabChoose = view.findViewById(R.id.fab_audio_choose);

        fabChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_AUDIO);
            }
        });
            btn_audio_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    isPlaying = true;
                }
                else if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlaying = false;
                }
            }
        });
        btn_audio_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlaying = false;
                }
            }
        });
            btn_audio_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                    isPlaying = true;
                }
            }
        });

        seek_bar_audio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO && resultCode == android.app.Activity.RESULT_OK && data != null && data.getData() != null) {
            audio_uri = data.getData();
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(getContext(), audio_uri);
            mediaPlayer.start();
            isPlaying = true;
            startSeekBarUpdate();
        }
    }
    public void startSeekBarUpdate() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    int seconds = currentPosition % 60;
                    int minutes = currentPosition / 60;
                    tv_current_time.setText(String.format("%02d:%02d", minutes, seconds));

                    seek_bar_audio.setMax(mediaPlayer.getDuration() / 1000);
                    seek_bar_audio.setProgress(currentPosition);

                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(runnable);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
        handler.removeCallbacksAndMessages(null);
    }
}

