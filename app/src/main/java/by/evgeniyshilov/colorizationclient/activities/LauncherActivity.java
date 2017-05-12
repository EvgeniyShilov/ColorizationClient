package by.evgeniyshilov.colorizationclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import by.evgeniyshilov.colorizationclient.R;
import by.evgeniyshilov.colorizationclient.api.DownloadingTask;
import by.evgeniyshilov.colorizationclient.api.UploadingTask;
import by.evgeniyshilov.colorizationclient.utils.CameraProvider;
import by.evgeniyshilov.colorizationclient.utils.GetBitmapTask;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOADING_SUCCESS_MESSAGE = "Loading completed";
    private static final String INTENT_ACTION = "PUSH_MESSAGE_RECEIVED";
    private static final int CODE_REQUEST_CAMERA = 0;
    private static final int CODE_REQUEST_GALLERY = 1;

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button cameraButton;
    private Button galleryButton;
    private Button loadButton;

    private PushHandler handler;

    private Bitmap bitmap;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViews();
        setUpViews();
        handler = new PushHandler(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(handler,
                new IntentFilter(INTENT_ACTION));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(handler);
        super.onDestroy();
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

    private void enableButtons(boolean enable) {
        cameraButton.setEnabled(enable);
        galleryButton.setEnabled(enable);
        loadButton.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        enableButtons(false);
        switch (v.getId()) {
            case R.id.b_camera:
                Pair<Intent, Uri> pair = CameraProvider.getInstance().getCameraIntent(this);
                imageUri = pair.second;
                startActivityForResult(pair.first, CODE_REQUEST_CAMERA);
                break;
            case R.id.b_gallery:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, CODE_REQUEST_GALLERY);
                break;
            case R.id.b_load:
                if (bitmap == null) {
                    enableButtons(true);
                    return;
                }
                AsyncTaskCompat.executeParallel(new UploadingTask(bitmap) {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onPostExecute(Throwable tr) {
                        super.onPostExecute(tr);
                        if (tr != null) enableButtons(true);
                        Toast.makeText(LauncherActivity.this, tr == null ? LOADING_SUCCESS_MESSAGE
                                : tr.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            enableButtons(true);
            return;
        }
        Uri uri = requestCode == CODE_REQUEST_CAMERA ? imageUri
                : requestCode == CODE_REQUEST_GALLERY ? data.getData() : null;
        imageUri = null;
        if (uri == null) {
            enableButtons(true);
            return;
        }
        AsyncTaskCompat.executeParallel(new GetBitmapTask(this, uri) {
            @Override
            protected void onPostExecute(Throwable tr) {
                super.onPostExecute(tr);
                enableButtons(true);
                if (tr == null) {
                    LauncherActivity.this.bitmap = getBitmap();
                    imageView.setImageBitmap(bitmap);
                }
                else Toast.makeText(LauncherActivity.this, tr.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class PushHandler extends BroadcastReceiver {

        private LauncherActivity activity;

        public PushHandler(LauncherActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, final Intent intent) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String id = intent.getStringExtra("id");
                    AsyncTaskCompat.executeParallel(new DownloadingTask(id) {
                        @Override
                        protected void onPostExecute(Throwable tr) {
                            super.onPostExecute(tr);
                            activity.enableButtons(true);
                            activity.progressBar.setVisibility(View.GONE);
                            if (tr == null) {
                                activity.bitmap = getBitmap();
                                activity.imageView.setImageBitmap(activity.bitmap);
                            }
                            else Toast.makeText(activity, tr.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
