package com.app.pocketfarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pocketfarm.R;

public class WelcomePagerAdapter extends PagerAdapter {

    Context context;

    int images[] = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    int headings[] = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three
    };

    int description[] = {
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three
    };

    public WelcomePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length; // Return the length of the headings array
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideTitleImage = view.findViewById(R.id.titleImage);
        TextView slideHeading = view.findViewById(R.id.texttitle);
        TextView slideDescription = view.findViewById(R.id.textdeccription);

        slideTitleImage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
