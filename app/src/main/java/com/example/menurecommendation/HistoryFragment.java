package com.example.menurecommendation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;
import static com.example.menurecommendation.FirstLaunchActivity.sharedPrefFile;

public class HistoryFragment extends Fragment {
    private SharedPreferences mPreferences;

    public HistoryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.history);
        MainActivity.appBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        MainActivity.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appBar.setNavigationIcon(null);
                getActivity().onBackPressed();
            }
        });
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ArrayList<String> historySet = new ArrayList<>();
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int historyCounter = mPreferences.getInt(MenuGenerationFragment.HISTORY_COUNTER, 0);
        for (int i=historyCounter; i>0;i--)
            historySet.add("History "+i);
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                R.layout.history_list_item, historySet);
        ListView listView = rootView.findViewById(R.id.history_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int historyCounter = mPreferences.getInt(MenuGenerationFragment.HISTORY_COUNTER, 0);
                String savedString = mPreferences.getString(MenuGenerationFragment.HISTORY + (historyCounter - position - 1), "");
                StringTokenizer st = new StringTokenizer(savedString, ",");
                int token = st.countTokens();
                ArrayList<RecipeDetailsData> allRecipeData = new ArrayList<>();
                MyDBHandler db = MainActivity.dbHandler;
                for (int j = 0; j < token; j++) {
                    RecipeDetailsData data = db.findSingleRecipeByID(Integer.parseInt(st.nextToken()));
                    allRecipeData.add(data);
                }
                ((MainActivity) getContext()).generateRecipePage(true, allRecipeData);
            }
        });
        return rootView;
    }
}
