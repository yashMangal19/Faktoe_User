package com.example.faktoe_user.Cart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.OrderManagement.OrderDetailsModel;
import com.example.faktoe_user.OrderManagement.OrderShopModel;
import com.example.faktoe_user.Products.ProductModel;
import com.example.faktoe_user.Products.ProductsAdapter;
import com.example.faktoe_user.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FragmentUserCart extends Fragment {
    View v;

    private String UserState, UserCity, UserId, UserPhoneNumber;

    private SharedPreferences mPreferences;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference CartRef;

    RecyclerView cartProductsRecyclerView;
    CartProductsAdapter cartProductsAdapter;
    List<ProductCartModel> cartProductsList;

    TextView TotalCost;
    Button BtnPreOrder, BtnClearCart;


    long CartCost = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_cart, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        cartProductsRecyclerView = v.findViewById(R.id.RecyclerView_Cart);
        TotalCost = v.findViewById(R.id.TextTotalPrice);
        BtnPreOrder = v.findViewById(R.id.BtnPreOrder);
        BtnClearCart = v.findViewById(R.id.ClearCartBtn);

        cartProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));

        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        CartRef = db.collection("Users").document(UserState)
                .collection(UserCity).document(UserId).collection("Cart");

        BtnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for( QueryDocumentSnapshot document : queryDocumentSnapshots ){
                                    CartRef.document(document.getId()).delete();
                                }
                            }
                        });
            }
        });

        CartRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                CartCost = 0;
                cartProductsList = new ArrayList<>();
                cartProductsAdapter = new CartProductsAdapter(getContext(), cartProductsList, CartRef);
                for (QueryDocumentSnapshot document : value) {
                    ProductCartModel productCartModel = document.toObject(ProductCartModel.class);
                    CartCost += productCartModel.getCount()*productCartModel.getSellingPrice();
                    cartProductsList.add(productCartModel);
                    cartProductsAdapter.notifyDataSetChanged();

                }
                TotalCost.setText(String.valueOf(CartCost));
                cartProductsRecyclerView.setAdapter(cartProductsAdapter);
            }
        });

        BtnPreOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CartRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String UniqueId = UUID.randomUUID().toString();
                                for( QueryDocumentSnapshot document : queryDocumentSnapshots ){

                                    OrderDetailsModel orderDetailsModel = document.toObject(OrderDetailsModel.class);
                                    OrderShopModel orderShopModel = document.toObject(OrderShopModel.class);
                                    orderShopModel.setUserId(UserId);

                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("Rated","false");
                                    map.put("Packed","false");

                                    DocumentReference UserOrder = db.collection("Users").document(UserState)
                                            .collection(UserCity).document(UserId).collection("Orders")
                                            .document(UniqueId + Objects.requireNonNull(document.get("shopId")));

                                    UserOrder.set(map);

                                    UserOrder.collection("Products").add(orderDetailsModel);

                                    DocumentReference ShopOrder = db.collection("Shop").document(UserState)
                                            .collection(UserCity).document(Objects.requireNonNull(document.get("shopId")).toString()).collection("Orders")
                                            .document(UniqueId + UserId);

                                    ShopOrder.set(map);

                                    ShopOrder.collection("Products").add(orderShopModel);

                                    CartRef.document(document.getId()).delete();
                                }
                            }
                        });
            }
        });
        return v;
    }

}
