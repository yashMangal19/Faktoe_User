package com.example.faktoe_user.Products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> list;
    DocumentReference UserRef;
    HashMap<String,Long> CartProducts;
    int count = 0;

    public ProductsAdapter(Context context, List<ProductModel> list, DocumentReference UserRef, HashMap<String,Long> CartProducts) {
        this.list = list;
        this.context = context;
        this.UserRef = UserRef;
        this.CartProducts = CartProducts;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_dashboard_home_product, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        final long[] productCount = {0};
        holder.productTitle.setText(list.get(position).getName());

        if(list.get(position).getMRP() == (list.get(position).getSellingPrice())){
            holder.productPrice.setText("Rs. " + list.get(position).getSellingPrice());
        }else{
            holder.productPrice.setText("Rs. " +list.get(position).getMRP());
            holder.productPrice.setTextColor(R.color.light_gray);
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.productPriceSelling.setText("Rs. " +list.get(position).getSellingPrice());
        }

        holder.productCategory.setText(list.get(position).getCategory());

        if(list.get(position).getBrand().equals("")){
            holder.productBrand.setVisibility(View.INVISIBLE);
        }else{
            holder.productBrand.setText(list.get(position).getBrand());
        }

        holder.productRating.setText(list.get(position).getRating());
        holder.productDescription.setText(list.get(position).getDescription());

        Picasso.get().load(list.get(position).getImageUrl()).into(holder.productImage);

        holder.productDescriptionEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.productDescriptionEditor.getText().toString().equals("Read more")){
                    holder.productDescription.setMaxLines(100);
                    holder.productTitle.setMaxLines(5);
                    holder.productDescriptionEditor.setText("Read less");
                }else{
                    holder.productDescription.setMaxLines(1);
                    holder.productTitle.setMaxLines(1);
                    holder.productDescriptionEditor.setText("Read more");
                }
            }
        });

        if(!CartProducts.isEmpty()){
            for (Map.Entry<String,Long> mapElement : CartProducts.entrySet()) {
                if((list.get(position).getShopId() + "-" + list.get(position).getId()).equals((String)mapElement.getKey())){
                    holder.productAddBtn.setVisibility(View.GONE);
                    holder.productAddLinearLayout.setVisibility(View.VISIBLE);
                    productCount[0] = (mapElement.getValue());
                    holder.numberOfProductTextView.setText(String.valueOf(productCount[0]));
                    UserRef.collection("Cart").
                            whereEqualTo("id",mapElement.getKey().substring(21))
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(!queryDocumentSnapshots.isEmpty()){
                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                            list.get(holder.getAbsoluteAdapterPosition()).setDocRef(UserRef.collection("Cart").document(documentSnapshot.getId()));
                                        }
                                    }
                                }
                            });
                }
            }
        }

        holder.productAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CartProducts.isEmpty()){
                    Map.Entry<String,Long> entry = CartProducts.entrySet().iterator().next();
                    String key= entry.getKey();
                    Log.d("0000000",list.get(holder.getAbsoluteAdapterPosition()).getShopId() + " " + key.substring(20));
                    if(!key.substring(20).equals(list.get(holder.getAbsoluteAdapterPosition()).getShopId())){
                        if(count == 0){
                            count += 1;
                            Toast.makeText(context, "Press Add Button again as cart contains products from another shop", Toast.LENGTH_SHORT).show();
                        }else if(count == 1){
                            UserRef.collection("Cart").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for( QueryDocumentSnapshot document : queryDocumentSnapshots ){
                                                UserRef.collection("Cart").document(document.getId()).delete();
                                            }
                                        }
                                    });
                            Toast.makeText(context, "Cart Cleared", Toast.LENGTH_SHORT).show();
                            CartProducts.clear();
                            holder.productAddBtn.setVisibility(View.GONE);
                            holder.productAddLinearLayout.setVisibility(View.VISIBLE);
                            holder.numberOfProductTextView.setText(String.valueOf(productCount[0] +1));
                            list.get(holder.getAbsoluteAdapterPosition()).setDocRef(UserRef.collection("Cart").document());
                            list.get(holder.getAbsoluteAdapterPosition()).getDocRef().set(list.get(holder.getAbsoluteAdapterPosition()));
                            list.get(holder.getAbsoluteAdapterPosition()).getDocRef().update("count",1);
                        }
                    }else{
                        holder.productAddBtn.setVisibility(View.GONE);
                        holder.productAddLinearLayout.setVisibility(View.VISIBLE);
                        holder.numberOfProductTextView.setText(String.valueOf(productCount[0] +1));
                        list.get(holder.getAbsoluteAdapterPosition()).setDocRef(UserRef.collection("Cart").document());
                        list.get(holder.getAbsoluteAdapterPosition()).getDocRef().set(list.get(holder.getAbsoluteAdapterPosition()));
                        list.get(holder.getAbsoluteAdapterPosition()).getDocRef().update("count",1);
                    }
                }else{
                    holder.productAddBtn.setVisibility(View.GONE);
                    holder.productAddLinearLayout.setVisibility(View.VISIBLE);
                    holder.numberOfProductTextView.setText(String.valueOf(productCount[0] +1));
                    list.get(holder.getAbsoluteAdapterPosition()).setDocRef(UserRef.collection("Cart").document());
                    list.get(holder.getAbsoluteAdapterPosition()).getDocRef().set(list.get(holder.getAbsoluteAdapterPosition()));
                    list.get(holder.getAbsoluteAdapterPosition()).getDocRef().update("count",1);
                }

            }
        });

        holder.productSubtractImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productCount[0] > 1 ){
                    productCount[0] -= 1;
                    holder.numberOfProductTextView.setText(String.valueOf(productCount[0]));
                    Map<String,Object> changeCount = new HashMap<>();
                    changeCount.put("count",productCount[0]);
                    list.get(holder.getAbsoluteAdapterPosition()).getDocRef().update(changeCount);
                }else{
                    holder.productAddBtn.setVisibility(View.VISIBLE);
                    holder.productAddLinearLayout.setVisibility(View.GONE);
                    productCount[0] = 0;
                    list.get(holder.getAbsoluteAdapterPosition()).getDocRef().delete();
                    list.get(holder.getAbsoluteAdapterPosition()).setDocRef(null);

                }
            }
        });

        holder.productAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productCount[0] += 1;
                holder.numberOfProductTextView.setText(String.valueOf(productCount[0]));
                Map<String,Object> changeCount = new HashMap<>();
                changeCount.put("count",productCount[0]);
                list.get(holder.getAbsoluteAdapterPosition()).getDocRef().update(changeCount);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView productTitle, productPrice, productPriceSelling, productCategory, productBrand, productRating, productDescription, productDescriptionEditor, numberOfProductTextView;
        ImageView productImage, productSubtractImage, productAddImage;
        Button productAddBtn;
        LinearLayout productAddLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productPriceSelling = itemView.findViewById(R.id.productPriceSelling);
            productCategory = itemView.findViewById(R.id.productCategory);
            productBrand = itemView.findViewById(R.id.productBrand);
            productRating = itemView.findViewById(R.id.productRating);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
            productDescriptionEditor = itemView.findViewById(R.id.productDescriptionEditor);
            productAddLinearLayout = itemView.findViewById(R.id.addProductLinearLayout);
            productAddBtn = itemView.findViewById(R.id.addProductButton);
            productAddImage = itemView.findViewById(R.id.addProductImage);
            productSubtractImage = itemView.findViewById(R.id.subtractProductImage);
            numberOfProductTextView = itemView.findViewById(R.id.numberOfProductTextView);
        }
    }
}
