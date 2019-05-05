package com.example.menurecommendation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;
import static com.example.menurecommendation.FirstLaunchActivity.sharedPrefFile;

public class MenuGenerationFragment extends Fragment {
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;
    private static View rootView;

    private MyDBHandler db;
    private SharedPreferences mPreferences = null;
    private Random rand = new Random();
    private StringBuilder allRecipeID;

    public static final String MEAT_COUNTER = "MeatCounter";
    public static final String VEGGIE_COUNTER = "VeggieCounter";
    public static final String SOUP_COUNTER = "SoupCounter";
    public static final String OUT = "Out";

    public static final String PREVIOUS_RECIPE_DATA = "PreviousRecipeID";
    public static final String IS_FROM_HISTORY = "IsFromHistory";
    public static final String HISTORY = "History";
    public static final String HISTORY_COUNTER ="HistoryCounter";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            setup();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu_generation, container, false);
        if (mPreferences == null)
            mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Bundle args = getArguments();
        if (args != null)
            mPreferences.edit().putBoolean(OUT, false).commit();
        boolean isBackPressed = mPreferences.getBoolean(OUT, false);
        Log.d("ISBACKEDPRESSED", ""+isBackPressed);
        if (isBackPressed) {
            MainActivity.appTitle.setText(R.string.today_menu);
            setHasOptionsMenu(true);
            String allID = mPreferences.getString(PREVIOUS_RECIPE_DATA, "");
            StringTokenizer st = new StringTokenizer(allID, ",");
            int token = st.countTokens();
            ArrayList<RecipeDetailsData> allRecipeData = new ArrayList<>();
            MyDBHandler db = MainActivity.dbHandler;
            for (int j = 0; j < token; j++) {
                RecipeDetailsData data = db.findSingleRecipeByID(Integer.parseInt(st.nextToken()));
                allRecipeData.add(data);
            }
            setupFromPrevious(allRecipeData);
        } else {

            if (args != null) {
                MainActivity.appTitle.setText(R.string.history);
                ArrayList<RecipeDetailsData> data = (ArrayList<RecipeDetailsData>) args.getSerializable(IS_FROM_HISTORY);
                setupFromHistory(data);
            } else {
                MainActivity.appTitle.setText(R.string.today_menu);
                setHasOptionsMenu(true);
                setup();
            }
        }
        MainActivity.appBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        MainActivity.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appBar.setNavigationIcon(null);
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void setup(){
        allRecipeID = new StringBuilder();
        db = MainActivity.dbHandler;
        ArrayList<RecipeDetailsData> data = new ArrayList<>();
        data.addAll(MeatGeneration());
        data.addAll(VeggieGeneration());
        data.addAll(SoupGeneration());
        int historyCounter = mPreferences.getInt(HISTORY_COUNTER,0);
        allRecipeID.deleteCharAt(allRecipeID.length() - 1);
        mPreferences.edit().putString(HISTORY+historyCounter, allRecipeID.toString()).commit();
        mPreferences.edit().putInt(HISTORY_COUNTER, ++historyCounter).commit();
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mAdapter = new CardViewAdapter(data);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        StringBuilder sb = new StringBuilder();
        for (RecipeDetailsData x : data){
            sb.append(x.getID()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        mPreferences.edit().putString(PREVIOUS_RECIPE_DATA,sb.toString()).commit();
    }

    private void setupFromHistory(ArrayList<RecipeDetailsData> data){
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mAdapter = new CardViewAdapter(data);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        StringBuilder sb = new StringBuilder();
        for (RecipeDetailsData x : data){
            sb.append((x.getID())).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        mPreferences.edit().putString(PREVIOUS_RECIPE_DATA,sb.toString()).commit();
    }

    private void setupFromPrevious(ArrayList<RecipeDetailsData> data){
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mAdapter = new CardViewAdapter(data);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<RecipeDetailsData> MeatGeneration(){
        int userPrefNumber = mPreferences.getInt(FirstLaunchActivity.NOM_KEY, 0);
        boolean isSelectBeef = mPreferences.getBoolean(FirstLaunchActivity.BEEF_KEY,false);

        boolean isSelectPork = mPreferences.getBoolean(FirstLaunchActivity.PORK_KEY,false);
        boolean isSelectChicken = mPreferences.getBoolean(FirstLaunchActivity.CHICKEN_KEY,false);

        List<String> foodType = new ArrayList<>();
        if (!isSelectBeef)
            foodType.add("Beef");
        if (!isSelectPork)
            foodType.add("Pork");
        if (!isSelectChicken)
            foodType.add("Chicken");

        return GenerationMechanism(userPrefNumber, MEAT_COUNTER, foodType);
    }

    private ArrayList<RecipeDetailsData> VeggieGeneration(){
        int userPrefNumber = mPreferences.getInt(FirstLaunchActivity.NOV_KEY, 0);
        boolean isSelectBroccoli = mPreferences.getBoolean(FirstLaunchActivity.BROCCOLI_KEY,false);
        boolean isSelectLettuce = mPreferences.getBoolean(FirstLaunchActivity.LETTUCE_KEY,false);
        boolean isSelectSpinach = mPreferences.getBoolean(FirstLaunchActivity.SPINACH_KEY,false);

        List<String> foodType = new ArrayList<>();
        if (!isSelectBroccoli)
            foodType.add("Broccoli");
        if (!isSelectLettuce)
            foodType.add("Lettuce");
        if (!isSelectSpinach)
            foodType.add("Spinach");

        Log.d("SIZE",""+foodType.size());

        return GenerationMechanism(userPrefNumber, VEGGIE_COUNTER, foodType);
    }

    private ArrayList<RecipeDetailsData> SoupGeneration(){
        int userPrefNumber = mPreferences.getInt(FirstLaunchActivity.NOS_KEY, 0);
        boolean isSelectChineseSoup = mPreferences.getBoolean(FirstLaunchActivity.CS_KEY,false);
        boolean isSelectWesternSoup = mPreferences.getBoolean(FirstLaunchActivity.WS_KEY,false);

        List<String> foodType = new ArrayList<>();
        if (!isSelectChineseSoup)
            foodType.add("Chinese soup");
        if (!isSelectWesternSoup)
            foodType.add("Western soup");

        return GenerationMechanism(userPrefNumber, SOUP_COUNTER, foodType);
    }

    private ArrayList<RecipeDetailsData> GenerationMechanism(int userPrefNumber, String counterType, List<String> foodType){
        ArrayList<RecipeDetailsData> result = new ArrayList<>();
        List<Integer> chosenNumber = new ArrayList<>();
        int genNumber;

        Log.d("ALL DATA", ""+userPrefNumber);
        for (String x: foodType)
            Log.d("ALL DATA", x);

        int counter = mPreferences.getInt(counterType,0);

        for (int i = 0; i < userPrefNumber; i++){
            String tag = foodType.get(counter++ % foodType.size());
            List<Integer> IDs = db.findIDsByTag(tag);
            for (int x: IDs)
                Log.d("ALL id", ""+x);
            do {
                genNumber = IDs.get(rand.nextInt(IDs.size()));
            } while (chosenNumber.contains(genNumber));
            chosenNumber.add(genNumber);
            allRecipeID.append(genNumber).append(",");
            result.add(db.findSingleRecipeByID(genNumber));
        }

        SharedPreferences.Editor prefEditor = mPreferences.edit();
        if (foodType.size() != 0){
            prefEditor.putInt(counterType, counter % foodType.size());
            prefEditor.apply();
            prefEditor.commit();
        }
        return result;
    }
}