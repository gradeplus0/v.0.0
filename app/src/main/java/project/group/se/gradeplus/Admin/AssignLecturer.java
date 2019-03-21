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
import project.plusPlatform.Lecturer;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class AssignLecturer extends AppCompatActivity {

    public final static String MODULE_ID = "ID";
    private Lecturer lc;
    private ListView listview;
    private int moduleId;
    private List<Lecturer> lecturers;
    private Registry registry;
    private Module module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_lecturer);
        listview = findViewById(R.id.assign_lecturer_listview);
        registry = Registry.getInstance();
        registry.startDatabase(this);
        moduleId = getIntent().getIntExtra(MODULE_ID,-1);
        module = new Module(moduleId,"");
        lecturers = registry.getAllUnAssignedLecturer(module);
        final ArrayAdapter<Lecturer> arrayAdapter;
        if(lecturers == null){
            Lecturer lc = new Lecturer(-1,"","");
            lc.setName("All lecturers are assigned");
            arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new Lecturer[]{lc});
        }else{
            arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lecturers);
        }
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssignLecturer that = AssignLecturer.this ;
                that.registry.startDatabase(that);
                if(that.lecturers !=null){
                    Lecturer lecturer = that.lecturers.get(position);
                    if(that.registry.enrollUser(lecturer,that.module)){
                        Toast.makeText(that, "Lecturer is assigned", Toast.LENGTH_SHORT).show();
                        that.lecturers.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(that, "Lecturer is not assigned due to some problems", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(that, "Lecturer is not assigned due to database problem", Toast.LENGTH_SHORT).show();
                }
                that.registry.stopDatabase();

            }
        });
    }
}
