package project.group.se.gradeplus.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

public class StudentList extends AppCompatActivity {
    private ListView studentList;
    public static final String MODULE ="m";
    private int moduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentList = findViewById(R.id.student_list_view);
        Registry registry = Registry.getInstance();
        registry.startDatabase(this);
        moduleId = getIntent().getIntExtra(MODULE,0);
        List<Student> students = registry.getAllStudentsFromModule(new Module(moduleId,""));
        if(students != null){
            ArrayAdapter<Student> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,students);
            studentList.setAdapter(adapter);
        }else{
            ArrayList<Student> s = new ArrayList<>();
            Student ss = new Student(-1,"Student not enrolled yet","");
            ss.setName("Students not enrolled yet");
            s.add(ss);
            ArrayAdapter<Student> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,s);
            studentList.setAdapter(adapter);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        registry.stopDatabase();
    }
}
