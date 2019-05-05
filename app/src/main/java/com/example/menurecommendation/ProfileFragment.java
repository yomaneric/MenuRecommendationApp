package com.example.menurecommendation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileFragment extends Fragment {
    private final int RESULT_CODE = 100;

    TextView txtDistrictContent;
    TextView txtDislikeContent;
    TextView txtNOPContent;
    TextView txtWelcomeContent;
    TextView txtCuisinePContent;

    private SharedPreferences mPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.profile);
        MainActivity.appBar.setNavigationIcon(null);
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        Button editButton = rootView.findViewById(R.id.button);

        txtDistrictContent = rootView.findViewById(R.id.txtDistrictContent);
        txtDislikeContent = rootView.findViewById(R.id.txtDislikeContent);
        txtWelcomeContent = rootView.findViewById(R.id.txtWelcomeContent);
        txtNOPContent = rootView.findViewById(R.id.txtNOPContent);
        txtCuisinePContent = rootView.findViewById(R.id.txtCunsinePContent);

        mPreferences = getActivity().getSharedPreferences(FirstLaunchActivity.sharedPrefFile, getActivity().MODE_PRIVATE);

        setupText();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), FirstLaunchActivity.class);
                intent.putExtra(FirstLaunchActivity.COME_FROM_FRAGMENT_FLAG, true);
                startActivityForResult(intent, RESULT_CODE);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE && resultCode == Activity.RESULT_OK) {
            setupText();
            Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT);
        }
    }

    private void setupText(){
        String welcomeMessage = "Welcome " + mPreferences.getString(FirstLaunchActivity.WELCOME_Reply,"") + " :)";
        String DislikeMessage = "";
        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.district_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> NOPAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.no_of_people_array, android.R.layout.simple_spinner_item);


        ArrayAdapter<CharSequence> NOMAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.no_of_meat_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> NOVAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.no_of_viggie_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> NOSAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.no_of_soup_array, android.R.layout.simple_spinner_item);

        int district = mPreferences.getInt(FirstLaunchActivity.District_KEY,0);
        int nop = mPreferences.getInt(FirstLaunchActivity.NOP_KEY,1);
        int nom = mPreferences.getInt(FirstLaunchActivity.NOM_KEY,0);
        int nov = mPreferences.getInt(FirstLaunchActivity.NOV_KEY,0);
        int nos = mPreferences.getInt(FirstLaunchActivity.NOS_KEY,0);

        boolean isSelectBeef = mPreferences.getBoolean(FirstLaunchActivity.BEEF_KEY,false);
        boolean isSelectPork = mPreferences.getBoolean(FirstLaunchActivity.PORK_KEY,false);
        boolean isSelectChicken = mPreferences.getBoolean(FirstLaunchActivity.CHICKEN_KEY,false);
        boolean isSelectLettuce = mPreferences.getBoolean(FirstLaunchActivity.LETTUCE_KEY,false);
        boolean isSelectBroccoli = mPreferences.getBoolean(FirstLaunchActivity.BROCCOLI_KEY,false);
        boolean isSelectSpinach = mPreferences.getBoolean(FirstLaunchActivity.SPINACH_KEY,false);
        boolean isSelectChineseSoup = mPreferences.getBoolean(FirstLaunchActivity.CS_KEY,false);
        boolean isSelectWesternSoup = mPreferences.getBoolean(FirstLaunchActivity.WS_KEY,false);

        String sDistrict = districtAdapter.getItem(district).toString();
        String Nop = NOPAdapter.getItem(nop-1).toString();
        String Nom = NOMAdapter.getItem(nom).toString();
        String Nov = NOVAdapter.getItem(nov).toString();
        String Nos = NOSAdapter.getItem(nos).toString();


        String Cuisine = "Meat: " + Nom + ", Viggie: " + Nov + ", Soup: " + Nos;

        if(isSelectBeef){
            DislikeMessage += "#Beef " ;
        }
        if(isSelectPork){
            DislikeMessage += "#Pork " ;
        }
        if(isSelectChicken){
            DislikeMessage += "#Chicken " ;
        }
        if(isSelectLettuce){
            DislikeMessage += "#Lettuce " ;
        }
        if(isSelectBroccoli){
            DislikeMessage += "#Broccoli " ;
        }
        if(isSelectSpinach){
            DislikeMessage += "#Spinach " ;
        }
        if(isSelectChineseSoup){
            DislikeMessage += "#Chinese soup " ;
        }
        if(isSelectWesternSoup){
            DislikeMessage += "#Western soup " ;
        }

        txtWelcomeContent.setText(welcomeMessage);
        txtDistrictContent.setText(sDistrict);
        txtDislikeContent.setText(DislikeMessage);
        txtNOPContent.setText(Nop);
        txtCuisinePContent.setText(Cuisine);
    }
}
