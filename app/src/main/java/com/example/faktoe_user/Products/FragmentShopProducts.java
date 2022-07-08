package com.example.faktoe_user.Products;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.Cart.ProductCartModel;
import com.example.faktoe_user.R;
import com.example.faktoe_user.UserHomeFragment;
import com.example.faktoe_user.adapters.PopularShopsAdapter;
import com.example.faktoe_user.models.PopularShopsModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentShopProducts extends Fragment {
    View v;

    private String UserState, UserCity, UserId, UserPhoneNumber;

    private HashMap<String,String> CartMap = new HashMap<String,String>();
    private HashMap<String,Long> CartProducts = new HashMap<String,Long>();

    private SharedPreferences mPreferences;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;
    private DocumentReference UserRef;

    RecyclerView productsRecyclerView;
    ProductsAdapter productsAdapter;
    List<ProductModel> productsList;

    LinearLayout BtnBackToShopList;

    private String ShopId;

    private ListenerRegistration listenerRegistration;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_dashboard_shop_products, container, false);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        ShopId  = bundle.getString("ShopId");

        productsRecyclerView = v.findViewById(R.id.RecyclerView_Products);
        BtnBackToShopList = v.findViewById(R.id.BtnBackToShopList);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        UserRef = db.collection("Users").document(UserState).collection(UserCity)
                .document(UserId);

        productRef = db.collection("Shop").document(UserState)
                .collection(UserCity).document(ShopId).collection("Products");

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));

        db.collection("Users").document(UserState).collection(UserCity)
                .document(UserId).collection("Cart")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                ProductCartModel productCartModel = documentSnapshot.toObject(ProductCartModel.class);
                                CartProducts.put(ShopId + "-" + productCartModel.getId(),productCartModel.getCount());
                            }

                        }
                    }
                });

                        db.collection("Shop").document(UserState)
                                .collection(UserCity).document(ShopId).collection("Products")
                                .whereEqualTo("InStock","true")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        productsList = new ArrayList<>();
                                        productsAdapter = new ProductsAdapter(getContext(), productsList, UserRef, CartProducts);
                                        for (QueryDocumentSnapshot document : value) {
                                            ProductModel productModel = document.toObject(ProductModel.class);
                                            productsList.add(productModel);
                                            productsAdapter.notifyDataSetChanged();
                                        }
                                        productsRecyclerView.setAdapter(productsAdapter);

                                    }
                                });

        BtnBackToShopList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.userDashboardFragContainer,new UserHomeFragment());
                fragmentTransaction.commit();
            }
        });


        return v;
    }
}

