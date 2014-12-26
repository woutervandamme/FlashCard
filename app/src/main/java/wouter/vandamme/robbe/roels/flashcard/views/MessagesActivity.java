package wouter.vandamme.robbe.roels.flashcard.views;

import android.os.Bundle;
import android.view.Menu;

import wouter.vandamme.robbe.roels.flashcard.R;


public class MessagesActivity extends HeaderActivity {

    public MessagesActivity(){
        super("List of messages, tap on a message to read it.");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

}
