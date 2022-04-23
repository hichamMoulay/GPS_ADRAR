package com.dev_app.gps_adrar.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev_app.gps_adrar.MainActivity;
import com.dev_app.gps_adrar.R;
import com.dev_app.gps_adrar.testing.MapsActivityTEST;

public class about extends AppCompatActivity {

    private Button homePage, btn_next;
    private int position;
    private ViewPager viewPage;
    private LinearLayout layout_number;
    private coustemViewPage coustemViewPage;
    private TextView[] textNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
        String viewLearn = preferences.getString("appView", "0");

        if (viewLearn.equals("1")) {
            Intent intent = new Intent(about.this, MapsActivityTEST.class);
            startActivity(intent);
            finish();
        } else {

            viewPage = findViewById(R.id.viewPage);
            layout_number = findViewById(R.id.layout_number);
            btn_next = findViewById(R.id.btn_next);
            homePage = findViewById(R.id.homePage);
            coustemViewPage = new coustemViewPage(this);

            viewPage.setAdapter(coustemViewPage);

            setNumberPager(0);
            viewPage.addOnPageChangeListener(changeListener);

            homePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
                    editor.putString("appView", "1");
                    editor.apply();

                    Intent intent = new Intent(about.this, MapsActivityTEST.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    private void setNumberPager(int position) {

        textNumber = new TextView[4];
        layout_number.removeAllViews();

        for (int i = 0; i < textNumber.length; i++) {

            textNumber[i] = new TextView(this);
            textNumber[i].setText(Html.fromHtml("&#8226"));
            textNumber[i].setTextSize(35);

            layout_number.addView(textNumber[i]);
        }
        textNumber[position].setTextColor(Color.RED);

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int positionItem) {

//            Animation animation = AnimationUtils.loadAnimation(About_Application.this, R.anim.reight_left);

            position = positionItem;
            setNumberPager(position);

            if (positionItem == 3) {

                homePage.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.INVISIBLE);

            } else {

                homePage.setVisibility(View.INVISIBLE);
                btn_next.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void onClickAbout(View view) {

        switch (view.getId()) {

            case R.id.btn_next:
                viewPage.setCurrentItem(position + 1);
                break;
            case R.id.btn_skip:
                // case R.id.homePage:

                SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();
                editor.putString("appView", "1");
                editor.apply();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}