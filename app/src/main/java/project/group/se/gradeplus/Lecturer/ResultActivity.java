package project.group.se.gradeplus.Lecturer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

public class ResultActivity extends AppCompatActivity {
    public static final String MODULE_ID = "MODULEID";
    public static final String MODULE_NAME = "MODULENAME";
    public static final String WORK_ID = "WORKID";
    public static final String WORK_NAME = "WORKNAME";

    private Registry registry;

    private Module module;
    private AssessedWork work;
    private List<Student> students;

    private ListView studentListView;
    private ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        registry = Registry.getInstance();
        Intent intent = getIntent();
        module = new Module(intent.getIntExtra(MODULE_ID,0),intent.getStringExtra(MODULE_NAME));
        work = new AssessedWork(intent.getIntExtra(WORK_ID,0),intent.getStringExtra(WORK_NAME));
        studentListView = findViewById(R.id.result_listview);
        registry.startDatabase(this);
        students = registry.getStudentWithNoResults(work,module);
        if(students !=null){
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,students);
            studentListView.setAdapter(adapter);
        }else{
            Toast.makeText(this, "Result is uploaded or Student not enrolled yet", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this,WorkHome.class);
            in.putExtra(WorkHome.MODULE_NAME,module.getName());
            in.putExtra(WorkHome.MODULE_ID,module.getModuleId());
            in.putExtra(WorkHome.WORK_ID,work.getAssessedWorkId());
            in.putExtra(WorkHome.WORK_NAME,work.getName());
            startActivity(in);
        }
        studentListView.setOnItemClickListener(new ListListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_screen,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("Item is selected");
        if(item.getItemId() == R.id.action_refresh){
            Intent intent = getIntent();
            overridePendingTransition(0,0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0,0);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class ListListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ResultActivity that = ResultActivity.this;
            AssessedWork wrk = that.work;
            Student student = that.students.get(position);
            Intent intent = new Intent(that,ResultScreenActivity.class);
            intent.putExtra(ResultScreenActivity.WORK_NAME,wrk.getName());
            intent.putExtra(ResultScreenActivity.WORK_ID,wrk.getAssessedWorkId());
            intent.putExtra(ResultScreenActivity.STUDENT_ID,student.getId());
            intent.putExtra(ResultScreenActivity.STUDENT_NAME,student.getName());
            intent.putExtra(ResultScreenActivity.STUDENT_EMAIL,student.getEmailAddress());
            startActivity(intent);
        }
    }
}
