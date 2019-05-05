package com.example.menurecommendation;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.menurecommendation.FirstLaunchActivity.sharedPrefFile;


public class RecipeDetailsFragment extends Fragment {
    private TextView NameTextView ;
    private TextView DescriptionTextView ;
    private TextView StepTextView ;
    private Button IngredientsButton;
    final static String ID_KEY = "ID_KEY";
    private int searchID;
    private com.example.menurecommendation.RecipeDetailsData RecipeDetailsData;
    private ImageView image;
    private MyDBHandler db;

    public RecipeDetailsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        Bundle args = getArguments();
        getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE).edit().putBoolean(MenuGenerationFragment.OUT, true).commit();
        NameTextView = rootView.findViewById(R.id.recipename);
        DescriptionTextView = rootView.findViewById(R.id.description);
        StepTextView = rootView.findViewById(R.id.step);
        IngredientsButton = rootView.findViewById(R.id.ingredient_button);
        image = rootView.findViewById(R.id.imageView1);
        MainActivity.appBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        MainActivity.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appBar.setNavigationIcon(null);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        if (args != null) {
            searchID = args.getInt(ID_KEY);
            db = MainActivity.dbHandler;
            RecipeDetailsData = db.findSingleRecipeByID(searchID);
            NameTextView.setText(RecipeDetailsData.getRecipeName());
            MainActivity.appTitle.setText(R.string.detailed_recipe);
            DescriptionTextView.setText("Cooking Time: "+RecipeDetailsData.getCookingTime()+" mins  |  Difficulty: "+RecipeDetailsData.getDifficulty() + " / 5");
            String step=RecipeDetailsData.getSteps();
            StepTextView.setText(step.replaceAll("Step", "\nStep"));
            IngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater)
                            getActivity().getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.ingredient_details, null);
                    TextView ingredientList = popupView.findViewById(R.id.ingredient_list);
                    TextView quantityList = popupView.findViewById(R.id.quantity_list);
                    TextView UnitList = popupView.findViewById(R.id.unit_list);
                    TextView NOPText = popupView.findViewById(R.id.number_of_person_menu);

                    StringBuilder ingredients = new StringBuilder();
                    StringBuilder quantity = new StringBuilder();
                    StringBuilder unit = new StringBuilder();

                    List<IngredientData> ingredientDataList = db.findIngredients(searchID);

                    SharedPreferences mPreferences =
                            getActivity().getSharedPreferences(FirstLaunchActivity.sharedPrefFile, getActivity().MODE_PRIVATE);

                    int NOP = mPreferences.getInt(FirstLaunchActivity.NOP_KEY, 1);
                    for (IngredientData data : ingredientDataList){
                        ingredients.append(data.getIngredient()).append("\n");
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(1);
                        quantity.append(df.format(data.getQuatity() * NOP)).append("\n");
                        unit.append(data.getUnit() != null ? data.getUnit() : "").append("\n");
                    }

                    ingredientList.setText(ingredients.toString());
                    quantityList.setText(quantity.toString());
                    UnitList.setText(unit.toString());
                    if (NOP == 1)
                        NOPText.setText(String.format("This recipe may serve for " + NOP + " person"));
                    else NOPText.setText(String.format("This Recipe may serve for " + NOP + " people"));
                    NOPText.setGravity(Gravity.CENTER);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                    popupWindow.setAnimationStyle(R.style.Animation);
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                }
            });
            Resources resource = getContext().getResources();
            int resourceId = resource.getIdentifier("recipe_"+RecipeDetailsData.getID(), "drawable",
                    getContext().getPackageName());
            image.setImageResource(resourceId);
            Log.d("recipe name", ""+RecipeDetailsData.getRecipeName());
         }
        return rootView;
    }



}
