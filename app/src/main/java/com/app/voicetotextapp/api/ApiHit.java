package com.app.voicetotextapp.api;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ApiHit extends AsyncTask<String, String, StringBuilder> {

    private String urlString;
    private StringBuilder sb;

    public ApiHit(String url) {
        this.urlString = url;
    }

    @Override
    protected StringBuilder doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return sb;
    }

    abstract protected void onPostExecute(StringBuilder stringBuilder);
}
