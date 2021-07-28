//package com.example.easycart;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.example.easycart.Model.ItemModel;
//import com.example.easycart.Utils.SQLiteHelper;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//import org.jetbrains.annotations.NotNull;
//
//public class addNewItem extends BottomSheetDialogFragment {
//
//    public static final String TAG = "addNewItem";
//
//    // widgets
//    private EditText nameEdit, priceEdit, quantityEdit;
//    private Button btn;
//
//    private SQLiteHelper sqLiteHelper;
//
//    public static addNewItem newInstance(){
//        return  new addNewItem();
//    }
//
//    @Nullable
//    @org.jetbrains.annotations.Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.my_row, container, false);
//        return v;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        nameEdit = (EditText) view.findViewById(R.id.name_input);
//        priceEdit = (EditText) view.findViewById(R.id.price_input);
//        quantityEdit = (EditText) view.findViewById(R.id.qty_input);
//        btn = (Button) view.findViewById(R.id.saveBtn);
//
//        sqLiteHelper = new SQLiteHelper(getActivity());
//
//        boolean isUpdate = false;
//
//        Bundle bundle = getArguments();
//        if(bundle != null){
//            isUpdate = true;
//            String item = bundle.getString("item");
//            nameEdit.setText(item);
//
//            if(item.length() > 0){
//                btn.setEnabled(false);
//            }
//        }
///*        nameEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.toString().equals("")){
//                    btn.setEnabled(false);
//                    btn.setBackgroundColor(Color.GRAY);
//                }else{
//                    btn.setEnabled(true);
//                    btn.setBackgroundColor(getResources().getColor(R.color.primary));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });*/
//
//        final boolean finalIsUpdate = isUpdate;
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = nameEdit.getText().toString().trim();
//                String price = priceEdit.getText().toString().trim();
//                String quantity = quantityEdit.getText().toString().trim();
//
//                if (finalIsUpdate){
//                    sqLiteHelper.updateName(bundle.getInt("id"), name);
//                    sqLiteHelper.updatePrice(bundle.getInt("price"), price);
//                    sqLiteHelper.updateQuantity(bundle.getInt("quantity"), quantity);
//                }else{
//                    ItemModel item = new ItemModel();
//                    item.setName(name);
//                    item.setPrice(toString().indexOf(price));
//                    item.setQuantity(toString().indexOf(quantity));
//                    sqLiteHelper.insertItem(item);
//                }
//                dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
//        super.onDismiss(dialog);
//        Activity activity = getActivity();
//        if(activity instanceof onDialogCloseListener){
//            ((onDialogCloseListener)activity).onDialogClose(dialog);
//        }
//    }
//}
package com.example.easycart;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.easycart.Model.ItemModel;
import com.example.easycart.Utils.SQLiteHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class addNewItem extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewItem";
    private EditText nameEdit, priceEdit, quantityEdit; // used in onViewCreated();
    private Button saveBtn, cancelBtn;                  // used in onViewCreated();
    private View view;                                  // used in onCreateView();
    private SQLiteHelper sqLiteHelper;

    public static addNewItem newInstance() {
        return new addNewItem();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         *  inflater is used to instantiate the contents of layout XML files into their corresponding View objects
         *  Here, prompt_user_input XML file is used as a dialog
         */
        view = inflater.inflate(R.layout.prompt_user_input, container, false);
        return view;
    }

    // use findViewById in onViewCreated, make sure the view is fully created.
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        // get the button from the dialog
        saveBtn = view.findViewById(R.id.saveBtn);
        cancelBtn = view.findViewById(R.id.delBtn);

        // get the editText element from the dialog
        nameEdit = view.findViewById(R.id.name_input);
        priceEdit = view.findViewById(R.id.price_input);
        quantityEdit = view.findViewById(R.id.qty_input);

        // Initialize the sqLiteHelper
        sqLiteHelper = new SQLiteHelper(getActivity());

        // onClickListener for save button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the user's input from the editText element
                String newName = nameEdit.getText().toString().trim();
                String newPrice = priceEdit.getText().toString();
                String newQty = quantityEdit.getText().toString().trim();

                // check whether the user fill in all the info.
                if(!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newPrice) && !TextUtils.isEmpty(newQty)){
                    // store the item info into itemModel
                    ItemModel itemModel = new ItemModel();
                    itemModel.setName(newName);
                    itemModel.setPrice(Double.parseDouble(newPrice));
                    itemModel.setQuantity(Integer.parseInt(newQty));

                    // insert the item into database
                    sqLiteHelper.insertItem(itemModel);
                    Toast.makeText(getContext(), "New Item Added!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Info Not Complete", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof onDialogCloseListener) {
            ((onDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}