package cashin.scrapout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import cashin.scrapout.fragments.OneFragment;
import cashin.scrapout.fragments.TwoFragment;
import cashin.scrapout.fragments.ThreeFragment;


public class OrdersHistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RequestParamsBuilder reqParamsBuilder;
    HttpMethods httpMethods;
    ProgressDialog dialog;
    Gson gson;
    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        reqParamsBuilder = new RequestParamsBuilder();
        store = new Store(getBaseContext());
        httpMethods = new HttpMethods();
        gson = new GsonBuilder().create();
        dialog = new ProgressDialog(OrdersHistoryActivity.this, R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading..");


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        getAllOrders();
    }

    private void getAllOrders() {
        dialog.show();
        String accessToken = store.get("accesstoken");
        reqParamsBuilder.addHeader("accesstoken", accessToken);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject response = httpMethods.get(reqParamsBuilder.getHeaderParams(), "/users/orders");
                    final int responseCode = Integer.parseInt(response.get("responseCode").toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            if(responseCode >= 200 && responseCode <= 300) {
                                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                                Orders all_orders = gson.fromJson(response.toString(), Orders.class);
                                //OneFragment active = new OneFragment();
                                adapter.addFragment(new OneFragment(all_orders), "Orders");
                                //adapter.addFragment(new TwoFragment(all_orders), "All Orders");
                                //adapter.addFragment(new ThreeFragment(all_orders), "Cancelled Orders");
                                viewPager.setAdapter(adapter);
                                tabLayout = (TabLayout) findViewById(R.id.tabs);
                                tabLayout.setupWithViewPager(viewPager);
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("sdad");
                }
            }
        });
        thread.start();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
