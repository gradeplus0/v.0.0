package project.group.se.gradeplus.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

public class EnrolStudent extends AppCompatActivity {

    public final static String MODULE_ID = "MODULE";
    public final static String MODULE_NAME = "Modulename";
    private List<Student> unenrolled;
    private int moduleId;
    private String name;
    private ListView studentList;
    private Registry registry;
    private ArrayAdapter<Student> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrol_student);
        registry = Registry.getInstance();
        registry.startDatabase(this);
        if(savedInstanceState !=null){
            moduleId = savedInstanceState.getInt("moduleId");
        }else {
            moduleId = getIntent().getIntExtra(MODULE_ID,-1);
            name = getIntent().getStringExtra(MODULE_NAME);
        }
        System.out.println("IN ENROL CLASS : module id : "+moduleId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.unenrolled = registry.getUnEnrolledStudents(new Module(moduleId,""));
        this.studentList = findViewById(R.id.enrol_student_listview);
        if(this.unenrolled!=null){
            adapter = new ArrayAdapter<Student>(this,android.R.layout.simple_list_item_1,this.unenrolled);
            studentList.setAdapter(adapter);
        }else{
            Student student = new Student(-1,"","");
            student.setName("All the students are enrolled already or not registered yet.");
            adapter = new ArrayAdapter<Student>(this,android.R.layout.simple_list_item_1,new Student[]{student});
            studentList.setAdapter(adapter);
        }
        registry.stopDatabase();
        this.studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnrolStudent s = EnrolStudent.this;
                s.registry.startDatabase(s);
                if(s.unenrolled !=null){
                    Student student = s.unenrolled.get(position);
                    if(student.getId()!=-1){
                        if(s.registry.enrollUser(student,new Module(s.moduleId,""))){
                            Toast.makeText(s, "Student is enrolled", Toast.LENGTH_SHORT).show();
                            s.unenrolled.remove(position);
                            s.adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(s, "Student is not enrolled due to database problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(s, "Student is not enrolled due to database problem", Toast.LENGTH_SHORT).show();
                }
                s.registry.stopDatabase();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("moduleId",this.moduleId);
    }

}
