package com.mmu.nfciddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
	private static NfcAdapter mAdapter;
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	private static String []key;
	private static Intent readIntent;
	final int ACTIVITY_CHOOSE_FILE = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
	
	public void getKey(View v){
		Intent chooseFile;
		Intent intent;
		chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
		chooseFile.setType("file/*");
		intent = Intent.createChooser(chooseFile, "Choose a file");
		startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
	}
	
	public void read(View v){
		if( key == null){
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage("Use Mifare Default Key");

			// set a positive/yes button and create a listener
			alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				// Save the data from the UI to the database - already done
				public void onClick(DialogInterface arg0, int arg1) {
					Intent intent = new Intent(MainActivity.this, ReadActivity.class);
			    	intent.putExtra("strings", key);
			    	startActivity(intent);
				}
			});
			
			alertbox.show();
		}
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    		case ACTIVITY_CHOOSE_FILE: {
    			if (resultCode == RESULT_OK){
    				Uri uri = data.getData();
    				String filePath = uri.getPath();
    				readFile(filePath);
    			}
    		}
    	}
    }
    
    private void readFile(String filePath) {
    	File file = new File(filePath);
    	StringBuilder text = new StringBuilder();
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(file));
    		Integer counter = 0;
    		String line;
    		while ((line = br.readLine()) != null) {
    			text.append(line);
    			text.append(".");
    			counter++;
    		}
    		br.close();
    		key = text.toString().split("\\.");

    	}
    	catch (IOException e) {
    	    Log.e("ReadFile",e.getLocalizedMessage());
    	}
    }
}
