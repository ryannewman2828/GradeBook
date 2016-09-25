package com.newman.ryann.newj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Term extends AppCompatActivity {

    // The variables responsible for the Listview
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    // The list representation of the file
    private ArrayList<String> data;

    // The variables for the term
    private int numClasses;
    private int termId;
    private double average;
    private String termName;

    // The variables for the Textviews
    private EditText termTitle;
    private TextView termAverage;

    // The variables for the save file
    private File file;
    private final static String DATA = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize the variables for the Textviews
        termTitle = (EditText) findViewById(R.id.title);
        termAverage = (TextView) findViewById(R.id.average);

        // Retrieves the data from the previous activity
        Intent intentExtras = getIntent();
        Bundle extraBundle = intentExtras.getExtras();
        if(extraBundle != null) {
            if (extraBundle.containsKey("termID"))
                termId = extraBundle.getInt("termID");

            if (extraBundle.containsKey("termName"))
                termName = extraBundle.getString("termName");
        }

        // Initialize the Listview variables
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();

        // Initialize the file variables
        file = new File(getApplicationContext().getFilesDir(), DATA);
        data = Utility.fileToList(file);

        // Gets the input the moment of a change in the title
        termTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Updates the data list at the same index with the new name
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    termName = "New_Term";
                } else {
                    termName = s.toString();
                }
                String temp = Utility.setName(Utility.getFromID(data, termId), termName);
                int index = data.indexOf(Utility.getFromID(data, termId));
                data.remove(Utility.getFromID(data, termId));
                data.add(index, temp);
                Utility.listToFile(file, data);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Build the Listview from the class tags located in data
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).substring(0,data.get(i).indexOf(":")).equals("class")
                    && Utility.getIDT(data.get(i)) == termId){
                arrayList.add(Utility.getName(data.get(i)) + " - " + Utility.getAverage(data.get(i)) + "%");
                average += Utility.getAverage(data.get(i));
            }
        }
        numClasses = arrayList.size();

        // Calculates the term average and updates the data list with this information
        String temp;
        if(numClasses == 0){
            temp = Utility.setAverage(Utility.getFromID(data, termId), 0.0);
        } else {
            temp = Utility.setAverage(Utility.getFromID(data, termId), average / numClasses);
        }
        int index = data.indexOf(Utility.getFromID(data, termId));
        data.remove(Utility.getFromID(data, termId));
        data.add(index, temp);
        Utility.listToFile(file, data);

        // Sets the hint/text for the Textview
        if(termName.equals("New_Term")) {
            termTitle.setHint(termName);
        } else {
            termTitle.setText(termName);
        }
        termAverage.setText("Term Average: " + Utility.getAverage(Utility.getFromID(data, termId)) + "%");

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
                Intent intent = new Intent(Term.this, Classes.class);
                intent.putExtra("termID", termId);
                intent.putExtra("classID", position);
                intent.putExtra("className", Utility.getName(Utility.getFromID(data, termId, position)));
                startActivity(intent);
            }
        });

        // Adds an element to the listView when the fab is clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(getApplicationContext().getFilesDir(), DATA);
                arrayList.add("New_Class - 0%");
                data.add("class:" + termId + ":" + numClasses + ":New_Class:0.00");
                numClasses++;
                Utility.listToFile(file, data);
                listView.setAdapter(adapter);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}