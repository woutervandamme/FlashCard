package wouter.vandamme.robbe.roels.flashcard.domain;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Robbe on 26/12/2014.
 */
public class DatabaseTask extends AsyncTask<String, Void, String>  {
    private String data = null;
    private String error = null;
    private boolean done = false;
    private boolean fetched = false;
    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader = null;

        Log.v("DBTASK","Before try just entered doInBackground");
        try {
            // Defined URL where to send data
            URL url = new URL(params[0]);
            // Send POST data request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(params[1].getBytes().length));
            connection.setUseCaches (false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes(params[1]);
            wr.flush();
            wr.close();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            Log.v("LINE","ready to read line");
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                Log.v("LINE","line is: " +line);
                sb.append(line + "");
            }

            // Append Server Response To Content String
            data = sb.toString();
            Log.v("Data",data);
        } catch (Exception ex) {
            Log.v("DBTASK","Error: "+ ex );
            error = ex.getMessage();
            done = true;
        } finally {
            try {
                reader.close();
            }

            catch (Exception ex) {
                Log.v("DBTASK","reader error");
                done = true;
            }
        }
        if(data!=null) {
            fetched = true;
        }
        done = true;

        return null;
    }

    public boolean fetched(){
        return fetched;
    }

    public boolean done(){
        return done;
    }

    public String getJsonString(){
        return data;
    }

}
