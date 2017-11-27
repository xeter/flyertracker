package org.xeter.flyertracker.android.flyer.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.xeter.flyertracker.android.R;
import org.xeter.flyertracker.android.flyer.TextKeys;

public class FlyerDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyer_details);

        addActionBar();

        final Intent intent = getIntent();
        final String title = intent.getStringExtra(TextKeys.FLYER_TITLE);
        final String description = intent.getStringExtra(TextKeys.FLYER_DESCRIPTION);
        final String imgPath = intent.getStringExtra(TextKeys.FLYER_IMG_PATH);

        final TextView flyerDetailsTitleTextView = findViewById(R.id.flyer_details_title);
        flyerDetailsTitleTextView.setText(title);

        final TextView flyerDetailsDescriptionTextView = findViewById(R.id.flyer_details_description);
        flyerDetailsDescriptionTextView.setText(description);

        final ImageView imgView = findViewById(R.id.flyer_details_img);
        imgView.setImageURI(Uri.parse(imgPath));

        final TextView recordText = findViewById(R.id.flyer_details_record_text);
        final Animation blinkAnimation = AnimationUtils.loadAnimation(FlyerDetailsActivity.this, R.anim.blink);
        recordText.setVisibility(View.INVISIBLE);

        final Animation gettingVisibleAnimation = AnimationUtils.loadAnimation(FlyerDetailsActivity.this, R.anim.getting_visible);
        final ImageButton stopStartButton = findViewById(R.id.flyer_details_button_stop_start);
        stopStartButton.setOnClickListener(new View.OnClickListener() {

            private boolean recording = false;

            @Override
            public void onClick(View v) {
                if (recording) {
                    changeState(android.R.drawable.ic_notification_overlay, View.INVISIBLE);
                    recordText.clearAnimation();
                } else {
                    changeState(android.R.drawable.ic_media_pause, View.VISIBLE);
                    recordText.startAnimation(blinkAnimation);
                }
            }

            private void changeState(final int imageResource, final int recordTextVisibility) {
                stopStartButton.setImageResource(imageResource);
                recordText.setVisibility(recordTextVisibility);
                recording = !recording;
                stopStartButton.startAnimation(gettingVisibleAnimation);
            }
        });
    }

    private void addActionBar() {
        final Toolbar toolbar = findViewById(R.id.flyerDetailsToolBar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
