package com.example.menurecommendation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.menurecommendation.FirstLaunchActivity.sharedPrefFile;

public class HomeFragment extends Fragment {

    public HomeFragment(){}

    public interface generateRecipePageListener{
        void generateRecipePage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.home);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button genButton = rootView.findViewById(R.id.generate_button);
        Button hisButton = rootView.findViewById(R.id.history_button);

        genButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE).edit().putBoolean(MenuGenerationFragment.OUT, false).commit();
                ((MainActivity)getActivity()).generateRecipePage(false, null);
            }
        });

        hisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE).edit().putBoolean(MenuGenerationFragment.OUT, false).commit();
                ((MainActivity)getActivity()).generateHistoryPage();
            }
        });
        return rootView;
    }
}

