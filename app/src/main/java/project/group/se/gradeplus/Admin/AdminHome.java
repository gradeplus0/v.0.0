package project.group.se.gradeplus.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import project.group.se.gradeplus.R;

public class AdminHome extends AppCompatActivity {

    private Button moduleButton;
    private Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        moduleButton = findViewById(R.id.module_buttom);
        registrationButton = findViewById(R.id.registration_button);
        moduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,ModuleActivity.class);
                startActivity(intent);
                System.out.println("Module button pressed");
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this,RegistrationActivity.class);
                startActivity(intent);
                System.out.println("Registration button pressed");
            }
        });
    }
}
