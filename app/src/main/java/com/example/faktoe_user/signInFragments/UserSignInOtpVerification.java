package com.example.faktoe_user.signInFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faktoe_user.R;
import com.example.faktoe_user.UserHomeFragment;
import com.example.faktoe_user.activities.UserDashboard;
import com.example.faktoe_user.signUpFragments.UserSignUpDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSignInOtpVerification
 * create an instance of this fragment.
 */
public class UserSignInOtpVerification extends Fragment {
    View v;
    EditText inputOtp1, inputOtp2, inputOtp3, inputOtp4, inputOtp5, inputOtp6;
    TextView Description, wrongNumber;
    Button verifyBtn;
    ProgressBar progressBar;

    private String verificationId;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_user_sign_in_otp_verification, container, false);

        inputOtp1 = v.findViewById(R.id.inputOtp1_SignIn);
        inputOtp2 = v.findViewById(R.id.inputOtp2_SignIn);
        inputOtp3 = v.findViewById(R.id.inputOtp3_SignIn);
        inputOtp4 = v.findViewById(R.id.inputOtp4_SignIn);
        inputOtp5 = v.findViewById(R.id.inputOtp5_SignIn);
        inputOtp6 = v.findViewById(R.id.inputOtp6_SignIn);

        Description = v.findViewById(R.id.Description_SignIn_Otp);
        verifyBtn = v.findViewById(R.id.verify_otp_SignIn);
        progressBar = v.findViewById(R.id.progressBar_verify_otp_SignIn);

        wrongNumber = v.findViewById(R.id.TextView_wrongNumber_SignIn);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        // Getting User Entered mobil number in text view STARTS
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String data = bundle.getString("phone");
        Description.setText("A 6 digit code has been sent to +91 " + data); // ENDS


        //Getting Verification ID
        verificationId = bundle.getString("verificationId");
        Log.d("Verification Id:", verificationId);

        // On pressing continue button
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checking otp is in valid format or not
                if  (inputOtp1.getText().toString().trim().isEmpty()||
                        inputOtp2.getText().toString().trim().isEmpty()||
                        inputOtp3.getText().toString().trim().isEmpty()||
                        inputOtp4.getText().toString().trim().isEmpty()||
                        inputOtp5.getText().toString().trim().isEmpty()||
                        inputOtp6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "OTP is not valid", Toast.LENGTH_SHORT).show();

                } else {

                    //Changing visibility of progress bar, and continue button
                    progressBar.setVisibility(View.VISIBLE);
                    verifyBtn.setVisibility(View.INVISIBLE);

                    if (verificationId != null){

                        // Storing user entered otp in a String
                        String code =  inputOtp1.getText().toString().trim()+
                                inputOtp2.getText().toString().trim()+
                                inputOtp3.getText().toString().trim()+
                                inputOtp4.getText().toString().trim()+
                                inputOtp5.getText().toString().trim()+
                                inputOtp6.getText().toString().trim();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

                        FirebaseAuth.getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            progressBar.setVisibility(View.VISIBLE);
                                            verifyBtn.setVisibility(View.INVISIBLE);
                                            //FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(getActivity(), "Mobile No. verified", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getActivity(), UserDashboard.class);
                                            startActivity(intent);

                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            verifyBtn.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(), "OTP not valid", Toast.LENGTH_SHORT).show();
                                            Log.d("myapp", "Not Successful");

                                        }
                                    }
                                });
                    }
                }
            }
        });

        wrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.putString(getString(R.string.UserState),"");
                mEditor.commit();

                mEditor.putString(getString(R.string.UserDistrict),"");
                mEditor.commit();

                mEditor.putString(getString(R.string.UserPhoneNumber),"");
                mEditor.commit();

                mEditor.putString(getString(R.string.UserId),"");
                mEditor.commit();

                UserSignInMobileVerification newFragment =  new UserSignInMobileVerification();

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, newFragment)
                        .commit();
            }
        });


        // Automatically movies to next EditText STARTS
        inputOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputOtp1.getText().toString().length() == 1) {
                    inputOtp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputOtp2.getText().toString().length() == 1) {
                    inputOtp3.requestFocus();
                }else{
                    inputOtp1.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputOtp3.getText().toString().length() == 1) {
                    inputOtp4.requestFocus();
                }else{
                    inputOtp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputOtp4.getText().toString().length() == 1) {
                    inputOtp5.requestFocus();
                }else{
                    inputOtp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputOtp5.getText().toString().length() == 1) {
                    inputOtp6.requestFocus();
                }else{
                    inputOtp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputOtp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputOtp5.getText().toString().isEmpty()) {
                    inputOtp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        return v;
    }
}