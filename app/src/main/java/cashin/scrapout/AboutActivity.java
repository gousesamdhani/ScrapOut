package cashin.scrapout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by palisn on 2/7/16.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
