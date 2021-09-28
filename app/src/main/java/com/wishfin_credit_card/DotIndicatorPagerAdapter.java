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
            heading.setText("Credit card for all your needs apply just in minutes");
            subheading.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        } else if (position == 1) {
            img.setBackgroundResource(R.drawable.ic_dashboardtwo);
            heading.setText("Many Exciting offers & rewards on applying credit card");
            subheading.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        }
        if (position == 2) {
            img.setBackgroundResource(R.drawable.ic_dashboardthree);
            heading.setText("Get most suitable credit card from top banks");
            subheading.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
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
