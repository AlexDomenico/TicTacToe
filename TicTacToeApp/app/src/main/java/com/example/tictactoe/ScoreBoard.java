package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.tictactoe.dialogs.UpdateDialogFragment;
import com.example.tictactoe.dialogs.UpdateOrDeleteDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard extends AppCompatActivity
        implements AdapterView.OnItemClickListener, UpdateOrDeleteDialogFragment.NoticeDialogListener,
         UpdateDialogFragment.UserNamePasser{

    private ListView itemsListView;
    private PlayerDB db;

    private static String playerUpdateOrDelete = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        itemsListView = (ListView) findViewById(R.id.itemsListView);
        itemsListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);


        db = new PlayerDB(this);
        updateDisplay();
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
                boolean repeatUser = CheckIfUsernameExists(userName);

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        SimpleAdapter a = (SimpleAdapter) itemsListView.getAdapter();
        HashMap<String, String> x = (HashMap<String, String>) a.getItem(position);

        // get player name
        playerUpdateOrDelete = x.get("name");

        // Add bundle with name for fragment
        Bundle bundle = new Bundle();
        bundle.putString("username", playerUpdateOrDelete);

        // Ask update or delete
        DialogFragment newFragment = new UpdateOrDeleteDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "updateOrDelete");
    }

    //Update selected
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Bundle bundle = new Bundle();
        bundle.putString("username", playerUpdateOrDelete);

        DialogFragment newFragment = new UpdateDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "update");
    }

    @Override
    public void userNamePasser(String updatedUserName){
        boolean repeatUser = CheckIfUsernameExists(updatedUserName);

        if (!repeatUser) {
            //Update player username
            try {
                db.UpdatePlayer(playerUpdateOrDelete, updatedUserName);
                Toast.makeText(getApplicationContext(), "User " + updatedUserName + " updated",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerUpdateDelete_Cancel();
        updateDisplay();
    }

    private boolean CheckIfUsernameExists(String username) {
        ArrayList<HashMap<String, String>> users = db.getPlayers();
        boolean repeatUser = false;

        //Checks if name is already used
        for(HashMap<String, String> entry : users) {
            String name = String.valueOf(entry.get("name"));

            //If username exists return error toast
            if (name.equals(username)) {
                Toast.makeText(getApplicationContext(), "Username " + username + " already used",
                        Toast.LENGTH_LONG).show();
                repeatUser = true;
                //End loop if found
                break;
            }
        }
        return repeatUser;
    }


    //Delete selected
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
//        DialogFragment newFragment = new DeleteDialogFragment();
//        newFragment.show(getSupportFragmentManager(), "delete");

        PlayerDelete();
    }

    public static void PlayerUpdateDelete_Cancel(){
        playerUpdateOrDelete = null;
    }

    public void PlayerDelete(){
        if (playerUpdateOrDelete != null){
            try {
                db.DeletePlayer(playerUpdateOrDelete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateDisplay();
        playerUpdateOrDelete = null;
    }
}
