package cashin.scrapout.com.scrapout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;

/**
 * Created by palisn on 2/7/16.
 */
public class ConfirmActivity extends AppCompatActivity {
    TextView orderId;
    Intent mainIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_page);
        orderId = (TextView) findViewById(R.id.orderView);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        orderId.setText("OD"+hour);

        mainIntent = new Intent(this,MainActivity.class);
        
    }
    @Override
    public void onBackPressed() {
        startActivity(mainIntent);
        finish();
        super.onBackPressed();

    }
}
