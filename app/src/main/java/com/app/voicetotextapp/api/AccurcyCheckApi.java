package com.app.voicetotextapp.api;

import com.app.voicetotextapp.api.callback.AccuracyCheckCallback;
import com.app.voicetotextapp.api.data.AccuracyResponse;
import com.google.gson.Gson;

public class AccurcyCheckApi extends ApiHit {
    AccuracyCheckCallback docCallback;

    public AccurcyCheckApi(AccuracyCheckCallback docCallback, String[] params) {
        super("https://api.dandelion.eu/datatxt/sim/v1/?"
                + "text1=" + params[0]
                + "&text2=" + params[1]
                + "&token=" + params[2]);
        this.docCallback = docCallback;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        if (docCallback == null) {
            return;
        }
        AccuracyResponse response = new Gson().fromJson(stringBuilder.toString(), AccuracyResponse.class);
        docCallback.onAccurcyFetched("Language Accuracy : " + (Double.parseDouble(response.getSimilarity())*100) + "%");
    }
}
