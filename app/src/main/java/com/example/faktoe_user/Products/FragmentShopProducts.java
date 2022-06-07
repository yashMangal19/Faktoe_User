package com.example.faktoe_user.Products;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FragmentShopProducts extends Fragment {
    View v;

    private String UserState, UserCity, UserId, UserPhoneNumber;

    private SharedPreferences mPreferences;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;
    private DocumentReference UserRef;

    private ProductAdapter adapter;

    private String ShopId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_dashboard_shop_products, container, false);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        ShopId  = bundle.getString("ShopId");

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        UserRef = db.collection("Users").document(UserState).collection(UserCity)
                .document(UserId);

        productRef = db.collection("Shop").document(UserState)
                .collection(UserCity).document(ShopId).collection("Products");

        setUpRecyclerView();

        return v;
    }

    private void setUpRecyclerView() {
        Query query = productRef.whereEqualTo("InStock", "true");
        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();

        adapter = new ProductAdapter(getActivity(),options, UserRef);

        RecyclerView recyclerView = v.findViewById(R.id.RecyclerView_Products);
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
