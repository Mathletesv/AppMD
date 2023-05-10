package com.example.appmd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamActivity2 extends AppCompatActivity {

    Spinner healthQuestons ;
    EditText editText;
    boolean isFirstTimeGetFocused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2);
        isFirstTimeGetFocused = true;
        editText = findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if (hasFocus && isFirstTimeGetFocused) {
                    editText.setText("");
                    isFirstTimeGetFocused = false;
                }
            }});

        try {
            addHealthQuestions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addHealthQuestions() throws IOException, JSONException {

        healthQuestons = (Spinner) findViewById(R.id.healthQuestions);
        new AsyncThread().execute();


    }

    public void addSymptomToList(View view ) {

        String choiceName = healthQuestons.getSelectedItem().toString();
        editText = findViewById(R.id.editText);
        String choiceValue = editText.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("choiceName", choiceName);
        intent.putExtra("choiceValue", choiceValue);
        setResult(RESULT_OK, intent);
        finish();
    }



    public class AsyncThread extends AsyncTask<String, Void, ArrayList<JSONObject>> {
        List<String> questions;
        @Override
        protected ArrayList<JSONObject> doInBackground(String... strings) {
            try {
                questions = new APIClient().getHealthQuestions();
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
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ExamActivity2.this, android.R.layout.simple_list_item_1, questions);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            healthQuestons.setAdapter(dataAdapter);
        }
    }

}
