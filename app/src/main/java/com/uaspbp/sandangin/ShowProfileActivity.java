package com.uaspbp.sandangin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uaspbp.sandangin.API.ApiClient;
import com.uaspbp.sandangin.API.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class ShowProfileActivity extends AppCompatActivity {
//
//    private TextView twNama, twNim, twFakultas, twProdi, twJenisKelamin;
//    private String sIdUser, sNama, sNim, sFakultas, sProdi, sJenisKelamin;
//    private Button btnLogout;
//    private ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_profile);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.show();
//
//        twNama = findViewById(R.id.twNama);
//        twNim = findViewById(R.id.twNim);
//        twFakultas = findViewById(R.id.twFakultas);
//        twProdi = findViewById(R.id.twProdi);
//        twJenisKelamin = findViewById(R.id.twJenisKelamin);
//        btnLogout = findViewById(R.id.btnLogout);
//
//        Bundle bundle = getIntent().getExtras();
//        sIdUser = bundle.getString("id");
//        loadUserById(sIdUser);
//
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//
////        btnUpdet = findViewById(R.id.btnUpdate);
////        btnUpdet.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent i = new Intent(ShowProfileActivity.this, EditUserActivity.class);
////                i.putExtra("id",sIdUser);
////                startActivity(i);
////            }
////        });
//    }
//
//    private void loadUserById(String sIdUser) {
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<UserResponse> call = apiService.getUserById(sIdUser, "data");
//
//        call.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                sNama = response.body().getUsers().get(0).getNama();
//                sNim = response.body().getUsers().get(0).getNim();
//                sFakultas = response.body().getUsers().get(0).getFakultas();
//                sProdi = response.body().getUsers().get(0).getProdi();
//                sJenisKelamin = response.body().getUsers().get(0).getJenis_kelamin();
//
//                twNama.setText(sNama);
//                twNim.setText(sNim);
//                twFakultas.setText(sFakultas);
//                twProdi.setText(sProdi);
//                twJenisKelamin.setText(sJenisKelamin);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                Toast.makeText(ShowProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });
//    }
//}


public class ShowProfileActivity extends AppCompatActivity {
    private TextView twNama, twEmail, twTelpon;
    private String sIdUser, sNama, sEmail, sTelpon;
    private Button btnUpdet;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        twNama = findViewById(R.id.twNama);
        twEmail = findViewById(R.id.twEmail);
        twTelpon = findViewById(R.id.twTelpon);

        sIdUser = getIntent().getStringExtra("id");
        loadUserbyId(sIdUser);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserbyId(sIdUser);
            }
        });

        btnUpdet = findViewById(R.id.btnUpdet);
        btnUpdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(com.uaspbp.sandangin.ShowProfileActivity.this, EditUserActivity.class);
                i.putExtra("id",sIdUser);
                startActivity(i);
            }
        });
    }

    private void loadUserbyId(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.getUserById(id, "data");

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sNama = response.body().getUsers().get(0).getNama();
                sEmail = response.body().getUsers().get(0).getEmail();
                sTelpon= response.body().getUsers().get(0).getTelpon();

                twNama.setText(sNama);
                twEmail.setText(sEmail);
                twTelpon.setText(sTelpon);

                progressDialog.dismiss();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(com.uaspbp.sandangin.ShowProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
