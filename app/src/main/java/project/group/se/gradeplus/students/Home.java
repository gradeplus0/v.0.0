package project.group.se.gradeplus.students;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import project.group.se.gradeplus.R;
import project.plusPlatform.CurrentUser;
import project.plusPlatform.Messages;
import project.plusPlatform.Registry;
import project.plusPlatform.Result;
import project.plusPlatform.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }

    private Result bestModule;
    private Result worstModule;
    private Result bestWork; // Done
    private Result worstWork; // Done
    private Result[] topThree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Registry registry =Registry.getInstance();
        registry.startDatabase(getContext());
        Student student = (Student) CurrentUser.getInstance().getUser();
        List<Result> results = registry.getMarksForStudent(student);
        String[] allInfo = new String[20];

        System.out.println(results);
        registry.stopDatabase();

        RecyclerView messageRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_home,container,false);
        CardMessageAdapter adapter = new CardMessageAdapter(allInfo);
        messageRecycler.setAdapter(adapter);

        GridLayoutManager layout = new GridLayoutManager(getActivity(),2);
        messageRecycler.setLayoutManager(layout);
        // Inflate the layout for this fragment
        return messageRecycler;
    }

    private void sort(List<Result> results){
        if(results != null) {
            boolean sorted = false;
            while (!sorted) {
                sorted = true;
                for (int x = 0; x < results.size() - 1; x++) {
                    if (results.get(x).compareTo(results.get(x + 1)) == 1) {
                        Result deleted = results.remove(x);
                        results.set(x + 1, deleted);
                        sorted = false;
                    }
                }
            }

            this.bestWork = results.get(results.size()-1);
            this.worstWork = results.get(0);
        }
    }

}
