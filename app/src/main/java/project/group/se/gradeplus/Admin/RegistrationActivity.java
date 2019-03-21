package project.group.se.gradeplus.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import project.group.se.gradeplus.R;
import project.plusPlatform.Lecturer;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;
import project.plusPlatform.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private RadioButton student;
    private RadioButton lecturer;
    private Button registerButton;
    private Registry registry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registry = Registry.getInstance();
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.field_name);
        email = findViewById(R.id.field_eamil);
        password = findViewById(R.id.field_password);
        student = findViewById(R.id.type_option_student);
        lecturer = findViewById(R.id.type_option_lecturer);
        registerButton = findViewById(R.id.register_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = name.getText().toString();
                String user_email = email.getText().toString();
                String user_pass = password.getText().toString();
                String type = "";
                User user = null;
                if(student.isChecked()){
                    type="Student";
                    user = new Student(registry.getTotalStudents()+1,user_email,user_pass);
                    user.setName(user_name);
                }else {
                    type="Lecturer";
                    user = new Lecturer(registry.getLecturers().size(),user_email,user_pass);
                    user.setName(user_name);
                }
                registry.startDatabase(getApplicationContext());
                if(registry.addUser(user,type)){
                    Toast.makeText(RegistrationActivity.this, type+" is added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegistrationActivity.this, type+" is not added", Toast.LENGTH_SHORT).show();
                }
                registry.stopDatabase();
            }
        });
    }
}
