package com.salve;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addUrl(View view) {
        BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();
        final BandClient bandClient = BandClientManager.getInstance().create(this, pairedBands[0]);

        // Note: the BandPendingResult.await() method must be called from a background thread.
        // An exception will be thrown if called from the UI thread.
        final BandPendingResult<ConnectionState> pendingResult =
                bandClient.connect();


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ConnectionState state = pendingResult.await();
                    if (state == ConnectionState.CONNECTED) {
                        String fwVersion = null;
                        String hwVersion = null;
                        try {
                            BandPendingResult<String> pendingResult =
                                    bandClient.getFirmwareVersion();
                            fwVersion = pendingResult.await();
                            pendingResult = bandClient.getHardwareVersion();
                            hwVersion = pendingResult.await();
                            Log.e("RRR", fwVersion + " " + hwVersion);
                            // do work related to Band firmware & hardware versions
                        } catch (InterruptedException ex) {
                            // handle InterruptedException
                            ex.printStackTrace();
                        } catch (BandIOException ex) {
                            // handle BandIOException
                            ex.printStackTrace();
                        } catch (BandException ex) {
                            // handle BandException
                            ex.printStackTrace();
                        }
                        // do work on success
                    } else {
                        // do work on failure
                    }
                } catch (InterruptedException ex) {
                    // handle InterruptedException
                    ex.printStackTrace();
                } catch (BandException ex) {
                    // handle BandException
                    ex.printStackTrace();
                }
            }
        }).start();


    }
}
