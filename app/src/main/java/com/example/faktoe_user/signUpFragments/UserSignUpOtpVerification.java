package com.example.faktoe_user.signUpFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;




public class    UserSignUpOtpVerification extends Fragment {
    private String verificationId;
    View view;
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    Button verifyOtp, reOtp;
    TextView phoneView;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_sign_up_otp_verification, container, false);

        //Assigning Id's
        otp1 = view.findViewById(R.id.inputOtp1);
        otp2 = view.findViewById(R.id.inputOtp2);
        otp3 = view.findViewById(R.id.inputOtp3);
        otp4 = view.findViewById(R.id.inputOtp4);
        otp5 = view.findViewById(R.id.inputOtp5);
        otp6 = view.findViewById(R.id.inputOtp6);
        phoneView = view.findViewById(R.id.phoneView);
        reOtp = view.findViewById(R.id.reOtp);
        verifyOtp = view.findViewById(R.id.verifyOtp);
        progressBar = view.findViewById(R.id.progressBarVerify);
        progressBar.setVisibility(View.INVISIBLE);


        String phoneNumberEntered = phoneView.getText().toString().trim();


        // CountDownTimer on Resend OTP button for 60 seconds
        new CountDownTimer(60000,1000){
            public void onTick(long millisUntilFinished){
                reOtp.setText("RESEND OTP in "+ millisUntilFinished / 1000);
            }
            public void onFinish(){
                reOtp.setText("RESEND OTP");


                //Clicking on Resend Otp Button
                reOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        // Resending OTP process starts
                        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                                Toast.makeText(getActivity(), "OTP is successfully sent", Toast.LENGTH_SHORT).show();


                            }
                        };


                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber("+91" + phoneNumberEntered)       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(requireActivity())                 // Activity (for callback binding)
                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);
                    }
                });



            }


        }.start();

        // Getting User Entered mobil number in text view STARTS
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String data = bundle.getString("phone");
        phoneView.setText(data); // ENDS


        // Getting Verification ID
        verificationId = bundle.getString("verificationId");
        Log.d("Verification Id:", verificationId);


        // Automatically movies to next EditText STARTS
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp1.getText().toString().length() == 1) {
                    otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp2.getText().toString().length() == 1) {
                    otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp3.getText().toString().length() == 1) {
                    otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp4.getText().toString().length() == 1) {
                    otp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp5.getText().toString().length() == 1) {
                    otp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        // ENDS


         // On pressing continue button
         verifyOtp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //Checking otp is in valid format or not
                 if  (otp1.getText().toString().trim().isEmpty()||
                         otp2.getText().toString().trim().isEmpty()||
                         otp3.getText().toString().trim().isEmpty()||
                         otp4.getText().toString().trim().isEmpty()||
                         otp5.getText().toString().trim().isEmpty()||
                         otp6.getText().toString().trim().isEmpty()) {
                     Toast.makeText(getActivity(), "OTP is not valid", Toast.LENGTH_SHORT).show();

                 } else {

                     //Changing visibility of progress bar, and continue button
                     progressBar.setVisibility(View.VISIBLE);
                     verifyOtp.setVisibility(View.INVISIBLE);

                     if (verificationId != null){

                         // Storing user entered otp in a String
                         String code =  otp1.getText().toString().trim()+
                                 otp2.getText().toString().trim()+
                                 otp3.getText().toString().trim()+
                                 otp4.getText().toString().trim()+
                                 otp5.getText().toString().trim()+
                                 otp6.getText().toString().trim();

                          PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                          Log.d("myapp", "credential failed");

                          FirebaseAuth.getInstance()
                                 .signInWithCredential(credential)
                                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                     @Override
                                     public void onComplete(@NonNull Task<AuthResult> task) {
                                         if (task.isSuccessful()){
                                             progressBar.setVisibility(View.VISIBLE);
                                             verifyOtp.setVisibility(View.INVISIBLE);
                                             //FirebaseUser user = mAuth.getCurrentUser();
                                             Toast.makeText(getActivity(), "Mobile No. verified", Toast.LENGTH_SHORT).show();

                                             Bundle bundle = new Bundle();
                                             bundle.putString("PhoneNo", phoneView.getText().toString().trim());
                                             UserSignUpDetails fragment = new UserSignUpDetails();
                                             fragment.setArguments(bundle);
                                             //assert getFragmentManager() != null;
                                             getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerUserSingUp, fragment).commit();
                                             Log.d("myapp", "Verify Complete");

                                         } else {
                                             progressBar.setVisibility(View.GONE);
                                             verifyOtp.setVisibility(View.VISIBLE);
                                             Toast.makeText(getActivity(), "OTP not valid", Toast.LENGTH_SHORT).show();
                                             Log.d("myapp", "Not Successful");

                                         }
                                     }
                                 });
                     }
                 }
             }
         });
        return view;
    }
}
