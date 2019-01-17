package com.blks.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.blks.antrscapp.R;

import static com.blks.utils.LoginUtils.isNetwork;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private MediaPlayer mediaPlayer;
    private CountDownTimer downTimer;
    private int count;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {

            isNetwork = true;

            if (downTimer != null) {
                downTimer.cancel();
            }

            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

        } else {
            Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
            isNetwork = false;
            if (downTimer == null) {
                downTimer = new CountDownTimer(2 * 60 * 1000, 60 * 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (!isNetwork) {
                            starPlayVoice();
                        }
                    }
                };
            }

            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.network_error);
//                mediaPlayer.setLooping(true);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (!isNetwork && ++count < 3 && mp != null) {
                            mp.start();
                        }
                    }
                });
            }

            starPlayVoice();
        }

    }

    private void starPlayVoice() {
        if (!mediaPlayer.isPlaying()) {
            count = 0;
            mediaPlayer.start();
            downTimer.start();
        }
    }

}
