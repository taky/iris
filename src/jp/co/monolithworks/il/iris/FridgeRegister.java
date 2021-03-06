package jp.co.monolithworks.il.iris;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;

public class FridgeRegister extends Application {
	
    private static Context mContext;
    private static Map<String, Object> mState;
    private static int mListPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mState = new HashMap<String, Object>();
        mListPosition = -1;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Map<String, Object> getState() {
        return mState;
    }
    
    public static int getListPosition(){
        return mListPosition; 
    }
    
    public static void setListPosition(int position){
        mListPosition = position;
    }
}
