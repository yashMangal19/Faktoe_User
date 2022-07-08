package com.example.faktoe_user.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.faktoe_user.FragmentCommunication;
import com.example.faktoe_user.Products.FragmentShopProducts;
import com.example.faktoe_user.R;
import com.example.faktoe_user.models.PopularShopsModel;


import java.util.List;

public class PopularShopsAdapter extends RecyclerView.Adapter<PopularShopsAdapter.ViewHolder> {

    private Context context;
    private List<PopularShopsModel> list;
    private FragmentCommunication mCommunicator;

    public PopularShopsAdapter(Context context, List<PopularShopsModel> list, FragmentCommunication communicator) {
        this.list = list;
        this.context = context;
        this.mCommunicator= communicator;
    }


    @NonNull
    @Override
    public PopularShopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_shops_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularShopsAdapter.ViewHolder holder, int position) {

        context = holder.itemView.getContext();

        Glide.with(context).load(list.get(position).getSignatureImage()).into(holder.signatureImage);
        holder.shopName.setText(list.get(position).getShopName());
        holder.address.setText(list.get(position).getAddress());

        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommunicator.respond(holder.getAbsoluteAdapterPosition(),list.get(holder.getAbsoluteAdapterPosition()).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView signatureImage;
        TextView shopName, address;
        CardView productCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            signatureImage = itemView.findViewById(R.id.shopImg);
            shopName = itemView.findViewById(R.id.shopName);
            address = itemView.findViewById(R.id.shopAddr);
            productCard = itemView.findViewById(R.id.ProductCard);
        }
    }
}




