package com.example.faktoe_user.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.faktoe_user.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;


public class ProductCartAdapter extends FirestoreRecyclerAdapter<ProductCartModel, ProductCartAdapter.ProductCartHolder> {

    Context context;

    public ProductCartAdapter(Context context, @NonNull FirestoreRecyclerOptions<ProductCartModel> options) {
        super(options);
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull ProductCartHolder holder, int position, @NonNull ProductCartModel model) {
        holder.productTitle.setText(model.getName());

        if(model.getMRP().equals(model.getSellingPrice())){
            holder.productPrice.setText("Rs. " + model.getSellingPrice());
        }else{
            holder.productPrice.setText("Rs. " + model.getMRP());
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
    }

    @NonNull
    @Override
    public ProductCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_cart_product, parent, false);

        return new ProductCartHolder(v);
    }

    static class ProductCartHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, productPriceSelling, productCategory, productBrand, productRating, productDescription, productDescriptionEditor, numberOfProductTextView;
        ImageView productImage, productSubtractImage, productAddImage;
        LinearLayout productAddLinearLayout;

        public ProductCartHolder(@NonNull View itemView) {
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

