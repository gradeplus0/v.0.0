package project.group.se.gradeplus.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import project.group.se.gradeplus.R;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class ModulePageActivity extends AppCompatActivity {

    public final static String MODULE_ID = "MODULE";
    public final static String MODULE_NAME = "M";
    private Module module;
    private TextView moduleTitle;
    private Button studentListPage;
    private Button lecturerPage;
    private Button assignLecturerPage;
    private Button enrolStudentPage;

    //For back pressed
    public static int moduleId = -1;
    public static String moduleName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_page);
        int id = -1;
        String name = "";
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt("moduleId");
            name = savedInstanceState.getString("moduleName");
        }else{
            Intent intent = getIntent();
            id = intent.getIntExtra(MODULE_ID,0);
            name = intent.getStringExtra(MODULE_NAME);
        }
        System.out.println("IN MODULEPAGE CLASS : module id : "+id);
        module = new Module(id,name);
        moduleTitle = findViewById(R.id.module_title);
        moduleTitle.setText(this.module.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentListPage = findViewById(R.id.student_list);
        lecturerPage = findViewById(R.id.lecturer);
        assignLecturerPage = findViewById(R.id.assignLecturer);
        enrolStudentPage = findViewById(R.id.enrolStudent);

        studentListPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModulePageActivity.this,StudentList.class);
                intent.putExtra(StudentList.MODULE,module.getModuleId());
                startActivity(intent);
            }
        });

        lecturerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModulePageActivity.this,LecturerPage.class);
                intent.putExtra(LecturerPage.MODULE,module.getModuleId());
                startActivity(intent);
            }
        });

        enrolStudentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModulePageActivity.this,EnrolStudent.class);
                intent.putExtra(EnrolStudent.MODULE_ID,module.getModuleId());
                intent.putExtra(EnrolStudent.MODULE_NAME,module.getName());
                startActivity(intent);
            }
        });

        assignLecturerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModulePageActivity.this,AssignLecturer.class);
                intent.putExtra(AssignLecturer.MODULE_ID,module.getModuleId());
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("moduleId",module.getModuleId());
        outState.putString("moduleName",module.getName());
    }
}
