package com.wishfin_credit_card.FontClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class PoppinsMedium extends androidx.appcompat.widget.AppCompatTextView {

    public PoppinsMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PoppinsMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoppinsMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                "Poppins-Medium.ttf");
        setTypeface(typeface, Typeface.NORMAL);

    }
}