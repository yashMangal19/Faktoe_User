package com.example.faktoe_user.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.faktoe_user.R;
import com.example.faktoe_user.signUpFragments.UserSignUpMobileVeriication;

public class UserSignUpFragmentContainer extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_fragment_container);
        textView = findViewById(R.id.signIn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
            public void openActivity(){
                Intent intent = new Intent(UserSignUpFragmentContainer.this, UserSignInFragment.class);
                startActivity(intent);
            }
        });


        //Setting mobile verificaton fragment default in fragment container
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerUserSingUp, new UserSignUpMobileVeriication()).commit();
    }
}