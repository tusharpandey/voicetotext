package com.app.voicetotextapp.questionCreator;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DocReader extends AsyncTask<String, String, StringBuilder> {

    DocCallback docCallback;

    public DocReader(DocCallback docCallback) {
        this.docCallback = docCallback;
    }

    private StringBuilder sb;

    @Override
    protected StringBuilder doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://raw.githubusercontent.com/livecareetest/VoiceToText/master/samplefile?raw=true");

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

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        if (docCallback == null) {
            return;
        }
        docCallback.onQuestionFetched(stringBuilder == null ? "" : stringBuilder.toString());
    }
}
