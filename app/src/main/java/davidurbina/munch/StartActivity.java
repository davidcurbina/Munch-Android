/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package davidurbina.munch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    ViewGroup container;
    Scene current;
    Scene other;
    Transition mytransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("StartActivity","Here");

        //mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);

        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        // Registering BroadcastReceiver
        //registerReceiver();

        sharedpreferences = getSharedPreferences("Location", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("Latitude","0") != "0" && sharedpreferences.getString("Longitude","0") != "0"){
            Intent intent = new Intent(StartActivity.this, Main_Activty.class);
            intent.putExtra("Long", sharedpreferences.getString("Latitude","0"));
            intent.putExtra("Lat",sharedpreferences.getString("Longitude","0"));
            Log.d("Lat", sharedpreferences.getString("Latitude", "0"));
            Log.d("Long",sharedpreferences.getString("Longitude","0"));
            Log.d("StartActivity","Transitioning");
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition);
        Log.i("Application Start", "Success");


        //Assign Scenes
        container = (ViewGroup) findViewById(R.id.container);

        current = Scene.getSceneForLayout(container,
                R.layout.trans_mainenter,
                this);
        other = Scene.getSceneForLayout(container,
                R.layout.trans_mainexit,
                this);
        current.enter();


    }

    @Override
    protected void onResume() {
        super.onResume();
        other = Scene.getSceneForLayout(container,
                R.layout.trans_mainexit,
                this);
        //TransitionManager.go(other);
        goToScene(other, "In");

        final Button loginbtn = (Button) findViewById(R.id.btnLogin);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other = Scene.getSceneForLayout(container,
                        R.layout.trans_mainenter,
                        getApplicationContext());
                //TransitionManager.go(other);
                goToScene(other,"Login");
            }
        });

        final Button startbtn = (Button) findViewById(R.id.btnStart);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other = Scene.getSceneForLayout(container,
                        R.layout.trans_mainenter,
                        getApplicationContext());
                //TransitionManager.go(other);
                goToScene(other, "Start");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void goToScene(Scene scene, final String effect) {
        TransitionSet transition = new TransitionSet();
        Fade fadeOut = new Fade(Fade.OUT);
        fadeOut.setDuration(500);
        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(1000);
        if(effect.equals("In"))
        {
            transition
                    .addTransition(fadeIn);
        } else {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    Log.i("Trans","Started");
                }
                @Override
                public void onTransitionEnd(Transition transition) {
                    if(effect == "Login") {
                        Intent intent = new Intent(StartActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    } else if (effect == "Start"){
                        Intent intent = new Intent(StartActivity.this, Location_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
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

    public void onBackPressed() {
        //
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.putExtra("EXIT", true);
                        //startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        return;
    }
}
