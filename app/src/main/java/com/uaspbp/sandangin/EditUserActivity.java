package com.uaspbp.sandangin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uaspbp.sandangin.API.ApiClient;
import com.uaspbp.sandangin.API.ApiInterface;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private EditText etNama, etTelpon, etEmail, etPassword;
    private Button btnCancel, btnUpdate;
    private String sNama, sEmail, sTelpon;
    private String sIdUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        etNama = findViewById(R.id.etNama);
        etTelpon = findViewById(R.id.etTelpon);
        etPassword = findViewById(R.id.etPassword);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNama.getText().toString().isEmpty()) {
                    etNama.setError("Isikan dengan benar");
                    etNama.requestFocus();
                } else if(etTelpon.getText().length() > 10) {
                    etTelpon.setError("Minimal 10 angka");
                    etTelpon.requestFocus();
                } else if(etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Isikan dengan benar");
                    etPassword.requestFocus();
                } else {
                    progressDialog.show();
                    updateUser(sIdUser);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        sIdUser = bundle.getString("id");
        loadUserById(sIdUser);
    }

    private void loadUserById(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.uaspbp.sandangin.UserResponse> add = apiService.getUserById(id, "data");

        add.enqueue(new Callback<com.uaspbp.sandangin.UserResponse>() {
            @Override
            public void onResponse(Call<com.uaspbp.sandangin.UserResponse> call, Response<com.uaspbp.sandangin.UserResponse> response) {
                sNama = response.body().getUsers().get(0).getNama();
                sTelpon = response.body().getUsers().get(0).getTelpon();
                sEmail = response.body().getUsers().get(0).getEmail();

                etNama.setText(sNama);
                etEmail.setText(sEmail);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<com.uaspbp.sandangin.UserResponse> call, Throwable t) {
                Toast.makeText(com.uaspbp.sandangin.EditUserActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void updateUser(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.uaspbp.sandangin.UserResponse> call = apiService.updateUser(id, "data", etNama.getText().toString(),
                etEmail.getText().toString(), etTelpon.getText().toString(), etPassword.getText().toString());

        call.enqueue(new Callback<com.uaspbp.sandangin.UserResponse>() {
            @Override
            public void onResponse(Call<com.uaspbp.sandangin.UserResponse> call, Response<com.uaspbp.sandangin.UserResponse> response) {
                progressDialog.dismiss();
                Toast.makeText(com.uaspbp.sandangin.EditUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();
//                Intent intent = new Intent(EditUserActivity.this, ShowListUserActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onFailure(Call<com.uaspbp.sandangin.UserResponse> call, Throwable t) {
                Toast.makeText(com.uaspbp.sandangin.EditUserActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}