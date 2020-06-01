package com.example.assignment3.networkconnection;

import com.example.assignment3.Credential;
import com.example.assignment3.DateFormat;
import com.example.assignment3.Person;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
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
            results = response.body().string();
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
            inputAndQueryPasswords[1] = queryResult.getJSONObject(0).getString("passwordHash");
        }catch (Exception e){
            e.printStackTrace();
        }
        return inputAndQueryPasswords;
    }

    public String addPerson(String[] details) {
        // parameters String list content:
        /* userDetails = {usernameStr, passwordStr, firstNameStr, surnameStr,
         genderStr, DoBStr, addressStr, stateStr, postcodeStr}; */
        int userId = -1;
        String strResponse = "";

        // handle error: username already exists
        String methodPath = "restws.credential/findByUsername/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + details[0]);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        // if the return string is not empty, the username already exists
        if (!strResponse.equals("[]")) {
            strResponse = "Username already exists!";
            return strResponse;
        }

        // get current count of user to initialize user id
        methodPath = "restws.person/count";
        builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            userId = Integer.parseInt(response.body().string()) + 1;
        }catch (Exception e){
            e.printStackTrace();
        }

        // insert Person entry
        Person person = new Person(userId, details[2], details[3], details[4],
                details[5], details[6], details[7], details[8]);

        Gson gson = new Gson();
        String personJson = gson.toJson(person);

        methodPath = "restws.person/";
        RequestBody body = RequestBody.create(personJson, JSON);
        request = new Request.Builder()
                        .url(BASE_URL + methodPath)
                        .post(body)
                        .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // insert Credential entry
        java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        String todayStr = DateFormat.dateStrAddTail(new SimpleDateFormat("yyyy-MM-dd").format(today));
        Credential credential = new Credential(userId, details[0], details[1], todayStr, person);

        gson = new Gson();
        String credentialJson = gson.toJson(credential);

        methodPath = "restws.credential/";
        body = RequestBody.create(credentialJson, JSON);
        request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse += response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // finally, define response string
        if (strResponse.isEmpty()) {
            strResponse = "You have successfully registered!";
        }
        else {
            strResponse = "Error occured when registering. Please try again.";
        }

        return strResponse;
    }

    // convert String to java.sql.Date
    public static java.sql.Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }
}


//{"credentialId":5,"passwordHash":"7c4a8d09ca3762af61e59520943dc26494f8941b","personId":{"DoB":"May 1, 2020","address":"","firstName":"shawn","gender":"Female","personId":5,"postcode":"","stateName":"","surname":""},"signupDate":"May 31, 2020","username":"test1"}

//"credentialId":1,"passwordHash":"7c4a8d09ca3762af61e59520943dc26494f8941b","personId":{"address":"Long Street, Wuhu","dob":"1994-11-22T00:00:00+08:00","firstName":"Ashken","personId":1,"postcode":"8069","stateName":"Anhui","surname":"Bear"},"signupDate":"2020-01-01T00:00:00+08:00","username":"ashken"}

//v3 DoB不对
// {"credentialId":5,"passwordHash":"7c4a8d09ca3762af61e59520943dc26494f8941b","personId":{"DoB":"2020-05-01","address":"","firstName":"shawn","gender":"Female","personId":5,"postcode":"","stateName":"","surname":""},"signupDate":"2020-05-31","username":"test1"}

//correct
//{"credentialId":1,"passwordHash":"7c4a8d09ca3762af61e59520943dc26494f8941b","personId":{"address":"Long Street, Wuhu","dob":"1994-11-22T00:00:00+08:00","firstName":"Ashken","personId":1,"postcode":"8069","stateName":"Anhui","surname":"Bear"},"signupDate":"2020-01-01T00:00:00+08:00","username":"ashken"}