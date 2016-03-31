package cashin.scrapout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private UserSession session;
    private Button proceed;
    private Boolean is_ironSelected,is_paperSelected,is_steelSelected,is_booksSelected,is_cardBoardsSelected;
    private int proceedButtonToggle;
    Intent order_Intent;
    private ImageView meIron,mePaper;
    private SharedPreferences sharedPreferences;
    private String uName,uMNumber;
    private LinearLayout contentLayout;
    private Display display;
    private Point size;
    private static final int Iron=1,Steel=2,Paper=3,Books=4,CardBoards=5,More=6;

    private int imageNames[] = {
            R.drawable.iron_icon,
            R.drawable.steel_icon,
            R.drawable.paper_icon,
            R.drawable.books_icon,
            R.drawable.cardboard_icon,
            -1
    };

    private String name[] = {
            "Iron",
            "Steel",
            "Paper",
            "Books",
            "Cardboard",
            "Watch this Space!! " +"\n"+
                    "More will Come Here"
    };

    private boolean isSelected[] = {
            true,true,true,true,true,true
    };

    private ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();

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

        //Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Side Navigation User Page
        View headerview = navigationView.getHeaderView(0);
        RelativeLayout header = (RelativeLayout) headerview.findViewById(R.id.nav_lay);
        TextView myName = (TextView) headerview.findViewById(R.id.userName);
        myName.setText(uName);
        if(header != null) {
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        display = this.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        //Below two variables are for proceed button toggle feature
        is_ironSelected=false;
        is_paperSelected=false;
        is_booksSelected=false;
        is_steelSelected=false;
        is_cardBoardsSelected=false;
        proceedButtonToggle=0;

        uName="Hi There!";
        //Get UserName if any
        sharedPreferences = getApplicationContext().getSharedPreferences("UserLog", 0);
        if (sharedPreferences.contains("Name"))
        {
            uName = sharedPreferences.getString("Name", "");
        }

        //generateView();
        populateChallengesLayout();

        proceed = (Button) findViewById(R.id.main_proceed);
        //proceed.setAlpha(.5f);

        //Grey out Proceed Button
        if(proceedButtonToggle!=0)
            proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

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
                    else
                        order_Intent.putExtra("iron","no");
                    if(is_paperSelected){
                        order_Intent.putExtra("paper","yes");
                    }
                    else
                        order_Intent.putExtra("paper","no");
                    if(is_booksSelected){
                        order_Intent.putExtra("books","yes");
                    }
                    else
                        order_Intent.putExtra("books","no");
                    if(is_cardBoardsSelected){
                        order_Intent.putExtra("cardboard","yes");
                    }
                    else
                        order_Intent.putExtra("cardboard","no");
                    if(is_steelSelected){
                        order_Intent.putExtra("steel","yes");
                    }
                    else
                        order_Intent.putExtra("steel","no");
                    startActivity(order_Intent);
                }
            }
        });

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        Intent ordersHistoryIntent = new Intent(this, OrdersHistoryActivity.class);
        if (id == R.id.home) {
        } else if (id == R.id.rateCard) {
            startActivity(aboutIntent);
        } else if (id == R.id.myOrders) {
            startActivity(ordersHistoryIntent);
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
            startActivity(aboutIntent);
        } else if (id == R.id.privacyPolicy) {
            startActivity(aboutIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void populateChallengesLayout(){

        LinearLayout content_lay = (LinearLayout) findViewById(R.id.content_lay);

        content_lay.setOrientation(LinearLayout.VERTICAL);

        boolean isLeft = true;

        LinearLayout.LayoutParams horizontal_layout_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //horizontal_layout_lp.rightMargin = 3 * size.x / 375;
        //horizontal_layout_lp.leftMargin = 3 *size.x / 375;
        horizontal_layout_lp.topMargin = 4 * size.x / 375;

        for(int i = 0 ; i < 6 ; i++){
            OrderItem orderItem = new OrderItem();
            orderItem.name = name[i];
            orderItem.imageName = imageNames[i];
            orderItem.isSelected = isSelected[i];
            orderItems.add(orderItem);
        }

        System.out.println(orderItems.size());
        for(int i = 0 ; i < orderItems.size() ;){
            LinearLayout horizontal_layout = new LinearLayout(MainActivity.this.getApplicationContext());
            horizontal_layout.setOrientation(LinearLayout.HORIZONTAL);
            horizontal_layout.setGravity(Gravity.CENTER);
            horizontal_layout.setLayoutParams(horizontal_layout_lp);

            OrderItem orderItem = orderItems.get(i);
            RelativeLayout grid_item_left = populateGridItem(i, orderItem.name, orderItem.imageName, isLeft);
            grid_item_left.setId(i + 1);
            grid_item_left.setOnClickListener(this);
            i++;
            horizontal_layout.addView(grid_item_left);
            if(orderItems.size()==i){
                content_lay.addView(horizontal_layout);
                return;
            }
            isLeft = true^isLeft;
            orderItem = orderItems.get(i);
            RelativeLayout grid_item_right = populateGridItem(i, orderItem.name, orderItem.imageName,  isLeft);

            grid_item_right.setId(i+1);
            grid_item_right.setOnClickListener(this);
            i++;
            horizontal_layout.addView(grid_item_right);
            isLeft = true^isLeft;
            content_lay.addView(horizontal_layout);
        }
    }

    public RelativeLayout populateGridItem(int imageId, String desc, int image, boolean isLeft){
        RelativeLayout grid_item = new RelativeLayout(MainActivity.this.getApplicationContext());
        LinearLayout.LayoutParams grid_item_lp = new LinearLayout.LayoutParams((186 * size.x)/375, LinearLayout.LayoutParams.WRAP_CONTENT);
//        grid_item_lp.gravity = Gravity.CENTER;

        if(isLeft)
            grid_item_lp.rightMargin = 2 * size.x / 375;
        else
            grid_item_lp.leftMargin = 2 * size.x / 375;

        grid_item.setLayoutParams(grid_item_lp);
        //grid_item.setOrientation(LinearLayout.VERTICAL);
        //grid_item.setGravity(Gravity.CENTER);
        if(image != -1)
        grid_item.setBackgroundColor(Color.parseColor("#ffffff"));

        //TextView date_text = getTextView(date, 16f, "#557AC1", proxima_light);
        RelativeLayout.LayoutParams orderImageIdLP = new RelativeLayout.LayoutParams((186 * size.x)/375,RelativeLayout.LayoutParams.WRAP_CONTENT);
        orderImageIdLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        orderImageIdLP.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        orderImageIdLP.setMargins(10, 0, 10, 0);
        orderImageIdLP.topMargin = -100*size.y/720;
        orderImageIdLP.bottomMargin = -50*size.y/720;
        ImageView orderImage = new ImageView(MainActivity.this.getApplicationContext());
        orderImage.setId(imageId);

        if(image > -1) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), image);
            orderImage.setImageBitmap(bitmap);
        }

        //orderImage.setBackgroundResource((int)resid);
        //measuredHeight = orderImage.getDrawable().getIntrinsicHeight();
        //measuredWidth = orderImage.getDrawable().getIntrinsicWidth();

        orderImage.setLayoutParams(orderImageIdLP);

        RelativeLayout.LayoutParams desc_text_lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        desc_text_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        desc_text_lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        desc_text_lp.bottomMargin = 30;
        if(size.x < 500 && desc.length() > 20)
            desc_text_lp.bottomMargin = 10;
        TextView desc_text =getTextView(desc, 18f);
        desc_text.setTextColor(Color.BLACK);
        desc_text.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        desc_text.setLayoutParams(desc_text_lp);
        grid_item.addView(orderImage);
        grid_item.addView(desc_text);

        return grid_item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case Steel:
                if (!is_steelSelected) {
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    proceed.setTextColor(Color.BLACK);
                    is_steelSelected = !is_steelSelected;
                    proceedButtonToggle += 1;
                    v.setBackgroundColor(Color.parseColor("#006400"));
                } else {
                    is_steelSelected = !is_steelSelected;
                    proceedButtonToggle -= 1;
                    if (proceedButtonToggle == 0){
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                        proceed.setTextColor(Color.WHITE);}
                    v.setBackgroundColor(Color.WHITE);
                }
                break;
            case Iron:
                if (!is_ironSelected) {
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    proceed.setTextColor(Color.BLACK);
                    is_ironSelected = !is_ironSelected;
                    proceedButtonToggle += 1;
                    v.setBackgroundColor(Color.parseColor("#006400"));
                } else {
                    is_ironSelected = !is_ironSelected;
                    proceedButtonToggle -= 1;
                    if (proceedButtonToggle == 0){
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                        proceed.setTextColor(Color.WHITE);}
                    v.setBackgroundColor(Color.WHITE);

                }
                break;
            case Paper:
                if (!is_paperSelected) {
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    proceed.setTextColor(Color.BLACK);
                    proceed.setTextColor(Color.BLACK);
                    is_paperSelected = !is_paperSelected;
                    proceedButtonToggle += 1;
                    v.setBackgroundColor(Color.parseColor("#006400"));
                } else {
                    is_paperSelected = !is_paperSelected;
                    proceedButtonToggle -= 1;
                    if (proceedButtonToggle == 0){
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                        proceed.setTextColor(Color.WHITE);}
                    v.setBackgroundColor(Color.WHITE);
                }
                break;
            case Books:
                if (!is_booksSelected) {
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    proceed.setTextColor(Color.BLACK);
                    is_booksSelected = !is_booksSelected;
                    proceedButtonToggle += 1;
                    v.setBackgroundColor(Color.parseColor("#006400"));
                } else {
                    is_booksSelected = !is_booksSelected;
                    proceedButtonToggle -= 1;
                    if (proceedButtonToggle == 0){
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                        proceed.setTextColor(Color.WHITE);}
                    v.setBackgroundColor(Color.WHITE);
                }
                break;
            case CardBoards:
                if (!is_cardBoardsSelected) {
                    proceed.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    proceed.setTextColor(Color.BLACK);
                    is_cardBoardsSelected = !is_cardBoardsSelected;
                    proceedButtonToggle += 1;
                    v.setBackgroundColor(Color.parseColor("#006400"));
                } else {
                    is_cardBoardsSelected = !is_cardBoardsSelected;
                    proceedButtonToggle -= 1;
                    if (proceedButtonToggle == 0){
                        proceed.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                        proceed.setTextColor(Color.WHITE);}
                    v.setBackgroundColor(Color.WHITE);
                }
                break;
            default:
                break;
        }

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

    public TextView getTextView(String text, float textSize){
        TextView textView = new TextView(MainActivity.this);
        textView.setText(text);
        textView.setTextSize(textSize);
        return textView;
    }
}
