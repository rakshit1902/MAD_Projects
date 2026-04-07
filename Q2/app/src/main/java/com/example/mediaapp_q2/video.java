package com.example.mediaapp_q2;

import static java.security.AccessController.getContext;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

public class video extends Fragment {
    VideoView videoView;
    boolean isPlaying = false;
    ImageButton btn_video_play;
    ImageButton btn_video_next;
    ImageButton btn_video_restart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video, container, false);

        btn_video_play = view.findViewById(R.id.btn_video_play);
        btn_video_next = view.findViewById(R.id.btn_video_next);
        btn_video_restart = view.findViewById(R.id.btn_video_restart);
        videoView = view.findViewById(R.id.video_view);
        btn_video_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView != null && !videoView.isPlaying()) {
                    videoView.start();
                    isPlaying = true;
                } else if (videoView != null && videoView.isPlaying()) {
                    videoView.pause();
                    isPlaying = false;
                }
            }
        });
        btn_video_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView != null) {
                    videoView.stopPlayback();
                    isPlaying = false;
                }
            }
        });
        btn_video_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView != null) {
                    videoView.seekTo(0);
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
        return view;
    }
    public void startSeekBarUpdate() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView != null) {
                    int currentPosition = videoView.getCurrentPosition() / 1000;
                    int seconds = currentPosition % 60;
                    int minutes = currentPosition / 60;
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
            isPlaying = false;
        }
    }
}
