package com.newman.ryann.newj;

import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ryan N on 2015-12-26.
 */
public class Utility {

    private static FileOutputStream out;
    private static StringBuilder text;

    // Takes in a file and produces the ArrayList representation of the information in the file
    public static ArrayList fileToList(File file){
        ArrayList<String> list = new ArrayList<String>();
        text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
        }
        catch (IOException e) {
            // Error
        }
        return list;
    }

    // Takes in an ArrayList and saves the information to the data file
    public static void listToFile(File file, ArrayList<String> list){
       try {
           //Reset the file
           out = new FileOutputStream(file, false);
           out.write("".getBytes());

           //Rewrite the file
           out = new FileOutputStream(file, true);

           for(int i = 0; i < list.size(); i++){
               out.write((list.get(i) + "\n").getBytes());
               out.flush();
           }
           out.close();
       } catch (IOException e){
        // Error
       }
    }

    // Removes n parts of the string to the colon
    private static String cutToColon(String item, int n){
        String result = item;
        for (int i = 0; i < n; i++){
            result = result.substring(result.indexOf(":") + 1);
        }
        return result;
    }

    // Gets the item from the data list based on the termID
    public static String getFromID(ArrayList<String> list, int IDT){
        String title = "";
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).substring(0,list.get(i).indexOf(":")).equals("term")){
                String temp = list.get(i).substring(list.get(i).indexOf(":") + 1);
                if(temp.substring(0, temp.indexOf(":")).equals(IDT + "")){
                    title = list.get(i);
                }
            }
        }
        return title;
    }

    // Gets the item from the data list based on the termID and the classID
    public static String getFromID(ArrayList<String> list, int IDT, int IDC){
        String title = "";
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).substring(0,list.get(i).indexOf(":")).equals("class")){
                String temp = list.get(i).substring(list.get(i).indexOf(":") + 1);
                if(temp.substring(0, temp.indexOf(":")).equals(IDT + "")){
                    temp = temp.substring(temp.indexOf(":") + 1);
                    if(temp.substring(0, temp.indexOf(":")).equals(IDC + "")){
                        title = list.get(i);
                    }
                }
            }
        }
        return title;
    }

    // Gets the item from the data list based on the termID, classID, and the entryID
    public static String getFromID(ArrayList<String> list, int IDT, int IDC, int IDE){
        String title = "";
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).substring(0,list.get(i).indexOf(":")).equals("entry")){
                String temp = list.get(i).substring(list.get(i).indexOf(":") + 1);
                if(temp.substring(0, temp.indexOf(":")).equals(IDT + "")){
                    temp = temp.substring(temp.indexOf(":") + 1);
                    if(temp.substring(0, temp.indexOf(":")).equals(IDC + "")){
                        temp = temp.substring(temp.indexOf(":") + 1);
                        if(temp.substring(0, temp.indexOf(":")).equals(IDE + "")){
                            title = list.get(i);
                        }
                    }
                }
            }
        }
        return title;
    }

    // Produces the name of the item
    public static String getName(String item){
        if(item.indexOf(":") != -1) {
            if (item.substring(0, item.indexOf(":")).equals("term")) {
                item = cutToColon(item, 2);
                return item.substring(0, item.indexOf(":"));
            } else if (item.substring(0, item.indexOf(":")).equals("class")) {
                item = cutToColon(item, 3);
                return item.substring(0, item.indexOf(":"));
            } else if (item.substring(0, item.indexOf(":")).equals("entry")) {
                item = cutToColon(item, 4);
                return item.substring(0, item.indexOf(":"));
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    // Sets the name of the item to nme
    public static String setName(String item, String nme){
        String result = "";
        if (item.substring(0, item.indexOf(":")).equals("term")) {
            result += "term:";
            item = item.substring(item.indexOf(":") + 1);
            result += item.substring(0, item.indexOf(":") + 1);
            item = item.substring(item.indexOf(":") + 1);
            result += nme + ":";
            item = item.substring(item.indexOf(":") + 1);
            result += item;
        } else if (item.substring(0, item.indexOf(":")).equals("class")) {
            result += "class:";
            item = item.substring(item.indexOf(":") + 1);
            result += item.substring(0, item.indexOf(":") + 1);
            item = item.substring(item.indexOf(":") + 1);
            result += item.substring(0, item.indexOf(":") + 1);
            item = item.substring(item.indexOf(":") + 1);
            result += nme + ":";
            item = item.substring(item.indexOf(":") + 1);
            result += item;
        } else if (item.substring(0, item.indexOf(":")).equals("entry")) {
            result += "entry:";
            item = item.substring(item.indexOf(":") + 1);
            result += item.substring(0, item.indexOf(":") + 1);
            item = item.substring(item.indexOf(":") + 1);
            result += item.substring(0, item.indexOf(":") + 1);
            item = item.substring(item.indexOf(":") + 1);
            result += item.substring(0, item.indexOf(":") + 1);
            item = item.substring(item.indexOf(":") + 1);
            result += nme + ":";
            item = item.substring(item.indexOf(":") + 1);
            result += item;
        }
        return result;
    }

    // Produces the average of the item
    public static double getAverage(String item){
        if(item.substring(0, item.indexOf(":")).equals("term")){
            item = item.substring(item.lastIndexOf(":") + 1);
            return Double.parseDouble(item);
        } else if(item.substring(0, item.indexOf(":")).equals("class")){
            item = item.substring(item.lastIndexOf(":") + 1);
            return Double.parseDouble(item);
        } else {
            return 0;
        }
    }

    // Sets the average of the item to avg
    public static String setAverage(String item, double avg){
        String result = "";
        result += item.substring(0, item.lastIndexOf(":") + 1);
        result += (int) (avg * 100) / 100.0;
        return result;
    }

    // Produces the termID of the item
    public static int getIDT(String item){
        int id;
        item = cutToColon(item, 1);
        id = Integer.parseInt(item.substring(0, item.indexOf(":")));
        return id;
    }

    // Produces the classID of the item, -1 if no classID
    public static int getIDC(String item){
        int id;
        if(item.substring(0,item.indexOf(":")).equals("term")){
            id = -1;
        } else {
            item = cutToColon(item, 2);
            id = Integer.parseInt(item.substring(0, item.indexOf(":")));
        }
        return id;
    }

    // Produces the entryID of the item, -1 if no entryID
    public static int getIDE(String item){
        int id;
        if(item.substring(0,item.indexOf(":")).equals("term")
                || item.substring(0,item.indexOf(":")).equals("class")){
            id = -1;
        } else {
            item = cutToColon(item, 3);
            id = Integer.parseInt(item.substring(0, item.indexOf(":")));
        }
        return id;
    }

    // Produces the updated entry item with the name, score, scoreTotal and weighting in their appropriate positions
    public static String updateEntry(String item ,String name, double score, double scoreTotal, double weighting){
        String result="";
        result = "entry:" + getIDT(item) + ":" + getIDC(item) + ":" +
                getIDE(item) + ":" + name + ":" + score + ":" + scoreTotal + ":" + weighting;
        return result;
    }

    // Produces the score of the entry item
    public static double getScore(String item){
        item = cutToColon(item, 5);
        return Double.parseDouble(item.substring(0, item.indexOf(":")));
    }

    // Produces the scoreTotal of the entry item
    public static double getScoreTotal(String item){
        item = cutToColon(item, 6);
        return Double.parseDouble(item.substring(0, item.indexOf(":")));
    }

    // Produces the weighting of the entry item
    public static double getWeighting(String item){
        item = cutToColon(item, 7);
        return Double.parseDouble(item);
    }

    public static void deleteTerm(ArrayList<String> data, int id){
        int length = data.size();
        for (int i = 0; i < length; i++){
            if(getIDT(data.get(i)) == id){
                data.remove(i);
            } else if (getIDT(data.get(i)) > id) {
                data.set(i, "term:" + (getIDT(data.get(i)) - 1) + ":" + cutToColon(data.get(i), 2));
            }
        }
    }

    public static void deleteClass(ArrayList<String> data, int id){
        int length = data.size();
        for (int i = 0; i < length; i++){
            if(getIDC(data.get(i)) == id){
                data.remove(i);
            } else if (getIDC(data.get(i)) > id) {
                data.set(i, "class:" + (getIDC(data.get(i)) - 1) + ":" + cutToColon(data.get(i), 3));
            }
        }
    }

    public static void deleteEntry(ArrayList<String> data, int id){
        int length = data.size();
        for (int i = 0; i < length; i++){
            if(getIDE(data.get(i)) == id){
                data.remove(i);
            } else if (getIDE(data.get(i)) > id) {
                data.set(i, "entry:" + (getIDE(data.get(i)) - 1) + ":" + cutToColon(data.get(i), 4));
            }
        }
    }
}