package com.abc.mxcloneshare.service;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;

import androidx.annotation.RequiresApi;

import com.abc.mxcloneshare.activity.ShareActivity;
import com.abc.mxcloneshare.db.AccessDatabase;
import com.abc.mxcloneshare.view.TextDrawable;
import com.abc.mxcloneshare.util.AppUtils;
import com.abc.mxcloneshare.model.NetworkDevice;
import com.genonbeta.android.database.SQLQuery;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DeviceChooserService extends ChooserTargetService
{
    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName targetActivityName, IntentFilter matchedFilter)
    {
        AccessDatabase database = AppUtils.getDatabase(getApplicationContext());
        List<ChooserTarget> list = new ArrayList<>();

        // use default accent color for light theme
        TextDrawable.IShapeBuilder iconBuilder = AppUtils.getDefaultIconBuilder(getApplicationContext());

        for (NetworkDevice device : database.castQuery(new SQLQuery.Select(AccessDatabase.TABLE_DEVICES), NetworkDevice.class)) {
            if (device.isLocalAddress)
                continue;

            Bundle bundle = new Bundle();

            bundle.putString(ShareActivity.EXTRA_DEVICE_ID, device.deviceId);

            TextDrawable textImage = iconBuilder.buildRound(device.nickname);
            Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            textImage.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            textImage.draw(canvas);

            float result = (float) device.lastUsageTime / (float) System.currentTimeMillis();

            list.add(new ChooserTarget(
                    device.nickname,
                    Icon.createWithBitmap(bitmap),
                    result,
                    targetActivityName,
                    bundle
            ));
        }

        return list;
    }
}
