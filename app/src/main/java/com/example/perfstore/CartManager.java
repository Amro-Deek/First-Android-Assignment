package com.example.perfstore;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String CART_NAME = "CART";
    private static final String CART_KEY = "cart_items";
    public static void addToCart(Context context, Perfume perfume) {
        List<Perfume> cart = getCart(context);

        boolean found = false;
        for (Perfume p : cart) {
            if (p.getName().equalsIgnoreCase(perfume.getName())) {
                p.quantity++;
                found = true;
                break;
            }
        }
        if (!found) {
            Perfume copy = new Perfume(perfume);
            copy.quantity = 1;
            cart.add(copy);
        }
        saveCart(context, cart);
    }

    public static List<Perfume> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CART_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(CART_KEY, null);
        if (json == null) return new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Perfume>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private static void saveCart(Context context, List<Perfume> cart) {
        SharedPreferences prefs = context.getSharedPreferences(CART_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(cart);
        editor.putString(CART_KEY, json);
        editor.commit();
    }
}