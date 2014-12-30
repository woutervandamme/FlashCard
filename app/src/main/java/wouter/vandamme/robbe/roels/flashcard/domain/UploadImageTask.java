package wouter.vandamme.robbe.roels.flashcard.domain;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Robbe on 30/12/2014.
 */
public class UploadImageTask extends AsyncTask<String, Void, String> implements Subject{

    ArrayList<Observer> observers = new ArrayList<Observer>();

    /************* Php script path ****************/
    String upLoadServerUri = "http://www.twittervoetbal.be/blub.php";
    boolean done = false;
    boolean complete = false;

    String errorMessage;

    long fileSize,bytesDone;


    @Override
    protected String doInBackground(String... urls) {
        String filepath = urls[0];
        String fileName = urls[1];

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(filepath);
        int serverResponseCode = 0;



        if (!sourceFile.isFile()) {
            errorMessage = "The path you gave does not point to a file.";
            done = true;
        }else{
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                fileSize = sourceFile.getTotalSpace();
                bytesDone = 0;

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    bytesDone += bytesRead;
                    notifyObservers();
                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.v("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    complete = true;
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (FileNotFoundException e) {
                errorMessage = "Your image could not be found.";
                done = true;

            } catch (MalformedURLException e) {
                errorMessage = "Something went wrong connecting to the server. Try again later";
                done = true;

            } catch (ProtocolException e) {
                errorMessage = "There was a protocol error, please contact the app admin";
                done = true;


            } catch (IOException e) {
                errorMessage = "There was a write or read error, please try again.";
                done = true;

            }
            done = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        notifyObservers();
    }

    @Override
    public void register(Observer observer){
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers){
            o.update();
        }
    }

    @Override
    public boolean getUpdate() {
        return done;
    }

    public boolean isDone(){
        return done;
    }
    public boolean isComplete(){
        return complete;
    }
    public String getErrorMessage(){
        return errorMessage;
    }

    public int getProgress(){
       return (int) ((bytesDone/fileSize) * 100);
    }


}
