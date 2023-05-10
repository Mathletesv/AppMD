package com.example.appmd;

import android.provider.Settings;
import android.util.Log;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class APIClient {

    private static String host ;
    private static String baseUrl;
    private static String apiKey ;
    private static String sessionId;

    public APIClient() throws IOException, JSONException {

        host = "endlessmedicalapi1.p.rapidapi.com";
        baseUrl = "https://endlessmedicalapi1.p.rapidapi.com";
        apiKey = "d75c4af195mshf4c5b2ab68d1e73p1ef0b0jsn8b397385fee2";

        if(sessionId == null || sessionId.length() < 1) {
            Response response = initSession();
            JSONObject obj = new JSONObject(response.body().string());
            sessionId = (String) obj.get("SessionID");
            acceptTermsOfUse();
        }

    }
    /*   public static void main(String[] args) throws IOException, JSONException {

        APIClient myclient = new APIClient();
        Response response = myclient.initSession();

*//*
        System.out.println("Resonse code: " + response.code());
        System.out.println(response);

*//*
        JSONObject obj = new JSONObject(response.body().string());
        String sessionId = (String) obj.get("SessionID");
//        System.out.println("Session ID: " + sessionId);

        Response responseTerms = myclient.acceptTermsOfUse(sessionId, apiKey);
//       System.out.println("TOC Response code: " + responseTerms.code());
//        System.out.println(responseTerms);

        Response responseFeatures = myclient.getFeatures();
        System.out.println(responseFeatures.body().string());

        Response responseOutcomes = myclient.getOutcomes();
        System.out.println(responseOutcomes.body().string());

        Response responseUpdateFeature = myclient.updateFeature(sessionId, "age", "50");
        responseUpdateFeature = myclient.updateFeature(sessionId, "BMI", "40");
        responseUpdateFeature = myclient.updateFeature(sessionId, "SBP", "250");
        responseUpdateFeature = myclient.updateFeature(sessionId, "DBP", "150");
        Response responseAnalyzeDisease = myclient.analyzeDisease(sessionId);
        System.out.println(responseAnalyzeDisease.body().string());

    }*/



    public Response initSession() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "/InitSession")
                .get()
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .build();

        Log.e("init Request: " , request.toString());
        return client.newCall(request).execute();

    }

    public Response acceptTermsOfUse() throws IOException {

        OkHttpClient client = new OkHttpClient();

        final String passPhrase = "I have read, understood and I accept and agree to comply with the " +
                "Terms of Use of EndlessMedicalAPI and Endless Medical services. The Terms of Use are available on endlessmedical.com";

        String urlString = baseUrl + "/AcceptTermsOfUse?SessionID=" + sessionId + "&passphrase=" + URLEncoder.encode(passPhrase, "UTF-8");
        // String urlString = baseUrl + "/AcceptTermsOfUse";
//      System.out.println("TOC URL: " + urlString);

        RequestBody requestBody = new FormBody.Builder()
                //.add("SessionID", sessionId)
                //.add("passphrase", passPhrase)
                .build();
        System.out.println(requestBody.toString());
        Request request = new Request.Builder()
                .url(urlString)
                .post(requestBody)
                .addHeader("x-rapidapi-host", baseUrl.substring(8))
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();

    }

    public Response getFeatures() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "/GetFeatures")
                .get()
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .build();
        Log.i("Request", request.toString());
        Response response = client.newCall(request).execute();
        Log.i("Response" , response.toString());
        return response;
    }

    public Response updateFeature(String parameterName, String parameterValue) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder().build();

        Request request = new Request.Builder()
                .url(baseUrl + "/UpdateFeature?SessionID=" + sessionId + "&name=" + parameterName + "&value=" + URLEncoder.encode(parameterValue, "UTF-8"))
                .post(requestBody)
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();

    }

    public Response deleteFeature(String parameterName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(baseUrl + "/DeleteFeature?SessionID=" + sessionId + "&name=" + parameterName)
                .post(requestBody)
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    public Response analyzeDisease() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "/Analyze?SessionID=" + sessionId)
                .get()
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .build();

        return client.newCall(request).execute();

    }

    public Response getOutcomes() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "/GetOutcomes")
                .get()
                .addHeader("x-rapidapi-host", host)
                .addHeader("x-rapidapi-key", apiKey)
                .build();

        return client.newCall(request).execute();

    }

    public List<String> getHealthQuestions() throws IOException, JSONException {

        List<String> questions = new ArrayList<>();
        JSONArray jsonArray =  (JSONArray) (new JSONObject(getFeatures().body().string())).get("data");
        for(int i = 0; i < jsonArray.length(); i++) {
            questions.add((String) jsonArray.get(i));
        }

        return questions;
    }

    public List<String> getOutcomesList() throws IOException, JSONException {

        for(String key : GlobalValues.featuresMap.keySet()) {
            updateFeature(key, GlobalValues.featuresMap.get(key));
        }

        Response response = analyzeDisease();
        List<String> diseases = new ArrayList<>();
        JSONArray jsonArray =  (JSONArray) (new JSONObject(response.body().string())).get("Diseases");
        for(int i = 0; i < jsonArray.length(); i++) {
            Log.i(i+"", jsonArray.get(i).toString());
            diseases.add(cleanupDisease(jsonArray.get(i).toString()));
        }
        /*String diseaseAdder = "";
        Double percent = 0.0;
        String diseaseAdder2 = "";

        diseaseAdder = diseases.get(0).replace("{\"","Chance of ");
        diseaseAdder.replace("\":\"",":");
        percent = Math.floor(Double.valueOf(diseaseAdder.substring(diseaseAdder.indexOf(":")+1)) * 100) / 100;
        diseaseAdder.substring(0,diseaseAdder.indexOf(":")+1);
        diseaseAdder2 = diseaseAdder+(percent.toString());

        List<String> modifiedResults = new ArrayList<>();
        modifiedResults.add(diseaseAdder2);

         */

        return diseases;

    }

    private String cleanupDisease(String jsonString) {

        jsonString = jsonString.substring(1, jsonString.length() -1);
        String diseaseString = jsonString.split(":")[0];
        diseaseString = diseaseString.substring(1, diseaseString.length() - 1);
        String percentString = jsonString.split(":")[1];
        percentString = percentString.substring(1, percentString.length() - 1);
        double percentVal = Double.parseDouble(percentString);
        percentString = (Math.floor(percentVal * 10000) / 100.0) + "%";

        return diseaseString + " : " + percentString;

    }
}
