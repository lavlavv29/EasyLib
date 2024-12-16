package com.example.perpustakaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.AuthenticationCallback;
import com.auth0.android.provider.WebAuthProvider;

public class SettingsActivity extends AppCompatActivity {


    private Button btnOnOff;
    private boolean isNotificationsOn;
    private SharedPreferences sharedPreferences;
    private Auth0 account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        account = new Auth0(
                "0LgJfQp1SFwYKNHKbu8VJdP4Sug169Lt",
                "dev-k86utbxfpfhqo4hr.us.auth0.com"
        );

        btnOnOff = findViewById(R.id.btn_onoff);
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        isNotificationsOn = sharedPreferences.getBoolean("notifications", true);
        updateButtonText();

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNotificationsOn = !isNotificationsOn;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications", isNotificationsOn);
                editor.apply();
                updateButtonText();
            }
        });

        Button btnBack = findViewById(R.id.btn_back2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PeminjamanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(v -> {
            WebAuthProvider.logout(account).withScheme("demo").start(SettingsActivity.this, new AuthenticationCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            Toast.makeText(SettingsActivity.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            Toast.makeText(SettingsActivity.this, "Logout failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });


    }

    private void updateButtonText() {
        if (isNotificationsOn) {
            btnOnOff.setText("Notifications Off");
        } else {
            btnOnOff.setText("Notifications On");
        }
    }
}