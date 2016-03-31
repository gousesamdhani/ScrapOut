package cashin.scrapout;

import android.app.ProgressDialog;
import android.content.Context;
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


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String PREFER_NAME = "UserLog";
    private String LoginStatus="null";
    private RequestParamsBuilder reqParamsBuilder;
    private HttpMethods httpMethods;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private UserSession session;
    SharedPreferences sharedPreferences;
    Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // User Session Manager
        session = new UserSession(getApplicationContext());
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);
        httpMethods = new HttpMethods();
        reqParamsBuilder = new RequestParamsBuilder();
        sharedPreferences = getApplicationContext().getSharedPreferences("UserLog", 0);
        editor = sharedPreferences.edit();
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the SignUp activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            return;
        }
        _loginButton.setEnabled(false);
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        verifyAccess(email, password);
    }

    private void verifyAccess(final String email, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        reqParamsBuilder.add("email", email);
        reqParamsBuilder.add("password", Utils.md5Hash(password));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject response = httpMethods.post(reqParamsBuilder.getReqParams(),reqParamsBuilder.getHeaderParams(), "/auth");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        try {
                            int responseCode = Integer.parseInt(response.get("responseCode").toString());
                            if (responseCode == Constants.SUCCESS) {
                                onLoginSuccess(response, email, password);
                            } else {
                                Toast.makeText(LoginActivity.this, "Response Code " + responseCode, Toast.LENGTH_LONG);
                                onLoginFailed();
                            }
                        } catch (Exception e) {
                            Utils.displayToast(LoginActivity.this, "Unable to login");
                        }
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(JSONObject response, String email, String password) {
        try {
            _loginButton.setEnabled(true);
            JSONObject data = response.getJSONObject("data");
            String accessToken = data.get("accesstoken").toString();
            session.createUserLoginSession(email, password);
            LoginStatus = "Success";
            editor.putString("accesstoken", accessToken);
            editor.commit();
            Intent i = new  Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        catch(Exception e) {
            Utils.displayToast(LoginActivity.this, "Unable to Authenticate");
        }
    }

    public void onLoginFailed() {
        // username / password doesn't match&
        Toast.makeText(getApplicationContext(),
                "Username/Password is incorrect",
                Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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
