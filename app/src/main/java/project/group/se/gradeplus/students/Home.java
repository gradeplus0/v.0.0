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

    private Result bestModule; // Done
    private Result worstModule; // Done
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
        String[] allInfo = null;
        if(results !=null && results.size()>0) {
            allInfo = new String[4];
            registry.stopDatabase();
            if(this.sort(results)) {
                organiseModules(results);
                allInfo[2] = "Your best module\n\n" + bestModule.getModuleName();
                allInfo[3] = "Need to work hard (module)\n\n" + worstModule.getModuleName();
                allInfo[0] = "Best assessed-work(module)\n\n " + bestWork.getWork() + " (" + bestWork.getModuleName() + ")";
                allInfo[1] = "Worst assessed-work(module)\n\n " + worstWork.getWork() + " (" + worstWork.getModuleName() + ")";
            }
        }else{
            allInfo = new String[1];
            allInfo[0] = "Marks are not published yet";
        }
        RecyclerView messageRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_home,container,false);
        CardMessageAdapter adapter = new CardMessageAdapter(allInfo);
        messageRecycler.setAdapter(adapter);

        GridLayoutManager layout = new GridLayoutManager(getActivity(),2);
        messageRecycler.setLayoutManager(layout);
        // Inflate the layout for this fragment
        return messageRecycler;
    }

    private boolean sort(List<Result> results){
        if(results != null && results.size()>1) {
            boolean sorted = false;
            while (!sorted) {
                sorted = true;
                for (int x = 0; x < results.size() - 1; x++) {
                    if (results.get(x).compareTo(results.get(x + 1)) == 1) {
                        Result deleted = results.remove(x);
                        if(results.size() == x+1){
                            results.add(deleted);
                        }else {
                            results.set(x + 1, deleted);
                        }
                        sorted = false;
                    }
                }
            }

            this.bestWork = results.get(results.size()-1);
            this.worstWork = results.get(0);
            return true;
        }else if(results !=null && results.size()==1){
            this.bestWork = results.get(0);
            this.worstWork = results.get(0);
            return true;
        }
        return false;
    }


    private void organiseModules(List<Result> results){
        List<String> modulenames = new ArrayList<>();
        List<Double> averages = new ArrayList<>();
        List<Integer> counter = new ArrayList<>();
        for(int i = 0; i<results.size();i++){
            Result result = results.get(i);
            if(modulenames.contains(result.getModuleName())){
                int marks = result.getMarks();
                int index = modulenames.indexOf(result.getModuleName());
                int count = counter.get(index);
                double average = averages.get(index);
                double newAverage  =((average*count)+marks)/(count+1);
                averages.set(index,newAverage);
                counter.set(index,count+1);
            }else{
                modulenames.add(result.getModuleName());
                averages.add((double) result.getMarks());
                counter.add(1);
            }
        }
        if(modulenames.size()>1) {
            boolean sorted = false;
            while (!sorted) {
                sorted = true;
                for (int i = 0; i < modulenames.size() - 1; i++) {
                    if (averages.get(i) > averages.get(i + 1)) {
                        String helper = modulenames.get(i+1);
                        modulenames.set(i+1,modulenames.get(i));
                        modulenames.set(i,helper);

                        double helper1 = averages.get(i+1);
                        averages.set(i+1,averages.get(i));
                        averages.set(i,helper1);

                        int count = counter.get(i+1);
                        counter.set(i+1,counter.get(i));
                        counter.set(i,count);
                    }
                }
            }
            String best = modulenames.get(modulenames.size()-1);
            Result reqModule = this.compare(results,best);
            if(reqModule!=null){
                bestModule = reqModule;
            }

            String worst = modulenames.get(0);
            Result reqModule2 = this.compare(results,worst);
            if(reqModule2 !=null){
                worstModule = reqModule2;
            }
        }else if(modulenames.size()==1){
            bestModule = results.get(0);
            worstModule = bestModule;
        }

    }

    public Result compare(List<Result> results,String module){
        if(results !=null){
            for(Result result: results){
                if(result!=null){
                    if(result.sameModule(module)){
                        return result;
                    }
                }
            }
        }
        return null;
    }

}
