package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import db.DBException;
import domain.Group;
import domain.MessageType;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;


public class GroupSettingsActivity extends CustomActivity {

    private int groupID;
    private String groupname;
    private boolean canInvite,canAddQuestions;
    private Facade facade;
    private String extraInfo ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        facade = Facade.getInstance();
        groupname=getIntent().getExtras().getString("GroupName");
        Log.v("Groupname in settings", groupname);
        groupID=getIntent().getExtras().getInt("GroupID");
        canAddQuestions=getIntent().getExtras().getBoolean("canAddQuestions");
        canInvite=getIntent().getExtras().getBoolean("canInvite");
        try {
            Group g = facade.getGroup(groupID);
            if(!g.getAdmin().getEmail().equals(facade.getCurrentUser().getEmail())){
                findViewById(R.id.adminPanel).setVisibility(View.GONE);
            }
            ((EditText) findViewById(R.id.nameEditText)).setText(groupname);
            ((CheckBox) findViewById(R.id.inviteCheckbox)).setChecked(canInvite);
            ((CheckBox) findViewById(R.id.questionCheckbox)).setChecked(canAddQuestions);
        } catch (DBException e) {
           showToast( getResources().getString(R.string.errorGroupGet));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_settings, menu);
        return true;
    }

    public void saveAnswer(View view){
        ((EditText) findViewById(R.id.questionEditText)).getText();
        ((EditText) findViewById(R.id.answerEditText)).getText();
    }

    public void addInfo(View view){
        showAddextraInfo();


    }

    private void showAddextraInfo() {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Extra info");
            alert.setMessage("Add extra info to the question");

            final EditText input = new EditText(this);
            input.setText(extraInfo);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    extraInfo = input.getText().toString();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
    }


    @Override
    public void onBackPressed(){
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        boolean invite = ((CheckBox) findViewById(R.id.inviteCheckbox)).isChecked();
        boolean question = ((CheckBox) findViewById(R.id.questionCheckbox)).isChecked();

        if(!(groupname.equals(name)&&invite==canInvite&&question==canAddQuestions)){
            try {
                facade.updateGroup(groupID,name,canInvite,canAddQuestions);
            } catch (DBException e) {
                showToast( getResources().getString(R.string.errorGroupUpdate));
            }
        }
        GroupSettingsActivity.super.onBackPressed();
    }


}
