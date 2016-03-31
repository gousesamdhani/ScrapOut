package cashin.scrapout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by palisn on 2/7/16.
 */
public class OrderActivity extends AppCompatActivity {

    EditText oIronQuantity,oPaperQuantity,oPincode,oMobNumer,oUname,oSteelQuantity,oBooksQunatity,oCBQuanity, oAddress;
    Button cancel,submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Store store;
    Gson gson ;
    RelativeLayout pView,iView,sView,bView,cbView;
    ProgressDialog progressDialog;
    HttpMethods httpMethods;
    Intent confirmIntent;
    Spinner state;
    Spinner district;
    Spinner area;
    Districts districts;
    Areas areas;
    States states;
    String stateID;
    String districtID;
    String areaID;
    String uName,uMNumber,iq,pq,sq,bq,cbq,addrs,name,mNumber,pincode,iron_clicked,paper_clicked,steel_clicked,cb_clicked,books_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);
        oIronQuantity = (EditText) findViewById(R.id.iron_quantity);
        oPaperQuantity = (EditText) findViewById(R.id.paper_quantity);
        oSteelQuantity = (EditText) findViewById(R.id.steelQuantity);
        oBooksQunatity = (EditText) findViewById(R.id.books_quantity);
        oCBQuanity = (EditText) findViewById(R.id.cardBoard_quantity);
        oAddress = (EditText) findViewById(R.id.address);
        oPincode = (EditText) findViewById(R.id.orderPincode);
        oMobNumer = (EditText) findViewById(R.id.oMNumber);
        cancel = (Button) findViewById(R.id.ocancel);
        submit = (Button) findViewById(R.id.oSubmit);
        oUname = (EditText) findViewById(R.id.orderUName);
        pView = (RelativeLayout) findViewById(R.id.pView);
        iView = (RelativeLayout) findViewById(R.id.iView);
        sView = (RelativeLayout) findViewById(R.id.sView);
        bView = (RelativeLayout) findViewById(R.id.bView);
        cbView = (RelativeLayout) findViewById(R.id.cbView);
        state = (Spinner) findViewById(R.id.orderState);
        district = (Spinner) findViewById(R.id.orderDistrict);
        area = (Spinner) findViewById(R.id.orderArea);
        sharedPreferences = getApplicationContext().getSharedPreferences("UserLog", 0);
        editor = sharedPreferences.edit();
        store = new Store(getApplicationContext());
        httpMethods = new HttpMethods();
        gson = new GsonBuilder().create();
        progressDialog = new ProgressDialog(OrderActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading..");

        Bundle extras = getIntent().getExtras();
        iron_clicked = null;
        paper_clicked = null;
        steel_clicked = null;
        cb_clicked = null;
        books_clicked = null;
        if (extras != null) {
            iron_clicked = extras.getString("iron");
            steel_clicked = extras.getString("steel");
            paper_clicked = extras.getString("paper");
            cb_clicked = extras.getString("cardboard");
            books_clicked = extras.getString("books");
            Log.i("OrderActivity",iron_clicked);

            if(paper_clicked.equals("yes"))
            {
                store.get("paper");
                pView.setVisibility(View.VISIBLE);
            }
            if(iron_clicked.equals("yes"))
            {
                iView.setVisibility(View.VISIBLE);
            }
            if(steel_clicked.equals("yes"))
            {
                sView.setVisibility(View.VISIBLE);
            }
            if(books_clicked.equals("yes"))
            {
                bView.setVisibility(View.VISIBLE);
            }
            if(cb_clicked.equals("yes"))
            {
                cbView.setVisibility(View.VISIBLE);
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
        cancel.setBackgroundColor(Color.BLACK);
        cancel.setTextColor(Color.WHITE);
        submit.setBackgroundColor(Color.YELLOW);
        submit.setTextColor(Color.BLACK);

        loadStatesData();

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<StateInfo> statesInfo = states.getStatesInfo();
                StateInfo stateInfo = statesInfo.get(position);
                stateID = stateInfo.getId();
                loadDistrictsData(stateID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do Nothing
            }
        });

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<District> districtsInfo = districts.getDistrictsInfo();
                District districtInfo = districtsInfo.get(position);
                districtID = districtInfo.getId();
                loadAreasData(stateID, districtID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<AreaInfo> areasInfo = areas.getAreas();
                AreaInfo areaInfo = areasInfo.get(position);
                areaID = areaInfo.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    //send the order details to server and get the order ID
                    progressDialog.show();
                    final RequestParamsBuilder requestParamsBuilder = new RequestParamsBuilder();
                    requestParamsBuilder.addHeader("accesstoken", store.get("accesstoken"));
                    requestParamsBuilder.add("state_id", stateID);
                    requestParamsBuilder.add("district_id", districtID);
                    requestParamsBuilder.add("area_id", areaID);
                    requestParamsBuilder.add("phone_number", oMobNumer.getText().toString());
                    requestParamsBuilder.add("address", oAddress.getText().toString());
                    requestParamsBuilder.add("pin", oPincode.getText().toString());
                    requestParamsBuilder.add("materials", getOrderDetails());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject response = httpMethods.post(requestParamsBuilder.getReqParams(),requestParamsBuilder.getHeaderParams(),"/users/orders");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String order_number = "";
                                    try {
                                        int responseCode = Integer.parseInt(response.get("responseCode").toString());
                                        if(responseCode >= 200 && responseCode <=300) {
                                            progressDialog.dismiss();
                                            JSONObject data = response.getJSONObject("data");
                                            order_number = data.getString("order_number");
                                            confirmIntent.putExtra("order_number", order_number);
                                            startActivity(confirmIntent);
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Utils.displayToast(getBaseContext(), "Unable to take order details");
                                        }
                                    } catch(Exception e) {
                                        progressDialog.dismiss();
                                        Utils.displayToast(getBaseContext(), "Unable to take order");
                                        System.out.println("Something went wrong");
                                    }
                                }
                            });
                        }
                    });
                    thread.start();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private String getOrderDetails() {

        JSONArray orderDetails = new JSONArray();
        if(iron_clicked.equals("yes"))
            orderDetails.put(getDetails(1, oIronQuantity, "0"));
        if(paper_clicked.equals("yes"))
            orderDetails.put(getDetails(7, oPaperQuantity, "0"));
        if(steel_clicked.equals("yes"))
            orderDetails.put(getDetails(8, oSteelQuantity, "0"));
        if(books_clicked.equals("yes"))
            orderDetails.put(getDetails(9, oBooksQunatity, "0"));
        if(cb_clicked.equals("yes"))
            orderDetails.put(getDetails(10, oCBQuanity, "0"));
        return orderDetails.toString();
    }

    private JSONObject getDetails(int id, EditText materialQty, String price) {

        JSONObject details = new JSONObject();
        try {
            details.put("material_id", id);
            details.put("material_qty", materialQty.getText());
            details.put("price", price);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return details;
    }

    private void loadStatesData() {
        progressDialog.show();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(OrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final RequestParamsBuilder requestParamsBuilder = new RequestParamsBuilder();
        String acc = store.get("accesstoken");
        requestParamsBuilder.addHeader("accesstoken", store.get("accesstoken"));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = httpMethods.get(requestParamsBuilder.getHeaderParams(), "/users/states");
                    states = gson.fromJson(response.toString(), States.class);
                    ArrayList<StateInfo> statesInfo = states.getStatesInfo();
                    for(int i=0; i < statesInfo.size(); i++) {
                        spinnerAdapter.add(statesInfo.get(i).getStateName());
                    }
                    loadDistrictsData("1");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        state.setAdapter(spinnerAdapter);
                        progressDialog.dismiss();
                    }
                });
            }
        });
        thread.start();
    }

    private void loadDistrictsData(final String stateID) {
        progressDialog.show();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(OrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final RequestParamsBuilder requestParamsBuilder = new RequestParamsBuilder();
        String acc = store.get("accesstoken");
        requestParamsBuilder.addHeader("accesstoken", store.get("accesstoken"));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = httpMethods.get(requestParamsBuilder.getHeaderParams(), "/users/states/" + stateID + "/districts");
                    districts = gson.fromJson(response.toString(), Districts.class);
                    ArrayList<District> districtsInfo = districts.getDistrictsInfo();
                    for(int i=0; i < districtsInfo.size(); i++) {
                        spinnerAdapter.add(districtsInfo.get(i).getDistrict_name());
                    }
                    loadAreasData(stateID,"1");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        district.setAdapter(spinnerAdapter);
                        progressDialog.dismiss();
                    }
                });
            }
        });
        thread.start();
    }

    private void loadAreasData(final String stateID, final String districtID) {
        progressDialog.show();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(OrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final RequestParamsBuilder requestParamsBuilder = new RequestParamsBuilder();
        String acc = store.get("accesstoken");
        requestParamsBuilder.addHeader("accesstoken", store.get("accesstoken"));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = httpMethods.get(requestParamsBuilder.getHeaderParams(), "/users/states/" + stateID + "/districts/" + districtID + "/areas");
                    areas = gson.fromJson(response.toString(), Areas.class);
                    ArrayList<AreaInfo> areasInfo = areas.getAreas();
                    for(int i=0; i < areasInfo.size(); i++) {
                        spinnerAdapter.add(areasInfo.get(i).getArea_name());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        area.setAdapter(spinnerAdapter);
                        progressDialog.dismiss();
                    }
                });
            }
        });
        thread.start();
    }

    public boolean isValid()
    {
        boolean valid = true;
        if(paper_clicked.equals("yes"))
        {
            pq = oPaperQuantity.getText().toString();
            if (pq.isEmpty() || pq.length() < 1) {
                oPaperQuantity.setError("wrong format");
                valid = false;
            } else {
                if (Integer.parseInt(pq) <= 0) {
                    oPaperQuantity.setError("Value should be greater than 0");
                } else
                    oPaperQuantity.setError(null);
            }
        }
        if(iron_clicked.equals("yes"))
        {
            iq = oIronQuantity.getText().toString();
            if (iq.isEmpty() || iq.length() < 1) {
                oIronQuantity.setError("wrong format");
                valid = false;
            } else {
                if (Integer.parseInt(iq) <= 0) {
                    oIronQuantity.setError("Value should be greater than 0");
                } else
                    oIronQuantity.setError(null);
            }
        }
        if(steel_clicked.equals("yes"))
        {
            sq = oSteelQuantity.getText().toString();
            if (sq.isEmpty() || sq.length() < 1) {
                oSteelQuantity.setError("wrong format");
                valid = false;
            } else {
                if (Integer.parseInt(sq) <= 0) {
                    oSteelQuantity.setError("Value should be greater than 0");
                } else
                    oSteelQuantity.setError(null);
            }
        }
        if(books_clicked.equals("yes"))
        {
            bq = oBooksQunatity.getText().toString();
            if (bq.isEmpty() || bq.length() < 1) {
                oBooksQunatity.setError("wrong format");
                valid = false;
            } else {
                if (Integer.parseInt(bq) <= 0) {
                    oBooksQunatity.setError("Value should be greater than 0");
                } else
                    oBooksQunatity.setError(null);
            }
        }
        if(cb_clicked.equals("yes"))
        {
            cbq = oCBQuanity.getText().toString();
            if (cbq.isEmpty() || cbq.length() < 1) {
                oCBQuanity.setError("wrong format");
                valid = false;
            } else {
                if (Integer.parseInt(cbq) <= 0) {
                    oCBQuanity.setError("Value should be greater than 0");
                } else
                    oCBQuanity.setError(null);
            }
        }

        name =  oUname.getText().toString();
        mNumber =  oMobNumer.getText().toString();
        pincode =  oPincode.getText().toString();
        addrs = oAddress.getText().toString();
        if (name.isEmpty() || name.length() < 0) {
            oUname.setError("at least 0 characters");
            valid = false;
        } else {
            oUname.setError(null);
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

        if (addrs.isEmpty() || addrs.length() < 4  ) {
            oAddress.setError("Address cannot be empty");
            valid = false;
        } else {
            oAddress.setError(null);
        }

        if(state.getSelectedItem() == null) {
            Utils.displayToast(getBaseContext(), "Select State");
            return false;
        }

        if(district.getSelectedItem() == null) {
            Utils.displayToast(getBaseContext(), "Please select District");
            return false;
        }

        if(area.getSelectedItem() == null) {
            Utils.displayToast(getBaseContext(), "Select State");
            return false;
        }
        return valid;
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
