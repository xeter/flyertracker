package org.xeter.flyertracker.android.flyer.create;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.xeter.flyertracker.android.R;
import org.xeter.flyertracker.android.db.FlyerReaderDbHelper;
import org.xeter.flyertracker.android.flyer.list.Flyer;

import java.io.ByteArrayOutputStream;

public class NewFlyerActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flyer);

        addActionBar();
        addSaveButton();
        addImageView();
    }

    private void addImageView() {
        final ImageView newFlyerTakePictureButton = findViewById(R.id.newFlyerTakePictureButton);
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            newFlyerTakePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takeNewPicture();
                }
            });
        } else {
            newFlyerTakePictureButton.setVisibility(View.INVISIBLE);
        }
    }

    private void addSaveButton() {

        final FlyerReaderDbHelper flyerReaderDbHelper = new FlyerReaderDbHelper(this);

        final FloatingActionButton newFlyerSaveFab = findViewById(R.id.newFlyerSaveFab);
        newFlyerSaveFab.setOnClickListener(new View.OnClickListener() {

            private Flyer flyer = new Flyer();

            @Override
            public void onClick(View view) {
                final String newTitle = ((EditText) findViewById(R.id.newFlyerTitleEditText)).getText().toString();
                final String newDescription = ((EditText) findViewById(R.id.newFlyerDescriptionEditText)).getText().toString();

                this.flyer = flyerReaderDbHelper.addFlyer(new Flyer(flyer.getId(), newTitle, newDescription, imagePath));

                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
            }
        });
    }

    private void addActionBar() {
        final Toolbar toolbar = findViewById(R.id.newFlyerToolBar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void takeNewPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");
            final ImageView flyerPreviewImage = findViewById(R.id.newFlyerTakePictureButton);
            flyerPreviewImage.setImageBitmap(imageBitmap);

            imageUri = getImageUri(getApplicationContext(), imageBitmap);
            imagePath = getRealPathFromURI(imageUri);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
