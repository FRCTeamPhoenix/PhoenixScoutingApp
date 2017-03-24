package org.team2342.phoenixscouting;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //Basic Info
    private EditText mTeamNumber;
    private RatingBar mTeamRating;
    private EditText mGeneralComments;
    private Button mButtonSubmit;

    //Auto Abilities
    private CheckBox mCheckStation1;
    private CheckBox mCheckStation2;
    private CheckBox mCheckStation3;
    private CheckBox mCheckShootsAuto;
    private CheckBox mCheckDepositGear;

    //Robot Capabilities
    private CheckBox mCheckGearFloor;
    private CheckBox mCheckGearLoaded;
    private CheckBox mCheckShootsHigh;
    private CheckBox mCheckShootsLow;
    private CheckBox mCheckCanClimb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.showOverflowMenu();
        setSupportActionBar(mToolbar);

        mTeamNumber = (EditText) findViewById(R.id.teamNumber);
        mTeamRating = (RatingBar) findViewById(R.id.teamRating);
        mGeneralComments = (EditText) findViewById(R.id.generalComments);
        mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);

        //TODO: Rework station names to refer to field landmarks
        mCheckStation1 = (CheckBox) findViewById(R.id.checkStation1);
        mCheckStation2 = (CheckBox) findViewById(R.id.checkStation2);
        mCheckStation3 = (CheckBox) findViewById(R.id.checkStation3);
        mCheckShootsAuto = (CheckBox) findViewById(R.id.checkShootsAuto);
        mCheckDepositGear = (CheckBox) findViewById(R.id.checkDepositGear);

        mCheckGearFloor = (CheckBox) findViewById(R.id.checkGearFloor);
        mCheckGearLoaded = (CheckBox) findViewById(R.id.checkGearLoaded);
        mCheckShootsHigh = (CheckBox) findViewById(R.id.checkShootsHigh);
        mCheckShootsLow = (CheckBox) findViewById(R.id.checkShootsLow);
        mCheckCanClimb = (CheckBox) findViewById(R.id.checkCanClimb);
    }

    public void onSubmit(View view){
        JSONArray allData;
        JSONObject data = new JSONObject();

        try {
            InputStream stream = openFileInput("scoutingData.json");
            InputStreamReader inputReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inputReader);
            String lineString;
            if(stream != null && (lineString = reader.readLine()) != null){
                allData = new JSONArray(lineString);
                Log.println(Log.INFO, "dataDump", allData.toString(3));
            }
            else {
                allData = new JSONArray();
            }
            stream.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error Opening Database", Toast.LENGTH_SHORT).show();
            return;
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error Opening Database", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            data.put("teamNumber", Integer.parseInt(mTeamNumber.getText().toString()));
            data.put("teamRating", mTeamRating.getRating());
            data.put("comments", mGeneralComments.getText().toString());

            data.put("autoStation1", mCheckStation1.isChecked());
            data.put("autoStation2", mCheckStation2.isChecked());
            data.put("autoStation3", mCheckStation3.isChecked());
            data.put("shootsAuto", mCheckShootsAuto.isChecked());
            data.put("autoDepositGear", mCheckDepositGear.isChecked());

            data.put("gearFloor", mCheckGearFloor.isChecked());
            data.put("gearLoaded", mCheckGearLoaded.isChecked());
            data.put("shootsHigh", mCheckShootsHigh.isChecked());
            data.put("shootsLow", mCheckShootsLow.isChecked());
            data.put("canClimb", mCheckCanClimb.isChecked());

            allData.put(data);

            OutputStream outputStream = openFileOutput("scoutingData.json", MODE_PRIVATE);
            outputStream.write(allData.toString().getBytes());
            outputStream.close();

            Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error Saving Data", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error Saving Data", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error Saving Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearDataBase(){
        OutputStream outputStream = null;
        try {
            outputStream = openFileOutput("scoutingData.json", MODE_PRIVATE);
            outputStream.close();
            Toast.makeText(getApplicationContext(), "Database Cleared", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDataBase(){
        File database = new File(getFilesDir(), "scoutingData.json");
        Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), "org.team2342.phoenixscouting.fileprovider", database);
        if(fileUri != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            startActivity(sendIntent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Error Sending Database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.clearData:
                clearDataBase();
                return true;
            case R.id.sendData:
                sendDataBase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
