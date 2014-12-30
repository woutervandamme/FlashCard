package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import db.DBException;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;

public class SettingsActivity extends CustomActivity {


    EditText nameEditText, oldPasswordEditText, passwordEditText, repeatPasswordEditText;
    Facade facade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        oldPasswordEditText= (EditText) findViewById(R.id.oldPasswordEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        repeatPasswordEditText = (EditText) findViewById(R.id.repeatPasswordEditText);
        facade = Facade.getInstance();
        nameEditText.setText(facade.getCurrentUser().getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void saveSettings(View view){
        String name = nameEditText.getText().toString();

        if(oldPasswordEditText.getText().toString().equals(facade.getCurrentUser().getPw())){
            if(validateForm(nameEditText)) {
                if (!passwordEditText.getText().toString().isEmpty()) {
                    String pw = passwordEditText.getText().toString();
                    String pwRep = repeatPasswordEditText.getText().toString();
                    if (pw.equals(pwRep)) {
                        try {
                            facade.updateUser(name, pw);
                            Intent intent = new Intent(this, MenuActivity.class);
                            startActivity(intent);
                        } catch (DBException e) {
                            showToast("Error in database. Please try again later");
                        }
                    } else {
                        showToast("Your passwords do not match, please try again");
                        passwordEditText.setText("");
                        repeatPasswordEditText.setText("");
                    }
                } else {
                    if (!name.equals(facade.getCurrentUser().getName())) {
                        try {
                            facade.updateUser(name, facade.getCurrentUser().getPw());
                            Intent intent = new Intent(this, MenuActivity.class);
                            startActivity(intent);
                        } catch (DBException e) {
                            showToast("Error in database. Please try again later");
                        }
                    }else{
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }else{
            showToast("Your old password is incorrect");
            passwordEditText.setText("");
            repeatPasswordEditText.setText("");
            oldPasswordEditText.setText("");
        }

    }

}
