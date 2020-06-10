package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.assignment3.networkconnection.NetworkConnection;
import com.example.assignment3.util.DateTimeFormat;
import com.example.assignment3.util.SHA;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {
    NetworkConnection networkConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        networkConnection = new NetworkConnection();

        // registration info
        final EditText username = findViewById(R.id.signup_username);
        final EditText password = findViewById(R.id.signup_password);
        final EditText confirmPassword = findViewById(R.id.signup_confirm_password);
        final EditText firstName = findViewById(R.id.signup_first_name);
        final EditText surname = findViewById(R.id.signup_surname);
        final EditText address = findViewById(R.id.signup_address);
        final EditText state = findViewById(R.id.signup_state);
        final EditText postcode = findViewById(R.id.signup_postcode);
        final DatePicker DoB = findViewById(R.id.date_Picker);

        final RadioGroup genderGroup = findViewById(R.id.gender_group);

        // submit button
        Button submitBtn = findViewById(R.id.signup_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameStr = username.getText().toString();
                // hash password
                String passwordStr = null;
                try {
                    passwordStr = SHA.sha1(password.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // hash confirm password
                String confirmPasswordStr = null;
                try {
                    confirmPasswordStr = SHA.sha1(confirmPassword.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                final String firstNameStr = firstName.getText().toString();
                final String surnameStr = surname.getText().toString();
                final String addressStr = address.getText().toString();
                final String stateStr = state.getText().toString();
                final String postcodeStr = postcode.getText().toString();
                final String DoBStr = DateTimeFormat.dateStrAddTail(DateTimeFormat.getDateStr(DoB));

                // check selected gender and decide gender string
                final String genderStr;
                switch (genderGroup.getCheckedRadioButtonId()) {
                    case R.id.isFemale:
                        genderStr = "Female";
                        break;
                    case R.id.isMale:
                        genderStr = "Male";
                        break;
                    case R.id.isOthers:
                        genderStr = "Others";
                        break;
                    default:
                        genderStr = "";
                }

                final String[] userDetails = {usernameStr, passwordStr, firstNameStr, surnameStr,
                                            genderStr, DoBStr, addressStr, stateStr, postcodeStr};

                if (usernameStr.isEmpty() || passwordStr.isEmpty() || firstNameStr.isEmpty()) {
                    Toast.makeText(SignUpActivity.this,"Do not leave entries with * empty!",Toast.LENGTH_LONG).show();
                }
                else if(passwordStr != confirmPasswordStr) {
                    Toast.makeText(SignUpActivity.this,"Confirm password is different. Check again!",Toast.LENGTH_LONG).show();
                }
                else {
                    SubmitNewUser submitNewUser = new SubmitNewUser();
                    submitNewUser.execute(userDetails);
                }
            } });
    }


    private class SubmitNewUser extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... strings) {
            return networkConnection.addPerson(strings[0]);
        }

        @Override
        protected void onPostExecute(String addUserStatement) {
            // if successfully registered, let the new user login
            if(addUserStatement.startsWith("!@#getUserName:")) {
                String username = addUserStatement.replace("!@#getUserName:", "");
                Toast.makeText(SignUpActivity.this, "You have successfully registered!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this,
                        HomeActivity.class);
                // pass the new user's userId to Home Activity
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else {
                Toast.makeText(SignUpActivity.this, addUserStatement, Toast.LENGTH_LONG).show();
            }
        }
    }
}
