package wouter.vandamme.robbe.roels.flashcard.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import db.DBException;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;
import wouter.vandamme.robbe.roels.flashcard.domain.JSONWebDB;

public class MainActivity extends CustomActivity {

    EditText email;
    EditText password;
    Facade facade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

        //TODO Remove this before sending in.
        email.setText("test@test.lol");
        password.setText("test");
        //Until here.

        facade = Facade.getInstance();
        facade.setDatabase(new JSONWebDB());
    }

    public void toRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    public void login(View view){
        if(validateForm(email,password)) {
            String emailTrimmedString = email.getText().toString().trim();
            String passwordString = password.getText().toString();
            try {
                if (emailTrimmedString != null && passwordString != null) {
                    if (facade == null) {
                        Log.v("DATA", "Facade null");
                    }

                    if (facade.login(emailTrimmedString, passwordString)) {
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    } else {
                        showToast(getResources().getString(R.string.errorLogin));
                    }
                }
            } catch (DBException e) {
                showToast(getResources().getString(R.string.errorDatabase));
            }
        }
    }



}
