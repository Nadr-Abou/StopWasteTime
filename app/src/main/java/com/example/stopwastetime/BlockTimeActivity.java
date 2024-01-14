package com.example.stopwastetime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.example.stopwastetime.databinding.ActivityBlockTimeBinding;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;

public class BlockTimeActivity extends AppCompatActivity {
    protected ActivityBlockTimeBinding binding;
    long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block_time);
        Duration duration = Duration.between(LocalTime.now(), LocalTime.parse(getIntent().getStringExtra("endBlockTime")));
        endTime = duration.toMillis();
        CountDownTimer timer = new CountDownTimer(endTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                int seconds = (int) secondsLeft % 3600 % 60;
                int minutes = (int) Math.floor(secondsLeft % 3600 / 60);
                int hours = (int) Math.floor(secondsLeft / 3600);

                binding.BlockTimeText.setText(String.format(hours + ":" + minutes + ":" + seconds));
            }

            @Override
            public void onFinish() {
                String foregroundApp = getIntent().getStringExtra("foregroundApp");
                navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
                Intent intent = getPackageManager().getLaunchIntentForPackage(foregroundApp);
                startActivity(intent);
                MyService.isBlockActive = true;
                MyService.goQuestionBlock = true;
                finishAndRemoveTask();
            }

        }.start();
        findViewById(R.id.finishButton).setOnClickListener(v -> {
            timer.cancel();
            finishAndRemoveTask();
            Intent i=new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            String foregroundApp = getIntent().getStringExtra("foregroundApp");
            Intent intent = getPackageManager().getLaunchIntentForPackage(foregroundApp);
            startActivity(intent);
            MyService.isBlockActive = true;
            MyService.goQuestionBlock = true;
        });

    }
}