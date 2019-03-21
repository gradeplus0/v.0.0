package project.plusPlatform;
import java.io.Serializable;
import java.util.*;

public class Lecturer extends User implements Serializable {

	private Registry database;
	private List<Module> modules;

	public Lecturer(int id, String emailAddress, String password) {
		super(id,emailAddress,password);
	}

	public List<Module> getTeachingModules() {
		return this.modules;
	}

	/**
	 * 
	 * @param teachingModules
	 */
	public boolean setTeachingModules(ArrayList<Module> teachingModules) {
		if(teachingModules != null){
			this.modules = teachingModules;
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param assessed_work
	 * @param marks
	 * @param feedback
	 */
	public boolean addResult(AssessedWork assessed_work, Map<Integer, Integer> marks, Map<Integer, String> feedback) {
		if(assessed_work == null){
			return false;
		}
		return assessed_work.setResults(marks);
	}

	/**
	 * 
	 * @param module
	 */
	public boolean createAssessedWork(Module module,String name) {
		return module.addAssessedWork(module.getAssessedWorks().size(),name);
	}

}