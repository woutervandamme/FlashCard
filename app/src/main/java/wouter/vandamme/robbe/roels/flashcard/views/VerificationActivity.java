package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import db.DBException;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;


public class VerificationActivity extends HeaderActivity {
    Facade facade;

    String groupname;
    int groupID;
    boolean canInvite;
    boolean canAddQuestions ;
    Intent toGroupSettings;

    public VerificationActivity(){
        super("Here you find some extra information on the question at hand, if you want you can give feedback about the question, submit an edit of the quesiton or just continiue to the next question");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        facade = Facade.getInstance();
        ((TextView) findViewById(R.id.responseTextView)).setText(getIntent().getExtras().getString("answercheck"));
        ((TextView) findViewById(R.id.extraInfoTextView)).setText(getIntent().getExtras().getString("extraInfo"));
        ((TextView) findViewById(R.id.title_id)).setText(getIntent().getExtras().getString("GroupName"));
    }

    public void nextQuestion(View view){
        Intent verify = new Intent(this,QuestionActivity.class);

        groupname = getIntent().getExtras().getString("GroupName");
        groupID = getIntent().getExtras().getInt("GroupID");
        canInvite = getIntent().getExtras().getBoolean("canInvite");
        canAddQuestions = getIntent().getExtras().getBoolean("canAddQuestions");

        verify.putExtra("GroupName",groupname);
        verify.putExtra("GroupID",groupID);
        verify.putExtra("canAddQuestions",canAddQuestions);
        verify.putExtra("canInvite",canInvite);
        startActivity(verify);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verification, menu);
        return true;
    }

    public void share(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choice)
                .setItems(R.array.choices,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            if(canInvite) {
                                showEmailInputDialog();
                            }else{
                                showToast("You do not have the privileges to invite users");
                            }

                        }else{
                            try{
                                facade = Facade.getInstance();
                                if(canAddQuestions || (facade.getGroupAdmin(groupID).getEmail().equals(facade.getCurrentUser().getEmail()))) {
                                    toGroupSettings.putExtra("GroupName", groupname);
                                    toGroupSettings.putExtra("GroupID", groupID);
                                    toGroupSettings.putExtra("canAddQuestions", canInvite);
                                    toGroupSettings.putExtra("canInvite", canAddQuestions);
                                    startActivity(toGroupSettings);
                                }else{
                                    showToast("You do not have enough rights");
                                }
                            } catch (DBException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        builder.create().show();
    }
    public void showEmailInputDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Invite user");
        alert.setMessage("Insert the e-mail of the user you wish to invite");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = input.getText().toString();
                if (facade == null) {
                    facade = Facade.getInstance();
                }
                if(email.isEmpty()){
                    showToast("Empty textfield try again!");
                }else {
                    try{
                        if(facade.getUser(email)!= null){
                            facade.addUserToGroup(groupID,email);
                        }else{
                            showToast("User not found try again");
                        }
                    } catch (DBException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Just closes.
            }
        });

        alert.show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }




}
