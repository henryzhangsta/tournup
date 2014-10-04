package mipsmob.tournup.utilities;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class JSONUtilities {

    private static String getUrlBody(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int character = reader.read();

        while (character != -1) {
            builder.append((char) character);
            character = reader.read();
        }

        return builder.toString();
    }

    public static void readJSONFromUrl(String url, String name, Callback callback) {
        new ReadJSONTask(url, name, callback).execute("");
    }

    private static class ReadJSONTask extends AsyncTask<String, Void, Void> {

        String url;
        String name;
        Callback callback;

        public ReadJSONTask(String url, String name, Callback callback) {
            this.url = url;
            this.name = name;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                InputStream input = (new URL(url)).openStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
                String jsonText = getUrlBody(buffer);
                JSONObject json = new JSONObject(jsonText);
                JSONArray jsonArray = json.getJSONArray(name);
                input.close();

                callback.onDataRetrieved(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onRetrievalFailed();
            }

            return null;
        }

    }

 }