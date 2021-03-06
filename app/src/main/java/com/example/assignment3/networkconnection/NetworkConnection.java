package com.example.assignment3.networkconnection;

import com.example.assignment3.dataclass.Credential;
import com.example.assignment3.util.DateTimeFormat;
import com.example.assignment3.dataclass.Memoir;
import com.example.assignment3.dataclass.Person;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        String todayStr = DateTimeFormat.dateStrAddTail(new SimpleDateFormat("yyyy-MM-dd").format(today));
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
            // new user successfully registered, pass the username out
            // in the form of
            strResponse = "!@#getUserName:" + details[0];
        }
        else {
            strResponse = "Error occured when registering. Please try again.";
        }

        return strResponse;
    }

//    // convert String to java.sql.Date
//    public static java.sql.Date strToDate(String strDate) {
//        String str = strDate;
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date d = null;
//        try {
//            d = format.parse(str);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        java.sql.Date date = new java.sql.Date(d.getTime());
//        return date;
//    }

    // get user's first name from username
    public String getFirstName(String username) {
        final String methodPath = "restws.credential/findByUsername/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + username);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            JSONArray queryResult = new JSONArray(response.body().string());
            JSONObject personId = new JSONObject(queryResult.getJSONObject(0).getString("personId"));
            results = personId.getString("firstName");
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    // get user's id
    public Integer getUserId(String username) {
        final String methodPath = "restws.credential/findByUsername/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + username);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            JSONArray queryResult = new JSONArray(response.body().string());
            JSONObject personId = new JSONObject(queryResult.getJSONObject(0).getString("personId"));
            results = personId.getString("personId");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Integer.parseInt(results);
    }

    // get cinema count (to decide new cinema id)
    public Integer getCinemaCount() {
        Integer cinemaId = null;

        final String methodPath = "restws.cinema/count/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath );
        Request request = builder.build();
        
        try {
            Response response = client.newCall(request).execute();
            cinemaId = Integer.parseInt(response.body().string());
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return cinemaId;
    }

    // get cinema info
    public Memoir.Cinema getCinema(int cinemaId) {
        Memoir.Cinema cinema = null;

        final String methodPath = "restws.cinema/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + cinemaId);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject queryResult = new JSONObject(response.body().string());

            cinema = new Memoir.Cinema(
                    cinemaId,
                    queryResult.getString("cinemaName"),
                    queryResult.getString("cinemaLocation"),
                    queryResult.getString("cinemaPostcode")
            );

        }catch (Exception e){
            e.printStackTrace();
        }
        return cinema;
    }

    // get person info
    public Person getPerson(int userId) {
        Person person = null;

        final String methodPath = "restws.person/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + userId);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject queryResult = new JSONObject(response.body().string());

            person = new Person(
                    Integer.parseInt(queryResult.getString("personId")),
                    queryResult.getString("firstName"),
                    queryResult.getString("surname"),
                    "Male",
                    "1998-01-27T00:00:00+08:00",
                    queryResult.getString("address"),
                    queryResult.getString("stateName"),
                    queryResult.getString("postcode")
            );

        }catch (Exception e){
            e.printStackTrace();
        }
        return person;
    }

    // get memoir list to display in home page
    public String getMemoirForHome(Integer personId) {
        final String methodPath = "restws.memoir/findMemoir2020/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + personId);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    // get all memoir entries
    public String getMemoirData(Integer personId) {
        final String methodPath = "restws.memoir/findByPersonId/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath + personId);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    // get movie search result
    public String getMovieSearchResult(String keyword) {
        String urlKeyword = URLEncoder.encode(keyword);
        Request request = new Request.Builder()
                .url("https://movie-database-imdb-alternative.p.rapidapi.com/?page=1&r=json&s="+urlKeyword)
                .get()
                .addHeader("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e524238e00msh82ec894551f7e7cp1b7de0jsn58a5c160ad7e")
                .build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String getMovieDetail(String imdbId) {
        Request request = new Request.Builder()
                .url("https://movie-database-imdb-alternative.p.rapidapi.com/?i=" + imdbId + "&r=json")
                .get()
                .addHeader("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e524238e00msh82ec894551f7e7cp1b7de0jsn58a5c160ad7e")
                .build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public String submitMemoir(List<Map<String, String>> sourceData) {
        // in the parameter:
        // sourceData.get(0) is cinema info;
        // sourceData.get(1) is memoir info;

        // parameters String list content:
        int memoirId = -1;
        String strResponse = "";
        Person person = getPerson(Integer.parseInt(sourceData.get(1).get("personId")));
        Integer cinemaId = getCinemaCount() + 1;

        String methodPath = "restws.memoir/count";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();

        // get current count of user to initialize user id
        try {
            Response response = client.newCall(request).execute();
            memoirId = Integer.parseInt(response.body().string()) + 1;
        }catch (Exception e){
            e.printStackTrace();
        }

        // upload cinema entry data
        Memoir.Cinema cinema = new Memoir.Cinema(
                cinemaId,
                sourceData.get(0).get("cinemaName"),
                sourceData.get(0).get("cinemaPostcode")
        );

        Gson gson = new Gson();
        String cinemaJson = gson.toJson(cinema);

        methodPath = "restws.cinema/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);

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

        // deal with empty comment case, to make sure data can be correctly parsed
        String comment = sourceData.get(1).get("comment");
        if(comment == "") comment = " ";

        // upload memoir entry data
        Memoir memoir = new Memoir(
                memoirId,
                sourceData.get(1).get("movieName"),
                DateTimeFormat.dateFormatConvert(sourceData.get(1).get("movieReleaseDate")),
                sourceData.get(1).get("watchDate"),
                sourceData.get(1).get("watchTime"),
                comment +";;;lll;;;"+sourceData.get(1).get("cinemaPostcode")+";;;lll;;;"+sourceData.get(1).get("imdbId"),
                (int)(Float.parseFloat(sourceData.get(1).get("score"))*10.0),
                person,
                new Memoir.Cinema(cinemaId, sourceData.get(1).get("imdbId"), sourceData.get(1).get("cinemaPostcode")),
                sourceData.get(1).get("cinemaPostcode"),
                sourceData.get(1).get("imdbId")
        );

        gson = new Gson();
        String memoirJson = gson.toJson(memoir);

        methodPath = "restws.memoir/";
        body = RequestBody.create(memoirJson, JSON);

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

        return strResponse;
    }

    public HashMap<String, String> getCoordinate(Integer userId) throws UnsupportedEncodingException, JSONException {

        // used to store cinema-coordinate pair
        // user location is also stored here whose key value is "user"
        HashMap<String, String> cinemaCoordPair = new HashMap<>();
        // Cinema location set. Using set can reduce query times
        HashSet<String> cinemaSet = new HashSet<>();

        /* load user's address */
        Person person = getPerson(userId);
        String userLocation = person.getAddress() + " " + person.getStateName();// + " " + person.getPostcode();
        String userLocationURL = URLEncoder.encode(userLocation, "UTF-8");
        Request request = new Request.Builder()
                .url("https://geocode-address-to-location.p.rapidapi.com/v1/geocode/search?limit=1&lang=en&text=" +
                        userLocationURL)
                .get()
                .addHeader("x-rapidapi-host", "geocode-address-to-location.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e524238e00msh82ec894551f7e7cp1b7de0jsn58a5c160ad7e")
                .build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userCoord = getLatLng(results);
        // add user data into hashmap
        cinemaCoordPair.put("user", userCoord);


        /* load cinema location */
        String memoirSourceData = getMemoirData(userId);
        // parse source data
        JSONArray memoirEntries = new JSONArray(memoirSourceData);
        for(int i = 0; i < memoirEntries.length(); i++) {
            JSONObject entry = (JSONObject) memoirEntries.get(i);
            String cinemaName = entry.getJSONObject("cinemaId").getString("cinemaName");
            String cinemaLocation = entry.getJSONObject("cinemaId").getString("cinemaLocation");
            String cinemaPostcode = entry.getJSONObject("cinemaId").getString("cinemaPostcode");
            String cinemaAddress = cinemaName + " " + cinemaLocation + " " + cinemaPostcode;
            // add cinema address into set
            cinemaSet.add(cinemaAddress);
        }

        // geocode cinemas
        for(String entry : cinemaSet) {
            String cinemaLocationURL = URLEncoder.encode(entry, "UTF-8");
            // get source data from API
            request = new Request.Builder()
                    .url("https://geocode-address-to-location.p.rapidapi.com/v1/geocode/search?limit=1&lang=en&text=" +
                            cinemaLocationURL)
                    .get()
                    .addHeader("x-rapidapi-host", "geocode-address-to-location.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "e524238e00msh82ec894551f7e7cp1b7de0jsn58a5c160ad7e")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // parse and get LatLng
            String cinemaCoord = getLatLng(results);

            cinemaCoordPair.put(entry, cinemaCoord);
        }

        return cinemaCoordPair;
    }

    // get LatLng coord from source data
    private String getLatLng(String sourceData) throws JSONException {
        String longtitude = null;
        String latitude = null;

        JSONObject jsonObject = new JSONObject(sourceData);
        JSONObject feature = (JSONObject) jsonObject.getJSONArray("features").get(0);
        longtitude = feature.getJSONObject("properties").getString("lon");
        latitude = feature.getJSONObject("properties").getString("lat");

        return latitude + ", " + longtitude;
    }
}
