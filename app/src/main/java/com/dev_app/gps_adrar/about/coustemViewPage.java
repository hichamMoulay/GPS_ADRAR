package com.dev_app.gps_adrar.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dev_app.gps_adrar.R;


public class coustemViewPage extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public coustemViewPage(Context context) {
        this.context = context;
    }


    int[] img = {

            R.drawable.ic_login,
            R.drawable.icon_atm,
            R.drawable.icon_settings,
            R.drawable.icon_hotel,

    };

    int[] title = {

            /*R.string.title_pageHome,
            R.string.language,
            R.string.action_settings,
            R.string.login,
            R.string.myProfile,
            R.string.search,
            R.string.spisiality,
            R.string.facultysiens_technology,
            R.string.departemontInfo,
            R.string.close


            R.string.About_library,
            R.string.About_programmer,
            R.string.bookWait,
            R.string.title_book,
            R.string.specialty,
            R.string.title_activity_math_info,
            R.string.skip,
    };

    int [] des={

            R.string.vide,
            R.string.des_language,
            R.string.desSetting,
            R.string.desLogin,
            R.string.des_my_profile,
            R.string.des_search,
            R.string.vide,
            R.string.desFacultysiens,
            R.string.des_departemontInfo,
            R.string.des_close



            R.string.choseDepartoment,
            R.string.addNote,
            R.string.cancel,
            R.string.common_signin_button_text,
            R.string.close,
            R.string.tab_text_1,
            R.string.student,*/

    };


    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView imageView=view.findViewById(R.id.imgSlider);

        imageView.setImageResource(img[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
