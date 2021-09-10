package com.abc.mxcloneshare.service;

import android.content.SharedPreferences;

import com.abc.mxcloneshare.util.AppUtils;
import com.abc.mxcloneshare.util.NotificationUtils;
import com.abc.mxcloneshare.db.AccessDatabase;

abstract public class Service extends android.app.Service
{
    private NotificationUtils mNotificationUtils;

    public AccessDatabase getDatabase()
    {
        return AppUtils.getDatabase(this);
    }

    public SharedPreferences getDefaultPreferences()
    {
        return AppUtils.getDefaultPreferences(getApplicationContext());
    }

    public NotificationUtils getNotificationUtils()
    {
        if (mNotificationUtils == null)
            mNotificationUtils = new NotificationUtils(getApplicationContext(), getDatabase(), getDefaultPreferences());

        return mNotificationUtils;
    }
}
