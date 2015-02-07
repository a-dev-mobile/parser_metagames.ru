package el917.rgames;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.material.widget.TabIndicator;

import java.util.Calendar;

import el917.rgames.biling.IabHelper;
import el917.rgames.biling.IabResult;
import el917.rgames.biling.Inventory;
import el917.rgames.biling.PreferencesHelper;
import el917.rgames.biling.Purchase;
import el917.rgames.utils.UtilsHelper;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends FragmentActivity {
    static TabIndicator indicator;
    ViewPager viewPager;
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvE32PHK5VPHLbK7QxZl0zT0pmtZoHMyHlTdu2NMW8wY5InlZf72oLHamh91fEDX7XwyKTBYVJzGlSQ2qxlsRb9TXTvZgv9PQCbCzt0gJs3iJsac8wjI/uLe8wqgn6fFJIZ9vgy7IoJbDzDr5vp+lPJiwfU5i0vGuUiTaNMC2vQ7zMguR+clKtUc3ZLeMn76JyFjq5HRUkvsxp5MKgUrT/wZZmHbRK5OOzsH4MFGp9ks0LyhxDsl2vAPicBfFRm71Xe93YX+VwEaA/XdvOZopc9JlyL25i5eNIEfKojcbuFF6JNydR7WJCvZtd/Gn1xIQxkIcxrX8pRwJA4onHTbuiwIDAQAB";


    private static final String TAG = "purchasefordisablingadvertising";
    // id вашей покупки из админки в Google Play
    static final String SKU_ADS_DISABLE = "remove.ads.banner";

    static final int RC_REQUEST = 10001;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        // грузим настройки
        PreferencesHelper.loadSettings(this);
        if (UtilsHelper.isOnline(context)) {
            viewPager = (ViewPager) findViewById(R.id.pager);
            adapter = new PagerAdapter(getSupportFragmentManager(),
                    getResources().getStringArray(R.array.console_name)
                    , getResources().getStringArray(R.array.console_url)
            );
            viewPager.setAdapter(adapter);
            indicator = (TabIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(viewPager);

        } else {
            final RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            final ImageView imageView = new ImageView(context);
            rootLayout.addView(imageView);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.not_internet);
            MaterialRippleLayout.on(imageView).rippleColor(Color.parseColor("#212B60"))
                    .rippleAlpha(0.2f)
                    .rippleHover(true)
                    .create();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UtilsHelper.isOnline(context)) {
                        viewPager = (ViewPager) findViewById(R.id.pager);
                        adapter = new PagerAdapter(getSupportFragmentManager(),
                                getResources().getStringArray(R.array.console_name)
                                , getResources().getStringArray(R.array.console_url)

                        );
                        viewPager.setAdapter(adapter);
                        indicator = (TabIndicator) findViewById(R.id.indicator);
                        indicator.setViewPager(viewPager);

                        imageView.setVisibility(View.GONE);
                        rootLayout.removeView(imageView);


                        // инициализация билинга
                    } else {
                        Toast.makeText(context, "Для работы программы необходим интернет", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        // инициализация билинга
      //  billingInit();


    }

    static IabHelper mHelper;

    private void startDialogAbout() {
        // Get app version
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "N/A";
        }
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle(Html.fromHtml(getString(R.string.app_name_and_version, versionName)));
        materialDialog.setMessage(Html.fromHtml(getString(R.string.about_body, Calendar.getInstance().get(Calendar.YEAR))));
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (loadBooleanFromFile()) {
            menu.findItem(R.id.buy).setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                startDialogAbout();

                return true;
       /*     case R.id.buy:

                if (!PreferencesHelper.isAdsDisabled()) {
                    buy();
                }else   startDialogBuy();
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buy() {
        if (!PreferencesHelper.isAdsDisabled()) {
            /*
             * для безопасности сгенерьте payload для верификации. В данном
			 * примере просто пустая строка юзается. Но в реальном приложение
			 * подходить к этому шагу с умом.
			 */
            String payload = "";
            mHelper.launchPurchaseFlow(this, SKU_ADS_DISABLE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        }
    }

    private void billingInit() {
        mHelper = new IabHelper(this, BASE64_PUBLIC_KEY);

        // включаем дебагинг (в релизной версии ОБЯЗАТЕЛЬНО выставьте в false)
        mHelper.enableDebugLogging(true);

        // инициализируем; запрос асинхронен
        // будет вызван, когда инициализация завершится
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    return;
                }

                // чекаем уже купленное
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }


    // Слушатель для востановителя покупок.
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        private static final String TAG = "QueryInventoryFinishedListener";

        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            LOG.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                LOG.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            LOG.d(TAG, "Query inventory was successful.");

			/*
             * Проверяются покупки. Обратите внимание, что надо проверить каждую
			 * покупку, чтобы убедиться, что всё норм! см.
			 * verifyDeveloperPayload().
			 */

            Purchase purchase = inventory.getPurchase(SKU_ADS_DISABLE);
            PreferencesHelper.savePurchase(context,
                    PreferencesHelper.Purchase.DISABLE_ADS, purchase != null
                            && verifyDeveloperPayload(purchase));
            //   isShow = (!PreferencesHelper.isAdsDisabled());

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            LOG.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        /*
         * TODO: здесь необходимо свою верификацию реализовать Хорошо бы ещё с
		 * использованием собственного стороннего сервера.
		 */

        return true;
    }

    // Прокает, когда покупка завершена
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            LOG.d(TAG, "Purchase finished: " + result + ", purchase: "
                    + purchase);
            if (result.isFailure()) {
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                return;
            }

            LOG.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_ADS_DISABLE)) {

                LOG.d(TAG, "Purchase for disabling ads done. Congratulating user.");
                //  Toast.makeText(getApplicationContext(), "Покупка для отключения рекламы - сделано.", Toast.LENGTH_SHORT).show();
                // сохраняем в настройках, что отключили рекламу
                startDialogBuy();
                PreferencesHelper.savePurchase(context, PreferencesHelper.Purchase.DISABLE_ADS, true);
                // отключаем рекламу
                // isShow = (!PreferencesHelper.isAdsDisabled());
            }

        }
    };

    private void startDialogBuy() {

        final MaterialDialog materialDialog = new MaterialDialog(this);

        materialDialog.setTitle(Html.fromHtml(getString(R.string.purchase_successfully_title)));
        materialDialog.setMessage(Html.fromHtml(getString(R.string.purchase_successfully_body)));
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.show();
    }


    public static boolean loadBooleanFromFile() {
        SharedPreferences sharedPreferences;
        boolean i = false;

        sharedPreferences = context.getSharedPreferences(PreferencesHelper.SETTINGS, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("TAG_DISABLED_ADS")) {
            i = sharedPreferences.getBoolean("TAG_DISABLED_ADS", false);
        }
        return i;
    }

    public static Context context;
    // This class makes the ad request and loads the ad.

    public static class AdMobController extends Fragment {
        private AdView ads;


        public AdMobController() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            LOG.d("TTT", String.valueOf(!loadBooleanFromFile()));
          /*  if (!loadBooleanFromFile()) {*/
                ads = (AdView) getView().findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
                // Start loading the ad in the background.
              //  ads.loadAd(adRequest);



        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }


        @Override
        public void onPause() {
            if (ads != null) {
                ads.pause();
            }
            super.onPause();
        }


        @Override
        public void onResume() {
            super.onResume();
            if (ads != null) {
                ads.resume();
            }
        }


        @Override
        public void onDestroy() {
            if (ads != null) {
                ads.destroy();
            }


            if (mHelper != null)
                mHelper.dispose();
            mHelper = null;
            super.onDestroy();
        }

    }


}


