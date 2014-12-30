package wouter.vandamme.robbe.roels.flashcard.views;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import db.DBException;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;

public class RegisterActivity extends CustomActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}


    public void register(View view){
        EditText emailTextEdit = (EditText) findViewById(R.id.emailTextEdit);
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText repeatPasswordEditText = (EditText) findViewById(R.id.repeatPasswordEditText);

        if(validateForm(emailTextEdit,nameEditText,passwordEditText,repeatPasswordEditText)){
            Facade facade = Facade.getInstance();
            String pw = passwordEditText.getText().toString();
            String pwRep = repeatPasswordEditText.getText().toString();
            if(pw.equals(pwRep)) {
                String email = emailTextEdit.getText().toString();
                String name  = nameEditText.getText().toString();
                try {
                    facade.addUser(email,name,pw);
                    Intent intent = new Intent(this,MainActivity.class);
                    showToast("Account created, login with your email and password");
                    startActivity(intent);
                } catch (DBException e) {
                    showToast("Something went wrong connecting to the database");
                }
            }else{
                showToast("Your passwords do not match, try again");
                passwordEditText.setText("");
                repeatPasswordEditText.setText("");
            }


        }

    }

}
