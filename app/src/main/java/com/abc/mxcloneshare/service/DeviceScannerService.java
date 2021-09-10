package com.abc.mxcloneshare.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.abc.mxcloneshare.base.AppConfig;
import com.abc.mxcloneshare.util.AddressedInterface;
import com.abc.mxcloneshare.util.AppUtils;
import com.abc.mxcloneshare.util.NetworkDeviceLoader;
import com.abc.mxcloneshare.util.NetworkDeviceScanner;
import com.abc.mxcloneshare.util.NetworkUtils;
import com.abc.mxcloneshare.model.NetworkDevice;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class DeviceScannerService extends Service implements NetworkDeviceScanner.ScannerHandler
{
    public static final String ACTION_SCAN_DEVICES = "com.abc.mxcloneshare.intent.action.SCAN_DEVICES";
    public static final String ACTION_SCAN_STARTED = "com.abc.mxcloneshare.intent.action.SCAN_STARTED";
    public static final String ACTION_DEVICE_SCAN_COMPLETED = "com.abc.mxcloneshare.intent.action.DEVICE_SCAN_COMPLETED";

    public static final String EXTRA_SCAN_STATUS = "com.abc.mxcloneshare.intent.extra.SCAN_STATUS";

    public static final String STATUS_OK = "com.abc.mxcloneshare.intent.status.OK";
    public static final String STATUS_NO_NETWORK_INTERFACE = "com.abc.mxcloneshare.intent.status.NO_NETWORK_INTERFACE";
    public static final String SCANNER_NOT_AVAILABLE = "com.abc.mxcloneshare.intent.status.SCANNER_NOT_AVAILABLE";

    private static NetworkDeviceScanner mDeviceScanner = new NetworkDeviceScanner();

    public static NetworkDeviceScanner getDeviceScanner()
    {
        return mDeviceScanner;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        if (intent != null && AppUtils.checkRunningConditions(this))
            if (ACTION_SCAN_DEVICES.equals(intent.getAction())) {
                String result = SCANNER_NOT_AVAILABLE;

                if (mDeviceScanner.isScannerAvailable()) {
                    List<AddressedInterface> interfaceList = NetworkUtils.getInterfaces(true, AppConfig.DEFAULT_DISABLED_INTERFACES);

                    NetworkDevice localDevice = AppUtils.getLocalDevice(getApplicationContext());
                    getDatabase().publish(localDevice);

                    for (AddressedInterface addressedInterface : interfaceList) {
                        NetworkDevice.Connection connection = new NetworkDevice.Connection(addressedInterface.getNetworkInterface().getDisplayName(), addressedInterface.getAssociatedAddress(), localDevice.deviceId, System.currentTimeMillis());
                        getDatabase().publish(connection);
                    }

                    result = mDeviceScanner.scan(interfaceList, this) ? STATUS_OK : STATUS_NO_NETWORK_INTERFACE;
                }

                getApplicationContext().sendBroadcast(new Intent(ACTION_SCAN_STARTED).putExtra(EXTRA_SCAN_STATUS, result));

                return START_STICKY;
            }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDeviceFound(InetAddress address, NetworkInterface networkInterface)
    {
        NetworkDevice.Connection connection = new NetworkDevice.Connection(networkInterface.getDisplayName(), address.getHostAddress(), "-", System.currentTimeMillis());
        getDatabase().publish(connection);

        NetworkDeviceLoader.load(getDatabase(), address.getHostAddress(), null);
    }

    @Override
    public void onThreadsCompleted()
    {
        getApplicationContext().sendBroadcast(new Intent(ACTION_DEVICE_SCAN_COMPLETED));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getDeviceScanner().interrupt();
    }
}
