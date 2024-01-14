package com.example.stopwastetime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.stopwastetime.databinding.ActivityInitialQuestionBinding;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InitialQuestionActivity extends AppCompatActivity {
    protected ActivityInitialQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_initial_question);
        String foregroundApp = getIntent().getStringExtra("foregroundApp");
        binding.correctImageButton.setOnClickListener(v -> {
            Long resetBlock;
            MyService.isBlockActive = false;
            if (LocalDateTime.now().isBefore(LocalDateTime.parse("05:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"))) && LocalDateTime.now().isAfter(LocalDateTime.parse("00:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")))) {
                resetBlock = LocalDateTime.now().until(LocalDate.now().atTime(5, 00), ChronoUnit.MINUTES);
            } else {
                resetBlock = LocalDateTime.now().until(LocalDate.now().plusDays(1).atTime(5, 00), ChronoUnit.MINUTES);
            }
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable riattivaBLocco = () -> {
                MyService.isBlockActive = true;
                MyService.goQuestionBlock = true;
            };
            executor.schedule(riattivaBLocco, resetBlock, TimeUnit.MINUTES);
            Intent intent = getPackageManager().getLaunchIntentForPackage(foregroundApp);
            startActivity(intent);
        });
        binding.wrongImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseExcuseActivity.class);
            intent.putExtra("foregroundApp", foregroundApp);
            startActivity(intent);
        });
    }
}