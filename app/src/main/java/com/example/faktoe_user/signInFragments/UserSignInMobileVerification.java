package com.example.faktoe_user.signInFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faktoe_user.R;
import com.example.faktoe_user.activities.MainActivity;
import com.example.faktoe_user.signUpFragments.UserSignUpOtpVerification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSignInMobileVerification# factory method to
 * create an instance of this fragment.
 */
public class UserSignInMobileVerification extends Fragment {


    View v;
    EditText ETMobileNumber;
    Button BtnContinue, BtnBack;
    ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth;

    private String code;
    private Boolean isRegistered;
    private final HashMap<String,String> map = new HashMap<String,String>();

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_user_sign_in_mobile_verification, container, false);
        ETMobileNumber = v.findViewById(R.id.userSignInMobile);
        BtnContinue = v.findViewById(R.id.mobileVerificationContinue);
        BtnBack = v.findViewById(R.id.backToMainActivity);
        progressBar = v.findViewById(R.id.progressBar_sending_otp);

        //Assigning FirebaseAuth to Firebase Instance.
        mAuth = FirebaseAuth.getInstance();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();


        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        });

        // Functions when OTP is send to phone number.
        PhoneAuthProvider.OnVerificationStateChangedCallbacks
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            /**
             onVerificationCompleted method will be called when the entered number SIM is in same Device.
             @param credential is send to second fragment to verify user.
             String code is also send to second fragment just to fill the editText automatically if verified.
             */
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            /**
             * onVerificationFailed method is called when verification is failed.
             * @param error displays error message if verification failed.
             */
            @Override
            public void onVerificationFailed(@NonNull FirebaseException error) {
                progressBar.setVisibility(View.GONE);
                BtnContinue.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }

            /**
             * onCodeSent method is called
             * @param verificationId is send to second fragment to make credential for verification
             */
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle = new Bundle();
                                bundle.putString("phone", ETMobileNumber.getText().toString().trim());
                                bundle.putString("verificationId", verificationId);
                                putDataInSharedPreferences();
                                UserSignInOtpVerification newFragment =  new UserSignInOtpVerification();

                                newFragment.setArguments(bundle);
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container_view, newFragment)
                                        .commit();

                            }
                        }, 10000
                );

            }
        };


        BtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ETMobileNumber.getText().toString().trim().isEmpty()){
                    if(ETMobileNumber.getText().toString().trim().length() == 10){
                        progressBar.setVisibility(View.VISIBLE);
                        BtnContinue.setVisibility(View.INVISIBLE);
                        db.collection("RegisteredUsers")
                                .whereEqualTo("phoneNo",ETMobileNumber.getText().toString().trim())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(!queryDocumentSnapshots.isEmpty()){
                                            isRegistered = true;
                                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                Map<String,Object> mapObject = new HashMap<String,Object>();
                                                mapObject = documentSnapshot.getData();
                                                for (Map.Entry<String, Object> entry : mapObject.entrySet()) {
                                                    if(entry.getValue() instanceof String){
                                                        map.put(entry.getKey(), (String) entry.getValue());
                                                    }
                                                }
                                            }
                                        }else{
                                            isRegistered = false;
                                        }

                                        if(isRegistered){
                                            // Requesting for an otp to entered number
                                            PhoneAuthOptions options =
                                                    PhoneAuthOptions.newBuilder(mAuth)
                                                            .setPhoneNumber("+91" + ETMobileNumber.getText().toString())       // Phone number to verify
                                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                            .setActivity(requireActivity())                 // Activity (for callback binding)
                                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                            .build();
                                            PhoneAuthProvider.verifyPhoneNumber(options);
                                        }
                                        else{
                                            progressBar.setVisibility(View.GONE);
                                            BtnContinue.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(), "Number not registered please sign up", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }else{
                        Toast.makeText(getActivity(), "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  v;

    }

    private void putDataInSharedPreferences() {
        mEditor.putString(getString(R.string.UserState),map.get("State"));
        mEditor.commit();

        mEditor.putString(getString(R.string.UserDistrict),map.get("District"));
        mEditor.commit();

        mEditor.putString(getString(R.string.UserPhoneNumber),map.get("phoneNo"));
        mEditor.commit();

        mEditor.putString(getString(R.string.UserId),map.get("Id"));
        mEditor.commit();
    }

    public  void  updateDetails(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}