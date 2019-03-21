package project.group.se.gradeplus.Admin;

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

import project.group.se.gradeplus.R;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class ModuleActivity extends AppCompatActivity {
    private Registry registry;
    private ListView list;
    private List<Module> modules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        list = findViewById(R.id.module_list);
        registry = Registry.getInstance();
        registry.startDatabase(this);
        this.modules= registry.getAllModules();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(this.modules != null){
            ArrayAdapter<Module> adapter = new ArrayAdapter<Module>(this,android.R.layout.simple_list_item_1,this.modules);
            list.setAdapter(adapter);
        }
        list.setOnItemClickListener(new ModuleListListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_create_module){
            Intent intent = new Intent(this,CreateModuleActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ModuleListListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ModuleActivity.this, ModulePageActivity.class);
            Module module = ModuleActivity.this.modules.get(position);
            intent.putExtra(ModulePageActivity.MODULE_ID,module.getModuleId());
            intent.putExtra(ModulePageActivity.MODULE_NAME,module.getName());
            startActivity(intent);
        }
    }
}
