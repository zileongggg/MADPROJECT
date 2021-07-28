package com.example.easycart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easycart.Adapter.MyAdapter;
import com.example.easycart.Model.ItemModel;
import com.example.easycart.Utils.SQLiteHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private EditText editName, editPrice, editQuantity;
    private Button editSave, editCancel;
    private List<ItemModel> itemModelList = new ArrayList<>();
    private MyAdapter myAdapter;
    SQLiteHelper sqLiteHelper;
    private String name;
    private double price;
    private int quantity;
    private ImageView itemImage;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        editSave = findViewById(R.id.editSave);
        editCancel = findViewById(R.id.editCancel);

        getIntentData();

        editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelper = new SQLiteHelper(DetailsActivity.this);
                int id = getIntent().getIntExtra("id", 0);
                editName = findViewById(R.id.nameEdit);
                editPrice = findViewById(R.id.priceEdit);
                editQuantity = findViewById(R.id.qtyEdit);
                String newName = editName.getText().toString().trim();
                String newPrice = editPrice.getText().toString();
                String newQty = editQuantity.getText().toString().trim();

                sqLiteHelper.updateName(id, newName);
                sqLiteHelper.updatePrice(id, Double.parseDouble(newPrice));
                sqLiteHelper.updateQuantity(id, Integer.parseInt(newQty));
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
            }
        });
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // item Image
        itemImage = findViewById(R.id.itemImage);
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Image"), PICK_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && requestCode == RESULT_OK){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                itemImage.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void getIntentData() {
        if (getIntent().hasExtra("itemName") && getIntent().hasExtra("itemPrice") && getIntent().hasExtra("itemQuantity")) {
            name = getIntent().getStringExtra("itemName");
            price = getIntent().getDoubleExtra("itemPrice", 0);
            quantity = getIntent().getIntExtra("itemQuantity", 0);

            setIntentData(name, price, quantity);
        }
    }

    public void setIntentData(String name, double price, int quantity) {
        editName = findViewById(R.id.nameEdit);
        editPrice = findViewById(R.id.priceEdit);
        editQuantity = findViewById(R.id.qtyEdit);

        editName.setText(name);
        editPrice.setText(String.valueOf(price));
        editQuantity.setText(String.valueOf(quantity));

    }

}
