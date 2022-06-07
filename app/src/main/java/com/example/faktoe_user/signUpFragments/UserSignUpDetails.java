package com.example.faktoe_user.signUpFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.faktoe_user.R;
import com.example.faktoe_user.UserHomeFragment;
import com.example.faktoe_user.activities.UserDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
import com.google.firestore.v1.WriteResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserSignUpDetails extends Fragment {
    View view;
    private TextInputEditText name, addr, email, pin;
    private Button cont;
    TextView mainH2;
    private String pinCode;
    public String data;
    private  String userDistrict, userState;
    private RequestQueue mRequestQueue;
    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    String UserID;
    private DatabaseReference rootRef;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_sign_up_details, container, false);


        // Assigning Id's
        name = view.findViewById(R.id.userName);
        addr = view.findViewById(R.id.userAddr);
        email = view.findViewById(R.id.userEmail);
        cont = view.findViewById(R.id.submitDetails);
        pin = view.findViewById(R.id.userPincode);
        mainH2 = view.findViewById(R.id.mainHead2);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        // Initializing Firestore
        fstore = FirebaseFirestore.getInstance();



        // strong user pin code input in String
        pinCode = Objects.requireNonNull(pin.getText()).toString().trim();

        //Colouring part of heading with color
        String txt = "Create new Account";
        SpannableString ss = new SpannableString(txt);
        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(getResources().getColor(R.color.blue));
        ss.setSpan(fcsBlue, 0, 14, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mainH2.setText(ss);






        // taking user mobile no. from mobile verification fragment
        Bundle bundle = this.getArguments();
        assert bundle != null;
        data  = bundle.getString("PhoneNo");

        // Making new Reques Queue
        mRequestQueue = Volley.newRequestQueue(requireActivity());


        // On pressing Continue button
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // strong user pin code input in String
                pinCode = pin.getText().toString().trim();

                // if user detail fields not filled properly
                if (Objects.requireNonNull(name.getText()).toString().trim().length()==0 ||
                        Objects.requireNonNull(addr.getText()).toString().trim().length()==0 ||
                        Objects.requireNonNull(email.getText()).toString().trim().length()==0 ||
                        pin.getText().toString().trim().length()==0
                        ){
                    Toast.makeText(getActivity(), "All fields Required", Toast.LENGTH_SHORT).show();
                } else {

                    continueToHome(pinCode);

                }
            }


            // new mehtod on pressing continue button
            private void continueToHome(String pinCode) {


                // clearing our cache of request queue.
                mRequestQueue.getCache().clear();



                // below is the url from where we will be getting
                // our response in the json format.
                String url = "https://api.data.gov.in/resource/6176ee09-3d56-4a3b-8115-21841576b2f6?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=1&filters[pincode]="+pinCode;

                // below line is use to initialize our request queue.
                RequestQueue queue = Volley.newRequestQueue(requireActivity());
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // inside this method we will get two methods
                        // such as on response method
                        // inside on response method we are extracting
                        // data from the json format.
                        try {
                            // we are getting data of post office
                            // in the form of JSON file.
                            JSONArray postOfficeArray = response.getJSONArray("records");
                            if (response.getString("count").equals("0")) {
                                // validating if the response status is success or failure.
                                // in this method the response status is having error and


                                Log.d("myapp", "Status Error");

                                Toast.makeText(getActivity(), "Enter valid pin code", Toast.LENGTH_SHORT).show();
                            } else {
                                // if the status is success we are calling this method

                                // here we are calling first object of our json array.
                                JSONObject obj = postOfficeArray.getJSONObject(0);

                                // inside our json array we are getting district name,
                                // state and country from our data.
                                userDistrict = obj.getString("districtname");
                                userState= obj.getString("statename");


                                //Getting the root node of the realtime database
                                rootRef = FirebaseDatabase.getInstance().getReference();

                                //Creating collection in Cloud



                                //Making new Hash Map String, Object
                                Map<String,Object> map = new HashMap<>();

                                // Putting user details in realtime database
                                map.put("Name", Objects.requireNonNull(name.getText()).toString());
                                map.put("Address", addr.getText().toString());
                                map.put("EmailId", email.getText().toString());
                                map.put("MobileNo", "+91"+data);



                                DocumentReference ref = fstore.collection("Users").document(userState).collection(userDistrict).document();
                                String Id = ref.getId();

                                        ref.set(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("myapp", "On Success");

                                                Map<String,Object> values = new HashMap<>();
                                                values.put("phoneNo",data);
                                                values.put("Id",Id);
                                                values.put("State",userState);
                                                values.put("District",userDistrict);

                                                fstore.collection("RegisteredUsers")
                                                        .add(values).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
//                                                        Bundle bundleDetails = new Bundle();
//                                                        bundleDetails.putString("district", userDistrict);
//                                                        bundleDetails.putString("pin", pinCode);
//                                                        UserHomeFragment newActivity =  new UserHomeFragment();
//                                                        newActivity.setArguments(bundleDetails);

                                                        mEditor.putString(getString(R.string.UserState),userState);
                                                        mEditor.commit();

                                                        mEditor.putString(getString(R.string.UserDistrict),userDistrict);
                                                        mEditor.commit();

                                                        mEditor.putString(getString(R.string.UserPhoneNumber),data);
                                                        mEditor.commit();

                                                        mEditor.putString(getString(R.string.UserId),Id);
                                                        mEditor.commit();


                                                        Intent intent = new Intent(getActivity(), UserDashboard.class);
                                                        startActivity(intent);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Myapp", "On Failure");
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Myapp", "On Failure");
                                            }
                                        });


                            }
                        } catch (JSONException e) {
                            // if we gets any error then it
                            // will be printed in log cat.
                            e.printStackTrace();
                           /* userDistrict.setText("Pin code  not valid");
                            userState.setText("Pin code  not valid");*/


                            Toast.makeText(getActivity(), "Enter valid pin code", Toast.LENGTH_SHORT).show();
                            Log.d("myapp", "Catch");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // below method is called if we get
                        // any error while fetching data from API.
                        // below line is use to display an error message.
                        Toast.makeText(getActivity(), "Error Occured.", Toast.LENGTH_SHORT).show();
                       /* userDistrict.setText("Pin code  not valid");
                        userState.setText("Pin code  not valid");*/
                        Log.d("myapp", "Error");
                    }
                });
                // below line is use for adding object
                // request to our request queue.
                queue.add(objectRequest);


            }
        });
        return view;
    }


}