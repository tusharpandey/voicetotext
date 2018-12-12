package com.app.voicetotextapp.questionCreator;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupParser extends AsyncTask<String,String,StringBuilder>{
    @Override
    protected StringBuilder doInBackground(String... strings) {
        final StringBuilder builder = new StringBuilder();

        try {
            Document doc = Jsoup.connect("https://docs.google.com/document/d/186l4B08wQqC1UuoCmqRYn4T1rdgirV9VMbIpKZXWIIw/edit").get();
            String title = doc.title();
            Elements links =  doc.getElementsByClass("kix-paragraphrenderer");


            builder.append(title).append("\n");

            for (Element link : links) {
                builder.append("\n").append("Link : ").append(link.attr("href"))
                        .append("\n").append("Text : ").append(link.text());
            }
        } catch (IOException e) {
            builder.append("Error : ").append(e.getMessage()).append("\n");
        }

        return null;
    }
}
