package com.example.musek.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.musek.R;

import com.example.musek.databinding.ActivitySignUpBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity implements SignUpInterface {
    private ActivitySignUpBinding binding;
    private LoginPresenter loginPresenter;
    private String phoneNumber, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginPresenter = new LoginPresenter(this, this, binding);
        setOnClick();

    }

    private void setOnClick() {
        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    private void checkPassword() {
        phoneNumber = binding.edPhoneNumber.getText().toString().trim();
        password = binding.edPassword.getText().toString().trim();
        String confirmPass = binding.edConfirmPassword.getText().toString().trim();
        loginPresenter.checkPassword(phoneNumber, password, confirmPass);
    }

    @Override
    public void createAccount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user_account");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(phoneNumber)) {
                    Toast.makeText(SignUpActivity.this, "Phone number" +
                            "is registered before", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child(phoneNumber).child("password").setValue(password);
                    onBackPressed();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}