package nat.flashcardcompetition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StartUpActivity extends AppCompatActivity {

    EditText name_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        name_view = (EditText) findViewById(R.id.startup_username);
        name_view.requestFocus();
    }

    public void login(View target){

        String name = name_view.getText().toString();

        if(!name.matches("^\\p{L}[\\p{L}\\s]*\\p{L}$") || name.equals("")){
            Toast.makeText(this, "The accepted name contains only letters.", Toast.LENGTH_LONG).show();
            return ;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.putString("name", name);
        editor.putString("android_id", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        editor.apply();

        Intent intent = new Intent(this, StudysetViewActivity.class);
        startActivity(intent);
        finish();
    }
}
