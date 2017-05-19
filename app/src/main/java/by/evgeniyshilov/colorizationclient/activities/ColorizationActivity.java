package by.evgeniyshilov.colorizationclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import by.evgeniyshilov.colorizationclient.R;
import by.evgeniyshilov.colorizationclient.api.DownloadingTask;
import by.evgeniyshilov.colorizationclient.api.UploadingTask;
import by.evgeniyshilov.colorizationclient.utils.BitmapHolder;

public class ColorizationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String INTENT_ACTION = "PUSH_MESSAGE_RECEIVED";
    private static final String LOADING_SUCCESS_MESSAGE = "Loading completed";

    private ImageView imageView;
    private ProgressBar progressBar;
    private Button backButton;

    private PushHandler handler;
    private AsyncTask currentTask;

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorization);
        findViews();
        backButton.setOnClickListener(this);
        bitmap = BitmapHolder.get();
        imageView.setImageBitmap(bitmap);
        handler = new PushHandler(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(handler,
                new IntentFilter(INTENT_ACTION));
        currentTask = new UploadingTask(bitmap) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Throwable tr) {
                super.onPostExecute(tr);
                if (tr != null) progressBar.setVisibility(View.GONE);
                Toast.makeText(ColorizationActivity.this, tr == null ? LOADING_SUCCESS_MESSAGE
                        : tr.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        AsyncTaskCompat.executeParallel(currentTask);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(handler);
        super.onDestroy();
    }

    private void findViews() {
        imageView = (ImageView) findViewById(R.id.iv);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        backButton = (Button) findViewById(R.id.b_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_back:
                if (currentTask != null && !currentTask.isCancelled())
                    currentTask.cancel(true);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentTask != null && !currentTask.isCancelled())
            currentTask.cancel(true);
        super.onBackPressed();
    }

    private static class PushHandler extends BroadcastReceiver {

        private ColorizationActivity activity;

        public PushHandler(ColorizationActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, final Intent intent) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String id = intent.getStringExtra("id");
                    activity.currentTask = new DownloadingTask(id) {
                        @Override
                        protected void onPostExecute(Throwable tr) {
                            super.onPostExecute(tr);
                            activity.progressBar.setVisibility(View.GONE);
                            if (tr == null) {
                                activity.bitmap = getBitmap();
                                activity.imageView.setImageBitmap(activity.bitmap);
                            } else Toast.makeText(activity, tr.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    };
                    AsyncTaskCompat.executeParallel(activity.currentTask);
                }
            });
        }
    }
}
