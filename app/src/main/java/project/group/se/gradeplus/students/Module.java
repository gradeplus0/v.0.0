package project.group.se.gradeplus.students;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.CurrentUser;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class Module extends Fragment {

    private ListView listview;
    private ArrayAdapter<project.plusPlatform.Module> adapter = null;
    private boolean isEnrolled = false;
    private List<project.plusPlatform.Module> modules;

    public Module() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view != null) {
            listview = view.findViewById(R.id.fragment_listview);
            Registry registry = Registry.getInstance();
            registry.startDatabase(getContext());
            Student student = (Student) CurrentUser.getInstance().getUser();
            List<project.plusPlatform.Module> modules = registry.getModulesOfStudent(student);
            if (modules != null) {
                isEnrolled = true;
                adapter = new ArrayAdapter<project.plusPlatform.Module>(getContext(), android.R.layout.simple_list_item_1, modules);
            } else {
                isEnrolled = false;
                modules = new ArrayList<>();
                modules.add(new project.plusPlatform.Module(-1, "You are not enrolled in any module yet"));
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, modules);
            }
            listview.setAdapter(adapter);
            registry.stopDatabase();
            listview.setOnItemClickListener(new ListListener(modules));
        }
    }

    public class ListListener implements ListView.OnItemClickListener{
        private List<project.plusPlatform.Module> modules;

        public ListListener(List<project.plusPlatform.Module> modules){
            super();
            this.modules = modules;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Module that = Module.this;
            if(that.isEnrolled){
                project.plusPlatform.Module module = this.modules.get(position);
                Intent intent = new Intent(getActivity(), StudentModuleInfo.class);
                intent.putExtra(StudentModuleInfo.MODULE_ID,module.getModuleId());
                intent.putExtra(StudentModuleInfo.MODULE_NAME,module.getName());
                startActivity(intent);
            }
        }
    }
}
