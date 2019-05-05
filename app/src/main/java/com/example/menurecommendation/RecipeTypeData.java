
package com.example.menurecommendation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

 class RecipeTypeData {
     static LinkedHashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        List<String> Meat = new ArrayList<>();
        Meat.add("Beef");
        Meat.add("Chicken");
        Meat.add("Pork");

        List<String> Vegetable = new ArrayList<>();
        Vegetable.add("Lettuce");
        Vegetable.add("Broccoli");
        Vegetable.add("Spinach");

        List<String> Soup = new ArrayList<>();
        Soup.add("Chinese Soup");
        Soup.add("Western Soup");

        expandableListDetail.put("Meat", Meat);
        expandableListDetail.put("Vegetable", Vegetable);
        expandableListDetail.put("Soup", Soup);

        return expandableListDetail;
    }
}
