package com.github.ulez.calltest;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {

    public static String TAG = "PhoneStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.d(TAG, "PhoneStateReceiver**Call State=" + state);
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG, "PhoneStateReceiver**Idle");
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Incoming call
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG, "PhoneStateReceiver**Incoming call " + incomingNumber);
                if (!killCall(context)) { // Using the method defined earlier
                    Log.d(TAG, "PhoneStateReceiver **Unable to kill incoming call");
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.d(TAG, "PhoneStateReceiver **Offhook");
            }
        } else if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            // Outgoing call
            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "PhoneStateReceiver **Outgoing call " + outgoingNumber);
            setResultData(null); // Kills the outgoing call
        } else {
            Log.d(TAG, "PhoneStateReceiver **unexpected intent.action=" + intent.getAction());
        }
    }

    public static boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);
            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);
            Log.e(TAG, "PhoneStateReceiver ** endCall");
        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.e(TAG, "PhoneStateReceiver **" + ex.getMessage());
            Log.e(TAG, "PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }

    public static void killCall2(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        Class clazz = Class.forName(telephonyManager.getClass().getName());
//        Method method = clazz.getDeclaredMethod("getITelephony");
//        method.setAccessible(true);
//        ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
//        telephonyService.endCall();
    }

}
