package wouter.vandamme.robbe.roels.flashcard.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import db.DBException;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;
import wouter.vandamme.robbe.roels.flashcard.domain.JSONWebDB;

public class MainActivity extends Activity {

    EditText email;
    EditText password;
    Facade facade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
    }

    public void toRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    public void login(View view){
        facade = Facade.getInstance();
        facade.setDatabase(new JSONWebDB());
        String emailTrimmedString = email.getText().toString().trim();
        String passwordString = password.getText().toString();
        try {
            if (emailTrimmedString != null && passwordString != null) {
                if(facade == null){
                    Log.v("DATA", "Facade null");
                }

                if (facade.login(emailTrimmedString, passwordString)) {
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    showToast("We did not find your email and password combination, please try again.");
                }
            }
        }catch(DBException e){
            showToast("Something went wrong connecting to the database, please try again later.");
        }
    }


    //Maybe this could be moved to a static "Functionalities" class.
    //If there is a use case for it.
    private void showToast(String message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,message, duration);
        toast.show();

    }


}
