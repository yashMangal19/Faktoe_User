package com.example.faktoe_user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.faktoe_user.Products.FragmentShopProducts;
import com.example.faktoe_user.activities.UserDashboard;
import com.example.faktoe_user.adapters.CategoryAdapter;


import com.example.faktoe_user.adapters.PopularShopsAdapter;
import com.example.faktoe_user.models.CategoryModel;


import com.example.faktoe_user.models.PopularShopsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment# factory method to
 * create an instance of this fragment.
 */
public class    UserHomeFragment extends Fragment {

    RecyclerView catRecyclerView, shopRecView;

    // Category Recycler view
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    // Popular Shop Recycler View
    PopularShopsAdapter popularShopsAdapter;
    List<PopularShopsModel> popularShopsModelList;
    // Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String UserState, UserCity, UserId, UserPhoneNumber;
    private SharedPreferences mPreferences;

    private HashMap<String,String> CartMap = new HashMap<String,String>();
    private ArrayList<String> CartProducts = new ArrayList<String>();

    View view;

    public UserHomeFragment(){
        // Nothing here
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_home, container, false);

        catRecyclerView = view.findViewById(R.id.catRecView);
        shopRecView = view.findViewById(R.id.ShopRecView);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        // Category
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);

        Log.d("myapp", "Rec. View Applied");

        // Popular Shops
        shopRecView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        //shopRecView.setAdapter(popularShopsAdapter);


        //@SuppressLint("NotifyDataSetChanged")
        db.collection("Category_Images")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {


                            CategoryModel categoryModel = document.toObject(CategoryModel.class);
                            categoryModelList.add(categoryModel);
                            categoryAdapter.notifyDataSetChanged();
                            Log.d("myapp","Data  Recieved");

                        }
                        catRecyclerView.setAdapter(categoryAdapter);
                    }
                });

        db.collection("Shop").document(UserState).collection(UserCity)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        popularShopsModelList = new ArrayList<>();
                        popularShopsAdapter = new PopularShopsAdapter(getContext(), popularShopsModelList, communication);
                        for (QueryDocumentSnapshot document : value) {
                            PopularShopsModel popularShopsModel = document.toObject(PopularShopsModel.class);
                            popularShopsModelList.add(popularShopsModel);
                            popularShopsAdapter.notifyDataSetChanged();
                        }
                        shopRecView.setAdapter(popularShopsAdapter);
                    }
                });

        return view;
    }

    FragmentCommunication communication=new FragmentCommunication() {
        @Override
        public void respond(int position,String ShopId) {
            FragmentShopProducts fragmentShopProducts = new FragmentShopProducts();
            Bundle bundle=new Bundle();
            bundle.putString("ShopId",ShopId);
            fragmentShopProducts.setArguments(bundle);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.userDashboardFragContainer,fragmentShopProducts);
            fragmentTransaction.commit();
        }
    };
}