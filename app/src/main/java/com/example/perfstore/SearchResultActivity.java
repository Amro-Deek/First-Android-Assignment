package com.example.perfstore;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private LinearLayout resultCards;
    private Button btnViewCart;
    private ArrayList<Perfume> filteredList;
    private SharedPreferences inventoryPrefs;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        resultCards = findViewById(R.id.resultCards);
        btnViewCart = findViewById(R.id.btnViewCart);
        inventoryPrefs = getSharedPreferences("INVENTORY", MODE_PRIVATE);


        String savedList = inventoryPrefs.getString("perfume_list_2ndActivity", "");
        Type listType = new TypeToken<ArrayList<Perfume>>() {}.getType();

        if (!savedList.isEmpty()) {
            filteredList = gson.fromJson(savedList, listType);
        } else {
            filteredList = (ArrayList<Perfume>) getIntent().getSerializableExtra("filteredPerfumes");//get data from 1st intent
            savePerfumeList();
        }

        if (filteredList != null && !filteredList.isEmpty()) {
            for (Perfume p : filteredList) {
                addPerfumeCard(p);
            }
        } else {
            Toast.makeText(this, "No perfumes match your search.", Toast.LENGTH_SHORT).show();
        }

        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void addPerfumeCard(Perfume p) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_perfume, resultCards, false);

        TextView txtName = cardView.findViewById(R.id.txtName);
        TextView txtBrand = cardView.findViewById(R.id.txtBrand);
        TextView txtPrice = cardView.findViewById(R.id.txtPrice);
        TextView txtGender = cardView.findViewById(R.id.txtGender);
        TextView txtQuantity = cardView.findViewById(R.id.txtQuantity);
        TextView txtOffer = cardView.findViewById(R.id.txtOffer);
        ImageView imgPerfume = cardView.findViewById(R.id.imgPerfume);
        Button btnAddToCart = cardView.findViewById(R.id.btnAddToCart);

        txtName.setText(p.name);
        txtBrand.setText("Brand: " + p.brand);
        txtPrice.setText("Price: $" + p.price);
        txtGender.setText("Gender: " + p.gender);
        txtQuantity.setText("Quantity: " + p.quantity);
        if (p.onOffer){
            txtOffer.setText("On Offer");
        }
        else {
            txtOffer.setText("Regular Price");
        }
        imgPerfume.setImageResource(p.imageResId);

        btnAddToCart.setOnClickListener(v -> {
            if (p.quantity > 0) {
                CartManager.addToCart(this, p);
                p.quantity--;
                txtQuantity.setText("Quantity: " + p.quantity);
                savePerfumeList(); // save changes
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Out of stock!", Toast.LENGTH_SHORT).show();
            }
        });
        resultCards.addView(cardView);
    }

    private void savePerfumeList() {
        String json = gson.toJson(filteredList);
        SharedPreferences.Editor editor = inventoryPrefs.edit();
        editor.putString("perfume_list_2ndActivity", json);
        editor.commit();
    }
}