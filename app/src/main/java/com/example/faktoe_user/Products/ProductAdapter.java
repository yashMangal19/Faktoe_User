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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends FirestoreRecyclerAdapter<ProductModel, ProductAdapter.ProductHolder> {

    Context context;
    DocumentReference UserRef;

    public ProductAdapter(Context context, @NonNull FirestoreRecyclerOptions<ProductModel> options, DocumentReference UserRef) {
        super(options);
        this.context = context;
        this.UserRef = UserRef;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull ProductModel model) {
        final int[] productCount = {0};
        holder.productTitle.setText(model.getName());

        if(model.getMRP().equals(model.getSellingPrice())){
            holder.productPrice.setText("Rs. " + model.getSellingPrice());
        }else{
            holder.productPrice.setText("Rs. " +model.getMRP());
            holder.productPrice.setTextColor(R.color.light_gray);
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.productPriceSelling.setText("Rs. " +model.getSellingPrice());
        }

        holder.productCategory.setText(model.getCategory());

        if(model.getBrand().equals("")){
            holder.productBrand.setVisibility(View.INVISIBLE);
        }else{
            holder.productBrand.setText(model.getBrand());
        }

        holder.productRating.setText(model.getRating());
        holder.productDescription.setText(model.getDescription());

        Picasso.get().load(model.getImageUrl()).into(holder.productImage);

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

        holder.productAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.productAddBtn.setVisibility(View.GONE);
                holder.productAddLinearLayout.setVisibility(View.VISIBLE);
                holder.numberOfProductTextView.setText(String.valueOf(productCount[0] +1));
                model.setDocRef(UserRef.collection("Cart").document());
                model.getDocRef().set(model);
                model.getDocRef().update("count",1);
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
                    model.getDocRef().update(changeCount);
                }else{
                    holder.productAddBtn.setVisibility(View.VISIBLE);
                    holder.productAddLinearLayout.setVisibility(View.GONE);
                    productCount[0] = 0;
                    model.getDocRef().delete();
                    model.setDocRef(null);

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
                model.getDocRef().update(changeCount);
            }
        });
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_dashboard_home_product, parent, false);

        return new ProductHolder(v);
    }

    static class ProductHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, productPriceSelling, productCategory, productBrand, productRating, productDescription, productDescriptionEditor, numberOfProductTextView;
        ImageView productImage, productSubtractImage, productAddImage;
        Button productAddBtn;
        LinearLayout productAddLinearLayout;

        public ProductHolder(@NonNull View itemView) {
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
