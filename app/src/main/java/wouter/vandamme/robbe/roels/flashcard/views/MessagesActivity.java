package wouter.vandamme.robbe.roels.flashcard.views;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import db.DBException;
import domain.Message;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;


public class MessagesActivity extends HeaderActivity {

    public ArrayList<String> messagesLijst;

    public MessagesActivity(){
        super("List of messages, tap on a message to read it.");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Facade facade = Facade.getInstance();

        ArrayList<Message> messages = null;


        //fetch de messages
        try {
            messages = facade.getMessagesForUser(facade.getCurrentUser().getEmail());
        } catch (DBException e) {
            showToast( getResources().getString(R.string.errorMessagesGet));
        }

        for(int i = 0 ; i < messages.size();i++){
            messagesLijst.add(messages.get(i).getTitle());
        }

        ListView messageList = (ListView)findViewById(R.id.messageListView);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messagesLijst);


        messageList.setAdapter(itemsAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

}
