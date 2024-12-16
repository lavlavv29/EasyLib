package com.example.perpustakaan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.callback.AuthenticationCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.result.UserProfile;
import com.example.perpustakaan.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Auth0 account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        account = new Auth0(
                getString(R.string.auth0_client_id),
                getString(R.string.auth0_domain)
        );

        binding.loginBtn.setOnClickListener(v -> {
            WebAuthProvider.login(account).withScheme("demo").withScope("openid profile email").start(this, new AuthenticationCallback<Credentials>() {
                        @Override
                        public void onFailure(AuthenticationException error) {
                            Toast.makeText(LoginActivity.this, "Login Error: \n" + error.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(Credentials credentials) {
                            String accessToken = credentials.getAccessToken();
                            showUserProfile(accessToken);
                        }
                    });
        });
    }



    private void showUserProfile(String accessToken) {
        AuthenticationAPIClient client = new AuthenticationAPIClient(account);

        client.userInfo(accessToken).start(new AuthenticationCallback<UserProfile>() {
            @Override
            public void onFailure(AuthenticationException error) {
                Toast.makeText(LoginActivity.this, "Error getting profile \n" + error.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(UserProfile result) {
                binding.nameTv.setText(result.getName());
                binding.emailTv.setText(result.getEmail());
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, PeminjamanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
