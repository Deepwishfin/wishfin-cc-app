package com.wishfin_credit_card;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class WishFinAnalytics extends Analytics {

    private FirebaseAnalytics firebaseAnalytics;

    private final String OPEN_APP = "open_app_new";
    private final String APP_OPENED = "app_opened_new";
    private final String ACTION_APP_OPENED = "action_app_launch_new";

    private final String RATE_APP = "rate_app";
    private final String ACTION_RATE_APP = "action_share_app";
    private final String RATED_APP = "rated_app";

    private final String OPEN_CIBIL_PAGE = "cibil_score_fetched";
    private final String CIBIL_PAGE_OPENED = "cibil_score_fetched";
    private final String ACTION_CIBIL_PAGE_OPENED = "action_cibil_score_fetched_launch";

    private final String BEST_CC_PAGE = "best_cc_page";
    private final String ACTION_BEST_CC_PAGE = "action_best_cc_page";
    private final String BEST_CC_PAGE_OPENED = "best_cc_page_opened";

    private final String BEST_REWARD_CC_PAGE = "best_reward_cc_page";
    private final String ACTION_BEST_REWARD_CC_PAGE = "action_best_reward_cc_page";
    private final String BEST_REWARD_CC_PAGE_OPENED = "best_reward_cc_page_opened";

    private final String LIFETIME_FREE_CC_PAGE = "lifetime_free_cc_page";
    private final String ACTION_LIFETIME_FREE_CC_PAGE = "action_lifetime_free_cc_page";
    private final String LIFETIME_FREE_CC_PAGE_OPENED = "lifetime_free_cc_page_opened";


    private final String BEST_TRAVEL_CC_PAGE = "best_travel_cc_page";
    private final String ACTION_BEST_TRAVEL_CC_PAGE = "action_best_travel_cc_page";
    private final String BEST_TRAVEL_CC_PAGE_OPENED = "best_travel_cc_page_opened";

    private final String BEST_FUEL_CC_PAGE = "best_fuel_cc_page";
    private final String ACTION_BEST_FUEL_CC_PAGE = "action_best_fuel_cc_page";
    private final String BEST_FUEL_CC_PAGE_OPENED = "best_fuel_cc_page_opened";

    private final String BEST_CASHBACK_CC_PAGE = "best_cashback_cc_page";
    private final String ACTION_BEST_CASHBACK_CC_PAGE = "action_best_cashback_cc_page";
    private final String BEST_CASHBACK_CC_PAGE_OPENED = "best_cashback_cc_page_opened";

    private final String SPECIAL_OFFER_PAGE = "special_offer_page";
    private final String ACTION_SPECIAL_OFFER_PAGE = "action_special_offer_page";
    private final String SPECIAL_OFFER_PAGE_OPENED = "special_offer_page_opened";


    private final String PROFILE_PAGE = "profile_page";
    private final String ACTION_PROFILE_PAGE = "action_profile_page";
    private final String PROFILE_PAGE_OPENED = "profile_page_opened";

    private final String HISTORY_PAGE = "history_page";
    private final String ACTION_HISTORY_PAGE = "action_history_page";
    private final String HISTORY_PAGE_OPENED = "history_page_opened";

    private final String INSTA_APPLY_PAGE = "insta_apply_page";
    private final String ACTION_INSTA_APPLY_PAGE = "action_insta_apply_page";
    private final String INSTA_APPLY_PAGE_OPENED = "insta_apply_page_opened";

    public WishFinAnalytics(Context context) {
        _context = context;
        init(_context);
    }

    @Override
    public void init(Context context) {
        if (firebaseAnalytics == null)
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void openApp() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, OPEN_APP);
        bundle.putString(ACTION_APP_OPENED, APP_OPENED);
        firebaseAnalytics.logEvent(OPEN_APP, bundle);
    }

    @Override
    public void cibilPage() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, OPEN_CIBIL_PAGE);
        bundle.putString(ACTION_CIBIL_PAGE_OPENED, CIBIL_PAGE_OPENED);
        firebaseAnalytics.logEvent(OPEN_CIBIL_PAGE, bundle);
    }

    @Override
    public void rateapp() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, RATE_APP);
        bundle.putString(ACTION_RATE_APP, RATED_APP);
        firebaseAnalytics.logEvent(RATE_APP, bundle);
    }


    @Override
    public void best_cc_page() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, BEST_CC_PAGE);
        bundle.putString(ACTION_BEST_CC_PAGE, BEST_CC_PAGE_OPENED);
        firebaseAnalytics.logEvent(BEST_CC_PAGE, bundle);
    }

    @Override
    public void best_reward_cc_page() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, BEST_REWARD_CC_PAGE);
        bundle.putString(ACTION_BEST_REWARD_CC_PAGE, BEST_REWARD_CC_PAGE_OPENED);
        firebaseAnalytics.logEvent(BEST_REWARD_CC_PAGE, bundle);

    }

    @Override
    public void lifetime_free_cc_page() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, LIFETIME_FREE_CC_PAGE);
        bundle.putString(ACTION_LIFETIME_FREE_CC_PAGE, LIFETIME_FREE_CC_PAGE_OPENED);
        firebaseAnalytics.logEvent(LIFETIME_FREE_CC_PAGE, bundle);

    }

    @Override
    public void best_travel_cc_page() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, BEST_TRAVEL_CC_PAGE);
        bundle.putString(ACTION_BEST_TRAVEL_CC_PAGE, BEST_TRAVEL_CC_PAGE_OPENED);
        firebaseAnalytics.logEvent(BEST_TRAVEL_CC_PAGE, bundle);

    }

    @Override
    public void best_fuel_cc_page() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, BEST_FUEL_CC_PAGE);
        bundle.putString(ACTION_BEST_FUEL_CC_PAGE, BEST_FUEL_CC_PAGE_OPENED);
        firebaseAnalytics.logEvent(BEST_FUEL_CC_PAGE, bundle);

    }

    @Override
    public void best_cashback_cc_page() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, BEST_CASHBACK_CC_PAGE);
        bundle.putString(ACTION_BEST_CASHBACK_CC_PAGE, BEST_CASHBACK_CC_PAGE_OPENED);
        firebaseAnalytics.logEvent(BEST_CASHBACK_CC_PAGE, bundle);

    }

    @Override
    public void special_offer_page() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, SPECIAL_OFFER_PAGE);
        bundle.putString(ACTION_SPECIAL_OFFER_PAGE, SPECIAL_OFFER_PAGE_OPENED);
        firebaseAnalytics.logEvent(SPECIAL_OFFER_PAGE, bundle);
    }


    @Override
    public void profilepage() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, PROFILE_PAGE);
        bundle.putString(ACTION_PROFILE_PAGE, PROFILE_PAGE_OPENED);
        firebaseAnalytics.logEvent(PROFILE_PAGE, bundle);
    }

    @Override
    public void history_page() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, HISTORY_PAGE);
        bundle.putString(ACTION_HISTORY_PAGE, HISTORY_PAGE_OPENED);
        firebaseAnalytics.logEvent(HISTORY_PAGE, bundle);

    }

    @Override
    public void insta_apply_clicked() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, INSTA_APPLY_PAGE);
        bundle.putString(ACTION_INSTA_APPLY_PAGE, INSTA_APPLY_PAGE_OPENED);
        firebaseAnalytics.logEvent(INSTA_APPLY_PAGE, bundle);

    }


}
