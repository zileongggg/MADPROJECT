package com.example.easycart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycart.Adapter.MyAdapter;
import com.example.easycart.Model.ItemModel;
import com.example.easycart.Utils.SQLiteHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onDialogCloseListener {

    private SQLiteHelper sqLiteHelper;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private MyAdapter myAdapter;
    private List<ItemModel> itemModelList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start of build of recycle view
        sqLiteHelper = new SQLiteHelper(this);
        itemModelList = sqLiteHelper.getAllItem();

        relativeLayout = findViewById(R.id.totalPriceSection);  // pass the layout to MyAdapter class for updating the total price.
        recyclerView = findViewById(R.id.recycleView);          // pass the recycleView to MyAdapter class for some actions.
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(this, itemModelList, recyclerView, relativeLayout);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        // update total price in main activity
        myAdapter.updateTotalPrice();

        // Floating action button
        fab = findViewById(R.id.addNewItemBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the dialog from the other class
                addNewItem.newInstance().show(getSupportFragmentManager(), addNewItem.TAG);
            }
        });

        // pass the recycleView to the ItemTouchHelper class to perform action
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new recycleViewTouchHelper(myAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // handle a specific onClickListener on top of the card view
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id = itemModelList.get(position).getId();

                // pass required info to the details activity
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("itemName", itemModelList.get(position).getName());
                intent.putExtra("itemPrice", itemModelList.get(position).getPrice());
                intent.putExtra("itemQuantity", itemModelList.get(position).getQuantity());
                intent.putExtra("itemNote", itemModelList.get(position).getNote());
                intent.putExtra("position", position);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onAddBtnClick(int position) {
                int id = itemModelList.get(position).getId();
                int qty = itemModelList.get(position).getQuantity();
                ++qty;
                // update the data in the itemModelList and SQLite
                itemModelList.get(position).setQuantity(qty);
                sqLiteHelper.updateQuantity(id, qty);
                myAdapter.updateTotalPrice();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMinusBtnClick(int position) {
                int id = itemModelList.get(position).getId();
                int qty = itemModelList.get(position).getQuantity();
                if (qty > 1) { qty--; } else { qty = 1; }
                // update the data in the itemModelList and SQLite
                itemModelList.get(position).setQuantity(qty);
                sqLiteHelper.updateQuantity(id, qty);
                myAdapter.updateTotalPrice();
                myAdapter.notifyDataSetChanged();
            }
        });

        // Bottom action bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.history) {
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

    }

    // perform some task when the dialog is closed
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        itemModelList = sqLiteHelper.getAllItem();
        // Collections.reverse(itemModelList);
        myAdapter.updateTotalPrice();
        myAdapter.setItem(itemModelList);
        myAdapter.notifyDataSetChanged(); // recall the view with the new added item.
    }

}