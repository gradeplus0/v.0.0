package project.group.se.gradeplus.students;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.CurrentUser;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

public class StudentModuleInfo extends AppCompatActivity {

    public static final String MODULE_ID="MODULEID";
    public static final String MODULE_NAME = "MODULENAME";

    private Student student;
    private Module module;
    private List<AssessedWork> works;

    private ListView listview;

    private boolean isUploaded= false;
    private ArrayAdapter<AssessedWork> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_module_info);
        student = (Student) CurrentUser.getInstance().getUser();
        Intent intent = getIntent();
        module = new Module(intent.getIntExtra(MODULE_ID,-1),intent.getStringExtra(MODULE_NAME));
        listview = findViewById(R.id.student_work_listview);
        Registry registry = Registry.getInstance();
        registry.startDatabase(this);
        works = registry.getAsssessedWorkByModule(module.getModuleId());
        System.out.println(works);
        if(works!=null && works.size()>0){

            isUploaded = true;
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,works);
        }else{
            isUploaded = false;
            works = new ArrayList<>();
            works.add(new AssessedWork(-1,"There is not assessed work created yet."));
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,works);
        }
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ListListener());
    }

    public class ListListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(isUploaded){
                AssessedWork work = works.get(position);
                Registry registry = Registry.getInstance();
                if(registry.isResultUploaded(work)) {
                    Intent intent = new Intent(StudentModuleInfo.this, ResultActivity.class);
                    intent.putExtra(ResultActivity.MODULE_ID, module.getModuleId());
                    intent.putExtra(ResultActivity.MODULE_NAME, module.getName());
                    intent.putExtra(ResultActivity.WORK_ID, work.getAssessedWorkId());
                    intent.putExtra(ResultActivity.WORK_NAME, work.getName());
                    startActivity(intent);
                }else{
                    Toast.makeText(StudentModuleInfo.this, "Result is not published yet", Toast.LENGTH_SHORT).show();
                }

                registry.stopDatabase();
            }
        }
    }
}
