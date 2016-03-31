package cashin.scrapout.com.scrapout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserSession session;
    private Button proceed;
    Boolean is_ironSelected,is_paperSelected;
    int proceedButtonToggle;
    Intent order_Intent;
    ImageView meIron,mePaper;
    SharedPreferences sharedPreferences;
    String uName,uMNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new UserSession(getApplicationContext());
        if(!session.isUserLoggedIn()){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        is_ironSelected=false;
        is_paperSelected=false;
        proceedButtonToggle=0;
        uName="Hi There!";
        sharedPreferences = getApplicationContext().getSharedPreferences("UserLog", 0);
        // get editor to edit in file
        if (sharedPreferences.contains("Name"))
        {
            uName = sharedPreferences.getString("Name", "");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        meIron= (ImageView) findViewById(R.id.mIron);
        mePaper = (ImageView) findViewById(R.id.mPaper);
        proceed = (Button) findViewById(R.id.main_proceed);
        proceed.setAlpha(.5f);
        meIron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!is_ironSelected) {
                    meIron.setBackgroundResource(R.drawable.select_green);
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    is_ironSelected = !is_ironSelected;
                    proceedButtonToggle += 1;
                } else {
                    meIron.setBackgroundColor(0);
                    is_ironSelected = !is_ironSelected;
                    proceedButtonToggle -= 1;
                    if (proceedButtonToggle == 0)
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
        if(proceedButtonToggle!=0)
            proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        mePaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_paperSelected) {
                    mePaper.setBackgroundResource(R.drawable.select_green);
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    is_paperSelected = !is_paperSelected;
                    proceedButtonToggle+= 1;
                }
                else{
                    mePaper.setBackgroundColor(0);
                    is_paperSelected = !is_paperSelected;
                    proceedButtonToggle-= 1;
                    if(proceedButtonToggle==0)
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);


        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.nav_lay);
        TextView myName = (TextView) headerview.findViewById(R.id.userName);
        myName.setText(uName);

        if(header != null) {
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    }
                });}
        navigationView.setNavigationItemSelectedListener(this);
        order_Intent = new Intent(MainActivity.this,OrderActivity.class);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(proceedButtonToggle==0){
                    proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(getBaseContext(), "Please choose atleast one item...", Toast.LENGTH_LONG).show();
                }
                else{
                    if(is_ironSelected){
                        order_Intent.putExtra("iron","yes");
                    }
                    if(is_paperSelected){
                        order_Intent.putExtra("paper","yes");
                    }
                    startActivity(order_Intent);

                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, AboutActivity.class);

        if (id == R.id.home) {
        } else if (id == R.id.rateCard) {
            startActivity(intent);
        } else if (id == R.id.myOrders) {
            startActivity(intent);
        } else if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "ScrapOut, sell your scrap and get paid for it");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Pick One" ));
        } else if (id == R.id.feedBack) {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "rootviz@hotmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for ScrapOut");
            Email.putExtra(Intent.EXTRA_TEXT, "Dear ScrapOut Team," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
        } else if (id == R.id.aboutUs) {
            startActivity(intent);
        } else if (id == R.id.privacyPolicy) {
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
