package com.mmu.nfciddle;

import java.io.IOException;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private static NfcAdapter mAdapter;
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	private static String [][]key;
	
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
		(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
		(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
		(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Setup an intent filter for all MIME based dispatches
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { MifareClassic.class
				.getName() } };

		Intent intent = getIntent();
		resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
  		// Parse the intent
  		String action = intent.getAction();
  		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
  			// status_Data.setText("Discovered tag with intent: " + intent);
  			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
  			MifareClassic mfc = MifareClassic.get(tagFromIntent);
  			byte[] data;

  			try {
  				mfc.connect();
  				boolean auth = false;
  				String cardData = null;
  				Log.i("resolveIntent", "key with:"+);
  				// Authenticating and reading Block 0 /Sector 1
  				auth = mfc.authenticateSectorWithKeyA(0,key3);

  				if (auth) {
  					data = mfc.readBlock(2);
  					cardData = getHexString(data, data.length);

  					if (cardData != null) {						
  						block_0_Data.setText(cardData);
  					} else {
  						showAlert(EMPTY_BLOCK_0);
  					}
  					
  					// reading Block 0 /Sector 1
  					data = mfc.readBlock(3);
  					cardData = getHexString(data, data.length);

  					if (cardData != null) {
  						block_1_Data.setT;ext(cardData);
  					} else {
  						showAlert(EMPTY_BLOCK_1);
  					}
  				} else {
  					showAlert(AUTH);
  				}

  			} catch (IOException e) {
  				Log.e(TAG, e.getLocalizedMessage());
  				showAlert(NETWORK);
  			}
  		}
  	}
    
    byte [] stringToHex(String key){
    	return key.getBytes();
    }
    
	public static String getHexString(byte[] raw, int len) {
		byte[] hex = new byte[2 * len];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= len)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}

		return new String(hex);
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
