package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoard extends AppCompatActivity
        implements View.OnClickListener{

    private ListView itemsListView;
    private PlayerDB db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        itemsListView = (ListView) findViewById(R.id.itemsListView);



        db = new PlayerDB(this);
        updateDisplay();
    }

    @Override
    public void onClick(View view) {

    }

    private void updateDisplay() {
        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = db.getPlayers();

        //create the resource, from, and to variables
        int resource = R.layout.scoreboard_listview_item;
        String[] from = {"name", "wins", "losses", "ties"};
        int[] to = {R.id.nameTextView, R.id.winsTextView, R.id.lossesTextView, R.id.tiesTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_score_board, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_user:
                Toast.makeText(this, "Add User",
                        Toast.LENGTH_SHORT).show();
                AddUser();
                return true;
            default:
                this.finish();
                return true;
        }
    }

    private void AddUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        //Set up the input
        final EditText userNameInput = new EditText(this);
        userNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(userNameInput);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userName = userNameInput.getText().toString();
                boolean repeatUser = false;

                ArrayList<HashMap<String, String>> users = db.getPlayers();

                //Checks if name is already used
                outerloop:
                for(HashMap<String, String> entry : users){
                    for(Map.Entry mapEntry : entry.entrySet()){
                        //Checks for only names
                        if (mapEntry.getKey() == "name"){
                            String name = mapEntry.getValue().toString();
                            //If username exists return error toast
                            if (name.equals(userName)){
                                Toast.makeText(getApplicationContext(), "User already added",
                                        Toast.LENGTH_SHORT).show();
                                repeatUser = true;
                                break outerloop;
                            }
                            //Skip to next item
                            else{
                                break;
                            }
                        }
                    }
                }
                if (!repeatUser){
                    try {
                        db.insertPlayer(userNameInput.getText().toString());
                        updateDisplay();
                        Toast.makeText(getApplicationContext(), "New User Added",
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}
