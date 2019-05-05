package com.example.menurecommendation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RecipeFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<String>> expandableListDetail;

    DataPassListener mCallback;

    public RecipeFragment(){}

    public interface DataPassListener{
        void passData(String data);
    }

    int[] listviewImage = new int[]{
            R.drawable.meat_banner, R.drawable.vegetable_banner,
            R.drawable.soup_banner
    };


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (getActivity() instanceof DataPassListener)
            mCallback = (DataPassListener) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_title_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("Menu","inflated");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            mCallback.passData(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.recipe);
        setHasOptionsMenu(true);
        MainActivity.appBar.setNavigationIcon(null);
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        expandableListView = rootView.findViewById(R.id.expandableListView);
        expandableListDetail = RecipeTypeData.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail, listviewImage);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                mCallback.passData(parent.getExpandableListAdapter().getChild(groupPosition, childPosition).toString());
                return false;
            }
        });
        return rootView;
    }
}
