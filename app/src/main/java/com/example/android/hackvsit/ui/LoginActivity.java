package com.example.android.hackvsit.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.hackvsit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;


    // UI references
    @BindView(R.id.number)
    EditText mNumberEditText;
    @BindView(R.id.til_number)
    TextInputLayout mNumberTIL;
    @BindView(R.id.sign_in_button)
    Button mSignInButton;
    @BindView(R.id.otp_sign_in_button)
    Button mOtpSignInButton;
    @BindView(R.id.otp_edit_text)
    EditText mOtpEditText;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.login_progress)
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNumberEditText.setError(null);

        // Store values at the time of the login attempt.
        String number = mNumberEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(number)) {
            mNumberEditText.setError(getString(R.string.error_field_required));
            focusView = mNumberEditText;
            cancel = true;
        } else if (!isNumberValid(number)) {
            mNumberEditText.setError(getString(R.string.error_invalid_number));
            focusView = mNumberEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mNumberEditText.setVisibility(View.GONE);
            mNumberEditText.setHint(null);
            mOtpEditText.setVisibility(View.VISIBLE);
            mOtpSignInButton.setVisibility(View.VISIBLE);
            mSignInButton.setVisibility(View.GONE);
            mNumberTIL.setVisibility(View.GONE);

            mAuthTask = new UserLoginTask(number);
            mAuthTask.execute((Void) null);
            mOtpSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean cancelOtp = false;
                    View focus = null;
                    String code = mOtpEditText.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        mOtpEditText.setError(getString(R.string.error_field_required));
                        focus = mNumberEditText;
                        cancelOtp = true;
                    } else if (!isOtpValid(code)) {
                        mOtpEditText.setError(getString(R.string.error_invalid_number));
                        focus = mNumberEditText;
                        cancelOtp = true;
                    }
                    if (cancelOtp) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focus.requestFocus();
                    }
                    else{

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                        signInWithPhoneAuthCredential(credential);
                        showProgress(true);
                    }
                }
            });
        }
    }

    private boolean isNumberValid(String number) {
        return number.length() == 10 && number.matches("^[0-9]*$");
    }
    private boolean isOtpValid(String number) {
        return number.length() == 6 && number.matches("^[0-9]*$");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNumber;

        UserLoginTask(String number) {
            mNumber = number;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + mNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    LoginActivity.this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks



            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);

//            if (success) {
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                finish();
//            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Timber.d("onVerificationCompleted:%s", credential);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Timber.w("onVerificationFailed %s", e.toString());

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(String verificationId,
                PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Timber.d( "onCodeSent:%s", verificationId);

            showProgress(false);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.d( "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            finish();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Timber.w("signInWithCredential:failure%s", task.getException().toString());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mOtpEditText.setError(getString(R.string.invalid_otp));
                            }
                        }
                    }
                });
    }

}

