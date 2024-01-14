package com.example.stopwastetime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.stopwastetime.adapter.ExcusesAdapter;
import com.example.stopwastetime.database.AppDatabase;
import com.example.stopwastetime.model.Excuses;
import com.example.stopwastetime.databinding.ActivityTimedPausesBinding;

public class TimedPausesActivity extends AppCompatActivity {

    protected ActivityTimedPausesBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timed_pauses);

        ExcusesAdapter excusesAdapter = new ExcusesAdapter(MainActivity.excuses);
        binding.pausesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.pausesRecyclerView.setAdapter(excusesAdapter);

        binding.submitExcuseButton.setOnClickListener(v -> {
            if (
                    binding.JustificationEditText.getText().toString().isEmpty() ||
                            binding.accessEditText.getText().toString().isEmpty() ||
                            binding.BlockEditText.getText().toString().isEmpty()
            ) {
                Toast.makeText(this, "One or more values are empty!", Toast.LENGTH_SHORT).show();
            } else {
                Excuses excuses = new Excuses(
                        binding.JustificationEditText.getText().toString(),
                        Integer.parseInt(binding.accessEditText.getText().toString()),
                        Integer.parseInt(binding.BlockEditText.getText().toString())
                );
                MainActivity.excuses.add(excuses);
                AppDatabase.getInstance(this).excuseDao().insertExcuse(excuses);

                binding.JustificationEditText.setText("");
                binding.accessEditText.setText("");
                binding.BlockEditText.setText("");

                excusesAdapter.notifyDataSetChanged();
            }
        });

        binding.bottomNav.setSelectedItemId(R.id.nav_TimedStops);
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_blackList) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();//per evitare pile di activity
                return true;
            } else return itemId == R.id.nav_TimedStops;
        });
    }
    //per evitare quando premuto il back button pressed di tornare alla home ma di tornare al mainactivity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}