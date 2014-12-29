package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import db.DBException;
import domain.Group;
import domain.MessageType;
import domain.Question;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;

public class QuestionActivity extends HeaderActivity {

    Question q;
    int groupID;
    String groupname;
    Facade facade;
    Intent toGroupSettings;
    boolean canInvite;
    boolean canAddQuestions;

    public QuestionActivity() {
        super("Answer the question in the text field and press 'validate' to check your answer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        groupname = getIntent().getExtras().getString("GroupName");
        groupID = getIntent().getExtras().getInt("GroupID");
        canInvite = getIntent().getExtras().getBoolean("canInvite");
        canAddQuestions = getIntent().getExtras().getBoolean("canAddQuestions");

        loadQuestion();

        toGroupSettings = new Intent(this, GroupSettingsActivity.class);
    }

    private void loadQuestion() {
        facade = Facade.getInstance();
        try {
            q = facade.getRandomQuestion(groupID);

            TextView questionTitle = (TextView) findViewById(R.id.questionTitleTextView);
            TextView questionText = (TextView) findViewById(R.id.questionTextView);
            if(q!=null) {
                questionTitle.setText("Question #" + q.getId());
                questionText.setText(q.getQuestion());
            }else{
                questionTitle.setText("No Questions!");
                questionText.setText("Use the extra options button (the one next to the gear) and choose: 'Add question'");
                (findViewById(R.id.verfyButton)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.answerEditText)).setVisibility(View.INVISIBLE);
            }

        } catch (DBException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.title_id)).setText(groupname);
    }

    @Override
    public void onResume(){
        super.onResume();
        facade = Facade.getInstance();
        try {
            Group g = facade.getGroup(groupID);
            groupname = g.getName();
            canInvite = g.canUserInviteFriends();
            canAddQuestions = g.canUserAddQuestion();
        } catch (DBException e) {
            e.printStackTrace();
        }
        loadQuestion();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    public void verify(View view){
        Intent verify = new Intent(this, VerificationActivity.class);
        verify.putExtra("extraInfo",q.getExtraInfo());
        TextView answer = (TextView) findViewById(R.id.answerEditText);

        if(answer.getText().toString().equals(q.getAnswer())) {
            verify.putExtra("answercheck", "Correct!");
        }else{
            verify.putExtra("answercheck", "Wrong");
        }
        verify.putExtra("GroupName",groupname);
        verify.putExtra("GroupID",groupID);
        verify.putExtra("canAddQuestions",canInvite);
        verify.putExtra("canInvite",canAddQuestions);
        startActivity(verify);
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
                facade.sendMessage("Invite to "+groupname,"You have been invited to join the group " + groupname + " with id: "+groupID, MessageType.INVITE.toString(),email);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void showToast(String message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,message, duration);
        toast.show();
    }

}
