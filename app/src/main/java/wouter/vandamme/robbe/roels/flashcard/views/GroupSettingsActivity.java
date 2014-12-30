package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import db.DBException;
import domain.Group;
import domain.MessageType;
import domain.QuestionFactory;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;
import wouter.vandamme.robbe.roels.flashcard.domain.Observer;
import wouter.vandamme.robbe.roels.flashcard.domain.UploadImageTask;


public class GroupSettingsActivity extends CustomActivity implements Observer {

    private static final int REQUEST_IMAGE = 100;
    private int groupID;
    private String groupname;
    private boolean canInvite,canAddQuestions;
    private Facade facade;
    private String extraInfo ="";

    boolean imageUpload;

    File destination;
    String imagePath;
    String fileName;
    UploadImageTask upload;

    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);
        loadFromIntents();
        loadOldData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_settings, menu);
        return true;
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
                showToast(getResources().getString(R.string.errorGroupUpdate));
            }
        }
        GroupSettingsActivity.super.onBackPressed();
    }



    public void saveAnswer(View view){
        String question;
        String type = "TEXT";
        if(imageUpload){
            type = "IMAGE";
            question = fileName;
        }else {
            question = ((EditText) findViewById(R.id.questionEditText)).getText().toString();
        }
        String answer = ((EditText) findViewById(R.id.answerEditText)).getText().toString();
        facade = Facade.getInstance();
        try {
            Log.v("Question", "Question is : " + question);
            facade.addQuestion(answer, extraInfo, question, type,groupID);
        } catch (DBException e) {
            showToast(e.getMessage());
        }
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("GroupName",groupname);
        intent.putExtra("GroupID",groupID);
        intent.putExtra("canAddQuestions",canAddQuestions);
        intent.putExtra("canInvite",canInvite);
        startActivity(intent);
    }


    public void takePicture(View view){
        fileName = generateFileName();
        destination = new File(Environment.getExternalStorageDirectory(), fileName);
        Log.v("Destination", destination.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK ){
                imagePath = destination.getPath();
                uploadImage(imagePath);
        }
        else{
            showToast("Image not taken!");
        }
    }

    private void uploadImage(String filePath) {
        upload = new UploadImageTask();
        upload.register(this);
        upload.execute(filePath, fileName);
        startDialog();
    }


    private String generateFileName() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date today = Calendar.getInstance().getTime();

        //Random just in case there is a new picture added the same second in the same group.
        Random r = new Random();
        String random = "" +r.nextFloat() * 100;

        return groupname + "-" + df.format(today) + "-" + random + ".jpeg";
    }


    public void addInfo(View view){
        showAddextraInfo();
    }



       // *********** Utility functions *********** \\

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
                showToast("Info saved!");
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void startDialog() {
        barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Uploading image");
        barProgressDialog.setMessage("Please wait.. ");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(100);
        barProgressDialog.show();
    }


    private void loadFromIntents() {
        facade = Facade.getInstance();
        groupname=getIntent().getExtras().getString("GroupName");
        groupID=getIntent().getExtras().getInt("GroupID");
        canAddQuestions=getIntent().getExtras().getBoolean("canAddQuestions");
        canInvite=getIntent().getExtras().getBoolean("canInvite");
    }

    private void loadOldData() {
        try {
            Group g = facade.getGroup(groupID);
            if(!g.getAdmin().getEmail().equals(facade.getCurrentUser().getEmail())){
                findViewById(R.id.adminPanel).setVisibility(View.GONE);
            }
            ((EditText) findViewById(R.id.nameEditText)).setText(groupname);
            ((CheckBox) findViewById(R.id.inviteCheckbox)).setChecked(canInvite);
            ((CheckBox) findViewById(R.id.questionCheckbox)).setChecked(canAddQuestions);
        } catch (DBException e) {
            showToast(getResources().getString(R.string.errorGroupGet));
        }
    }




    @Override
    public void update() {
        if(upload.isDone()){
            if(upload.isComplete()){
                showToast("Image uploaded");
                findViewById(R.id.questionEditText).setClickable(false);
                findViewById(R.id.questionEditText).setFocusable(false);
                findViewById(R.id.questionEditText).setFocusableInTouchMode(false);
                ((EditText)findViewById(R.id.questionEditText)).setCursorVisible(false);
                ((EditText) findViewById(R.id.questionEditText)).setText("Uploaded image");
                imageUpload = true;
            }else{

                showToast(upload.getErrorMessage());
            }
            barProgressDialog.dismiss();

        }else{
            barProgressDialog.setProgress(upload.getProgress());
        }
    }




}
