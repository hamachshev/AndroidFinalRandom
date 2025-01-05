package com.example.randomnumbergenerator.activities;



import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.randomnumbergenerator.R;
import com.example.randomnumbergenerator.lib.Utils;
import com.example.randomnumbergenerator.model.RandomNumber;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.randomnumbergenerator.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        mRandomNumber = new RandomNumber();
        initializeHistoryList(savedInstanceState,"HISTORY");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGameInSharedPrefs();
    }

    private void saveGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        Gson gson = new Gson();
        // Save current game  to/from default shared preferences
        editor.putString("HISTORY", gson.toJson(mNumberHistory));


        editor.apply();
    }

    private void initializeHistoryList (Bundle savedInstanceState, String key)
    {
        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (key, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        outState.putString("HISTORY", gson.toJson(mNumberHistory));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}