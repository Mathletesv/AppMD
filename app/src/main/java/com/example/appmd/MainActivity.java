package com.example.appmd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //fragments
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Button addSymptomButton;
    ListView featuresList;
    TextView topBanner;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    ArrayList<String> selectedFeatures = new ArrayList<String>();
    ArrayAdapter<String> featuresAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addSymptomButton = findViewById(R.id.button);
        topBanner = findViewById(R.id.textViewTitle);



        fragmentManager = getSupportFragmentManager();
        //Begin first transaction
        fragmentTransaction = fragmentManager.beginTransaction();
        //create top (button) and bottom (data) fragments and add to layout on bottom of XML
        TopFragment topFragment = new TopFragment();
        // fragmentTransaction.add(R.id.id_topFragment, topFragment);
        BottomFragment bottomFragment = new BottomFragment();
        // fragmentTransaction.add(R.id.id_bottomFragment, bottomFragment);

        //commit the transaction (end)
        fragmentTransaction.commit();

        addSymptomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSymptom(v);
            }
        });

        if(savedInstanceState != null) {
            Log.i("Instance", " Is not null");
            selectedFeatures = new ArrayList<String> (savedInstanceState.getStringArrayList("KEY1"));
            for(String feature: selectedFeatures) {
                Log.i("Feature: ", feature);
            }
        } else {
            Log.e("Saved Instance", " Is null");
        }
        featuresList  = findViewById(R.id.list_features);
        featuresAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectedFeatures);
        featuresList.setAdapter(featuresAdapter);

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, VirtualExamActivity.class);
        Button virtualVisit = (Button) findViewById(R.id.id_virtualVisit);
        String message = virtualVisit.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public class AsyncThread extends AsyncTask<String, Void, ArrayList<JSONObject>>{

        @Override
        protected ArrayList<JSONObject> doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
        }
    }

    public void addSymptom(View view) {
        Intent intent = new Intent(this, ExamActivity2.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Request Code: ", requestCode +"");
        Log.i( "Response Code: ", resultCode +"");
        if(requestCode == 2) {
            String choiceName = data.getStringExtra("choiceName");
            String choiceValue = data.getStringExtra("choiceValue");
            GlobalValues.featuresMap.put(choiceName, choiceValue);
            selectedFeatures.add(choiceName +"=\t" + choiceValue);
            featuresAdapter.notifyDataSetChanged();
            topBanner.setText("Your Symptoms are as follows:");

        }
    }

    public void analyzeDisease(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String savedSelectedFeatures = "KEY1";
        outState.putStringArrayList(savedSelectedFeatures, selectedFeatures);
    }


}
