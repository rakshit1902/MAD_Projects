package com.example.mediaapp_q2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class audio extends Fragment {
    MediaPlayer mediaPlayer;
    Uri audioUri = null;
    boolean isPlaying = false;

    Button btn_audio_play;
    ImageButton btn_audio_next, btn_audio_restart;
    SeekBar seek_bar_audio;
    TextView tv_current_time, tv_total_time; // Added tv_total_time
    Handler handler = new Handler();
    ActivityResultLauncher<Intent> audioPicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioPicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        audioUri = result.getData().getData();

                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                        }

                        mediaPlayer = MediaPlayer.create(requireContext(), audioUri);
                        Toast.makeText(requireContext(), "Audio file loaded", Toast.LENGTH_SHORT).show();
                        btn_audio_play.setText("PLAY");

                        // --- NEW LOGIC: Set total duration ---
                        int duration = mediaPlayer.getDuration() / 1000;
                        int minutes = duration / 60;
                        int seconds = duration % 60;
                        tv_total_time.setText(String.format("%02d:%02d", minutes, seconds));

                        startSeekBarUpdate();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio, container, false);

        btn_audio_play = view.findViewById(R.id.btn_video_play);
        btn_audio_next = view.findViewById(R.id.btn_video_next);
        btn_audio_restart = view.findViewById(R.id.btn_video_restart);
        seek_bar_audio = view.findViewById(R.id.seek_bar_video);
        tv_current_time = view.findViewById(R.id.tv_current_time);
        tv_total_time = view.findViewById(R.id.video_time); // Connect the end time view
        ImageButton fabChoose = view.findViewById(R.id.fab_audio_choose);

        fabChoose.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            audioPicker.launch(intent);
        });

        btn_audio_play.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlaying = false;
                    btn_audio_play.setText("PLAY");
                } else {
                    mediaPlayer.start();
                    isPlaying = true;
                    btn_audio_play.setText("PAUSE");
                }
            } else {
                Toast.makeText(requireContext(), "Please open an audio file first", Toast.LENGTH_SHORT).show();
            }
        });

        btn_audio_next.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(requireContext(), audioUri);
                isPlaying = false;
                btn_audio_play.setText("PLAY");
                seek_bar_audio.setProgress(0); // Reset progress bar
                tv_current_time.setText("00:00"); // Reset current time text
            }
        });

        btn_audio_restart.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                isPlaying = true;
                btn_audio_play.setText("PAUSE");
            }
        });

        seek_bar_audio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress * 1000);
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
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    int seconds = currentPosition % 60;
                    int minutes = currentPosition / 60;
                    tv_current_time.setText(String.format("%02d:%02d", minutes, seconds));

                    seek_bar_audio.setMax(mediaPlayer.getDuration() / 1000);
                    seek_bar_audio.setProgress(currentPosition);
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
        handler.removeCallbacksAndMessages(null);
    }
}