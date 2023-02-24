package com.example.musek.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.musek.R;
import com.example.musek.databinding.ActivitySignInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity implements SignInInterface{
    private ActivitySignInBinding binding;
    private LoginPresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new LoginPresenter(this,this,binding);
        setOnClick ();

    }

    private void setOnClick() {
        binding.btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.edPhoneNumber.getText().toString().trim();
                String password = binding.edPassword.getText().toString().trim();
                presenter.checkUserAccount(phoneNumber,password);
            }
        });
    }


    @Override
    public void checkAccount(String phoneNumber, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user_account");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(phoneNumber)) {
                    binding.tvShowError.setText("Account isn't existed");
                }else {
                    String getPassword = snapshot.child(phoneNumber)
                            .child("password").getValue(String.class);
                    if (!getPassword.equals(password)){
                        binding.tvShowError.setText("password is wrong");
                    }else {
                        Intent data = new Intent();
                        data.putExtra("key_phone_number", phoneNumber);
                        setResult(RESULT_OK, data);
                        finish();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}