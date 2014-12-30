package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import wouter.vandamme.robbe.roels.flashcard.R;

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


    public String checkInputVerification(EditText text){
        if(text.getText().toString() == null || text.getText().toString().isEmpty()){
            text.setError(getResources().getString(R.string.errorEmptyField));
            return null;
        } else {
            return text.getText().toString();
        }

    }

}
