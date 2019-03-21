package project.group.se.gradeplus.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project.group.se.gradeplus.R;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class CreateModuleActivity extends AppCompatActivity {

    private EditText name;
    private EditText credit;
    private Button submit;
    private Registry  registry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_module);
        name = findViewById(R.id.module_name);
        registry = Registry.getInstance();
        submit = findViewById(R.id.module_add_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moduleName = name.getText().toString();
                registry.startDatabase(getApplicationContext());
                if(registry.addModuleToDB(new Module(registry.getTotalModules(),moduleName))){
                    Toast.makeText(CreateModuleActivity.this, "Module is added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateModuleActivity.this, "Module exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
