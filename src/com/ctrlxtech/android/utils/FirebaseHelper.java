package com.ctrlxtech.android.utils;

import java.util.HashMap;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.simplelogin.FirebaseSimpleLoginError;
import com.firebase.simplelogin.FirebaseSimpleLoginUser;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.SimpleLoginCompletionHandler;

public class FirebaseHelper {

	private Firebase firebase;

	private static final String FIREBASE_URL = "https://ctrlx.firebaseio.com";

	private static final int DATACHANGED = 1;

	private boolean isAuthenticated;
	private boolean isUserCreated;
	private boolean isEmaiUserLogin;
	private boolean isEmailUserPwdChanged;
	private boolean isPwdResetEmailSent;
	private boolean isEmailUserDeleted;
	private boolean isFbUserLogin;
	private boolean isGgUserLogin;
	private boolean isTtUserLogin;

	public FirebaseHelper() {

	}

	public Firebase getInstance() {
		Firebase ref = new Firebase("https://ctrlx.firebaseio.com");
		return ref;
	}

	public boolean isAuthenticated() {
		Firebase authRef = this.getInstance().getRef().child(".info/authenticated");
		authRef.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				isAuthenticated = snapshot.getValue(Boolean.class);
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub

			}
		});

		return isAuthenticated;
	}

	public boolean createUser(String email, String pwd) {
		Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.createUser(email, pwd, new SimpleLoginAuthenticatedHandler() {

			@Override
			public void authenticated(FirebaseSimpleLoginError error,
					FirebaseSimpleLoginUser user) {
				if (error != null) {
					isUserCreated = false;
				} else {
					isUserCreated = true;
				}
			}
		});

		return isUserCreated;
	}

	public boolean logEmailUserIn(String email, String pwd) {
		Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.loginWithEmail(email, pwd, new SimpleLoginAuthenticatedHandler() {

			@Override
			public void authenticated(FirebaseSimpleLoginError error,
					FirebaseSimpleLoginUser user) {
				if (error != null) {
					isEmaiUserLogin = false;
				} else {
					isEmaiUserLogin = true;
				}
			}
		});
		return isEmaiUserLogin;
	}

	public boolean emailUserPwdChange(String email, String pwd, String newPwd) {
		Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.changePassword(email, pwd, newPwd, new SimpleLoginCompletionHandler() {

			@Override
			public void completed(FirebaseSimpleLoginError error, boolean success) {
				if (error != null) {
					isEmailUserPwdChanged = false;
				} else if(success){
					isEmailUserPwdChanged = true;
				}
			}
		});

		return isEmailUserPwdChanged;
	}

	public boolean sendPwdResetEmail(String email) {
		Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.sendPasswordResetEmail(email, new SimpleLoginCompletionHandler() {

			@Override
			public void completed(FirebaseSimpleLoginError error, boolean success) {
				if (error != null) {
					isPwdResetEmailSent = false;
				} else if (success) {
					isPwdResetEmailSent = true;
				}
			}
		});
		return isPwdResetEmailSent;
	}

	public boolean deleteEmailUser(String email, String pwd) {
		Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.removeUser(email, pwd, new SimpleLoginCompletionHandler() {

			@Override
			public void completed(FirebaseSimpleLoginError error, boolean success) {
				if (error != null) {
					isEmailUserDeleted = false;
				} else {
					isEmailUserDeleted = true;
				}
			}
		});
		return isEmailUserDeleted;
	}

	public void logout() {
		Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.logout();
	}

	public void storeUserData() {
		final boolean isNewUser = true;

		final Firebase firebase = this.getInstance();
		SimpleLogin mSimpleLogin = new SimpleLogin(firebase);
		mSimpleLogin.checkAuthStatus(new SimpleLoginAuthenticatedHandler() {

			@Override
			public void authenticated(FirebaseSimpleLoginError error,
					FirebaseSimpleLoginUser user) {
				if (isNewUser) {
					Map<String, String> map = new HashMap<String, String>();
		            map.put("email", user.getEmail());
		            map.put("provider", user.getProvider().name());
		            map.put("provider_id", user.getUserId());
		            firebase.child("users").child(user.getUid()).setValue(map);
				}
			}
		});
	}

}
