package com.example.perfstore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LinearLayout cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("CART", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String cartData = sharedPreferences.getString("cart_items", "");

        cart = findViewById(R.id.cart);
        if (!cartData.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Perfume>>() {}.getType();
            List<Perfume> cartList = gson.fromJson(cartData, listType);

            for (Perfume perfume : cartList) {
                View card = getLayoutInflater().inflate(R.layout.item_perfume, null);
                Button btnAdd = card.findViewById(R.id.btnAddToCart);
                if (btnAdd != null) btnAdd.setVisibility(View.GONE);

                ((TextView) card.findViewById(R.id.txtName)).setText(perfume.name);
                ((TextView) card.findViewById(R.id.txtBrand)).setText("Brand: " + perfume.brand);
                ((TextView) card.findViewById(R.id.txtPrice)).setText("Price: $" + perfume.price);
                ((TextView) card.findViewById(R.id.txtGender)).setText("Gender: " + perfume.gender);
                ((TextView) card.findViewById(R.id.txtQuantity)).setText("Qty: " + perfume.quantity);

                if (perfume.onOffer) {
                    card.findViewById(R.id.txtOffer).setVisibility(View.VISIBLE);
                }

                ((ImageView) card.findViewById(R.id.imgPerfume)).setImageResource(perfume.imageResId);

                cart.addView(card);
            }
        }
    }
}