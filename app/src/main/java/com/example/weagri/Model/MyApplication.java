package com.example.weagri.Model;

import android.app.Application;

import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
//        MobilistenActivityLifecycleCallbacks.register(this);
        super.onCreate();

        InitConfig initConfig = new InitConfig();
//        initConfig.setFont(Fonts.REGULAR, );

        ZohoSalesIQ.init(this, "q%2FetVzn%2B7suBUISHrv%2B4MAcJ28PTqq9c8NCG12tbTKEPyGbNr2VZjxcURpltcVF97toxu8fzIm4%3D_in", "VXYedrQX8SnJI7EFCpu01dOAbrxweRFJ9LoYwisslnQwtg7o2gI5TDmhdHf%2BpskotIQR5eZeZIfAQHqtrJtg%2Fvrhon6CEgZuOhnVA8woxXjqz2ZMOkOvsw%3D%3D", initConfig, new InitListener() {
            @Override
            public void onInitSuccess() {
                ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS);
            }

            @Override
            public void onInitError(int errorCode, String errorMessage) {
                //your code
            }
        });

    }
}