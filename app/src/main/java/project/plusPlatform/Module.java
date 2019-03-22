package project.plusPlatform;
import java.io.Serializable;
import java.util.*;

public class Module implements Serializable {

	private Registry database;
	private int moduleId;
	private String name;
	private List<Student> enrolledStudents ;
	private Lecturer assignedLecturer ;
	private List<AssessedWork> assessedWorks;

	public Module(int moduleId, String name) {
        this.moduleId = moduleId;
        this.name = name;
        this.database = Registry.getInstance();
        this.assignedLecturer = null;
        this.enrolledStudents = null;
        this.assessedWorks = null;
    }

	public int getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getEnrolledStudents() {
		return this.enrolledStudents;
	}

	/**
	 * 
	 * @param enrolledStudents
	 */
	public boolean setEnrolledStudents(List<Student> enrolledStudents) {
	    if(enrolledStudents != null){
            this.enrolledStudents = enrolledStudents;
            return true;
        }
        return false;

	}

	/**
	 * 
	 * @param lecturer
	 */
	public boolean setAssignedLecturer(Lecturer lecturer) {
		if(lecturer !=null){
		    this.assignedLecturer = lecturer;
		    return true;
		}
		return false;
	}

	public Lecturer getAssignedLecturer() {
		return this.assignedLecturer;
	}

	public List<AssessedWork> getAssessedWorks() {
		return this.assessedWorks;
	}

	public boolean addAssessedWork(int id,String name) {
	    for(AssessedWork work : assessedWorks){
	        if(work.getName().equals(name)){
	            return false;
            }
        }
        AssessedWork newWork = new AssessedWork(id,name);
	    return this.assessedWorks.add(newWork);
	}

    @Override
    public String toString() {
        return this.name;
    }

    public void setAssessedWorks(List<AssessedWork> assessedWorks) {
        this.assessedWorks = assessedWorks;
    }
}