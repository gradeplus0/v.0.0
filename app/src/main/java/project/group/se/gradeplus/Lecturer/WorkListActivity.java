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

import java.util.List;

import project.group.se.gradeplus.Admin.CreateModuleActivity;
import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class WorkListActivity extends AppCompatActivity {
    private Registry registry;
    public static final String MODULE_ID = "MODULEID";
    public static final String MODULE_NAME = "MODULENAME";

    private List<AssessedWork> works;
    private Module module;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);
        list = findViewById(R.id.work_list_view);
        registry = Registry.getInstance();
        registry.startDatabase(this);
        Intent intent = getIntent();
        module = new Module(intent.getIntExtra(MODULE_ID,0),intent.getStringExtra(MODULE_NAME));
        System.out.println("Module name : "+module.getName());
        works = registry.getAllAssessedWork(module);
        System.out.println("******************************** : "+works);
        ArrayAdapter<AssessedWork> adapter = null;
        if(works==null){
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new AssessedWork[]{new AssessedWork(-1,"There are no assessedWork created yet")});
        }else{
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,works);
        }
        list.setAdapter(adapter);
        registry.stopDatabase();
        list.setOnItemClickListener(new ListListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_work,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("Item is selected");
        if(item.getItemId() == R.id.action_create_work){
            Intent intent = new Intent(this, CreateWorkActivity.class);
            intent.putExtra(CreateWorkActivity.MODULE_ID,module.getModuleId());
            intent.putExtra(CreateWorkActivity.MODULE_NAME,module.getName());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ListListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WorkListActivity that = WorkListActivity.this;
            AssessedWork work = that.works.get(position);
            if(work.getAssessedWorkId() != -1){
                Intent intent = new Intent(WorkListActivity.this,WorkHome.class);
                intent.putExtra(WorkHome.MODULE_NAME,that.module.getName());
                intent.putExtra(WorkHome.MODULE_ID,that.module.getModuleId());
                AssessedWork wrk = works.get(position);
                intent.putExtra(WorkHome.WORK_ID,wrk.getAssessedWorkId());
                intent.putExtra(WorkHome.WORK_NAME,wrk.getName());
                startActivity(intent);
            }
        }
    }
}
