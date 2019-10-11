package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity
        implements View.OnClickListener {

    private Button button_start_game;
    private Button button_scores;
    private Button button_exit;
    private Spinner player_1_list;
    private Spinner player_2_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        button_start_game = (Button) findViewById(R.id.button_start_game);
        button_scores = (Button) findViewById(R.id.button_scores);
        button_exit = (Button) findViewById(R.id.button_exit);

        button_start_game.setOnClickListener(this);
        button_scores.setOnClickListener(this);
        button_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == button_start_game.getId()){
            Intent intent = new Intent(MainMenu.this, GameBoard.class);
            startActivity(intent);
        }
        else if (view.getId() == button_scores.getId()){
            Toast.makeText(this, R.string.scores_toast,
                    Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
            System.exit(0);
        }
    }
}