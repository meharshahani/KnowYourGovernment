package com.example.knowyourgovernment2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

class AsyncDataLoader extends AsyncTask<Void, Void, String>
{
    private String ApiUrl;
    private MainActivity mainActivity;
    private ArrayList<Official> officialList;

    //  private String prefix = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCLrFehyy_mpVOzQvUBBxOyzipBreztRgk&address=";

    AsyncDataLoader(MainActivity mainActivity, ArrayList<Official> officialList, String location)
    {
        this.mainActivity = mainActivity;
        this.officialList = officialList;
        String header = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
        String footer = String.format("&address=%s", location);

        ApiUrl = String.format("%s%s%s", header,
                mainActivity.getApplicationContext().getString(R.string.API_KEY), footer);
    }


    @Override
    protected String doInBackground(Void... voids)
    {
        JSONObject jsonObject = getDataOnStock(this.ApiUrl);
        if (jsonObject == null)
            return null;

        // clear the existing list
        officialList.clear();

        // replace it with the new representatives
        String location = parseJson(jsonObject);
        return location;    }

    @Override
    protected void onPostExecute(String location)
    {
        mainActivity.updateRecyclerView();
        mainActivity.updateLocation(location);
    }


    private JSONObject getDataOnStock(String url_)
    {
        JSONObject jsonObject = null;
        String urlToUse = Uri.parse(url_).toString();

        try
        {
            java.net.URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw (new Exception());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append('\n');
            }
            jsonObject = new JSONObject(sb.toString());
            return jsonObject;
        }
        catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }
    }

    private String parseJson(JSONObject jsonObject)
    {
        try
        {
            String location = getLocation(jsonObject);

            JSONArray offices = jsonObject.getJSONArray("offices");
            JSONArray officials = jsonObject.getJSONArray("officials");

            // For each office in the offices JSON Array
            for(int i =0; i < offices.length(); ++i)
            {
                JSONObject office = offices.getJSONObject(i);

                // Get the name of the office
                String officeName = office.getString("name");

                // Get all people working in this office
                JSONArray officialIndices = office.getJSONArray("officialIndices");
                for (int j=0; j < officialIndices.length(); ++j)
                {
                    Official newOffice = new Official();
                    newOffice.setOfficeName(officeName);

                    int officialIndex = officialIndices.getInt(j);
                    JSONObject official = officials.getJSONObject(officialIndex);
                    setOfficial(official, newOffice);

                    // add the new representative to the list
                    officialList.add(newOffice);
                }
            }
            return location;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLocation(JSONObject jsonObject)
    {
        JSONObject normalizedInput = null;
        try
        {
            normalizedInput = jsonObject.getJSONObject("normalizedInput");
            StringBuilder address = new StringBuilder();
            address.append(normalizedInput.getString("city"));
            address.append(", ");
            address.append(normalizedInput.getString("state"));
            if(!normalizedInput.getString("zip").isEmpty())
            {
                address.append(", ");
                address.append(normalizedInput.getString("zip"));
            }
            return address.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void setOfficial(JSONObject official, Official newOffice)
    {
        try
        {
            newOffice.setOfficialName(official.getString("name"));

            if (!official.isNull("address"))
            {
                JSONObject addressObj = official.getJSONArray("address").getJSONObject(0);
                String address = addressObj.getString("line1");
                if (!addressObj.isNull("line2"))
                    address = address.concat(addressObj.getString("line2"));
                if (!addressObj.isNull("line3"))
                    address = address.concat(addressObj.getString("line3"));

                address = address.concat(", " + addressObj.getString("city"));
                address = address.concat(", " + addressObj.getString("state"));
                address = address.concat(", " + addressObj.getString("zip"));

                newOffice.setAddress(address);
            }
            /* Party */
            if (!official.isNull("party"))
                newOffice.setParty(official.getString("party"));

            if (!official.isNull("phones"))
                newOffice.setPhone(official.getJSONArray("phones").getString(0));

            if (!official.isNull("urls"))
                newOffice.setUrl(official.getJSONArray("urls").getString(0));

            if (!official.isNull("emails"))
                newOffice.setEmail(official.getJSONArray("emails").getString(0));

            if (!official.isNull("photoUrl"))
                newOffice.setImageURL(official.getString("photoUrl"));

            if (!official.isNull("channels"))
            {
                JSONArray channels = official.getJSONArray("channels");
                for (int i =0; i < channels.length(); ++i)
                {
                    JSONObject obj = channels.getJSONObject(i);
                    String key = obj.getString("type");
                    String value = obj.getString("id");
                    newOffice.addChannelPair(key, value);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
