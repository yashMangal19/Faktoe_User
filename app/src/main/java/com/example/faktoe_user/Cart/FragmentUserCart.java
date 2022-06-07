package com.example.faktoe_user.Cart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faktoe_user.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentUserCart extends Fragment {
    View v;

    private String UserState, UserCity, UserId, UserPhoneNumber;

    private SharedPreferences mPreferences;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference CartRef;
    private ProductCartAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_cart, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        CartRef = db.collection("Users").document(UserState)
                .collection(UserCity).document(UserId).collection("Cart");

        setUpRecyclerView();


        return v;
    }

    private void setUpRecyclerView() {
        Query query = CartRef.orderBy("name");
        FirestoreRecyclerOptions<ProductCartModel> options = new FirestoreRecyclerOptions.Builder<ProductCartModel>()
                .setQuery(query, ProductCartModel.class)
                .build();

        adapter = new ProductCartAdapter(getActivity(),options);

        RecyclerView recyclerView = v.findViewById(R.id.RecyclerView_Cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
