package com.newman.ryann.newj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Classes extends AppCompatActivity {

    // The variables responsible for the Listview
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    // The list representation of the file
    private ArrayList<String> data;

    // The variables for the Class
    private int numEntries;
    private int termId;
    private int classId;
    private String className;

    // The variables for calculating the class average
    private double totalWeight;
    private double unweightedAverage;

    // The variables for the Textview
    private EditText classTitle;
    private TextView classAverage;

    // The variables for the save file
    private File file;
    private final static String DATA = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize the variables for the Textviews
        classTitle = (EditText) findViewById(R.id.title);
        classAverage = (TextView) findViewById(R.id.average);

        // Retrieves the data from the previous activity
        Intent intentExtras = getIntent();
        Bundle extraBundle = intentExtras.getExtras();
        if(extraBundle != null) {
            if (extraBundle.containsKey("termID"))
                termId = extraBundle.getInt("termID");

            if (extraBundle.containsKey("className"))
                className = extraBundle.getString("className");

            if(extraBundle.containsKey("classID"))
                classId = extraBundle.getInt("classID");
        }

        // Initialize the Listview variables
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();

        // Initialize the file variables
        file = new File(getApplicationContext().getFilesDir(), DATA);
        data = Utility.fileToList(file);

        // Gets the input the moment of a change in the title
        classTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    className = "New_Class";
                } else {
                    className = s.toString();
                }
                String temp = Utility.setName(Utility.getFromID(data, termId, classId), className);
                int index = data.indexOf(Utility.getFromID(data, termId, classId));
                data.remove(Utility.getFromID(data, termId, classId));
                data.add(index, temp);
                Utility.listToFile(file, data);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Build the Listview from the entry tags located in data, also partially calculates the average
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).substring(0,data.get(i).indexOf(":")).equals("entry")
                    && Utility.getIDT(data.get(i)) == termId
                    && Utility.getIDC(data.get(i)) == classId){
                double temp;
                if(Utility.getScoreTotal(data.get(i)) == 0) {
                   temp = 0;
                } else {
                    temp = (int) (Utility.getScore(data.get(i)) / Utility.getScoreTotal(data.get(i)) * 10000) / 100.0;
                }
                arrayList.add(Utility.getName(data.get(i)) + " - " + temp + "%");
                totalWeight += Utility.getWeighting(data.get(i));
                unweightedAverage += temp * Utility.getWeighting(data.get(i));
            }
        }
        numEntries = arrayList.size();

        // Calculates and updates the class average in the data list
        String temp;
        if(totalWeight == 0){
            temp = Utility.setAverage(Utility.getFromID(data, termId, classId), 0);
        } else {
            double avg = (int) (unweightedAverage / totalWeight * 100) / 100.0;
            temp = Utility.setAverage(Utility.getFromID(data, termId, classId), avg);
        }
        int index = data.indexOf(Utility.getFromID(data, termId, classId));
        data.remove(Utility.getFromID(data, termId, classId));
        data.add(index, temp);
        Utility.listToFile(file, data);

        // Sets the hint/text for the Textview
        if(className.equals("New_Class")) {
            classTitle.setHint(className);
        } else {
            classTitle.setText(className);
        }
        classAverage.setText("Class Average: " + Utility.getAverage(Utility.getFromID(data, termId, classId)) + "%");

        // Sets the adapter and changes the text colour to black
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, arrayList){

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById
                        (android.R.id.text1);

                //sets the colour
                textView.setTextColor(Color.BLACK);

                return view;
            }
        };
        listView.setAdapter(adapter);

        // Starts a new activity when a Listview item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Classes.this, Entry.class);
                intent.putExtra("termID", termId);
                intent.putExtra("classID", classId);
                intent.putExtra("entryID", position);
                intent.putExtra("entryName", Utility.getName(Utility.getFromID(data, termId, classId, position)));
                startActivity(intent);
            }
        });

        // Adds an element to the listView when the fab is clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(getApplicationContext().getFilesDir(), DATA);
                arrayList.add("New_Mark - 0%");
                data.add("entry:" + termId + ":" + classId + ":" + numEntries + ":New_Mark:0:0:0");
                numEntries++;
                Utility.listToFile(file, data);
                listView.setAdapter(adapter);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        // Sends the required information back to the term activity
        Intent returnValue = new Intent(Classes.this, Term.class);
        returnValue.putExtra("termID", termId);
        returnValue.putExtra("termName", Utility.getName(Utility.getFromID(data, termId)));
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