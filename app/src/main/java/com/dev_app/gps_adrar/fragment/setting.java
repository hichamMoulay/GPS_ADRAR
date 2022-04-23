package com.dev_app.gps_adrar.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dev_app.gps_adrar.MainActivity;
import com.dev_app.gps_adrar.R;
import com.dev_app.gps_adrar.gps;


public class setting extends Fragment {
    View view;
    CardView card1,card2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_setting, container, false);

        card1=view.findViewById(R.id.card1);
        card2=view.findViewById(R.id.card2);

        Animation animation=AnimationUtils.loadAnimation(getContext(),R.anim.left_right);
        Animation animation2=AnimationUtils.loadAnimation(getContext(),R.anim.right_left);

        card1.setAnimation(animation);
        card2.setAnimation(animation2);

        onBack();

        return  view;
    }

    void onBack(){
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK){
                    new MainActivity().setFrameLayout(new gps());

                    return true;
                }


                return false;
            }
        });
    }
}