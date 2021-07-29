package com.example.easycart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycart.Adapter.MyAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class recycleViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private MyAdapter myAdapter;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(myAdapter.getContext());

            builder.setTitle("Store into History");
            builder.setMessage("Are you sure you want to store the item into history?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myAdapter.storePurchasedItem(position);
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
        }
    }

    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        Drawable background;

        if (dX > 0) {
            p.setColor(myAdapter.getContext().getResources().getColor(R.color.red));
            // Draw Rect with varying right side, equal to displacement dX
            c.drawRect((float) itemView.getLeft() + dY, (float) itemView.getTop(),  (float) itemView.getRight(),
                    (float) itemView.getBottom(), p);
            background = ContextCompat.getDrawable(myAdapter.getContext(), R.drawable.ic_baseline_delete_24);
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
            background.draw(c);
        } else if (dX < 0) {
            p.setColor(myAdapter.getContext().getResources().getColor(R.color.secondary));
            c.drawRect((float) itemView.getLeft() + dY, (float) itemView.getTop(),  (float) itemView.getRight(),
                    (float) itemView.getBottom(), p);
            background = ContextCompat.getDrawable(myAdapter.getContext(), R.drawable.ic_baseline_save_alt_24);
            background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }
}
