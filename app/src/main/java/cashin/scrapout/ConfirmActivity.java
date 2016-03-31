package cashin.scrapout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by palisn on 2/7/16.
 */
public class ConfirmActivity extends AppCompatActivity {

    TextView orderId;
    Intent mainIntent;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_page);
        extras = getIntent().getExtras();
        orderId = (TextView) findViewById(R.id.orderView);
        String order_number = extras.getString("order_number");
        orderId.setTextColor(Color.RED);
        orderId.setText("Your Order was confirmed with the Order ID : "+order_number);
        mainIntent = new Intent(this,MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        startActivity(mainIntent);
        finish();
        super.onBackPressed();
    }
}
