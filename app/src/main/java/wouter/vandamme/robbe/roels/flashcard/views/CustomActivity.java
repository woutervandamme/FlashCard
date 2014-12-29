package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by wouter on 29/12/14.
 */
public class CustomActivity extends Activity {


    public void showToast(String message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,message, duration);
        toast.show();
    }

}
