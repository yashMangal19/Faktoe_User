package com.example.faktoe_user.signUpFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faktoe_user.R;
import com.example.faktoe_user.activities.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class UserSignUpMobileVeriication extends Fragment {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    View view;
    TextInputEditText mobNo;
    Button cont, back;
    ProgressBar progressBar;
    TextView mainHead2;

    String verificationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_sign_up_mobile_veriication, container, false);

        cont = view.findViewById(R.id.signUpMobVerCont);
        mobNo = view.findViewById(R.id.userSignUpMobileNo);
        back = view.findViewById(R.id.backToMainActivity);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mainHead2 = view.findViewById(R.id.mainHeading2);

        //Colouring part of heading with color
        String txt = "Create new Account";
        SpannableString ss = new SpannableString(txt);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(getResources().getColor(R.color.blue));
        ss.setSpan(fcsBlue, 0, 10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mainHead2.setText(ss);



        //mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        mAuth = FirebaseAuth.getInstance();

        // back to Main Activity click listener STARTS
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail();
            }
        });

        // OTP sending process begin
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Checking if entered number is empty
                if (TextUtils.isEmpty(Objects.requireNonNull(mobNo.getText()).toString())) {
                    Toast.makeText(getActivity(), "Enter valid number", Toast.LENGTH_SHORT).show();
                }
                //Checking if enterd number not equals to 10
                else if (mobNo.getText().toString().trim().length() != 10){
                    Toast.makeText(getActivity(), "Enter valid number", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendVerificationCode();
                }
            }
        });
        return view;
    }
    // back to Main Activity  Method Intent STARTS   (back button)
    public void updateDetail() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
    // back to Main Activity  Method Intent ENDS   (back button)


    // sending OTP Code  STARTS
    private void sendVerificationCode(){
        progressBar.setVisibility(View.VISIBLE);
        cont.setVisibility(View.INVISIBLE);

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                cont.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressBar.setVisibility(View.GONE);
                cont.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "OTP is successfully sent", Toast.LENGTH_SHORT).show();

                //passing verification ID and mobile number to other fragment
                Bundle bundle = new Bundle();
                bundle.putString("phone", mobNo.getText().toString().trim());
                bundle.putString("verificationId", verificationId);
                UserSignUpOtpVerification newFragment =  new UserSignUpOtpVerification();
                /*UserSignUpDetails detailsFragment = new UserSignUpDetails();
                detailsFragment.setArguments(bundle);*/
                newFragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerUserSingUp, newFragment)
                        .commit();
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + Objects.requireNonNull(mobNo.getText()).toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mcallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

}
