package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private TextView mAlsoKnownTv;
    private TextView mAlsoKnownLabel;
    private TextView mOriginTv;
    private TextView mOriginLabel;
    private TextView mDescriptionTv;
    private TextView mIngredientTv;
    private ImageView mSandwichIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // هنا فند فيو للبحث عن مصدركل صورة و اسم



        mSandwichIv = findViewById(R.id.picture);
        mAlsoKnownTv = findViewById(R.id.also_known_tv);
        mAlsoKnownLabel = findViewById(R.id.alsoKnownAs);
        mOriginTv = findViewById(R.id.origin);
        mOriginLabel = findViewById(R.id.placeOfOrigin);
        mDescriptionTv = findViewById(R.id.description_tv);
        mIngredientTv = findViewById(R.id.ingredients_tv);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
    // ضع كلمة سندويش بين الأقواس
        // أحذف الثلاثة الأسطر التي في الوسط
        populateUI(sandwich);

        setTitle(sandwich.getMainName());
    }

    // ضع العبارة في السترنق و جعله هنا يأخذها من المصادر
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    // ضع البارومتر
    // عمل جملة أشتراطية
    private void populateUI(Sandwich sandwich) {
        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));

            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i));
            }
            mAlsoKnownTv.setText(stringBuilder.toString());
        } else {
            mAlsoKnownTv.setVisibility(View.GONE);
            mAlsoKnownLabel.setVisibility(View.GONE);
        }

        // set Text to originTv
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            mOriginTv.setVisibility(View.GONE);
            mOriginLabel.setVisibility(View.GONE);
        } else {
            mOriginTv.setText(sandwich.getPlaceOfOrigin());
        }

        // set Text to descriptionTv
        mDescriptionTv.setText(sandwich.getDescription());

        // set Text to ingredientTv
        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\u2022");
            stringBuilder.append(sandwich.getIngredients().get(0));

            for (int i = 1; i < sandwich.getIngredients().size(); i++) {
                stringBuilder.append("\n");
                stringBuilder.append("\u2022");
                stringBuilder.append(sandwich.getIngredients().get(i));
            }
            mIngredientTv.setText(stringBuilder.toString());
        }

        // display the image
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mSandwichIv);
    }
    }

