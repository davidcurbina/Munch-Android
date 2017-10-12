package davidurbina.munch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Location_Activity extends AppCompatActivity {

    ViewGroup container;
    Scene current;
    Scene other;
    Transition mytransition;
    Button btnFindMe;
    private LocationListener locationListener;
    private LocationManager locationManager;
    EditText tbLocation;
    String mLatitude;
    String mLongitude;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition);

        //Assign Scenes
        container = (ViewGroup) findViewById(R.id.container);

        current = Scene.getSceneForLayout(container,
                R.layout.trans_find_exit,
                this);
        current.enter();

        other = Scene.getSceneForLayout(container,
                R.layout.trans_find_enter,
                this);
        //TransitionManager.go(other);
        goToScene(other, "In", "", "");

        btnFindMe  = (Button) findViewById(R.id.btnLocation);
        tbLocation = (EditText) findViewById(R.id.tbLocation);
        sharedpreferences = getSharedPreferences("Location", Context.MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                },10);
            }
            return;
        } else {
            configureButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        btnFindMe  = (Button) findViewById(R.id.btnLocation);
        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                btnFindMe.setVisibility(View.GONE);

                SingleShotLocationProvider.requestSingleUpdate(getApplicationContext(),
                        new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {


                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.longitude, location.latitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String zip = addresses.get(0).getPostalCode();
                                String country = addresses.get(0).getCountryName();
                                tbLocation.setText(city + "," + state + "," + country);

                                other = Scene.getSceneForLayout(container,
                                        R.layout.trans_find_exit,
                                        getApplicationContext());
                                //TransitionManager.go(other);
                                mLatitude = Float.toString(location.latitude);
                                mLongitude = Float.toString(location.longitude);

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("Latitude", mLatitude);
                                editor.putString("Longitude", mLongitude);
                                editor.commit();

                                progressBar.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                btnFindMe.setVisibility(View.VISIBLE);
                                goToScene(other, "Next", mLatitude, mLongitude);
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        other = Scene.getSceneForLayout(container,
                R.layout.trans_find_enter,
                this);
        //TransitionManager.go(other);
        goToScene(other, "In", "", "");
        configureButton();
        ImageView back = (ImageView) findViewById(R.id.imgback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void goToScene(Scene scene, final String effect, final String option1, final String option2) {
        TransitionSet transition = new TransitionSet();
        ChangeBounds changeBounds = new ChangeBounds();
        Fade fadeOut = new Fade(Fade.OUT);
        fadeOut.setDuration(100);
        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(500);
        if(effect.equals("In"))
        {
            transition
                    .addTransition(fadeIn);
        } else {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if(effect == "Out") {
                        finish();
                        overridePendingTransition(0, 0);
                    } else if (effect == "Next"){
                        Intent intent = new Intent(Location_Activity.this, Main_Activty.class);
                        intent.putExtra("Lat", option2);
                        intent.putExtra("Long",option1);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            transition
                    .addTransition(fadeOut);
        }
        TransitionManager.go(scene, transition);
    }


    @Override
    public void onBackPressed() {
        other = Scene.getSceneForLayout(container,
                R.layout.trans_signin_exit,
                this);

        //TransitionManager.go(other);
        goToScene(other, "Out","","");
    }
}
