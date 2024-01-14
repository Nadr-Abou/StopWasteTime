package com.example.stopwastetime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.stopwastetime.model.DialogIcons;
import com.example.stopwastetime.interfaccie.AppListViewInterface;
import com.example.stopwastetime.R;

import java.util.ArrayList;

public class DialogIconsAdapter extends ArrayAdapter<DialogIcons> {
    FragmentManager fragment;
    AppListViewInterface appListViewInterface;

    public DialogIconsAdapter(@NonNull Context context, ArrayList<DialogIcons> dialogIcons, AppListViewInterface appListViewInterface) {
        super(context, 0, dialogIcons);
        this.appListViewInterface = appListViewInterface;
        try {
            fragment = ((AppCompatActivity) context).getSupportFragmentManager();
        } catch (Exception e) {
            System.out.println("Errore: impossibile eseguire il getFragment per il seguente errore: \n" + e.getMessage());
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DialogIcons dialogIcons = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_icon, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.icon_image_layout);
        TextView textView = convertView.findViewById(R.id.icon_text_layout);
        ImageButton addButton = convertView.findViewById(R.id.addButton_dialog_icon);

        imageView.setImageDrawable(dialogIcons.icon);
        textView.setText(dialogIcons.appName);

        addButton.setOnClickListener(v -> {
            if (fragment != null) {
                appListViewInterface.addElementInBlack(dialogIcons);
            }
        });
        return convertView;
    }
}
