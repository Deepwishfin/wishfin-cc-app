package com.wishfin_credit_card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Arrays;
import java.util.List;

public class DotIndicatorPagerAdapter extends PagerAdapter {
    private static final List<Item> items =
            Arrays.asList(new Item(R.color.md_indigo_500), new Item(R.color.md_green_500), new Item(R.color.md_red_500));

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View item = LayoutInflater.from(container.getContext()).inflate(R.layout.material_page, container, false);
        ImageView img = item.findViewById(R.id.item_image);
        TextView heading = item.findViewById(R.id.heading);
        TextView subheading = item.findViewById(R.id.subheading);
        if (position == 0) {
            img.setBackgroundResource(R.drawable.ic_dashboardone);
            heading.setText("One App for all your Credit Card Needs");
            subheading.setText("Discover-Explore-Enable Best Credit Card Options");
        } else if (position == 1) {
            img.setBackgroundResource(R.drawable.ic_dashboardtwo);
            heading.setText("Discover which Credit Card you're Eligible for in one place");
            subheading.setText("Explore & Compare Credit Cards by features,reward & bank");
        }
        if (position == 2) {
            img.setBackgroundResource(R.drawable.ic_dashboardthree);
            heading.setText("Enable a Credit Cards that's right for you");
            subheading.setText("Apply for a Credit Card of your choice in minutes");
        }
        container.addView(item);
        return item;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private static class Item {
        private final int color;

        private Item(int color) {
            this.color = color;
        }
    }
}
