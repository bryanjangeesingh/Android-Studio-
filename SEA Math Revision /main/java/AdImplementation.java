import android.app.Application;

import com.brytech.seamathrevisiontnt.R;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdImplementation extends Application {

    InterstitialAd interstitialAd;

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, getString(R.string.Admob_app_id));

    }
}
