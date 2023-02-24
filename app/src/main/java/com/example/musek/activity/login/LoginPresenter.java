package com.example.musek.activity.login;

import android.app.Activity;

import com.example.musek.databinding.ActivitySignInBinding;
import com.example.musek.databinding.ActivitySignUpBinding;

public class LoginPresenter {
    SignUpInterface signUpInterface;
    SignInInterface signInInterface;
    Activity activity;
    ActivitySignUpBinding binding1;
    ActivitySignInBinding binding2;

    public LoginPresenter(SignUpInterface signUpInterface, Activity activity, ActivitySignUpBinding binding1) {
        this.signUpInterface = signUpInterface;
        this.activity = activity;
        this.binding1 = binding1;
    }
    public LoginPresenter(SignInInterface signInInterface, Activity activity, ActivitySignInBinding binding2) {
        this.signInInterface = signInInterface;
        this.activity = activity;
        this.binding2 = binding2;
    }

    public void checkPassword(String phoneNumber,String pass, String confirmPass){
        if (phoneNumber.isEmpty()){
            binding1.tvShowError.setText("Phone number is empty");
            return;
        }else if (phoneNumber.length() < 10){
            binding1.tvShowError.setText("Phone number is invalid");
            return;
        }else {
            binding1.tvShowError.setText("");
        }
        if (pass.isEmpty()) {
            binding1.tvShowError.setText("Password is empty");
            return;
        } else if (pass.length() < 6) {
            binding1.tvShowError.setText("Invalid password, at least 6 characters");
            return;
        } else {
            binding1.tvShowError.setText("");
        }
        if (confirmPass.isEmpty()) {
            binding1.tvShowError.setText("Confirm Password is empty");
            return;

        } else if (!confirmPass.equals(pass)) {
            binding1.tvShowError.setText("Confirm password doesn't match");
            return;
        }else {
            binding1.tvShowError.setText("");
        }
        if (binding1.tvShowError.getText().toString().trim().isEmpty()){
            signUpInterface.createAccount( );
        }

    }
    public void checkUserAccount (String phoneNumber, String password){
        if (phoneNumber.isEmpty()){
            binding2.tvShowError.setText("Phone number is empty");
            return;
        }else {
            binding2.tvShowError.setText("");

        }
        if (password.isEmpty()){
            binding2.tvShowError.setText("Password is empty");
            return;
        }else {
            binding2.tvShowError.setText("");
        }
        signInInterface.checkAccount(phoneNumber,password);
    }
}
