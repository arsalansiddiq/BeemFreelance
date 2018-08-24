package com.example.arsalansiddiq.beem.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.databases.BeemDatabase;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private NetworkUtils networkUtils;
    private EditText edtText_username, edtText_password;
    String username, password;
    private Button btn_login;
    private Intent intent;
    private BeemDatabase beemDatabase;
    private BeemPreferences beemPreferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        beemDatabase = new BeemDatabase(this);
        beemDatabase.getWritableDatabase();
        beemPreferences = new BeemPreferences(this);


        networkUtils = new NetworkUtils(LoginActivity.this);

        edtText_username = findViewById(R.id.edtText_username);
        edtText_password = findViewById(R.id.edtText_password);
        btn_login = findViewById(R.id.btn_login);

        username = String.valueOf(edtText_username.getText());
        password = String.valueOf(edtText_password.getText());

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
//                startActivity(intent);

                if (TextUtils.isEmpty(edtText_username.getText().toString()) || TextUtils.isEmpty(edtText_password.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please insert Valid credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    LoginRequest loginRequest = new LoginRequest(edtText_username.getText().toString(), edtText_password.getText().toString());
                    if (networkUtils.isNetworkConnected()) {
                        networkUtils.userLogin(loginRequest, new LoginInterface() {
                            @Override
                            public void success(Response<LoginResponse> loginResponse) {
                                if (loginResponse.body().getStatus() == 1) {

                                    beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_ON);

                                    if (beemDatabase.checkUserExist(loginResponse.body().getUserId())) {
                                        intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                        startActivity(intent);
                                    } else {
                                        beemDatabase.insertBAInfo(loginResponse.body().getUserId(),
                                                loginResponse.body().getName(), loginResponse.body().getBrand());
                                        intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void failed(String error) {
                                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
