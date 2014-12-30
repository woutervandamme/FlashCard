package wouter.vandamme.robbe.roels.flashcard.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;

/**
 * Created by Robbe on 30/12/2014.
 */
public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        TextView text;

        public LoadImageTask(ImageView bmImage,TextView text) {
            this.bmImage = bmImage;
            this.text = text;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            text.setVisibility(View.GONE);

        }
}
