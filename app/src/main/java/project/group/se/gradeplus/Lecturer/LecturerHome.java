package project.group.se.gradeplus.Lecturer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import project.group.se.gradeplus.R;
import project.plusPlatform.CurrentUser;
import project.plusPlatform.Lecturer;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;
import project.plusPlatform.User;

public class LecturerHome extends AppCompatActivity {

    private TextView idHeading;
    private TextView nameHeading;
    private EditText id;
    private EditText name;
    private Button infoButton;
    private Module module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_home);
        idHeading = findViewById(R.id.module_id_heading);
        nameHeading = findViewById(R.id.module_name_heading);
        id = findViewById(R.id.module_id_view);
        name = findViewById(R.id.module_name_view);
        infoButton = findViewById(R.id.module_detail_button);
        User user = CurrentUser.getInstance().getUser();
        Registry registry = Registry.getInstance();
        registry.startDatabase(this);
        System.out.println(user.getName());
        module = registry.getAllModulesOfLecturer(user.getId());
        if(module == null){
            idHeading.setText("Module is not assigned yet");
            nameHeading.setText("");
            id.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            infoButton.setVisibility(View.INVISIBLE);
        }else{
            id.setText(module.getModuleId()+"");
            name.setText(module.getName());
        }

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LecturerHome.this,WorkListActivity.class);
                intent.putExtra(WorkListActivity.MODULE_ID,module.getModuleId());
                intent.putExtra(WorkListActivity.MODULE_NAME,module.getName());
                startActivity(intent);
            }
        });
    }
}
