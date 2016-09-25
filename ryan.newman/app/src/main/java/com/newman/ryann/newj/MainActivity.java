package com.newman.ryann.newj;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TODO have the hints save the original name so no empty entries can be inputted (possibly done?)
    //TODO add ability to delete stuff

    // The variables responsible for the Listview
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    // The list representation of the file
    private ArrayList<String> data;

    // The number of elements in the list view
    private int numTerms;

    // The variables for the save file
    private File file;
    private final static String DATA = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the Listview variables
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        registerForContextMenu(listView);

        // Initialize the file variables
        file = new File(getApplicationContext().getFilesDir(), DATA);
        data = Utility.fileToList(file);

        // Build the Listview from the term tags located in data
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).substring(0, data.get(i).indexOf(":")).equals("term")){
                arrayList.add(Utility.getName(data.get(i)));
            }
        }
        numTerms = arrayList.size();

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
                String threadID = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, Term.class);
                intent.putExtra("termID", position);
                intent.putExtra("termName", Utility.getName(Utility.getFromID(data, position)));
                startActivity(intent);
            }
        });

        // Adds an element to the listView when the fab is clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(getApplicationContext().getFilesDir(), DATA);
                arrayList.add("New_Term");
                data.add("term:" + numTerms + ":New_Term:0.00");
                numTerms++;
                Utility.listToFile(file, data);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }
}