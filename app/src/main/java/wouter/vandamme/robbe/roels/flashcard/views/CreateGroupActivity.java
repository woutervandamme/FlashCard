package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import db.DBException;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;

public class CreateGroupActivity extends CustomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;

    }

    public void saveGroup(View view){
        Facade facade = Facade.getInstance();

        String groupname = ((TextView) findViewById(R.id.nameEditText)).getText().toString();
        boolean canIvite = ((CheckBox) findViewById(R.id.inviteCheckbox)).isChecked();
        boolean canCreate = ((CheckBox) findViewById(R.id.questionCheckbox)).isChecked();

        try {
            facade.addGroup(facade.getCurrentUser().getEmail(),groupname,canIvite,canCreate);
        } catch (DBException e) {
            showToast("Error occured when adding a group");
        }

        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);


    }
}
