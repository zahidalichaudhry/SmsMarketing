package com.example.thinkgeniux.sms_marketing.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thinkgeniux.sms_marketing.Constants;
import com.example.thinkgeniux.sms_marketing.R;

public class SigniIn extends AppCompatActivity {
 CardView carduser,cardpass;
 EditText username,userpass;
 String stringUsername,stringpass;
 TextView signIn;
 ConstraintLayout parent;
    Constants.TransitionType type;
 float elevation= (float) 10.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);

        //Remove notification bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signi_in);
        carduser=(CardView)findViewById(R.id.carduser);
        username=(EditText)findViewById(R.id.username);
        cardpass=(CardView)findViewById(R.id.cardpass);
        userpass=(EditText)findViewById(R.id.password);
        signIn=(TextView)findViewById(R.id.signin);
        parent=(ConstraintLayout)findViewById(R.id.parent);
        parent.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });
        type = (Constants.TransitionType) getIntent().getSerializableExtra(Constants.KEY_ANIM_TYPE);
        carduser.setCardElevation(0);
        cardpass.setCardElevation(0);
        username.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                carduser.setMinimumWidth(350);
                carduser.setMinimumHeight(100);
                carduser.setCardElevation(20);
            }
        });
        userpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardpass.setMinimumWidth(350);
                cardpass.setMinimumHeight(100);
                cardpass.setCardElevation(20);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringUsername=username.getText().toString();
                stringpass=userpass.getText().toString();
                signIn.setBackground(getResources().getDrawable(R.drawable.whitonblueripple));
//                Intent intent=new Intent(SigniIn.this, MainActivity.class);
//                startActivity(intent);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SigniIn.this);
//                Intent i = new Intent(SigniIn.this, MainActivity.class);
//                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.FadeJava);
//                i.putExtra(Constants.KEY_TITLE, "Fade By Java");
//                startActivity(i, options.toBundle());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SigniIn.this);
                Intent i = new Intent(SigniIn.this, MainActivity.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());
                finish();


            }
        });
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }
    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }


}
