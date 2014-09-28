package com.ctrlxtech.android.main;

import java.io.IOException;

import com.example.ctrlx.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;
import com.firebase.client.Firebase;
import com.firebase.simplelogin.FirebaseSimpleLoginError;
import com.firebase.simplelogin.FirebaseSimpleLoginUser;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.enums.Provider;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class ActivityLogin extends Activity implements ConnectionCallbacks, SimpleLoginAuthenticatedHandler {
	
	private SimpleLogin mSimpleLogin;
	private ProgressDialog mAuthProgressDialog;
	private FirebaseSimpleLoginUser mAuthenticatedUser;
	
	private LoginButton mFacebookLoginButton;
	
	private static final int RC_GOOGLE_LOGIN = 1;
	private GoogleApiClient mGoogleApiClient;
	private boolean mGoogleIntentInProgress;
	private boolean mGoogleLoginClicked;
	private ConnectionResult mGoogleConnectionResult;
	private SignInButton mGoogleLoginButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		mFacebookLoginButton = (LoginButton) findViewById(R.id.login_with_facebook);
		mFacebookLoginButton.setSessionStatusCallback(new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				onFacebookSessionStateChange(session, state, exception);
			}
		});
		
		
		mGoogleLoginButton = (SignInButton) findViewById(R.id.login_with_google);
		mGoogleLoginButton.setOnClickListener((OnClickListener) this);
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
							.addConnectionCallbacks(this)
							.addApi(Plus.API)
							.addScope(Plus.SCOPE_PLUS_LOGIN)
							.build();
		
		String firebaseUrl = getResources().getString(R.string.firebase_url);
		mSimpleLogin = new SimpleLogin(new Firebase(firebaseUrl), this.getApplicationContext());
		
		mAuthProgressDialog = new ProgressDialog(this);
		mAuthProgressDialog.setTitle("Loading");
		mAuthProgressDialog.setMessage("Authenticating with Firebase...");
		mAuthProgressDialog.setCancelable(false);
		mAuthProgressDialog.show();
		
		mSimpleLogin.checkAuthStatus(new SimpleLoginAuthenticatedHandler() {

			@Override
			public void authenticated(FirebaseSimpleLoginError error,
					FirebaseSimpleLoginUser user) {
				if (error != null) {
					Log.e("tag", "ERROR checking if user is authenticated: " + error);
				} else if (user == null) {
					Log.i("tag", "User is not authenticated");
				} else {
					Log.i("tag", "User is authenticated in: " + user);
					setAuthenticatedUser(user);
				}
				
				mAuthProgressDialog.hide();
			}
			
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//request by google api
		if (requestCode == RC_GOOGLE_LOGIN) {
			if (resultCode != RESULT_OK) {
				mGoogleLoginClicked = false;
			}
			
			mGoogleIntentInProgress = false;
			
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		} else {
			//otherwise, this probably the request by the facebook button
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this.mAuthenticatedUser != null) {
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		} else {
			return false;
		}
	}
	
	public void onClick(View view) {
		if (view.getId() == R.id.login_with_google) {
			mGoogleLoginClicked = true;
			if (!mGoogleApiClient.isConnecting()) {
				if (mGoogleConnectionResult != null) {
					resolveSignInError();
				} else if (mGoogleApiClient.isConnected()) {
					getGoogleOAuthTokenAndLogin();
				} else {
					Log.d("tag", "trying to connect to google api");
					mGoogleApiClient.connect();
				}
			}
		} else if (view.getId() == R.id.btn_login) {
			
		} else if (view.getId() == R.id.btn_signup) {
			Intent intent = new Intent();
			// redirect to signup activity
		}
	}
	
	private void setAuthenticatedUser(FirebaseSimpleLoginUser user) {
		if (user != null) {
			String name;
			switch (user.getProvider()) {
				case FACEBOOK:
					name = (String)user.getThirdPartyUserData().get("name");
					Log.d("tag", "log in as facebook name" + name);
					break;
				case GOOGLE:
					name = (String)user.getThirdPartyUserData().get("name");
					Log.d("tag", "log in as google name" + name);
					break;
				default:
					Log.d("tag", "log in as unknown provider");
					break;
			}
		}
		
		this.mAuthenticatedUser = user;
		
		invalidateOptionsMenu();
	}
	
	private void logout() {
		if (this.mAuthenticatedUser != null) {
			//logout from firebase
			mSimpleLogin.logout();
			//logout from frameworks after logout from firebase
			if (this.mAuthenticatedUser.getProvider() == Provider.FACEBOOK) {
				Session session = Session.getActiveSession();
				if (session != null) {
					if (!session.isClosed()) {
						session.closeAndClearTokenInformation();
					}
				} else {
					session = new Session(getApplicationContext());
					Session.setActiveSession(session);
					session.closeAndClearTokenInformation();
				}
			} else if (this.mAuthenticatedUser.getProvider() == Provider.GOOGLE){
				if (mGoogleApiClient.isConnected()) {
					Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
					mGoogleApiClient.disconnect();
				}
			}
			setAuthenticatedUser(null);
		}
	}
	
	private void showErrorDialog(String message) {
		new AlertDialog.Builder(this)
								.setTitle("Error")
								.setMessage(message)
								.setPositiveButton(android.R.string.ok, null)
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
	}
	
	public void authenticated(FirebaseSimpleLoginError error, FirebaseSimpleLoginUser user) {
		if (error != null) {
			Log.e("tag", "Error logging in: " + error);
			showErrorDialog("an error ocurred while authenticating with Firebase: " + error.getMessage());
		} else {
			Log.i("tag", "Logged in with user: " + user);
			setAuthenticatedUser(user);
		}
		mAuthProgressDialog.hide();
	}
	
	private void onFacebookSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			final String appId = getResources().getString(R.string.facebook_app_id);
			mAuthProgressDialog.show();
			mSimpleLogin.loginWithFacebook(appId, session.getAccessToken(), this);
		} else if (state.isClosed()) {
			if (mAuthenticatedUser != null && this.mAuthenticatedUser.getProvider() == Provider.FACEBOOK) {
				mSimpleLogin.logout();
				setAuthenticatedUser(null);
			}
		}
	}
	
	
	
	//google
	
	//a helper to resolve the current connectionResult error
	private void resolveSignInError() {
		if (mGoogleConnectionResult.hasResolution()) {
			mGoogleIntentInProgress = true;
			try {
				mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
			} catch (IntentSender.SendIntentException e) {
				mGoogleIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}
	
	private void getGoogleOAuthTokenAndLogin() {
		mAuthProgressDialog.show();
		//Get oauth token in background
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String> () {
			String errorMessage;
			@Override
			protected String doInBackground(Void... params) {
				String token = null;
				try {
					String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
					token = GoogleAuthUtil.getToken(ActivityLogin.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
				} catch (IOException transientEx) {
					//network or server error
					Log.e("tag", "error authenticating with google: " + transientEx);
					errorMessage = "Network error: " + transientEx.getMessage();
				} catch (UserRecoverableAuthException e) {
					Log.w("tag", "Recoverable Google Oauth error: " + e.toString());
					//we probably need to ask for permissions, so start the intent if there is none pending
					if (!mGoogleIntentInProgress) {
						mGoogleIntentInProgress = true;
						Intent recover = e.getIntent();
						startActivityForResult(recover, RC_GOOGLE_LOGIN);
					}
				} catch (GoogleAuthException authEx) {
					// the call is not ever expected to succeed assuming you have already verified that google play is installed
					Log.e("tag", "Error authenticating with google " + authEx.getMessage(), authEx);
					errorMessage = "Error authenticating with google: " + authEx.getMessage();
				}
				return token;
			}
			
		};
		task.execute();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		//connected with google api, use this to authenticate with firebase
		getGoogleOAuthTokenAndLogin();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!mGoogleIntentInProgress) {
			mGoogleConnectionResult = result;
			
			if (mGoogleLoginClicked) {
				resolveSignInError();
			}
		}
	}

}
