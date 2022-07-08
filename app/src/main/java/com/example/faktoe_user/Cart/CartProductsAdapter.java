package com.example.faktoe_user.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.ViewHolder> {

    private Context context;
    private List<ProductCartModel> list;
    private CollectionReference CartRef;

    public CartProductsAdapter(Context context, List<ProductCartModel> list, CollectionReference CartRef) {
        this.list = list;
        this.context = context;
        this.CartRef = CartRef;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public CartProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartProductsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_cart_product, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CartProductsAdapter.ViewHolder holder, int position) {

        holder.productTitle.setText(list.get(position).getName());

        if(list.get(position).getMRP() == (list.get(position).getSellingPrice())){
            holder.productPrice.setText("Rs. " + list.get(position).getSellingPrice());
        }else{
            holder.productPrice.setText("Rs. " + list.get(position).getMRP());
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

        holder.numberOfProductTextView.setText(String.valueOf(list.get(position).getCount()));

        holder.productAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartRef.whereEqualTo("name", list.get(holder.getAbsoluteAdapterPosition()).getName())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                                    Map<String,Object> changeCount = new HashMap<>();
                                    changeCount.put("count",list.get(holder.getAbsoluteAdapterPosition()).getCount() + 1);
                                    CartRef.document(document.getId()).update(changeCount);
                                }
                            }
                        });
            }
        });

        holder.productSubtractImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(holder.getAbsoluteAdapterPosition()).getCount() > 1){
                    CartRef.whereEqualTo("name", list.get(holder.getAbsoluteAdapterPosition()).getName())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                                        Map<String,Object> changeCount = new HashMap<>();
                                        changeCount.put("count",list.get(holder.getAbsoluteAdapterPosition()).getCount() -1);
                                        CartRef.document(document.getId()).update(changeCount);
                                    }
                                }
                            });
                }

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
        LinearLayout productAddLinearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitleCart);
            productPrice = itemView.findViewById(R.id.productPriceCart);
            productPriceSelling = itemView.findViewById(R.id.productPriceSellingCart);
            productCategory = itemView.findViewById(R.id.productCategoryCart);
            productBrand = itemView.findViewById(R.id.productBrandCart);
            productRating = itemView.findViewById(R.id.productRatingCart);
            productDescription = itemView.findViewById(R.id.productDescriptionCart);
            productImage = itemView.findViewById(R.id.productImageCart);
            productDescriptionEditor = itemView.findViewById(R.id.productDescriptionEditorCart);
            productAddLinearLayout = itemView.findViewById(R.id.addProductLinearLayoutCart);
            productAddImage = itemView.findViewById(R.id.addProductImageCart);
            productSubtractImage = itemView.findViewById(R.id.subtractProductImageCart);
            numberOfProductTextView = itemView.findViewById(R.id.numberOfProductTextViewCart);

        }
    }
}
