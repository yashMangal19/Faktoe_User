package com.example.faktoe_user.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.faktoe_user.R;
import com.example.faktoe_user.signInFragments.UserSignInMobileVerification;

public class UserSignInFragment extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in_fragment);
        textView = findViewById(R.id.signUp);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
            public void openActivity(){
                Intent intent = new Intent(UserSignInFragment.this, UserSignUpFragmentContainer.class);
                startActivity(intent);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, new UserSignInMobileVerification()).commit();
    }
}