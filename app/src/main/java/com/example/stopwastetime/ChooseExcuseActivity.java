package com.example.stopwastetime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.stopwastetime.model.Excuses;
import com.example.stopwastetime.databinding.ActivityChooseExcuseBinding;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChooseExcuseActivity extends AppCompatActivity {
    protected ActivityChooseExcuseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_excuse);
        if (MainActivity.excuses.size() <= 0) {
            Toast.makeText(this, "You need to set some blocks before use the app!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TimedPausesActivity.class);
            startActivity(intent);
        }
        NumberPicker numberPicker = binding.excusePicker;
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(MainActivity.excuses.size() - 1);
        numberPicker.setDisplayedValues(stringFromExcuses(MainActivity.excuses));
        binding.nextButton.setOnClickListener(v -> {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            MyService.isBlockActive = false;
            Runnable delayBlock = () -> {
                MyService.isBlockActive = true;
                MyService.goQuestionBlock = false;
                MyService.endBlockTime = LocalTime.now().plusMinutes(MainActivity.excuses.get(numberPicker.getValue()).getBlocco());
            };
            executor.schedule(delayBlock, MainActivity.excuses.get(numberPicker.getValue()).getTempoUtilizzo(), TimeUnit.MINUTES);
            Toast.makeText(this, "Avrai accesso all' app per " + MainActivity.excuses.get(numberPicker.getValue()).getTempoUtilizzo() + "m e sarai bloccato per " + MainActivity.excuses.get(numberPicker.getValue()).getBlocco(), Toast.LENGTH_SHORT).show();
            String foregroundApp = getIntent().getStringExtra("foregroundApp");
            Intent intent = getPackageManager().getLaunchIntentForPackage(foregroundApp);
            startActivity(intent);
        });
    }

    private String[] stringFromExcuses(ArrayList<Excuses> excuses) {
        String[] excusesString = new String[excuses.size()];
        for (int i = 0; i < excuses.size(); i++) {
            excusesString[i] = excuses.get(i).getScusa();
        }
        return excusesString;
    }

    private String getMassageForToast(int index) {
        return null;
    }
}