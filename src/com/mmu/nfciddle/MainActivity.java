package com.mmu.nfciddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static NfcAdapter mAdapter;
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;
	
	/*
	* Each sector will have their own 2 key, according to legend, 
	* > first key allow read only
	* > second key allow read/write 
	*/ 
	private static String [][]key;
	private static Intent readIntent;
	final int ACTIVITY_CHOOSE_FILE = 1;
	
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
		(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
		(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
		(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static byte[] asBytes (String s) {
        String s2;
        byte[] b = new byte[s.length() / 2];
        int i;
        for (i = 0; i < s.length() / 2; i++) {
            s2 = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte)(Integer.parseInt(s2, 16) & 0xff);
        }
        return b;
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
	
	public void getKey(View v){
		Intent chooseFile;
		Intent intent;
		chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
		chooseFile.setType("file/*");
		intent = Intent.createChooser(chooseFile, "Choose a file");
		startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
	}
	
	public void read(View v){
		Intent i = new Intent(this, ReadActivity.class);
		startActivity(i);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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
    	//Read text from file
    	StringBuilder text = new StringBuilder();
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(file));
    		String line;
    		while ((line = br.readLine()) != null) {
    			text.append(line);
    			text.append('\n');
    		}
    	}
    	catch (IOException e) {
    	    //You'll need to add proper error handling here
    	}
    	//Find the view by its id
    	//TextView tv = (TextView)findViewById(R.id.text_view);
    	//Set the text
    	//tv.setText(text);
    }
}
