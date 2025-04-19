package com.example.perfstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class MainActivity extends AppCompatActivity {

    private Spinner spnBrands;
    private RadioGroup radioGroup;
    private RadioButton rbtnMale, rbtnFemale;
    private EditText txtPerfume;
    private CheckBox chkDiscount;
    private Button btnSearch;
    private PerfumeManager perfumeManager;
    private List<Perfume> Perfumes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpViews();
        perfumeManager = new PerfumeManager(this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        getSharedPreferences("INVENTORY", MODE_PRIVATE).edit().clear().apply();
        Perfumes = perfumeManager.getPerfumes();
        extractBrandsToSpn();
        manageAvtivity();
    }

    private void manageAvtivity() {
        // Load updated perfumes from SharedPreferences (to handle quantity reducing)
        SharedPreferences prefs = getSharedPreferences("INVENTORY", MODE_PRIVATE);
        String json = prefs.getString("perfume_list_2ndActivity", "");

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Perfume>>() {}.getType();
        List<Perfume> loadedList = gson.fromJson(json, listType);

        if (loadedList != null && !loadedList.isEmpty()) {
            Perfumes = loadedList;
        }

        btnSearch.setOnClickListener(v -> {
            // get selected filters from the user
            String selectedBrand = spnBrands.getSelectedItem().toString().trim();
            String enteredName = txtPerfume.getText().toString().trim();
            int selectedGenderId = radioGroup.getCheckedRadioButtonId();
            boolean discountedOnly = chkDiscount.isChecked();
            String selectedGender = "";

            if (selectedGenderId != -1) {
                RadioButton selectedRadio = findViewById(selectedGenderId);
                selectedGender = selectedRadio.getText().toString().trim();
                if (selectedGender.equalsIgnoreCase("For Males")) {
                    selectedGender = "Male";
                } else if (selectedGender.equalsIgnoreCase("For Females")) {
                    selectedGender = "Female";
                }
            }

            boolean hasFilter = false;//to check if the user choose any filter
            if (!selectedBrand.equals("Select a Brand")){
                hasFilter = true;
            }
            if (!enteredName.isEmpty()){
                hasFilter = true;
            }
            if (!selectedGender.isEmpty()){
                hasFilter = true;
            }
            if (discountedOnly){
                hasFilter = true;
            }

            if (!hasFilter) {
                Toast.makeText(MainActivity.this, "Please select at least one filter!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Perfume> rsltSearchList = new ArrayList<>();

            for (Perfume p : Perfumes) {
                boolean match = true;

                if (!selectedBrand.equals("Select a Brand") && !p.brand.equalsIgnoreCase(selectedBrand)) {
                    match = false;
                }

                if (!enteredName.isEmpty() && !p.name.toLowerCase().contains(enteredName.toLowerCase())) {
                    match = false;
                }

                if (!selectedGender.isEmpty() && !p.gender.equalsIgnoreCase(selectedGender)) {
                    match = false;
                }

                if (discountedOnly && !p.onOffer) {
                    match = false;
                }

                if (match) {
                    rsltSearchList.add(p);
                }
            }

            // start new activity
            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
            intent.putExtra("filteredPerfumes", new ArrayList<>(rsltSearchList));
            startActivity(intent);
        });
    }

    private void extractBrandsToSpn() {
        // hash set to extract unique brands
        Set<String> brandSet = new HashSet<>();
        for (Perfume p : Perfumes) {
            brandSet.add(p.brand);
        }

        List<String> brandList = new ArrayList<>(brandSet);
        Collections.sort(brandList);  // sort The Brands alphabetically
        brandList.add(0, "Select a Brand");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandList) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.view.View view = super.getView(position, convertView, parent);
                ((android.widget.TextView) view).setTextColor(android.graphics.Color.WHITE); // set items color in the spinner to white
                return view;
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrands.setAdapter(adapter);
    }

    private void setUpViews() {
        spnBrands = findViewById(R.id.spnBrands);
        radioGroup = findViewById(R.id.radioGroup);
        rbtnMale = findViewById(R.id.rbtnMale);
        rbtnFemale = findViewById(R.id.rbtnFemale);
        txtPerfume = findViewById(R.id.txtPerfume);
        chkDiscount = findViewById(R.id.chkDiscount);
        btnSearch = findViewById(R.id.btnSearch);
    }
}

