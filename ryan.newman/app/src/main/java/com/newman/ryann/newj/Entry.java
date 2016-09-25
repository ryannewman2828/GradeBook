package com.newman.ryann.newj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Entry extends AppCompatActivity {

    // The list representation of the file
    private ArrayList<String> data;

    // The variables for the Entry
    private int termId;
    private int classId;
    private int entryId;
    private String entryName;

    // The variables for the Edittexts
    private EditText entryTitle;
    private EditText score;
    private EditText scoreTotal;
    private EditText weighting;

    // The variables for the save file
    private File file;
    private final static String DATA = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize the variables for the Edittexts
        entryTitle = (EditText) findViewById(R.id.title);
        score = (EditText) findViewById(R.id.score);
        scoreTotal = (EditText) findViewById(R.id.scoreTotal);
        weighting = (EditText) findViewById(R.id.weighting);

        // Retrieves the data from the previous activity
        Intent intentExtras = getIntent();
        Bundle extraBundle = intentExtras.getExtras();
        if (extraBundle != null) {
            if (extraBundle.containsKey("termID"))
                termId = extraBundle.getInt("termID");

            if (extraBundle.containsKey("classID"))
                classId = extraBundle.getInt("classID");

            if (extraBundle.containsKey("entryID"))
                entryId = extraBundle.getInt("entryID");

            if (extraBundle.containsKey("entryName"))
                entryName = extraBundle.getString("entryName");
        }

        // Initialize the file variables
        file = new File(getApplicationContext().getFilesDir(), DATA);
        data = Utility.fileToList(file);

        // Sets the hint/text for the Edittext
        if(entryName.equals("New_Mark")) {
            entryTitle.setHint(entryName);
        } else {
            entryTitle.setText(entryName);
        }
        score.setText(Utility.getScore(Utility.getFromID(data, termId, classId, entryId)) + "");
        scoreTotal.setText(Utility.getScoreTotal(Utility.getFromID(data, termId, classId, entryId)) + "");
        weighting.setText(Utility.getWeighting(Utility.getFromID(data, termId, classId, entryId)) * 100 + "");
    }

    public void saveEntry(View v) {
        // Retrieve the numbers from the editText
        file = new File(getApplicationContext().getFilesDir(), DATA);

        double s = 0, st = 0, w = 0;
        String name;

        try {
            s = (int) (Double.parseDouble(score.getText().toString()) * 100) / 100.0;
            st = (int) (Double.parseDouble(scoreTotal.getText().toString()) * 100) / 100.0;
            w = (int) (Double.parseDouble(weighting.getText().toString()) * 100) / 10000.0;

            // Updates the Entry in the data list
            if(entryTitle.getText().toString().isEmpty()){
                entryName = "New_Mark";
            } else {
                entryName = entryTitle.getText().toString();
            }
            String temp = Utility.updateEntry(Utility.getFromID(data, termId, classId, entryId), entryName, s, st, w);
            int index = data.indexOf(Utility.getFromID(data, termId, classId, entryId));
            data.remove(Utility.getFromID(data, termId, classId, entryId));
            data.add(index, temp);
            Utility.listToFile(file, data);

            // Toast to notify the user that the entry was saved
            Toast.makeText(getApplicationContext(), "Entry Saved", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Sends the required information back to the term activity
        Intent returnValue = new Intent(Entry.this, Classes.class);
        returnValue.putExtra("termID", termId);
        returnValue.putExtra("classID", classId);
        returnValue.putExtra("className", Utility.getName(Utility.getFromID(data, termId, classId)));
        this.finish();
        startActivity(returnValue);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // When the action bar back button is pressed, do the same as if the back button were pressed
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}