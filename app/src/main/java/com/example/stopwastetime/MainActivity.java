package com.example.stopwastetime;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwastetime.adapter.BlackListAdapter;
import com.example.stopwastetime.adapter.DialogIconsAdapter;
import com.example.stopwastetime.database.AppDatabase;
import com.example.stopwastetime.databinding.ActivityMainBinding;
import com.example.stopwastetime.dialog.Loading_Dialog;
import com.example.stopwastetime.dialog.MyDialogFragment;
import com.example.stopwastetime.interfaccie.AppListViewInterface;
import com.example.stopwastetime.interfaccie.RecycleViewInterface;
import com.example.stopwastetime.model.BlackList;
import com.example.stopwastetime.model.BlackListPackage;
import com.example.stopwastetime.model.DialogIcons;
import com.example.stopwastetime.model.Excuses;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecycleViewInterface, AppListViewInterface {
    protected ActivityMainBinding binding;
    //blacklist usato per la homepage e per indicare in modo grafico quali app e quali non sono all'interno della blacklist
    static ArrayList<BlackList> blackLists = new ArrayList<>();
    //usato per controllare quali app invece sono nella blacklist attraverso il package USATO PER I CONTROLLI NON PER LA GRAFICA
    static ArrayList<BlackListPackage> blackList_Package = new ArrayList<>();
    //usato per le giustificazioni che definiscono il tempo di blocco e di utilizzo
    static ArrayList<Excuses> excuses = new ArrayList<>();
    static PackageManager pm;
    ArrayList<DialogIcons> dialogIcons = new ArrayList<>();
    BlackListAdapter blackListAdapter;
    DialogIconsAdapter dialogIconsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        permissionAsked();

        listsInitialiazer();

        RecyclerView recyclerView = binding.AppRyclerViewList;
        pm = getPackageManager();

        blackListAdapter = new BlackListAdapter(blackLists, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(blackListAdapter);


        new Thread(new Runnable() {
            @Override
            public void run() {
                dialogIcons = getInstalledApplication();
                dialogIconsAdapter = new DialogIconsAdapter(MainActivity.this, dialogIcons, MainActivity.this);
            }
        }).start();


        binding.floatingAddButton.setOnClickListener(v ->
        {
            if (!dialogIcons.isEmpty()) {
                MyDialogFragment myDialogFragment = new MyDialogFragment(dialogIconsAdapter);
                myDialogFragment.show(getSupportFragmentManager(), "dialog fragment open");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Loading_Dialog loadingDialog = new Loading_Dialog();
                        loadingDialog.show(getSupportFragmentManager(), "loading dialog");
                        while (dialogIcons.isEmpty() || dialogIconsAdapter == null) {
                        }
                        if (((DialogFragment) getSupportFragmentManager().findFragmentByTag("loading dialog")) != null) {
                            MyDialogFragment myDialogFragment = new MyDialogFragment(dialogIconsAdapter);
                            ((DialogFragment) getSupportFragmentManager().findFragmentByTag("loading dialog")).dismiss();
                            myDialogFragment.show(getSupportFragmentManager(), "dialog fragment open");
                        }
                    }
                }).start();
            }
        });
        binding.bottomNav.setSelectedItemId(R.id.nav_blackList);
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_blackList) {
                return true;
            } else if (itemId == R.id.nav_TimedStops) {
                Intent intent = new Intent(this, TimedPausesActivity.class);
                startActivity(intent);
                finish();//per evitare pile di activity
                return true;
            }
            return false;
        });

        if (!isServiceActive()) {
            Intent mioServizio = new Intent(MainActivity.this, MyService.class);
            startForegroundService(mioServizio);
        }


    }

    boolean isServiceActive() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo servizi : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(servizi.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(int position) {
        String appName = blackLists.get(position).getName();
        AppDatabase.getInstance(this).blackListDao().deleteBlackList(blackLists.get(position));
        AppDatabase.getInstance(this).blackListPackageDao().deletePackage(blackList_Package.get(position));
        blackLists.remove(position);
        blackList_Package.remove(position);
        blackListAdapter.notifyItemRemoved(position);
        Toast.makeText(this, appName + " was removed from the blacklist", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void addElementInBlack(DialogIcons dialogIcons) {
        if (!blackList_Package.contains(new BlackListPackage(dialogIcons.packageName))) {
            BlackList bl = new BlackList(dialogIcons.appName, dialogIcons.icon);
            BlackListPackage blPackage = new BlackListPackage(dialogIcons.packageName);
            blackLists.add(bl);
            blackList_Package.add(blPackage);
            AppDatabase.getInstance(this).blackListDao().insertBlackList(bl);
            AppDatabase.getInstance(this).blackListPackageDao().insertPackage(blPackage);
            blackListAdapter.notifyDataSetChanged();
            Toast.makeText(this, dialogIcons.appName + " è stato aggiunto alla lista", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, dialogIcons.appName + " è gia presente nella lista", Toast.LENGTH_SHORT).show();
        }
        DialogFragment df = (DialogFragment) getSupportFragmentManager().findFragmentByTag("dialog fragment open");
        df.dismiss();
    }

    public ArrayList<DialogIcons> getInstalledApplication() {
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<DialogIcons> lDialogIcons = new ArrayList<>();
        for (ApplicationInfo appInfo : installedApplications) {
            if (((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) || appInfo.packageName.equals("com.google.android.youtube")) { //condizione per identificare se un app è un app di sistema o meno, le app di sistema non devono essere messe in blacklist
                String appName = (String) pm.getApplicationLabel(appInfo);
                String packageName = appInfo.packageName;
                Drawable icon = null;
                try {
                    icon = pm.getApplicationIcon(appInfo.packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lDialogIcons.add(new DialogIcons(appName, icon, packageName));
                //iconsAdapter.notifyDataSetChanged(); non è possibile eseguire questo comando in quanto non sono sul main thread che gestisce la UI
            }
        }
        Collections.sort(lDialogIcons, new Comparator<DialogIcons>() { //sostituibile con una lambda ma così è chiaro il funzionamento che consiste nel ordinare l'app per ordine alfabetico
            @Override
            public int compare(DialogIcons o1, DialogIcons o2) {
                return o1.appName.compareToIgnoreCase(o2.appName);
            }
        });
        return lDialogIcons;
    }

    void listsInitialiazer() {
        AppDatabase instance = AppDatabase.getInstance(this);
        blackLists = new ArrayList<>(instance.blackListDao().getAll());
        blackList_Package = new ArrayList<>(instance.blackListPackageDao().getBlPackages());
        excuses = new ArrayList<>(instance.excuseDao().getExcuses());
    }

    void permissionAsked() {
        AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), this.getPackageName());
        if (!Settings.canDrawOverlays(this) || mode != AppOpsManager.MODE_ALLOWED) {
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "For the application to work you need to accept some permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Accept", v -> {
                        AppOpsManager appOpsInside = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
                        int modeInside = appOpsInside.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), this.getPackageName());
                        if (modeInside != AppOpsManager.MODE_ALLOWED) {
                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            Uri.parse("package:" + getPackageName());
                            startActivity(intent);
                        }
                        if (!Settings.canDrawOverlays(this)) {
                            Intent intentOverlay = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            startActivity(intentOverlay);
                        }
                        permissionAsked();
                    });
            View snackBarView = snackbar.getView();
            snackBarView.setTranslationY(-250);
            snackbar.show();
        }

    }
}