package project.group.se.gradeplus.Lecturer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class CreateWorkActivity extends AppCompatActivity {

    public static final String MODULE_ID = "MODULEID";
    public static final String MODULE_NAME = "MODULENAME";
    private Module module;
    private EditText name;
    private Button button;
    private Registry registry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_work);
        Intent intent = getIntent();
        module = new Module(intent.getIntExtra(MODULE_ID,0),intent.getStringExtra(MODULE_NAME));
        registry = Registry.getInstance();
        registry.startDatabase(this);
        name = findViewById(R.id.assessed_work_name);
        button = findViewById(R.id.work_add_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namep = name.getText().toString();
                if(namep.isEmpty()){
                    Toast.makeText(CreateWorkActivity.this, "Please enter the name", Toast.LENGTH_SHORT).show();
                }else{
                    AssessedWork work = new AssessedWork(0,namep);
                    if(registry.addAssessedWork(module,work))
                        Toast.makeText(CreateWorkActivity.this, "Work is created", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(CreateWorkActivity.this, "Work is not created due to database problem", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
