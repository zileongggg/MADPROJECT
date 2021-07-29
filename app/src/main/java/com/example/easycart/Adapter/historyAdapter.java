package com.example.easycart.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycart.Model.ItemModel;
import com.example.easycart.R;
import com.example.easycart.Utils.SQLiteHelper;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.historyViewHolder> {

    List<ItemModel> purchasedItemList;
    RecyclerView recyclerView;
    Context context;

    public historyAdapter(Context context, List<ItemModel> purchasedItemList, RecyclerView recyclerView) {
        this.purchasedItemList = purchasedItemList;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    public void setPurchasedItemListItem(List<ItemModel> purchasedItemList) {
        this.purchasedItemList = purchasedItemList;
        notifyDataSetChanged();
    }

    public List<ItemModel> getPurchasedItemList() {
        return purchasedItemList;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @NotNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        // view.setOnClickListener(onClickListener);
        // view and the listener pass to MyViewHolder class
        return new historyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull historyAdapter.historyViewHolder holder, int position) {
        // display the data
        ItemModel item = purchasedItemList.get(position);
        if (item.getStatus() == 1) {
            DecimalFormat df = new DecimalFormat("#.##");
            holder.name.setText(item.getName());
            holder.price.setText("RM " + df.format(item.getPrice()));
            holder.qty.setText("Qty: " + item.getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return purchasedItemList.size();
    }

    // data pass to onBindViewHolder
    public static class historyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView name, price, qty;
        CardView historyItemCard;

        public historyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameHis);
            price =  itemView.findViewById(R.id.priceHis);
            qty = itemView.findViewById(R.id.qtyHis);

            // Floating Context Menu
            historyItemCard = itemView.findViewById(R.id.historyItemCard);
            historyItemCard.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 1,0, "Delete");
            menu.add(this.getAdapterPosition(), 2,1, "Restore");
        }
    }

    public void restorePurchasedItem(int position) {
        int status = 0;
        // first update the status in the database
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        purchasedItemList = getPurchasedItemList();
        int id = purchasedItemList.get(position).getId();
        sqLiteHelper.updateStatus(id,status);

        // remove the item from the list == remove from the recycle view
        purchasedItemList.remove(position);
        notifyDataSetChanged();
    }

    public void deletePurchasedItem(int position) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        ItemModel itemModel = purchasedItemList.get(position);
        sqLiteHelper.itemDelete(itemModel.getId());
        purchasedItemList.remove(position);
        notifyDataSetChanged();
    }
}
