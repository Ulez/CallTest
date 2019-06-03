package com.github.ulez.calltest;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by Ulez on 2019/6/3.
 * Email：1104128773@qq.com
 */
public class CallUtil {

    public static void makeCall(final Context context, String num) {
        // 首先拿到TelephonyManager
        try {
            TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;
            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            mthEndCall.setAccessible(true);
            Object obj = mthEndCall.invoke(telMag, (Object[]) null);
            Method mt = obj.getClass().getMethod("call", new Class[]{String.class, String.class});
            mt.setAccessible(true);
            mt.invoke(obj, new Object[]{context.getPackageName() + "", num});
            Toast.makeText(context, "拨打电话！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endCall(Context context) {
        try {
            TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;
            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            mthEndCall.setAccessible(true);
            Object obj = mthEndCall.invoke(telMag, (Object[]) null);
            Method mt = obj.getClass().getMethod("endCall");
            mt.setAccessible(true);
            mt.invoke(obj);
        } catch (Exception e) {
            Log.e("lcy", "end call error");
        }
    }

}
