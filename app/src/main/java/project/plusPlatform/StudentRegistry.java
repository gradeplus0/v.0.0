package project.plusPlatform;

import android.content.Context;

import java.util.List;
import java.util.Map;

public class StudentRegistry {
    private Registry registry;
    private Context context;

    private static StudentRegistry studentRegistry;

    private List<Module> modules;

    private StudentRegistry() {
        this.registry = Registry.getInstance();
        this.context = null;
    }

    public StudentRegistry getInstance(){
        if(studentRegistry == null){
            studentRegistry = new StudentRegistry();
        }
        return studentRegistry;
    }

    public void setContext(Context context){
        this.context = context;
        this.registry.startDatabase(context);
    }

    public List<Module> getModules(Student student){
        if(context != null) {
            List<Module> modules = registry.getModulesByStudentId(student.getId());
            if(modules !=null){
                return modules;
            }
        }
        return null;
    }

    public List<AssessedWork> getAssessedWork(Module module){
        if(context != null) {
            List<AssessedWork> works = registry.getAsssessedWorkByModule(module.getModuleId());
            for(AssessedWork work : works){
                module.addAssessedWork(work.getAssessedWorkId(),work.getName());
            }
            if(works !=null){
                return works;
            }
        }
        return null;
    }

   // public void getResults(AssessedWork work){
     //   if(context != null){
            //Map<Integer, Integer> results = registry.getResultsFromAssessedWork(work.getAssessedWorkId());
       //     work.setResults(results);
        //}
    //}

    public int getMarksOfStudent(Student student){
        if(context != null){
            int marks = registry.getMarksOfStudent(student.getId());
            return marks;
        }
        return -1;
    }
}
