package com.example.faktoe_user.OrderManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faktoe_user.Cart.CartProductsAdapter;
import com.example.faktoe_user.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private Context context;
    private List<OrderModel> list;
    private List<OrderDetailsModel> orderList;
    private DocumentReference UserRef;
    private int cost;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public OrderAdapter(Context context, List<OrderModel> list, DocumentReference UserRef) {
        this.context = context;
        this.list = list;
        this.UserRef = UserRef;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_management_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TVShopName.setText(list.get(position).getShopName());

        holder.RVOrderDetails.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL, false));

        UserRef.collection("Orders").document(list.get(position).getShopId())
                .collection("Products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        orderList = new ArrayList<>();
                        cost = 0;
                        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(context, orderList);
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            OrderDetailsModel orderDetailsModel = document.toObject(OrderDetailsModel.class);
                            cost += orderDetailsModel.getSellingPrice()*orderDetailsModel.getCount();
                            orderList.add(orderDetailsModel);
                        }
                        holder.TVTotalCost.setText("₹" + String.valueOf(cost));
                        holder.RVOrderDetails.setAdapter(orderDetailsAdapter);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView TVShopName, TVTotalCost;
        RecyclerView RVOrderDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TVShopName = itemView.findViewById(R.id.OrderShopName);
            RVOrderDetails = itemView.findViewById(R.id.OrderDetailsRecyclerView);
            TVTotalCost = itemView.findViewById(R.id.OrderTotalCost);
        }
    }
}
