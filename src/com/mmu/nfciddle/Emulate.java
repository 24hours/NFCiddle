package com.mmu.nfciddle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class Emulate extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emulate);
        Log.i("EMULATE","started");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_emulate, menu);
        return true;
    }
}
