package com.example.faktoe_user.OrderManagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.Cart.CartProductsAdapter;
import com.example.faktoe_user.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrderManagement extends Fragment {
    View v;

    RecyclerView OrdersRecyclerView;
    OrderAdapter orderAdapter;

    DocumentReference UserRef;

    private SharedPreferences mPreferences;
    private String UserState, UserCity, UserId, UserPhoneNumber;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<OrderModel> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_order_management, container, false);

        OrdersRecyclerView = v.findViewById(R.id.OrdersRecyclerView);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        OrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));

        UserRef =  db.collection("Users").document(UserState).collection(UserCity)
                .document(UserId);

        Log.d("000000",UserId +  UserCity + UserState);

        db.collection("Users").document(UserState).collection(UserCity)
                .document(UserId).collection("Orders")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        orderList = new ArrayList<>();
                        orderAdapter = new OrderAdapter(getContext(), orderList , UserRef);
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            db.collection("Shop").document(UserState).collection(UserCity)
                                    .document(document.getId().substring(36))
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            OrderModel orderModel = new OrderModel();
                                            orderModel.setShopName(documentSnapshot.getString("shopName"));
                                            Log.d("000000",documentSnapshot.getId());
                                            orderModel.setShopId(document.getId());
                                            orderList.add(orderModel);
                                            orderAdapter.notifyDataSetChanged();
                                        }
                                    });
                            OrdersRecyclerView.setAdapter(orderAdapter);
                        }
                    }
                });



        return v;
    }
}
