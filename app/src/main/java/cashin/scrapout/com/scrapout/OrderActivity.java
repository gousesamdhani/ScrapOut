package cashin.scrapout.com.scrapout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by sammy on 2/7/16.
 */
public class OrderActivity extends AppCompatActivity {

    EditText oIronQuantity,oPaperQuantity,oAddOne,oAddTwo,oPincode,oMobNumer,oUname;
    Button cancel,submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout pView,iView;
    Intent confirmIntent;
    String uName,uMNumber,iq,pq,aone,atwo,name,mNumber,pincode,iclicked,pclicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);
        oIronQuantity = (EditText) findViewById(R.id.iron_quantity);
        oPaperQuantity = (EditText) findViewById(R.id.paperQuantity);
        oAddOne = (EditText) findViewById(R.id.orderAone);
        oAddTwo = (EditText) findViewById(R.id.orderAtwo);
        oPincode = (EditText) findViewById(R.id.orderPincode);
        oMobNumer = (EditText) findViewById(R.id.oMNumber);
        cancel = (Button) findViewById(R.id.ocancel);
        submit = (Button) findViewById(R.id.oSubmit);
        oUname = (EditText) findViewById(R.id.orderUName);
        pView = (LinearLayout) findViewById(R.id.pView);
        iView = (LinearLayout) findViewById(R.id.iView);

        Bundle extras = getIntent().getExtras();
        iclicked = null;
        pclicked = null;
        if (extras != null) {
            Toast.makeText(getBaseContext(), "bundle", Toast.LENGTH_LONG).show();
            iclicked = extras.getString("iron");
            pclicked = extras.getString("paper");
            if(pclicked != null)
            {
                Toast.makeText(getBaseContext(), "iclicked", Toast.LENGTH_LONG).show();
                pView.setVisibility(View.VISIBLE);
            }
            if(iclicked != null)
            {
                Toast.makeText(getBaseContext(), "pclicked", Toast.LENGTH_LONG).show();
                iView.setVisibility(View.VISIBLE);
            }
        }

        confirmIntent = new Intent(this,ConfirmActivity.class);

        // creating an shared Preference file for the information to be stored
        // first argument is the name of file and second is the mode, 0 is private mode

        sharedPreferences = getApplicationContext().getSharedPreferences("UserLog", 0);
        // get editor to edit in file
        editor = sharedPreferences.edit();
        if (sharedPreferences.contains("Name"))
        {
            uName = sharedPreferences.getString("Name", "");
            oUname.setText(uName);
        }
        if (sharedPreferences.contains("mNumber"))
        {
            uMNumber = sharedPreferences.getString("mNumber", "");
            oMobNumer.setText(uMNumber);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    if(iclicked != null) {
                        editor.putString("iq", iq);
                    }
                    if(pclicked != null) {
                        editor.putString("pq", pq);
                    }
                    editor.putString("aone",aone);
                    editor.putString("atwo",atwo);
                    editor.putString("name",name);
                    editor.putString("mNumber",mNumber);
                    editor.putString("pincode",pincode);
                    editor.commit();

                    startActivity(confirmIntent);
                    finish();
                }
            }
        });
    }
    public boolean isValid()
    {boolean valid = true;
        if(iclicked != null) {
            iq = oIronQuantity.getText().toString();
        }
        if(pclicked != null) {
            pq = oPaperQuantity.getText().toString();
        }
        aone = oAddOne.getText().toString();
        atwo = oAddTwo.getText().toString();
        name =  oUname.getText().toString();
        mNumber =  oMobNumer.getText().toString();
        pincode =  oPincode.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            oUname.setError("at least 3 characters");
            valid = false;
        } else {
            oUname.setError(null);
        }

        if (aone.isEmpty() || aone.length() < 3) {
            oAddOne.setError("at least 3 characters");
            valid = false;
        } else {
            oAddOne.setError(null);
        }

        if (atwo.isEmpty() || atwo.length() < 3) {
            oAddTwo.setError("at least 3 characters");
            valid = false;
        } else {
            oAddTwo.setError(null);
        }

        if (mNumber.isEmpty() || mNumber.length() < 10  ) {
            oMobNumer.setError("wrong format");
            valid = false;
        } else {
            oMobNumer.setError(null);
        }

        if (pincode.isEmpty() || pincode.length() < 4  ) {
            oPincode.setError("wrong format");
            valid = false;
        } else {
            oPincode.setError(null);
        }

        if (iq.isEmpty() || iq.length() < 1  ) {
            oIronQuantity.setError("wrong format");
            valid = false;
        } else {
            if(Integer.parseInt(iq)<=3){
                oIronQuantity.setError("Value should be greater than 3");
            } else
            oIronQuantity.setError(null);
        }

        if (pq.isEmpty() || pq.length() < 1  ) {
                oPaperQuantity.setError("wrong format");
            valid = false;
        } else {
            if(Integer.parseInt(pq)<=3){
                oPaperQuantity.setError("Value should be greater than 3");
            } else
            oPaperQuantity.setError(null);
        }
        return valid;
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
