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

import com.example.tictactoe.dialogs.AddUserDialog;
import com.example.tictactoe.dialogs.UpdateDialogFragment;
import com.example.tictactoe.dialogs.UpdateOrDeleteDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard extends AppCompatActivity
        implements AdapterView.OnItemClickListener, UpdateOrDeleteDialogFragment.NoticeDialogListener,
         UpdateDialogFragment.UserNamePasser,
         AddUserDialog.AddUserNamePasser{

    private ListView itemsListView;
    private PlayerDB db;


    
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
                DialogFragment newFragment = new AddUserDialog();
                newFragment.show(getSupportFragmentManager(), "add user");
                return true;
            default:
                this.finish();
                return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        String activeUserName = GetActiveUserName(position);

        // Add bundle with name for fragment
        Bundle bundle = new Bundle();
        bundle.putString("username", activeUserName);

        // Ask update or delete
        DialogFragment newFragment = new UpdateOrDeleteDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "updateOrDelete");
    }

    private String GetActiveUserName(int position) {
        SimpleAdapter a = (SimpleAdapter) itemsListView.getAdapter();
        HashMap<String, String> adapterItem = (HashMap<String, String>) a.getItem(position);

        // get player name
        String activeUserName = adapterItem.get("name");
        return activeUserName;
    }

    //Update selected
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String activeUserName) {
        Bundle bundle = new Bundle();
        bundle.putString("activeUserName", activeUserName);

        DialogFragment newFragment = new UpdateDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "update");
    }

    @Override
    public void userNamePasser(String oldUserName, String updatedUserName){
        boolean repeatUser = CheckIfUsernameExists(updatedUserName);

        if (!repeatUser) {
            //Update player username
            try {
                db.UpdatePlayer(oldUserName, updatedUserName);
                Toast.makeText(getApplicationContext(), "User " + updatedUserName + " updated",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public void onDialogNegativeClick(DialogFragment dialog, String username) {
//        DialogFragment newFragment = new DeleteDialogFragment();
//        newFragment.show(getSupportFragmentManager(), "delete");

        PlayerDelete(username);
    }

    public void PlayerDelete(String username){
        if (username != null){
            try {
                db.DeletePlayer(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateDisplay();
    }

    @Override
    public void addUserNamePasser(String addUsername) {
        boolean repeatUser = CheckIfUsernameExists(addUsername);

        if (!repeatUser){
            try {
                db.insertPlayer(addUsername);
                updateDisplay();
                Toast.makeText(getApplicationContext(), "New User Added",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
