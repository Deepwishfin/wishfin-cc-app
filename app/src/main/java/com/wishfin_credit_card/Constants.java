package com.wishfin_credit_card;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class Constants {

    static String tempplace = "";
    static String templat = "";
    static String templong = "";
    public static String cardresponse = "";
    public static String loanresponse = "";
    static String ontimepaymentresponse = "";
    static String creditutilizationresponse = "";
    static String hardinquiryresponse = "";
    public static String hashkey = "";
    static int last6month = 0, hardcount = 0;
    static String apiresponse = "";
    static String refreshclick = "";
    static String updateavailable = "";
    static long apihittime = 0;
    static ArrayList<String> xValsa1;
    //    static ArrayList<Entry> yValsa1;
    static ArrayList<CibilQuestionanswergetset> multipleinput_question_list;
    static HashMap<String, ArrayList<CibilQuestionanswergetset>> radio_option_list;
    static List<String> radio_question_list;


    static String NetwordDetect(Context mcontext) {

        boolean WIFI = false;

        boolean MOBILE = false;

        String IPaddress = "";

        ConnectivityManager CM = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = new NetworkInfo[0];
        if (CM != null) {
            networkInfo = CM.getAllNetworkInfo();
        }

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI) {
            IPaddress = GetDeviceipWiFiData(mcontext);

        }

        if (MOBILE) {

            IPaddress = GetDeviceipMobileData();

        }

        return IPaddress;

    }

    private static String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    private static String GetDeviceipWiFiData(Context mcontext) {

        WifiManager wm = (WifiManager) mcontext.getApplicationContext().getSystemService(WIFI_SERVICE);

        String ip = null;
        if (wm != null) {
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        }

        return ip;

    }
}
