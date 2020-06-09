package com.example.assignment3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.assignment3.networkconnection.NetworkConnection;
import com.example.assignment3.util.SHA;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    NetworkConnection networkConnection = null;
    // input login info
    EditText inputUsername = null;
    EditText inputPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkConnection = new NetworkConnection();

        // input login info
        inputUsername = findViewById(R.id.login_username);
        inputPassword = findViewById(R.id.login_password);

        // sign-in button
        Button signInBtn = findViewById(R.id.signInButton);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputUsernameStr = inputUsername.getText().toString();
                final String inputPasswordStr = inputPassword.getText().toString();
                final String[] inputAccountPasswordList = {inputUsernameStr, inputPasswordStr};
                if (inputUsernameStr.isEmpty() || inputPasswordStr.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Username or password cannot be empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ValidateAccount validateAccount = new ValidateAccount();
                    validateAccount.execute(inputAccountPasswordList);
                }
            } });

        // Sign-up button
        Button signUpBtn = findViewById(R.id.signUpButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            } });
    }

    private class ValidateAccount extends AsyncTask<String[], Void, String[]> {
        @Override
        protected String[] doInBackground(String[]... strings) {
            return networkConnection.getPassword(strings[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String[] inputAndQueryPasswords) {
            // hash input password
            String hashedInputPassword = null;
            try {
                hashedInputPassword = SHA.sha1(inputAndQueryPasswords[0]);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // compare hashed input password and real password
            if (hashedInputPassword.equals(inputAndQueryPasswords[1])) {
                Intent intent = new Intent(MainActivity.this,
                        HomeActivity.class);
                // pass the user's username to Home Activity
                Bundle bundle = new Bundle();
                bundle.putString("username", inputUsername.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this,"Incorrect username or password",Toast.LENGTH_LONG).show();
            }
        }
    }
}


