package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity
        implements View.OnClickListener {

    private Button button_start_game;
    private Button button_scores;
    private Button button_exit;
    private Spinner player_1_list;
    private Spinner player_2_list;

    private PlayerDB db;

    @Override
    protected void onResume() {
        super.onResume();
        LoadSpinnerData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        button_start_game = (Button) findViewById(R.id.button_start_game);
        button_scores = (Button) findViewById(R.id.button_scores);
        button_exit = (Button) findViewById(R.id.button_exit);

        player_1_list = (Spinner) findViewById(R.id.player_1_list);
        player_2_list = (Spinner) findViewById(R.id.player_2_list);

        db = new PlayerDB(this);

        LoadSpinnerData();

        button_start_game.setOnClickListener(this);
        button_scores.setOnClickListener(this);
        button_exit.setOnClickListener(this);
    }

    private void LoadSpinnerData() {
        // Initialize
        ArrayAdapter<String> dataAdapter;
        ArrayList<String> namesListArray = null;

        //Populate list
        if (db != null){
            namesListArray = db.getNames();
        }
        else{
            namesListArray.add("Player1");
            namesListArray.add("Player2");
        }

        //Fill adapter
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namesListArray);

        //Set adapters to spinners
        player_1_list.setAdapter(dataAdapter);
        player_2_list.setAdapter(dataAdapter);

        //Make 2nd spinner different than first
        player_2_list.setSelection(1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == button_start_game.getId()){
            Intent intent = new Intent(MainMenu.this, GameBoard.class);
            startActivity(intent);
        }
        else if (view.getId() == button_scores.getId()){
            Intent intent = new Intent(MainMenu.this, ScoreBoard.class);
            startActivity(intent);
        }
        else{
            finish();
            System.exit(0);
        }
    }
}