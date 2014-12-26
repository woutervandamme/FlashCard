package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Robbe on 24/12/2014.
 */
public class HeaderActivity extends Activity {
    protected String extraInfo;
    public HeaderActivity(String extraInfo){ this.extraInfo = extraInfo; }
    protected void extraInfo(View view){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, extraInfo, duration);
        toast.show();
    }
    protected void toMessages(View view){
        Intent intent = new Intent(this, MessagesActivity.class);
        startActivity(intent);
    }
    protected void toSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


}
