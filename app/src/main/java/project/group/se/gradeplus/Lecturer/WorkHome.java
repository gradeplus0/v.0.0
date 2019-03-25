package project.group.se.gradeplus.Lecturer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

public class WorkHome extends AppCompatActivity {
    public static final String MODULE_ID = "MODULEID";
    public static final String MODULE_NAME = "MODULENAME";
    public static final String WORK_ID = "WORKID";
    public static final String WORK_NAME = "WORKNAME";
    private Module module;
    private AssessedWork work;
    private Registry registry;

    private Button addResult;
    private Button publishResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_home);
        Intent intent = getIntent();
        module = new Module(intent.getIntExtra(MODULE_ID,0),intent.getStringExtra(MODULE_NAME));
        work = new AssessedWork(intent.getIntExtra(WORK_ID,0),intent.getStringExtra(WORK_NAME));
        addResult = findViewById(R.id.add_result_button);
        publishResult = findViewById(R.id.publish_result_button);
        Registry registry = Registry.getInstance();
        registry.startDatabase(this);
        if(registry.isResultUploaded(work)){
            addResult.setVisibility(View.INVISIBLE);
        }
        registry.stopDatabase();
        addResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(WorkHome.this,ResultActivity.class);
                in.putExtra(WorkHome.MODULE_NAME,module.getName());
                in.putExtra(WorkHome.MODULE_ID,module.getModuleId());
                in.putExtra(WorkHome.WORK_ID,work.getAssessedWorkId());
                in.putExtra(WorkHome.WORK_NAME,work.getName());
                startActivity(in);
            }
        });

        publishResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkHome that = WorkHome.this;
                Registry registry = Registry.getInstance();
                registry.startDatabase(that);
                if(!registry.isResultUploaded(work)) {
                    List<Student> students = registry.getStudentWithNoResults(that.work, that.module);
                    if (students != null) {
                        if (students.size() == 0) {
                            registry.uploadResult(that.work);
                            Toast.makeText(that, "Result uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(that, "Please add results for all students", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(that, "Students not enrolled yet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(that, "Result is already uploaded", Toast.LENGTH_SHORT).show();
                }

                registry.stopDatabase();
            }
        });


    }
}
