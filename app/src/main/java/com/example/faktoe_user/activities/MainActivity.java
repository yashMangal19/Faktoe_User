package com.example.faktoe_user.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.faktoe_user.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button signUp, signIn;
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = findViewById(R.id.MainSignUpBtn);
        signIn = findViewById(R.id.MainSignInBtn);
        mAuth = FirebaseAuth.getInstance();

        //On click SIgnUP BUtton
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserSignUpFragmentContainer();
            }
            public void  openUserSignUpFragmentContainer(){
                Intent intent = new Intent(MainActivity.this, UserSignUpFragmentContainer.class);
                startActivity(intent);
            }
        });


        //On click SignIn Button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserSignInFragment();
            }
            public void openUserSignInFragment(){
                Intent intent = new Intent(MainActivity.this, UserSignInFragment.class);
                startActivity(intent);
            }
        });
    }
}