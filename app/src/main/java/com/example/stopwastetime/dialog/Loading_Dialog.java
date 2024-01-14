package com.example.stopwastetime.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.stopwastetime.R;

public class Loading_Dialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View dialogFragment = inflater.inflate(R.layout.activity_loading_dialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialogFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int dialogWidth = Resources.getSystem().getDisplayMetrics().widthPixels * 85 / 100;
            int dialogHeight = Resources.getSystem().getDisplayMetrics().heightPixels * 70 / 100;
            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        }
    }


}