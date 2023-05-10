package com.example.appmd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    ListView possibleDiseases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        try {
            addPossibleDiseases();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addPossibleDiseases() throws IOException, JSONException {

        possibleDiseases = (ListView) findViewById(R.id.diseaseList);
        new ResultsActivity.AsyncThread().execute();


    }

    public class AsyncThread extends AsyncTask<String, Void, ArrayList<JSONObject>> {
        List<String> results;

        @Override
        protected ArrayList<JSONObject> doInBackground(String... strings) {
            try {
                results = new APIClient().getOutcomesList();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ResultsActivity.this, android.R.layout.simple_list_item_1, results);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            possibleDiseases.setAdapter(dataAdapter);
        }
    }

}
