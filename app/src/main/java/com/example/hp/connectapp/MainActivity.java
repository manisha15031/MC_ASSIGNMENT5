package com.example.hp.connectapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    TextView text1, text2;
    Button button;
    String webURL= "https://www.iiitd.ac.in/about";
    String consoleView;
    private String output = null;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.textView);
        text2 = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.btn);

        if(savedInstanceState!=null) {
            output = savedInstanceState.getString("output");
            text2.setText(output);
            text2.setMovementMethod(new ScrollingMovementMethod());
            text2.setTextColor(Color.parseColor("#ffffff"));
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                button.setEnabled(false);
                AsyncTaskClass asyncTaskClass = new AsyncTaskClass();
                asyncTaskClass.execute(webURL);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("output", output);
    }

    public class AsyncTaskClass extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            String path=params[0];
            int file_length=0;
            try {
                Document document = Jsoup.connect(webURL).get();
                consoleView = document.text();
                return (document.getElementsByTag("title").text());
                //return document.text();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Downloading done!!";
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Downloading Data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            if(consoleView==null) {
                Toast.makeText(getApplicationContext(), "No network connection!!", Toast.LENGTH_LONG).show();
                return;
            }
            Log.d("MainActivity",consoleView);
            text2.setMovementMethod(new ScrollingMovementMethod());
            text2.setText(result);
            text2.setTextColor(Color.parseColor("#ffffff"));
            output=result;
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }

}
