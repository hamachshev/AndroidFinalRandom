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
    private final String HISTORY_KEY = "HISTORY";
    private final String CURRENT_RANDOM_NUMBER_KEY = "RANDOM_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMainBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        mRandomNumber = new RandomNumber();
        initializeHistoryList(savedInstanceState,HISTORY_KEY);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int from = Integer.parseInt(binding.contentMain.FromTextField.getText().toString());
                    int to = Integer.parseInt(binding.contentMain.ToTextField.getText().toString());
                    mRandomNumber.setFromTo(from, to);
                    int rand = mRandomNumber.getCurrentRandomNumber();
                    mNumberHistory.add(rand);
                    binding.contentMain.randomNumberDisplay.setText(String.valueOf(rand));
                }
                catch (NumberFormatException e){
                    Snackbar.make(binding.getRoot(), "Must enter a number in both fields first", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });


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
        editor.putString(HISTORY_KEY, gson.toJson(mNumberHistory));


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
        outState.putIntegerArrayList(HISTORY_KEY, mNumberHistory);
        outState.putString(CURRENT_RANDOM_NUMBER_KEY, binding.contentMain.randomNumberDisplay.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

       initializeHistoryList(savedInstanceState, HISTORY_KEY);

       binding.contentMain.randomNumberDisplay.setText(savedInstanceState.getString(CURRENT_RANDOM_NUMBER_KEY));
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
        if (id == R.id.action_show_history) {
            Utils.showInfoDialog (MainActivity.this,
                    "History", mNumberHistory.toString());
            return true;
        } else if (id == R.id.action_about) {
            Snackbar.make(binding.getRoot(), getString(R.string.about_text), Snackbar.LENGTH_LONG)
                    .show();
        } else if( id == R.id.action_delete_history){
            mNumberHistory.clear();
        }

        return super.onOptionsItemSelected(item);
    }

}