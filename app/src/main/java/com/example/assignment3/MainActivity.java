package com.example.assignment3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.assignment3.networkconnection.NetworkConnection;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NetworkConnection networkConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkConnection = new NetworkConnection();

        // Sign-in button
        final EditText inputAccount = findViewById(R.id.input_account);
        final EditText inputPassword = findViewById(R.id.input_password);

        Button signInBtn = findViewById(R.id.signInButton);
//        signInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,
//                        HomeActivity.class);
//                startActivity(intent);
//            } });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            // TODO: 连接数据库，验证帐号密码
            @Override
            public void onClick(View v) {
                final String inputAccountStr = inputAccount.getText().toString();
                final String inputPasswordStr = inputPassword.getText().toString();
                final String[] inputAccountPasswordList = {inputAccountStr, inputPasswordStr};
                if (inputAccountStr.isEmpty() || inputPasswordStr.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Username or password cannot be empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ValidateAccount validateAccount = new ValidateAccount();
                    validateAccount.execute(inputAccountPasswordList);
                }
            } });

        // Sign-up button
        // TODO: 测试用，临时修改
        Button signUpBtn = findViewById(R.id.signUpButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        HomeActivity.class);
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
                hashedInputPassword = sha.SHA1(inputAndQueryPasswords[0]);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // compare hashed input password and real password
            if (hashedInputPassword.equals(inputAndQueryPasswords[1])) {
                Intent intent = new Intent(MainActivity.this,
                        HomeActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this,"Incorrect username or password",Toast.LENGTH_LONG).show();
            }
        }
    }
}


