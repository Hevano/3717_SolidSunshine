package ca.bcit.emptyproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences savedValues;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        savedValues = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button buttonMenu = findViewById(R.id.btnMenu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        //Authentication
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            showLoginDialog();
        }
        //Log.println(Log.DEBUG, "", currentUser.getUid());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent i = getIntent();
        String address = i.getStringExtra("Address");
        if(address == null){
            focusOnMe();
        } else {
            search(address);
        }

    }

    private void search(String address){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        try {
           addressList = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address adr = addressList.get(0);
        LatLng latLng = new LatLng(adr.getLatitude(), adr.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    private void focusOnMe(){
        // Zoom into users location
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                addMarkerToMap(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            addMarkerToMap(lastKnownLocation);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private void addMarkerToMap(Location location) {
        String msg = String.format("Current Location: %4.3f Lat %4.3f Long.",
                location.getLatitude(),
                location.getLongitude());

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(latlng).title(msg));

        float zoomLevel = 15.0f;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoomLevel));
    }

    public void onSearchButton(View v){
        Intent i = new Intent(MapsActivity.this, SearchActivity.class);
        startActivity(i);
    }


    private void showLoginDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        final EditText editTextPass = dialogView.findViewById(R.id.editTextPass);

        final Button btnLogin = dialogView.findViewById(R.id.btnLogin);
        final Button btnSignup = dialogView.findViewById(R.id.btnSignup);

        dialogBuilder.setTitle("Login");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();
//
                login(userName, pass, alertDialog);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();

                createUser(userName, pass, alertDialog);
            }
        });
    }

    private void login(String email, String password, final AlertDialog a){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MapsActivity.this, "Authentication succeeded.",
                                    Toast.LENGTH_SHORT).show();
                            a.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MapsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void createUser(String email, String password, final AlertDialog a){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MapsActivity.this, "Authentication succeeded.",
                                    Toast.LENGTH_SHORT).show();
                            a.dismiss();
                            addEmailLookup(user.getEmail(), user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MapsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addEmailLookup(String email, String uid){
        DatabaseReference lookup = FirebaseDatabase.getInstance().getReference().child("emailLookup").child(email);
        lookup.setValue(uid);
    }

    public void Meet_List(View view) {
        Intent intent = new Intent(this, Meeting.class);
        startActivity(intent);
    }
}
