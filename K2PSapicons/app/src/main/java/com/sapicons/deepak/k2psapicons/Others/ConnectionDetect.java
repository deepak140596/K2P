package com.sapicons.deepak.k2psapicons.Others;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetect {

    Context context;

    public ConnectionDetect(Context context){
        this.context=context;
    }
    public boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service
                .CONNECTIVITY_SERVICE);

        if (connectivity!=null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState()== NetworkInfo.State.CONNECTED){
                    return true;
                }

            }
        }
        return false;
    }
}
