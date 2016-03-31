package cashin.scrapout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText _nameText;
    private EditText _emailText;
    private EditText _passwordText;
    private EditText _mobileNumber;
    private Button _signupButton;
    private TextView _loginLink;
    private HttpMethods httpMethods;
    private RequestParamsBuilder reqParamsBuilder;
    SharedPreferences sharedPreferences;
    Editor editor;
    UserSession session;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        reqParamsBuilder = new RequestParamsBuilder();
        httpMethods = new HttpMethods();
        session = new UserSession(getApplicationContext());
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _mobileNumber = (EditText) findViewById(R.id.input_number);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        // creating an shared Preference file for the information to be stored
        // first argument is the name of file and second is the mode, 0 is private mode

        sharedPreferences = getApplicationContext().getSharedPreferences("UserLog", 0);
        // get editor to edit in file
        editor = sharedPreferences.edit();


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        // TODO: Implement your own signup logic here.
        if (!validate()) {
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String mNumber = _mobileNumber.getText().toString();
        reqParamsBuilder.add("name", name);
        reqParamsBuilder.add("email", email);
        reqParamsBuilder.add("password", Utils.md5Hash(password));
        reqParamsBuilder.add("phone_number", mNumber);
        reqParamsBuilder.add("service_username", "admin");
        reqParamsBuilder.add("service_password", "1234");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject response = httpMethods.post(reqParamsBuilder.getReqParams(),reqParamsBuilder.getHeaderParams(), "/registration");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        try {
                            int responseCode = Integer.parseInt(response.get("responseCode").toString());
                            if (responseCode == Constants.SUCCESS) {
                                JSONObject data = response.getJSONObject("data");
                                String accessToken = data.get("accesstoken").toString();
                                onSignupSuccess(name, email, password, mNumber, accessToken);
                            } else if(responseCode == Constants.BAD_REQUEST ) {
                                if(userAlreadyExists(response))
                                    Toast.makeText(getBaseContext(), "Email-id already registered", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getBaseContext(), "Unable to Register", Toast.LENGTH_LONG).show();
                                onSignupFailed();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Unable to login "+responseCode, Toast.LENGTH_LONG).show();
                                onSignupFailed();
                            }
                        } catch (Exception e) {
                            Utils.displayToast(SignUpActivity.this, "Unable to login.. "+e.getMessage());
                            onSignupFailed();
                        }
                    }
                });
            }
        });
        thread.start();
    }

    private boolean userAlreadyExists(JSONObject response) {
        try {
            String detail = response.getJSONArray("errors").getJSONObject(0).get("detail").toString();
            if(detail.indexOf("User already exists.") > -1) {
                return true;
            }
        } catch(Exception e) {
            return false;
        }
        return false;
    }

    public void onSignupSuccess(String name, String email, String password, String mNumber, String accessToken) {
        //save access token and other details
        // as now we have information in string. Lets stored them with the help of editor
        session.createUserLoginSession(email, password);
        editor.putString("Name", name);
        editor.putString("mNumber", mNumber);
        editor.putString("accesstoken", accessToken);
        editor.commit();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        // Start the Main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String mNumber = _mobileNumber.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (mNumber.isEmpty() || mNumber.length() < 10  ) {
            _mobileNumber.setError("wrong format");
            valid = false;
        } else {
            _mobileNumber.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        password = password.trim();
        if (password.isEmpty()) {
            _passwordText.setError("should not be empty");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}