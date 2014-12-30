package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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


    public boolean validateForm(EditText ... form)  {
        boolean valid = true;
        for(int i = 0; i < form.length ; i++) {
            if(form[i].getText().toString().isEmpty() || form[i].getText().toString()==null){
                showToast("you entered some incorrect values,  try again ") ;
                form[i].setHintTextColor(Color.RED);
                valid = false;
            }
        }
        return valid;
    }

}
