package com.example.easycart.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycart.MainActivity;
import com.example.easycart.Model.ItemModel;
import com.example.easycart.R;
import com.example.easycart.Utils.SQLiteHelper;
import com.example.easycart.addNewItem;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    /**
     * onItemClickListener is used to make change on particular row view.
     * the listener will be the main activity
     */
    private OnItemClickListener mListener;
    private List<ItemModel> itemModelList;
    private MainActivity activity;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    Context context;

    // Interface for different onClick method on top of the view.
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onAddBtnClick(int position);

        void onMinusBtnClick(int position);
    }

    // get the listener from the main activity
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Constructor
    public MyAdapter(Context context, List<ItemModel> itemModelList, RecyclerView recyclerView, RelativeLayout relativeLayout) {
        this.context = context;
        this.itemModelList = itemModelList;
        this.recyclerView = recyclerView;
        this.relativeLayout = relativeLayout;
    }

    public void setItem(List<ItemModel> itemModelList) {
        this.itemModelList = itemModelList;
        notifyDataSetChanged();
    }

    public List<ItemModel> getItemList() {
        return itemModelList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        // view.setOnClickListener(onClickListener);
        // view and the listener pass to MyViewHolder class
        return new MyViewHolder(view, mListener);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, qty;
        Button addBtn, minusBtn;

        // Initialize element pass to onBindViewHolder
        public MyViewHolder(@NonNull @NotNull View itemView, OnItemClickListener listener) {
            super(itemView);
            // get the element of the card view from the recycle view.
            name = itemView.findViewById(R.id.name_field);
            price = itemView.findViewById(R.id.price_field);
            qty = itemView.findViewById(R.id.qty_field);
            addBtn = itemView.findViewById(R.id.addAmount);
            minusBtn = itemView.findViewById(R.id.minusAmount);

            // card onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        // get the position of the row
                        int position = getAdapterPosition();
                        // make sure the position is valid
                        if (position != RecyclerView.NO_POSITION) {
                            // pass the position to the interface OnItemClickListener then to Main Activity
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            // Increase and decrease amount button onClickListener in the main activity
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        // get the position of the row
                        int position = getAdapterPosition();
                        // make sure the position is valid
                        if (position != RecyclerView.NO_POSITION) {
                            // pass the position to the interface OnItemClickListener then to Main Activity
                            listener.onAddBtnClick(position);
                        }
                    }
                }
            });
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        // get the position of the row
                        int position = getAdapterPosition();
                        // make sure the position is valid
                        if (position != RecyclerView.NO_POSITION) {
                            // pass the position to the interface OnItemClickListener then to Main Activity
                            listener.onMinusBtnClick(position);
                        }
                    }
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position) {
        // set the content and display
        ItemModel item = itemModelList.get(position);
        if (item.getStatus() == 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            holder.name.setText(item.getName());
            holder.price.setText("RM " + df.format(item.getPrice()));
            holder.qty.setText(String.valueOf(item.getQuantity()));
        }
    }

    public Context getContext() {
        return context;
    }

    @SuppressLint("SetTextI18n")
    public void deleteItem(int position) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        ItemModel itemModel = itemModelList.get(position);
        sqLiteHelper.itemDelete(itemModel.getId());
        itemModelList.remove(position);
        updateTotalPrice();
        notifyDataSetChanged();
    }

    public void editItem(int position) {
        ItemModel itemModel = itemModelList.get(position);
        // pass data within activity and fragment
        Bundle bundle = new Bundle();
        bundle.putInt("id", itemModel.getId());
        bundle.putString("itemName", itemModelList.get(position).getName());
        bundle.putDouble("price", itemModel.getPrice());
        bundle.putInt("qty", itemModel.getQuantity());

        addNewItem item = new addNewItem();
        item.setArguments(bundle);
        item.show(activity.getSupportFragmentManager(), item.getTag());

    }

    @SuppressLint("SetTextI18n")
    public void updateTotalPrice() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        TextView totalPrice = relativeLayout.findViewById(R.id.totalPrice);
        // cast the price into 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        totalPrice.setText("RM " + Double.valueOf(df.format(sqLiteHelper.getTotalPrice())));
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public void storePurchasedItem(int position) {
        int status = 1;
        // first update the status in the database
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        int id = itemModelList.get(position).getId();
        sqLiteHelper.updateStatus(id,status);

        // remove the item from the list == remove from the recycle view
        itemModelList.remove(position);
        updateTotalPrice();
        notifyDataSetChanged();
    }

}
