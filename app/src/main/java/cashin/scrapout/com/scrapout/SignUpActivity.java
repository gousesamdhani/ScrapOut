package cashin.scrapout.com.scrapout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText _nameText;
    private EditText _emailText;
    private EditText _passwordText;
    private EditText _mobileNumber;
    private Button _signupButton;
    private TextView _loginLink;
    SharedPreferences sharedPreferences;
    Editor editor;
    UserSession session;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String mNumber = _mobileNumber.getText().toString();

        // as now we have information in string. Lets stored them with the help of editor
        session.createUserLoginSession(email,
                password);
        editor.putString("Name", name);
        editor.putString("mNumber", mNumber);
        editor.commit();
        // commit the values
    // TODO: Implement your own signup logic here.

    new android.os.Handler().

    postDelayed(
            new Runnable() {
                public void run() {
                    onSignupSuccess();
                    progressDialog.dismiss();
                }
            }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        // Start the Main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Please check Input", Toast.LENGTH_LONG).show();

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

        if (password.isEmpty() || password.length() < 6 || password.length() > 10 ) {
            _passwordText.setError("between 6 and 10 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}