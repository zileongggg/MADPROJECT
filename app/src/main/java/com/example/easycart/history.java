package com.example.easycart;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycart.Adapter.historyAdapter;
import com.example.easycart.Model.ItemModel;
import com.example.easycart.Utils.SQLiteHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;
    private RecyclerView recyclerView;
    private ItemModel itemModel;
    private historyAdapter historyAdapter;
    private List<ItemModel> purchasedItemList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // initialize the recycler veiw
        sqLiteHelper = new SQLiteHelper(this);
        purchasedItemList = sqLiteHelper.getAllPurchasedItem();

        recyclerView = findViewById(R.id.historyRecycleView);          // pass the recycleView to MyAdapter class for some actions.
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historyAdapter = new historyAdapter(this, purchasedItemList, recyclerView);
        recyclerView.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();

        // Bottom action bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.history_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.delHistoryBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(history.this);

                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to clear your history?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteHelper.deleteAllHistory();
                        historyAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        historyAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();*/
                purchasedItemList = sqLiteHelper.getAllPurchasedItem();
                sqLiteHelper.deleteAllHistory();
                purchasedItemList.clear();
                historyAdapter.setPurchasedItemListItem(purchasedItemList);
                historyAdapter.notifyDataSetChanged();

            }
        });

    }
}