package com.app.voicetotextapp.api;

import com.app.voicetotextapp.api.callback.AccuracyCheckCallback;
import com.app.voicetotextapp.api.data.AccuracyResponse;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.MathContext;

public class AccurcyCheckApi extends ApiHit {
    AccuracyCheckCallback docCallback;

    public AccurcyCheckApi(AccuracyCheckCallback docCallback, String[] params) {
        super("https://api.dandelion.eu/datatxt/sim/v1/?"
                + "text1=" + params[0]
                + "&text2=" + params[1]
                + "&token=" + params[2]
                + "&bow=always");
        this.docCallback = docCallback;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        if (docCallback == null) {
            return;
        }

        try {
            MathContext m = new MathContext(4);
            AccuracyResponse response = new Gson().fromJson(stringBuilder.toString(), AccuracyResponse.class);
            BigDecimal value = new BigDecimal(response.getSimilarity());
            docCallback.onAccurcyFetched("Language Accuracy : " + value.multiply(new BigDecimal(100), m) + "%");
        } catch (Exception e) {
            e.printStackTrace();
            docCallback.onAccurcyFetched("Something went wrong");
        }
    }
}
