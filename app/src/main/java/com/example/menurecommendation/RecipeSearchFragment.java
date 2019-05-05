package com.example.menurecommendation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.List;

public class RecipeSearchFragment extends Fragment {

    final static String DATA_RECEIVE = "data_receive";

    private RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private FloatingSearchView searchBarView;
    private String searchWord;
    private Boolean isNewSearch;
    private TextView notFoundText;
    private List<RecipeDetailsData> allRecipeDetailsData;

    private MyDBHandler db;

    private int span = 2;

    public RecipeSearchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_searching, container, false);
        Bundle args = getArguments();
        allRecipeDetailsData = null;
        isNewSearch = false;
        searchBarView = rootView.findViewById(R.id.search_bar_recipe);
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        notFoundText = rootView.findViewById(R.id.no_result_found);
        MainActivity.appBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        MainActivity.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appBar.setNavigationIcon(null);
                getActivity().onBackPressed();
            }
        });
        searchBarView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) { }
            @Override
            public void onSearchAction(String currentQuery) {
                Log.d("Searching",currentQuery);
                db = MainActivity.dbHandler;
                List<RecipeDetailsData> data = db.findHandlerByTag(currentQuery);
                Log.d("new",(data==null) + "");
                displayResult(data, isNewSearch);
            }
        });
        if (args != null) {
            searchWord = args.getString(DATA_RECEIVE);
            if (searchWord != null){ // Check if new search
                searchBarView.setSearchText(searchWord);
                db = MainActivity.dbHandler;
                allRecipeDetailsData = db.findHandlerByTag(searchWord);
            } else {
                isNewSearch = true;
            }
        }
        displayResult(allRecipeDetailsData, isNewSearch);
        return rootView;
    }

    public void displayResult(List<RecipeDetailsData> data, boolean NewSearch){
        Log.d("New", NewSearch+""+(data == null));
        if (data == null) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            if (!NewSearch)
                notFoundText.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            notFoundText.setVisibility(View.INVISIBLE);
            mAdapter = new ImageListAdapter(getContext(), data);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), span));
        }
        isNewSearch = false;
    }

}
