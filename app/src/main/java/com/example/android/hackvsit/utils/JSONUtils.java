package com.example.android.hackvsit.utils;

import com.example.android.hackvsit.model.Machine;
import com.example.android.hackvsit.model.Nutrition;
import com.example.android.hackvsit.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JSONUtils{

    private static final String ID = "_id";
    private static final String PRODUCTS = "products";
    private static final String VEND_ID = "vendId";
    private static final String LOC_LAT = "locLat";
    private static final String LOC_LANG = "locLong";
    private static final String PRODUCT_ID = "ID";
    private static final String NAME = "Name";
    private static final String NUTRITION_FACTS = "Nutrition Facts";
    private static final String MAX_QUANT = "Maximum Quantity";
    private static final String QUANT = "Cur Quantity";
    private static final String IMAGE = "Image";
    private static final String CALORIES = "Calories";
    private static final String FAT = "Total Fat";
    private static final String CALCIUM = "Calcium";
    private static final String CHOLESTEROL = "Cholesterol";
    private static final String IRON = "Iron";
    private static final String PROTEIN = "Protein";
    private static final String PRICE = "Price";
    private static final String REFER = "refer";
    private static final String MACHINE = "machine";


    public static Machine getMachineDetails(String jsonResponse){

        Machine machine = new Machine();
        try{
            JSONObject parentJson = new JSONObject(jsonResponse);
            JSONObject machineJson = parentJson.getJSONObject(MACHINE);

            machine.setmId(machineJson.getString(ID));
            machine.setmVendorId(machineJson.getString(VEND_ID));
            machine.setmLat(machineJson.getString(LOC_LAT));
            machine.setmLong(machineJson.getString(LOC_LANG));

            JSONArray referJson = parentJson.getJSONArray(REFER);
            ArrayList<String> recommendations = new ArrayList<>();
            for(int i=0;i<referJson.length();i++){
                String string = referJson.getString(i);
                recommendations.add(string);
            }
            machine.setmRecommendations(recommendations);

            ArrayList<Product> mProducts = new ArrayList<>();
            JSONArray productsArray = machineJson.getJSONArray(PRODUCTS);
            for(int i =0; i<productsArray.length();i++){
                Product product = new Product();
                JSONObject productJson = productsArray.getJSONObject(i);
                product.setmId(productJson.getInt(PRODUCT_ID));
                product.setmName(productJson.getString(NAME));
                product.setmMaxQuantity(productJson.getInt(MAX_QUANT));
                product.setmQuantity(productJson.getInt(QUANT));
                product.setmImageUrl(productJson.getString(IMAGE));
                product.setmPrice(productJson.getInt(PRICE));

                Nutrition nutrition = new Nutrition();
                JSONObject nutritionJson = productJson.getJSONObject(NUTRITION_FACTS);
                nutrition.setmCalcium(nutritionJson.getString(CALCIUM));
                nutrition.setmCholesterol(nutritionJson.getString(CHOLESTEROL));
                nutrition.setmIron(nutritionJson.getString(IRON));
                nutrition.setmFat(nutritionJson.getString(FAT));
                nutrition.setmCalories(nutritionJson.getString(CALORIES));
                nutrition.setmProtein(nutritionJson.getString(PROTEIN));
                product.setmNutrition(nutrition);
                mProducts.add(product);
            }
            machine.setmProducts(mProducts);

            return machine;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}