package com.example.faktoe_user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.faktoe_user.GlideApp;
import com.example.faktoe_user.R;
import com.example.faktoe_user.models.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryModel> list;
    private final Context context;


    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.list = list;
        this.context = context;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Context context = holder.itemView.getContext();
        GlideApp.with(context).load(list.get(position).getImage_url()).into(holder.catimg);
        //Picasso.get().load(list.get(position).getImage_url().toString()).into(holder.catimg);
        holder.cattxt.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catimg;
        TextView cattxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.catImg);
            cattxt = itemView.findViewById(R.id.catTxt);
        }
    }
}
