package com.ctrlxtech.android.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ctrlx.R;

public class ConfigActivity extends Activity implements OnClickListener {
	protected static final int SOCKET_CONNECT = 0;

	private EditText mEtDevicePwd;

	private Spinner mSpinner;

	private Button mBtnConfig;

	private TextView mTvShow;

	private ArrayAdapter<String> mAdapter;

	private String[] wifiList;

	private String mHomeWifi;
	private String mDeviceWifi;

	private String mHomePwd;
	private String mDevicePwd;

	private int mWifiIpAddr;

	private String routerIp;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		mEtDevicePwd = (EditText) findViewById(R.id.config_et_device_pwd);
		mSpinner = (Spinner) findViewById(R.id.config_spinner);
		mBtnConfig = (Button) findViewById(R.id.config_btn_config);
		mTvShow = (TextView) findViewById(R.id.config_tv_show);

		WifiAdmin wifiAdmin = new WifiAdmin(this);

		wifiAdmin.startScan();

		wifiList = wifiAdmin.getWifiSSIDList();

		mWifiIpAddr = wifiAdmin.getIPAddress();

		Log.i("tag", mWifiIpAddr + "");

		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wifiList);

		mSpinner.setAdapter(mAdapter);

		mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());

		mSpinner.setVisibility(View.VISIBLE);

		mBtnConfig.setOnClickListener(this);



	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			mDeviceWifi = parent.getItemAtPosition(position).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onClick(View v) {

		mHomePwd = mEtDevicePwd.getText().toString();
		if (mDeviceWifi != null && mDevicePwd != null) {
			WifiAdmin wifiAdmin = new WifiAdmin(this);
			routerIp = wifiAdmin.getRouterIpAddr();
			if (routerIp != null) {
				mThread.start();
			}
			wifiAdmin.addNetwork(wifiAdmin.createWifiInfo(mDeviceWifi, mDevicePwd, 3));
		}
	}

	private final Thread mThread = new Thread() {
		@Override
		public void run() {
			try {


				InetAddress serverAddr = InetAddress.getByName(routerIp);
				Socket socket = new Socket(serverAddr, 10000);

				Message m = Message.obtain();
				m.what = SOCKET_CONNECT;
				m.obj = "Connec success!!";
				mHandler.sendMessage(m);

				JSONObject json = new JSONObject();
				json.put("HomeWiFiSSID", mHomeWifi);
				json.put("HomeWiFiPwd", mHomePwd);

				String jsonStr = json.toString();
				PrintWriter writer = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				writer.print(json);
//				writer.print(jsonStr);
				writer.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SOCKET_CONNECT:
				mTvShow.append(msg.obj.toString());
			}
		}
	};
}
