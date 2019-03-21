package project.plusPlatform;
import java.util.*;

public class Student extends User {

	Registry database;
	List<Module> modules;

	public Student(int id, String emailAddress, String password) {
		super(id,emailAddress,password);
	}

	public List<Module> getModules() {
		return this.modules;
	}

	/**
	 * 
	 * @param modules
	 */
	public boolean setModules(List<Module> modules) {
		if(modules!=null) {
			this.modules = modules;
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param module
	 * @param work
	 */
	public double checkGrade(Module module, AssessedWork work) {
		Map<Integer,Integer> results = work.getResults();
		if(results!= null){
		    return results.get(this.getId());
        }
        return -1;
	}

	/**
	 * 
	 * @param work
	 */
	public boolean addComment(AssessedWork work,String comment) {
		if(work.getResults() != null){
		    return work.getComments().add(comment);
        }
        return false;
	}

    @Override
    public boolean equals(Object obj) {
        Student st = (Student) obj;
        if(st.getId() == this.getId()){
            return true;
        }
        return false;
    }
}