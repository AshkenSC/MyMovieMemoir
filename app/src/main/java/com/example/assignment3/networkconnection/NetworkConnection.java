package com.example.assignment3.networkconnection;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client=null;
    private String results;
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
    public NetworkConnection(){
        client=new OkHttpClient();
    }
    private static final String BASE_URL =
            "http://10.0.2.2:8080/Assignment1/webresources/";

    public String getAllPersons(){
        final String methodPath = "restws.person/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String[] getPassword(String[] inputAccountPasswordList) {
        // inputAccountPasswordList[0]: input account
        // inputAccountPasswordList[1]: input password (not hashed)

        // inputAndQueryPasswords[0]: input password (not hashed)
        // inputAndQueryPasswords[1]: query password using input account (hashed)

        String[] inputAndQueryPasswords = new String[2];
        inputAndQueryPasswords[0] = inputAccountPasswordList[1];

        // handle error: account is an empty string
        if (inputAccountPasswordList[0] == "") {
            inputAndQueryPasswords[0] = "";
            inputAndQueryPasswords[1] = "";
            return inputAndQueryPasswords;
        }

        final String methodPath = "restws.credential/findByUsername/" + inputAccountPasswordList[0];

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            JSONArray queryResult = new JSONArray(response.body().string());
           // inputAndQueryPasswords[1] = queryResult.get("passwordHash").toString();
            inputAndQueryPasswords[1] = queryResult.getJSONObject(0).getString("passwordHash");
            // TODO 访问JSON array
        }catch (Exception e){
            e.printStackTrace();
        }
        return inputAndQueryPasswords;
    }

}

