package lb.com.network.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import lb.com.network.project.Utils.Utils;
import lb.com.network.project.R;
import lb.com.network.project.adapter.ContactAdapter;
import lb.com.network.project.model.ApiBaseResponse;
import lb.com.network.project.model.Contact;
import lb.com.network.project.model.ContactDistance;
import lb.com.network.project.model.State;
import lb.com.network.project.rest.ApiClient;
import lb.com.network.project.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    public static final String CHANNEL_ID = "exampleForegroundServiceChannel";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
    private static final String PREF_NAME = "MyAppPreferences";
    private static int PRIVATE_MODE = 0;
    public static final String sysIdKey = "sysIdKey";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    //set of nearby contacts
    Set<Contact> nearByContacts = new HashSet<Contact>();
    ListView nearByBeaconLv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyBluetooth();

        TextView myIdView = (TextView) findViewById(R.id.myid);
        nearByBeaconLv = (ListView) findViewById((R.id.remoteBeacons));

        checkPermission();
        String uid = getsystemID();
        //remote_beacon_ids_set.add("My ID : "+ uid);
        myIdView.setText(uid);
        myIdView.setVisibility(View.VISIBLE);
        startserviceBroadcast(uid);
    }

    private String getsystemID(){
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        String systemID = prefs.getString(sysIdKey, null);
        if (systemID == null){
            systemID = Utils.generateUidNamespace();
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(sysIdKey, systemID);
        editor.commit();
        return systemID;
    }

    private void startserviceBroadcast(String uid) {
        Intent serviceIntent = new Intent(this, bgService.class);
        serviceIntent.putExtra(sysIdKey, uid);

//        application.enableMonitoring();
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App application = ((App) this.getApplicationContext());
        beaconManager.bind(this);
        application.setMonitoringActivity(this);
        updateLog(application.getLog());
    }

    private boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("This app needs background location access");
                        builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @TargetApi(23)
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                        PERMISSION_REQUEST_BACKGROUND_LOCATION);
                            }
                        });
                        builder.show();
                    }
                    else {

                    }

                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();
                }

            }
        }
        return false;
    }

    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //finish();
                    //System.exit(0);
                }

            });
            builder.show();

        }

    }

    public void updateLog(final String log) {
        Log.i("testing_log_adapater",log);


    }

    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d("test_ble", "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                    Beacon firstBeacon = beacons.iterator().next();
                    String beaconLog = "The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.";
                    Log.d("test_ble_Id1",String.valueOf(firstBeacon.getId1()));
                    String beaconID = String.valueOf(firstBeacon.getId1());

                    Log.d("test_ble",beaconLog);

                    //send to backend
                    ContactDistance contactDistance = new ContactDistance();
                    contactDistance.setUserID1(getsystemID());
                    contactDistance.setUserID2(beaconID.substring(2,beaconID.length()));
                    contactDistance.setDistance(firstBeacon.getDistance()*100);
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<ApiBaseResponse> call = apiService.createContactDistance(contactDistance);
                    call.enqueue(new Callback<ApiBaseResponse>() {
                        @Override
                        public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {

                        }

                        @Override
                        public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //update UI list
                    Contact nearbyContact = new Contact();
                    nearbyContact.setUuid(beaconID.substring(2,beaconID.length()));
                    nearbyContact.setDistance(firstBeacon.getDistance()*100);
                    //remove then add to update the distance if found
                    nearByContacts.remove(nearbyContact);
                    nearByContacts.add(nearbyContact);
                    ArrayList<Contact> list = new ArrayList<>(nearByContacts);
                    Collections.sort(list);
                    ContactAdapter
                            adapter = new ContactAdapter(
                            getApplicationContext(), R.layout.nearby_contact_row,
                            list);

                    nearByBeaconLv.setAdapter(adapter);


                }
            }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }


    public void updateState(View view) {
        State state = new State();
        state.setUserID(getsystemID());
        state.setIsIll(1);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ApiBaseResponse> call = apiService.updateState(state);
        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                Toast.makeText(getApplicationContext(), "State updated.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


