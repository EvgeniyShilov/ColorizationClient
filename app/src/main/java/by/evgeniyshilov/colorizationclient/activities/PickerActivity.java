package by.evgeniyshilov.colorizationclient.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import by.evgeniyshilov.colorizationclient.R;
import by.evgeniyshilov.colorizationclient.utils.BitmapHolder;
import by.evgeniyshilov.colorizationclient.utils.CameraProvider;
import by.evgeniyshilov.colorizationclient.utils.GetBitmapTask;

public class PickerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CODE_REQUEST_CAMERA = 0;
    private static final int CODE_REQUEST_GALLERY = 1;

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button cameraButton;
    private Button galleryButton;
    private Button loadButton;

    private Bitmap bitmap;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViews();
        setUpViews();
    }

    private void findViews() {
        imageView = (ImageView) findViewById(R.id.iv);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        cameraButton = (Button) findViewById(R.id.b_camera);
        galleryButton = (Button) findViewById(R.id.b_gallery);
        loadButton = (Button) findViewById(R.id.b_load);
    }

    private void setUpViews() {
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        loadButton.setOnClickListener(this);
    }

    private void enableControls(boolean enable) {
        progressBar.setVisibility(enable ? View.GONE : View.VISIBLE);
        cameraButton.setEnabled(enable);
        galleryButton.setEnabled(enable);
        loadButton.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        enableControls(false);
        switch (v.getId()) {
            case R.id.b_camera:
                Pair<Intent, Uri> pair = CameraProvider.getCameraIntent(this);
                imageUri = pair.second;
                startActivityForResult(pair.first, CODE_REQUEST_CAMERA);
                break;
            case R.id.b_gallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, CODE_REQUEST_GALLERY);
                break;
            case R.id.b_load:
                if (bitmap != null) {
                    BitmapHolder.save(bitmap);
                    startActivity(new Intent(this, ColorizationActivity.class));
                }
                enableControls(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = requestCode == CODE_REQUEST_CAMERA ? imageUri
                    : requestCode == CODE_REQUEST_GALLERY ? data.getData() : null;
            imageUri = null;
            if (uri != null) {
                AsyncTaskCompat.executeParallel(new GetBitmapTask(this, uri) {
                    @Override
                    protected void onPostExecute(Throwable tr) {
                        super.onPostExecute(tr);
                        enableControls(true);
                        if (tr == null) {
                            PickerActivity.this.bitmap = getBitmap();
                            imageView.setImageBitmap(bitmap);
                        } else Toast.makeText(PickerActivity.this, tr.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
        }
        enableControls(true);
    }
}
