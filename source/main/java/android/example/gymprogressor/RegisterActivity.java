package android.example.gymprogressor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static String URL_REGISTER = "https://gymprogressor.000webhostapp.com/app/register.php";
    private EditText mName, mEmail, mPassword, mcPassword, mAccessCode;
    private Button mBtnReg, mBtnBack;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* Background to be overlaid */
        getWindow().setBackgroundDrawableResource(R.drawable.register_bckg);

        mLoading = findViewById(R.id.loadingBar);
        mAccessCode = findViewById(R.id.token_field);
        mName = findViewById(R.id.name_field);
        mEmail = findViewById(R.id.email_field);
        mPassword = findViewById(R.id.password_field);
        mcPassword = findViewById(R.id.cpassword_field);
        mBtnReg = findViewById(R.id.register_button);
        mBtnBack = findViewById(R.id.back_button);

        mBtnReg.setOnClickListener(v -> {
            final String vCode = mAccessCode.getText().toString().trim();
            final String vName = mName.getText().toString().trim();
            final String vEmail = mEmail.getText().toString().trim();
            final String vPassword = mPassword.getText().toString().trim();
            final String vcPassword = mcPassword.getText().toString().trim();

            if (!TextUtils.isEmpty(vEmail) && !TextUtils.isEmpty(vPassword) && !TextUtils.isEmpty(vName) && !TextUtils.isEmpty(vCode)) {
                //Check password's length
                if (isPasswordValid(vPassword)) {
                    if(vcPassword.equals(vPassword)) {
                        if (isEmailValid(vEmail)) { // Check if string is an email
                            if(isNameValid(vName)){
                                Register(vName, vEmail, vPassword, vCode);
                            }
                            else mName.setError("Invalid name!");
                        }
                        else {
                            mEmail.setError("Invalid email!");
                        }
                    }
                    else{
                        mPassword.setError("Passwords are different!");
                        mcPassword.setError("Passwords are different!");
                    }
                } else {
                    mPassword.setError("Password must be at least 8 characters long.");
                }
            }
            if (TextUtils.isEmpty(vPassword)) {
                mPassword.setError("Please insert password");
            }
            if (TextUtils.isEmpty(vEmail)) {
                mEmail.setError("Please insert email");
            }
            if (TextUtils.isEmpty(vName)) {
                mName.setError("Please insert name");
            }
            if(TextUtils.isEmpty(vCode)){
                mAccessCode.setError("Please insert the Access Code.");
            }
            if (TextUtils.isEmpty(vEmail) && TextUtils.isEmpty(vPassword) && TextUtils.isEmpty(vName) && TextUtils.isEmpty(vCode)) {
                mAccessCode.setError("Please insert the Access Code");
                mName.setError("Please insert name");
                mEmail.setError("Please insert email");
                mPassword.setError("Please insert password");
            }
        });

        mBtnBack.setOnClickListener(v -> finish());
    }

    private boolean isPasswordValid(String passwod) {
        if(passwod.length() < 8) return false;
        else return true;
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isNameValid(String name){
        return !(Pattern.compile( "[^A-Za-z]" ).matcher(name).find());
    }

    private void Register(final String vName, final String vEmail, final String vPassword, final String vCode) {
        mLoading.setVisibility(View.VISIBLE);
        mBtnReg.setVisibility(View.GONE);
        mBtnBack.setVisibility(View.GONE);

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    try {
                        JSONObject jsonobject = new JSONObject(response);
                        String success = jsonobject.getString("success");
                        if (success.equals("1")) {
                            Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else if (success.equals("-1")) {
                            mEmail.setError("Your email is already used");
                        }
                        else if (success.equals("-2")) {
                            mAccessCode.setError("Invalid code! Contact the Administrator.");
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Register failed!" + e.toString(), Toast.LENGTH_SHORT).show();

                    }
                    mLoading.setVisibility(View.GONE);
                    mBtnReg.setVisibility(View.VISIBLE);
                    mBtnBack.setVisibility(View.VISIBLE);
                },
                error -> {
                    Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();

                    mLoading.setVisibility(View.GONE);
                    mBtnReg.setVisibility(View.VISIBLE);
                    mBtnBack.setVisibility(View.GONE);
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("code", vCode);
                params.put("name", vName);
                params.put("email", vEmail);
                params.put("password", vPassword);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(mStringRequest);
    }


}
