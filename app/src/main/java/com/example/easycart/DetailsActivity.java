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

    private EditText editName, editPrice, editQuantity, editNote;
    private Button editSave, editCancel;
    private List<ItemModel> itemModelList = new ArrayList<>();
    private MyAdapter myAdapter;
    private String name, note, image;
    private double price;
    private int quantity;
    private static final int PICK_IMAGE = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_PICK_CODE = 1001;
    private Uri imageUri;
    private ImageView itemImage;
    SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        editSave = findViewById(R.id.editSave);
        editCancel = findViewById(R.id.editCancel);

        // get all intent data
        getIntentData();

        editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelper = new SQLiteHelper(DetailsActivity.this);
                int id = getIntent().getIntExtra("id", 0);

                // get into the correct section
                editName = (EditText) findViewById(R.id.nameEdit);
                editPrice = (EditText) findViewById(R.id.priceEdit);
                editQuantity = (EditText) findViewById(R.id.qtyEdit);
                editNote = (EditText) findViewById(R.id.noteEdit);

                // get new input from the user input
                String newName = editName.getText().toString().trim();
                String newPrice = editPrice.getText().toString();
                String newQty = editQuantity.getText().toString().trim();
                String newNote = editNote.getText().toString().trim();

                sqLiteHelper.updateName(id, newName);
                sqLiteHelper.updatePrice(id, Double.parseDouble(newPrice));
                sqLiteHelper.updateQuantity(id, Integer.parseInt(newQty));
                sqLiteHelper.updateNote(id, newNote);
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
            }
        });
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // select item image
        itemImage = (ImageView) findViewById(R.id.itemImage);
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                         == PackageManager.PERMISSION_DENIED){
                        // request permission
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        // show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_PICK_CODE);

                    }else {
                        // permission granted
                        pickImageFromGallery();
                    }
                }else{
                    // system OS is less then marshamallow
                    pickImageFromGallery();
                }*/

                pickImageFromGallery();
            }
        });

    }

    public void pickImageFromGallery(){
        // intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_PICK_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted
                    pickImageFromGallery();
                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sqLiteHelper = new SQLiteHelper(DetailsActivity.this);
        int id = getIntent().getIntExtra("id", 0);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                sqLiteHelper.updateImage(id, imageUri.toString());
                itemImage.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void getIntentData() {
        if (getIntent().hasExtra("itemName") && getIntent().hasExtra("itemPrice") && getIntent().hasExtra("itemQuantity")) {
            name = getIntent().getStringExtra("itemName");
            price = getIntent().getDoubleExtra("itemPrice", 0);
            quantity = getIntent().getIntExtra("itemQuantity", 0);
            note = getIntent().getStringExtra("itemNote");
            image = getIntent().getStringExtra("itemImage");

            setIntentData(name, price, quantity, note, image);
        }
    }

    public void setIntentData(String name, double price, int quantity, String note, String image) {
        editName = findViewById(R.id.nameEdit);
        editPrice = findViewById(R.id.priceEdit);
        editQuantity = findViewById(R.id.qtyEdit);
        editNote = findViewById(R.id.noteEdit);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        editName.setText(name);
        editPrice.setText(String.valueOf(price));
        editQuantity.setText(String.valueOf(quantity));
        editNote.setText(note);

        // set image
        Bitmap bitmap = null;
        try {
            if (image != null){
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image));
                itemImage.setImageBitmap(bitmap);
            }else {
                itemImage.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
