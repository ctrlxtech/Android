package com.ctrlxtech.android.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.ctrlx.R;

public class ConnectApActivity extends Activity implements OnClickListener {
	private EditText mEtSSID;
	private EditText mPwd;
	private Button mBtnConnect;
	private String ssid;
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connectap);

		mEtSSID = (EditText) findViewById(R.id.config_et_ssid);
		mPwd = (EditText) findViewById(R.id.config_et_pwd);
		mBtnConnect = (Button) findViewById(R.id.config_btn_connect);

		mBtnConnect.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		//输入硬件ssid，pwd，断开当前wifi，连上硬件AP
		ssid = mEtSSID.getText().toString();
		pwd = mPwd.getText().toString();
		WifiAdmin wifiAdmin = new WifiAdmin(this);
		wifiAdmin.openWifi();
		wifiAdmin.addNetwork(wifiAdmin.createWifiInfo(ssid, pwd, 3));
		Intent intent = new Intent(ConnectApActivity.this, ConfigActivity.class);
		startActivity(intent);
	}


}
