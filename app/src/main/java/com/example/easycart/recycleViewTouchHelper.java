package com.example.easycart;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycart.Adapter.MyAdapter;
import com.example.easycart.Model.ItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class recycleViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private MyAdapter myAdapter;
    ArrayList<ItemModel> boughtItemList = new ArrayList<>();

    public recycleViewTouchHelper(MyAdapter myAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.myAdapter = myAdapter;
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView,
                          @NonNull @NotNull RecyclerView.ViewHolder viewHolder,
                          @NonNull @NotNull RecyclerView.ViewHolder target){

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        Collections.swap(myAdapter.getItemList(), fromPosition, toPosition);
        recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());

        return false;
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();

        // swipe right to delete the item
        if (direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder = new AlertDialog.Builder(myAdapter.getContext());

            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure you want to delete this item ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myAdapter.deleteItem(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myAdapter.notifyItemChanged(position);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{

        }
    }

    //@Override
   /* public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(myAdapter.getContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(myAdapter.getContext() , R.color.primary))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }*/
}
