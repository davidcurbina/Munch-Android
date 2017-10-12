package davidurbina.munch;

import android.os.Bundle;
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
import android.widget.ImageView;

public class Login_Activity extends AppCompatActivity {
    ViewGroup container;
    Scene current;
    Scene other;
    Transition mytransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytransition = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition);

        //Assign Scenes
        container = (ViewGroup) findViewById(R.id.container);

        current = Scene.getSceneForLayout(container,
                R.layout.trans_signin_exit,
                this);
        current.enter();

        //TransitionManager.go(other);

    }

    @Override
    public void onResume(){

        super.onResume();
        other = Scene.getSceneForLayout(container,
                R.layout.trans_signin_enter,
                this);
        goToScene(other, "In");

        ImageView back = (ImageView) findViewById(R.id.imgback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void goToScene(Scene scene, String effect) {
        TransitionSet transition = new TransitionSet();
        ChangeBounds changeBounds = new ChangeBounds();
        Fade fadeOut = new Fade(Fade.OUT);
        fadeOut.setDuration(500);
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
                    finish();
                    overridePendingTransition(0, 0);
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
        goToScene(other, "Out");
    }
}

