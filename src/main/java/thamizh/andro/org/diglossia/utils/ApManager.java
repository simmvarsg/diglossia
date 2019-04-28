package thamizh.andro.org.diglossia.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

public class ApManager
{
	private final WifiManager mWifiManager;
	  private Context context;
//check whether wifi hotspot on or off
	  public ApManager(Context context) {
		    this.context = context;
		    mWifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
		  }
public boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
    try {
      if (enabled) { // disable WiFi in any case
        mWifiManager.setWifiEnabled(false);
      }
      if(mWifiManager.getConfiguredNetworks().contains(wifiConfig.SSID)){
    	  int netId = mWifiManager.getConfiguredNetworks().get(mWifiManager.getConfiguredNetworks().indexOf(wifiConfig.SSID)).networkId;
    	  return mWifiManager.enableNetwork(netId, true);
      }
      else{
    	  mWifiManager.addNetwork(wifiConfig);
    	  Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
    	  return (Boolean) method.invoke(mWifiManager, wifiConfig, enabled);
      }
    } catch (Exception e) {
      Log.e(this.getClass().toString(), "", e);
      return false;
    }
  }
    public boolean setWifiEnabled(boolean enabled){
        WifiConfiguration netConfig = new WifiConfiguration();

        netConfig.SSID = "2GOAP";
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        try {
            Method setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled",  WifiConfiguration.class, boolean.class);
            boolean apstatus=(Boolean) setWifiApMethod.invoke(mWifiManager, netConfig,true);

            Method isWifiApEnabledmethod = mWifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(mWifiManager)){};
            Method getWifiApStateMethod = mWifiManager.getClass().getMethod("getWifiApState");
            int apstate=(Integer)getWifiApStateMethod.invoke(mWifiManager);
            Method getWifiApConfigurationMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(mWifiManager);
            Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
            return false;
        }
    }

}
