package com.wishfin_credit_card;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    private static String ACCESS_TOKEN = "access_token";
    private static String DEVICE_TOKEN = "device_token";
    private static String EXPIRE_IN = "expires_in";
    private static String TOKEN_TYPE = "token_type";
    private static String REFRESH_TOKEN = "refresh_token";
    private static String SECRET_KEY = "secret_key";
    private static String CIBIL_ID = "cibil_id";
    private static String CIBIL_SCORE = "cibil_score";
    private static String CIBIL_SCORE_CHECKED_OR_NOT = "cibil_score_checked_or_not";
    private static String CIBIL_FETCH_DATE = "cibil_date";
    private static String LOGIN = "login";
    private static String MASTER_USER_ID = "master_user_id";
    private static String MF_USER_ID = "mf_user_id";
    private static String FIRST_NAME = "first_name";
    private static String DOB = "dob";
    private static String PAN = "pan";
    private static String MOBILE = "mobile";
    private static String EMAILID = "emailid";
    private static String APPVERSION = "app_version";
    private static String LASTSMSDATE = "last_sms_date";
    private static String LOGINTYPE = "login_type";
    private static String MIDDLE_NAME = "middle_name";
    private static String LAST_NAME = "last_name";


    private static String APPINSTALLDATE = "app_install_date";
    private static String APPUPDATEDATE = "app_update_date";
    private static String STOREDAPPVERSION = "app_stored_version";

    private static String LATITUDE = "latitude";
    private static String LONGITUDE = "longitude";

    private static String ONTIMEPAYMENT = "ontime_payment";
    private static String CREDITUTILIZATION = "credit_utilization";
    private static String HARDINQUIRY = "hard_inquiry";

    private static String GOLD_USER_REGISTER_STATUS = "gold_user_register_status";
    private static String GOLD_USER_NAME = "gold_user_name";
    private static String GOLD_USER_ID = "gold_user_id";
    private static String GOLD_USER_TOKEN = "gold_user_token";
    private static String GOLD_USER_PINCODE = "gold_user_pincode";
    private static String GOLD_USER_STATE_CODE = "gold_user_state_code";
    private static String GOLD_USER_GENDER = "gold_user_gender";
    private static String GOLD_SALT = "gold_salt";
    private static String HEALTH_USER_ID = "health_user_id";
    private static String HEALTH_TOKEN = "health_token";
    private static String HEALTH_SALTKEY = "health_saltkey";
    private static String HEALTH_GENDER = "health_gender";
    private static String APP_TIME = "app_time";

    private static String UTMSOURCE = "utmsource";
    private static String UTMMEDIUM = "utmmedium";


    static void savePreference(SharedPreferences prefs, String key, Boolean value) {
        Editor e = prefs.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    static void savePreference(SharedPreferences prefs, String key, int value) {
        Editor e = prefs.edit();
        e.putInt(key, value);
        e.apply();
    }

    private static void savePreference(SharedPreferences prefs, String key, String value) {
        Editor e = prefs.edit();
        e.putString(key, value);
        e.apply();
    }

    static void dataclear(SharedPreferences prefs) {
        Editor e = prefs.edit();
        e.clear();
        e.apply();
    }


    static void save_access_token(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, ACCESS_TOKEN, value);
    }

    public static String get_access_token(SharedPreferences prefs) {
        return prefs.getString(ACCESS_TOKEN, "");
    }

    static void save_device_token(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, DEVICE_TOKEN, value);
    }

    public static String get_device_token(SharedPreferences prefs) {
        return prefs.getString(DEVICE_TOKEN, "");
    }

    static void save_expirein(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, EXPIRE_IN, value);
    }

    public static String get_expirein(SharedPreferences prefs) {
        return prefs.getString(EXPIRE_IN, "");
    }

    static void save_token_type(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, TOKEN_TYPE, value);
    }

    public static String get_token_type(SharedPreferences prefs) {
        return prefs.getString(TOKEN_TYPE, "");
    }

    static void save_refresh_token(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, REFRESH_TOKEN, value);
    }

    public static String get_refresh_token(SharedPreferences prefs) {
        return prefs.getString(REFRESH_TOKEN, "");
    }


    static void save_secret_key(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, SECRET_KEY, value);
    }

    static String get_secret_key(SharedPreferences prefs) {
        return prefs.getString(SECRET_KEY, "");
    }

    static void save_cibil_id(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, CIBIL_ID, value);
    }

    public static String get_cibil_id(SharedPreferences prefs) {
        return prefs.getString(CIBIL_ID, "");
    }

    static void save_cibil_score(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, CIBIL_SCORE, value);
    }

    static String get_cibil_score(SharedPreferences prefs) {
        return prefs.getString(CIBIL_SCORE, "");
    }

    static void save_cibil_fetch_date(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, CIBIL_FETCH_DATE, value);
    }

    static String get_cibil_fetch_date(SharedPreferences prefs) {
        return prefs.getString(CIBIL_FETCH_DATE, "");
    }

    static void save_login(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, LOGIN, value);
    }

    static String get_login(SharedPreferences prefs) {
        return prefs.getString(LOGIN, "");
    }

    static void save_masteruserid(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, MASTER_USER_ID, value);
    }

    static String get_masteruserid(SharedPreferences prefs) {
        return prefs.getString(MASTER_USER_ID, "");
    }


    static void save_mfuserid(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, MF_USER_ID, value);
    }

    static String get_mfuserid(SharedPreferences prefs) {
        return prefs.getString(MF_USER_ID, "");
    }

    static void save_firstname(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, FIRST_NAME, value);
    }

    public static String get_firstname(SharedPreferences prefs) {
        return prefs.getString(FIRST_NAME, "");
    }

//    static void save_lastname(SharedPreferences prefs, String value) {
//        com.wishfin_credit_card.SessionManager.savePreference(prefs, LAST_NAME, value);
//    }
//
//    public static String get_lastname(SharedPreferences prefs) {
//        return prefs.getString(LAST_NAME, "");
//    }

    static void save_dob(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, DOB, value);
    }

    static String get_dob(SharedPreferences prefs) {
        return prefs.getString(DOB, "");
    }

    static void save_pan(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, PAN, value);
    }

    static String get_pan(SharedPreferences prefs) {
        return prefs.getString(PAN, "");
    }

    static void save_mobile(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, MOBILE, value);
    }

    public static String get_mobile(SharedPreferences prefs) {
        return prefs.getString(MOBILE, "");
    }

    static void save_emailid(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, EMAILID, value);
    }

    public static String get_emailid(SharedPreferences prefs) {
        return prefs.getString(EMAILID, "");
    }

//    static void save_mname(SharedPreferences prefs, String value) {
//        com.wishfin_credit_card.SessionManager.savePreference(prefs, MIDDLE_NAME, value);
//    }
//
//    public static String get_mname(SharedPreferences prefs) {
//        return prefs.getString(MIDDLE_NAME, "");
//    }

    static void save_appversion(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, APPVERSION, value);
    }

    static String get_appversion(SharedPreferences prefs) {
        return prefs.getString(APPVERSION, "");
    }

    static void save_ontimepaymnet(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, ONTIMEPAYMENT, value);
    }

    static String get_ontimepayment(SharedPreferences prefs) {
        return prefs.getString(ONTIMEPAYMENT, "");
    }

    static void save_creditutilization(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, CREDITUTILIZATION, value);
    }

    static String get_creditutilization(SharedPreferences prefs) {
        return prefs.getString(CREDITUTILIZATION, "");
    }

    static void save_hardinquiry(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, HARDINQUIRY, value);
    }

    static String get_hardinquiry(SharedPreferences prefs) {
        return prefs.getString(HARDINQUIRY, "");
    }

    static void save_latitude(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, LATITUDE, value);
    }

    static String get_latitude(SharedPreferences prefs) {
        return prefs.getString(LATITUDE, "");
    }

    static void save_longitude(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, LONGITUDE, value);
    }

    static String get_longitude(SharedPreferences prefs) {
        return prefs.getString(LONGITUDE, "");
    }

    static void save_lastccapplydate(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, LASTSMSDATE, value);
    }

    static String get_lastccapplydate(SharedPreferences prefs) {
        return prefs.getString(LASTSMSDATE, "");
    }

    static void save_lead_id(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, APPINSTALLDATE, value);
    }

    static String get_lead_id(SharedPreferences prefs) {
        return prefs.getString(APPINSTALLDATE, "");
    }

    static void save_appupdatedate(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, APPUPDATEDATE, value);
    }

    static String get_appupdatedate(SharedPreferences prefs) {
        return prefs.getString(APPUPDATEDATE, "");
    }

    static void save_storedappversion(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, STOREDAPPVERSION, value);
    }

    static String get_storedappversion(SharedPreferences prefs) {
        return prefs.getString(STOREDAPPVERSION, "");
    }

//////////////////////////////////////////////


    public static void save_gold_user_name(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_NAME, value);
    }

    public static String get_gold_user_name(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_NAME, "");
    }


    public static void save_gold_user_id(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_ID, value);
    }

    public static String get_gold_user_id(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_ID, "");
    }


    public static void save_gold_user_token(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_TOKEN, value);
    }

    public static String get_gold_user_token(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_TOKEN, "");
    }


    public static void save_gold_user_pincode(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_PINCODE, value);
    }

    public static String get_gold_user_pincode(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_PINCODE, "");
    }

    public static void save_gold_user_register_status(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_REGISTER_STATUS, value);
    }

    public static String get_gold_user_register_status(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_REGISTER_STATUS, "");
    }

    public static void save_gold_user_state_code(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_STATE_CODE, value);
    }

    public static String get_gold_user_state_code(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_STATE_CODE, "");
    }

    public static void save_gold_user_gender(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_USER_GENDER, value);
    }

    public static String get_gold_user_gender(SharedPreferences prefs) {
        return prefs.getString(GOLD_USER_GENDER, "");
    }

    public static void save_gold_salt(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, GOLD_SALT, value);
    }

    public static String get_gold_salt(SharedPreferences prefs) {
        return prefs.getString(GOLD_SALT, "");
    }

    public static void save_health_user_id(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, HEALTH_USER_ID, value);
    }

    public static String get_health_user_id(SharedPreferences prefs) {
        return prefs.getString(HEALTH_USER_ID, "");
    }

    public static void save_health_token(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, HEALTH_TOKEN, value);
    }

    public static String get_health_token(SharedPreferences prefs) {
        return prefs.getString(HEALTH_TOKEN, "");
    }

    public static void save_health_salt_key(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, HEALTH_SALTKEY, value);
    }

    public static String get_health_salt_key(SharedPreferences prefs) {
        return prefs.getString(HEALTH_SALTKEY, "");
    }

    public static void save_health_gender(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, HEALTH_GENDER, value);
    }

    public static String get_health_gedner(SharedPreferences prefs) {
        return prefs.getString(HEALTH_GENDER, "");
    }

    public static void save_utmsource(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, UTMSOURCE, value);
    }

    public static String get_utmsource(SharedPreferences prefs) {
        return prefs.getString(UTMSOURCE, "");
    }

    public static void save_utmmedium(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, UTMMEDIUM, value);
    }

    public static String get_utmmedium(SharedPreferences prefs) {
        return prefs.getString(UTMMEDIUM, "");
    }

    public static void save_logintype(SharedPreferences prefs, String value) {
        com.wishfin_credit_card.SessionManager.savePreference(prefs, LOGINTYPE, value);
    }

    public static String get_logintype(SharedPreferences prefs) {
        return prefs.getString(LOGINTYPE, "");
    }

    static void save_mname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, MIDDLE_NAME, value);
    }

    public static String get_mname(SharedPreferences prefs) {
        return prefs.getString(MIDDLE_NAME, "");
    }

    static void save_lastname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, LAST_NAME, value);
    }

    public static String get_lastname(SharedPreferences prefs) {
        return prefs.getString(LAST_NAME, "");
    }

    static void save_cibil_checked_status(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, CIBIL_SCORE_CHECKED_OR_NOT, value);
    }

    public static String get_cibil_checked_status(SharedPreferences prefs) {
        return prefs.getString(CIBIL_SCORE_CHECKED_OR_NOT, "");
    }
    static void save_app_time(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, APP_TIME, value);
    }

    public static String get_app_time(SharedPreferences prefs) {
        return prefs.getString(APP_TIME, "");
    }


}