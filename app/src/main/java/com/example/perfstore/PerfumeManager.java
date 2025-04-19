package com.example.perfstore;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PerfumeManager {
    private static final String PERFUMES_KEY = "PERFUMES";
    private static final String FLAG = "FLAG";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public PerfumeManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        gson = new Gson();
        if (!prefs.getBoolean(FLAG, false)) {
            savePerfumesToPrefrences();
        }
    }

    private void savePerfumesToPrefrences() {
        List<Perfume> perfumeList = new ArrayList<>();

        perfumeList.add(new Perfume(
                "Acqua di Giò",
                "Armani",
                "Male",
                false,
                5,
                70.0,
                R.drawable.armani_acqua_di_gio
        ));

        perfumeList.add(new Perfume(
                "Bleu de Chanel",
                "Chanel",
                "Male",
                true,
                7,
                85.0,
                R.drawable.blue_de_chanel
        ));

        perfumeList.add(new Perfume(
                "Chanel No. 5",
                "Chanel",
                "Female",
                false,
                3,
                90.0,
                R.drawable.chanel_no5
        ));

        perfumeList.add(new Perfume(
                "Fahrenheit",
                "Dior",
                "Male",
                true,
                9,
                65.0,
                R.drawable.fahrenheit
        ));

        perfumeList.add(new Perfume(
                "Gucci Bloom",
                "Gucci",
                "Female",
                false,
                14,
                75.0,
                R.drawable.gucci_bloom
        ));

        perfumeList.add(new Perfume(
                "Shalimar",
                "Guerlain",
                "Female",
                true,
                16,
                60.0,
                R.drawable.shalimar
        ));

        perfumeList.add(new Perfume(
                "Black Orchid",
                "Tom Ford",
                "Unisex",
                false,
                2,
                95.0,
                R.drawable.tom_ford_black_orchid
        ));

        perfumeList.add(new Perfume(
                "Versace Eros",
                "Versace",
                "Male",
                true,
                20,
                80.0,
                R.drawable.versace_eros
        ));

        perfumeList.add(new Perfume(
                "Vacation",
                "Vacation Inc.",
                "Unisex",
                true,
                3,
                55.0,
                R.drawable.vacation
        ));

        String json = gson.toJson(perfumeList);
        editor.putString(PERFUMES_KEY, json);
        editor.putBoolean(FLAG, true);
        editor.commit();
    }

    public List<Perfume> getPerfumes() {
        String json = prefs.getString(PERFUMES_KEY, null);

        Type type = new TypeToken<List<Perfume>>(){}.getType();
        List<Perfume> perfumes = gson.fromJson(json, type);
        if(perfumes != null) {
            return perfumes;
        }
        return new ArrayList<>();

    }

    public void savePerfumes(List<Perfume> updatedList) {
        String json = gson.toJson(updatedList);
        editor.putString(PERFUMES_KEY, json);
        editor.commit();
    }

    public void decreaseQuantity(String perfumeName) {
        List<Perfume> list = getPerfumes();
        for (Perfume p : list) {
            if (p.name.equals(perfumeName) && p.quantity > 0) {
                p.quantity--;
                break;
            }
        }
        savePerfumes(list);
    }
}