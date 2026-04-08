package com.example.mediaapp_q2;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

public class video extends Fragment {
    VideoView videoView;
    EditText enter_video_url;
    Button btn_load_video, btn_video_play;
    ImageButton btn_video_next, btn_video_restart;

    // --- NEW UI Elements ---
    SeekBar seek_bar_video;
    TextView tv_current_time, tv_total_time;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video, container, false);

        videoView = view.findViewById(R.id.video_view);
        enter_video_url = view.findViewById(R.id.enter_video_url);
        btn_load_video = view.findViewById(R.id.btn_load_video);
        btn_video_play = view.findViewById(R.id.btn_video_play);
        btn_video_next = view.findViewById(R.id.btn_video_next);
        btn_video_restart = view.findViewById(R.id.btn_video_restart);

        seek_bar_video = view.findViewById(R.id.seek_bar_video);
        tv_current_time = view.findViewById(R.id.tv_current_time);
        tv_total_time = view.findViewById(R.id.video_time);

        btn_load_video.setOnClickListener(v -> {
            String url = enter_video_url.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a URL", Toast.LENGTH_SHORT).show();
                return;
            }

            videoView.stopPlayback();
            btn_video_play.setText("PLAY");
            Toast.makeText(requireContext(), "Loading... please wait", Toast.LENGTH_LONG).show();

            MediaController mediaController = new MediaController(requireContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setVideoPath(url);
            videoView.requestFocus();

            // Run when video is loaded and ready
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(false);
                videoView.start();
                btn_video_play.setText("PAUSE");
                Toast.makeText(requireContext(), "Playing!", Toast.LENGTH_SHORT).show();

                // --- NEW LOGIC: Set total duration ---
                int duration = videoView.getDuration() / 1000;
                int minutes = duration / 60;
                int seconds = duration % 60;
                tv_total_time.setText(String.format("%02d:%02d", minutes, seconds));

                // Start updating the UI time and progress bar
                startSeekBarUpdate();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                btn_video_play.setText("PLAY");
                Toast.makeText(requireContext(), "Error loading video. Try different URL", Toast.LENGTH_LONG).show();
                return true;
            });
        });

        btn_video_play.setOnClickListener(v -> {
            if (videoView != null) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    btn_video_play.setText("PLAY");
                } else {
                    videoView.start();
                    btn_video_play.setText("PAUSE");
                }
            }
        });

        btn_video_next.setOnClickListener(v -> {
            if (videoView != null) {
                videoView.stopPlayback();
                btn_video_play.setText("PLAY");
                seek_bar_video.setProgress(0);
                tv_current_time.setText("00:00");
            }
        });

        btn_video_restart.setOnClickListener(v -> {
            if (videoView != null) {
                videoView.seekTo(0);
                videoView.start();
                btn_video_play.setText("PAUSE");
            }
        });

        // --- NEW LOGIC: Make the video seekbar scrubbable ---
        seek_bar_video.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && videoView != null) {
                    videoView.seekTo(progress * 1000);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    public void startSeekBarUpdate() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView != null && videoView.isPlaying()) {
                    int currentPosition = videoView.getCurrentPosition() / 1000;
                    int seconds = currentPosition % 60;
                    int minutes = currentPosition / 60;
                    tv_current_time.setText(String.format("%02d:%02d", minutes, seconds));

                    seek_bar_video.setMax(videoView.getDuration() / 1000);
                    seek_bar_video.setProgress(currentPosition);
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}