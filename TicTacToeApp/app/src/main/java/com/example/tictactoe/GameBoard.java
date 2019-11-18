package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GameBoard extends AppCompatActivity
    implements TextView.OnEditorActionListener, View.OnClickListener {

    private SharedPreferences savedValues;

    private ImageButton aButtons[][] = new ImageButton[3][3];
    private TextView textViewGameInfo;
    private int turn;

    private List<String> xPlacements = new ArrayList<String>();
    private List<String> oPlacements = new ArrayList<String>();

    private String player1Name;
    private String player2Name;

    private PlayerDB db;

    private boolean gameEnded;

    private String[][] winningCombinations = {
            {"00", "01", "02"}, {"10", "11", "12"}, {"20", "21", "22"},
            {"00", "10", "20"}, {"01", "11", "21"}, {"02", "12", "22"},
            {"00", "11", "22"}, {"02", "11", "20"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        player1Name = extras.getString("player1");
        player2Name = extras.getString("player2");

        gameEnded = false;

        db = new PlayerDB(this);

        LoadWidgets();
        ResetBoard();
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        SaveGamePlacements();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GetSavedValues();
    }

    @Override
    public void onClick(View view) {
            BoardSquareClick(view);
    }

    private void BoardSquareClick(View view) {
        for (int i = 0; i < aButtons.length; i++){
            for (int j = 0; j < aButtons[i].length; j ++){
                //find id of image clicked
                if (view.getId() == aButtons[i][j].getId()){
                    //x turns are even
                    if ((turn % 2) == 0){
                        aButtons[i][j].setImageResource(R.drawable.x);
                        xPlacements.add("" + i + j);
                    }
                    // o turns are odd
                    else{
                        aButtons[i][j].setImageResource(R.drawable.o);
                        oPlacements.add("" + i + j);
                    }
                    aButtons[i][j].setEnabled(false);
                }
            }
        }
        turn++;
        UpdateTextViewGameInfo();
        //Can't have winner before 5 turns
        if (turn >= 5){
            CheckWinner();
        }
    }

    private void LoadWidgets() {
        aButtons[0][0] = findViewById(R.id.button00);
        aButtons[0][1] = findViewById(R.id.button01);
        aButtons[0][2] = findViewById(R.id.button02);
        aButtons[1][0] = findViewById(R.id.button10);
        aButtons[1][1] = findViewById(R.id.button11);
        aButtons[1][2] = findViewById(R.id.button12);
        aButtons[2][0] = findViewById(R.id.button20);
        aButtons[2][1] = findViewById(R.id.button21);
        aButtons[2][2] = findViewById(R.id.button22);

        textViewGameInfo = findViewById(R.id.textViewGameInfo);
    }

    private void SaveGamePlacements() {
        SharedPreferences.Editor editor = savedValues.edit();

        Set<String> xPlacementSet = new HashSet<>(xPlacements);
        Set<String> oPlacementSet = new HashSet<>(oPlacements);

        editor.clear();
        editor.putStringSet("xPlacementSet", xPlacementSet);
        editor.putStringSet("oPlacementSet", oPlacementSet);
        editor.putInt("turn", turn);
        editor.putString("player1", player1Name);
        editor.putString("player2", player2Name);
        editor.commit();
    }

    private void GetSavedValues() {
        String player1 = savedValues.getString("player1", null);
        String player2 = savedValues.getString("player2", null);

        if (player1.equals(player1Name) && player2.equals(player2Name)){
            Set<String> xPlacementSet = savedValues.getStringSet("xPlacementSet", null);
            Set<String> oPlacementSet = savedValues.getStringSet("oPlacementSet", null);
            turn = savedValues.getInt("turn", 0);

            if (xPlacementSet != null || oPlacementSet != null){
                for (String x : xPlacementSet){
                    xPlacements.add(x);
                }
                for (String o : oPlacementSet){
                    oPlacements.add(o);
                }
                BuildBoard();
            }
        }
        else{
            ResetBoard();
        }
    }

    private void BuildBoard() {
        for (int i = 0; i < aButtons.length; i++) {
            for (int j = 0; j < aButtons[i].length; j++) {
                String btnNameHolder = "" + i + j;
                if (xPlacements.contains(btnNameHolder)){
                    aButtons[i][j].setImageResource(R.drawable.x);
                    aButtons[i][j].setEnabled(false);
                }
                else if (oPlacements.contains(btnNameHolder)){
                    aButtons[i][j].setImageResource(R.drawable.o);
                    aButtons[i][j].setEnabled(false);
                }
                else{
                    aButtons[i][j].setImageResource(R.drawable.blank);
                    aButtons[i][j].setEnabled(true);
                }
            }
        }
        UpdateTextViewGameInfo();
        CheckWinner();
    }

    private void ResetBoard() {
        //Set buttons to empty and enabled
        for (int i = 0; i < aButtons.length; i++){
            for (int j = 0; j < aButtons[i].length; j ++){
                aButtons[i][j].setImageResource(R.drawable.blank);
                aButtons[i][j].setOnClickListener(this);
                aButtons[i][j].setEnabled(true);
            }
        }

        //Set turns to 0
        turn = 0;

        //if x or o placements (saved values) present clears
        if (xPlacements != null){
            xPlacements.clear();

        }
        if (oPlacements != null){
            oPlacements.clear();
        }

        UpdateTextViewGameInfo();
    }

    private void CheckWinner() {
        List<String> testListHolder = new ArrayList<String>();
        for (int i = 0; i < winningCombinations.length; i ++){
            for (int j = 0; j < winningCombinations[i].length; j++){
                //add all numbers for 1 winning combination
                testListHolder.add(winningCombinations[i][j]);
            }
            //Test if x's have winning combination
            if (xPlacements.containsAll(testListHolder)){
                XWins(testListHolder);
                break;
            }
            //Test if o's have winning combination
            else if(oPlacements.containsAll(testListHolder)){
                OWins(testListHolder);
                break;
            }
            //If neither have this winning combination clear and try next winning combination
            else{
                testListHolder.clear();
            }
        }
    }

    private void XWins(List<String> winningList) {
        String playerWins = textViewGameInfo.getContext().getResources().getString(R.string.playerWins);
        textViewGameInfo.setText(player1Name + " " + playerWins);
        for (int i = 0; i < winningList.size(); i++){
            //Gets board row location of 1 winning piece
            int boardRow = Integer.parseInt(winningList.get(i) )/ 10;
            //Gets board column location of 1 winning piece
            int boardColumn = Integer.parseInt(winningList.get(i) ) % 10;
            //Changes board piece of winning value
            aButtons[boardRow][boardColumn].setImageResource(R.drawable.win_x);
        }
        FreezeBoard();

        // Update DB for X win
        try {
            db.UpdateScores(player1Name, player2Name, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OWins(List<String> winningList) {
        String playerWins = textViewGameInfo.getContext().getResources().getString(R.string.playerWins);
        textViewGameInfo.setText(player2Name + " " + playerWins);
        for (int i = 0; i < winningList.size(); i++){
            //Gets board row location of 1 winning piece
            int boardRow = Integer.parseInt(winningList.get(i) ) / 10;
            //Gets board column location of 1 winning piece
            int boardColumn = Integer.parseInt(winningList.get(i) ) % 10;
            //Changes board piece of winning value
            aButtons[boardRow][boardColumn].setImageResource(R.drawable.win_o);
        }
        FreezeBoard();

        // Update DB for O win
        try {
            db.UpdateScores(player2Name, player1Name, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FreezeBoard() {
        for (int i = 0; i < aButtons.length; i++) {
            for (int j = 0; j < aButtons[i].length; j++) {
                aButtons[i][j].setEnabled(false);
            }
        }
        gameEnded = true;
    }

    private void UpdateTextViewGameInfo() {
        if (turn != 9){
            String playersTurn = textViewGameInfo.getContext().getResources().getString(R.string.playersTurn);
            if (turn % 2 == 0){
                textViewGameInfo.setText(player1Name + playersTurn);
            } else {
                textViewGameInfo.setText(player2Name + playersTurn);
            }
        }
        else{
            textViewGameInfo.setText(R.string.tie_game);
            FreezeBoard();
            // Update DB for tie game
            try {
                db.UpdateScores(player1Name, player2Name, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_game_board, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_game:
                Toast.makeText(this, "New Game",
                        Toast.LENGTH_SHORT).show();
                ResetBoard();
                return true;
            default:
                this.finish();
                if (gameEnded){
                    ResetBoard();
                }
                return true;
        }
    }
}
