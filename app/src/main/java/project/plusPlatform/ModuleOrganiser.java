package project.plusPlatform;

import java.util.List;

public class ModuleOrganiser extends User {

	private Registry database;

	public ModuleOrganiser(int id, String username,String password){
		super(id,username,password);
		database = Registry.getInstance();
	}

	/**
	 * 
	 * @param student
	 * @param module
	 */
	public boolean enrolStudent(Student student, Module module) {
		return module.getEnrolledStudents().add(student);
	}

	public boolean assignLecturer(Lecturer lecturer, Module module) {
		if(lecturer != null){
		    return module.setAssignedLecturer(lecturer);
        }
        return false;
	}
	public boolean createModule(String name) {
		List<Module> modules = database.getModules();
		for(Module module : modules){
		    if(module.getName().equals(name)){
		        return false;
            }
        }
		return modules.add(new Module(modules.size(),name));
	}


	public boolean RegisterUser(String emailAddress, String password, String type) {
	    if(type.equals("ST")){
		    List<Student> students = database.getStudents();
		    for(Student student : students){
		        if(student.getEmailAddress().equals(emailAddress)){
		            return false;
                }
            }
            return students.add(new Student(students.size(),emailAddress,password));
	    }else if(type.equals("LC")){
	        List<Lecturer> lectures = database.getLecturers();
	        for(Lecturer lecturer : lectures){
	            if(lecturer.getEmailAddress().equals(emailAddress)){
	                return false;
                }
            }
            return lectures.add(new Lecturer(lectures.size(),emailAddress,password));
        }else{
	        return false;
        }
	}

	/**
	 * 
	 * @param work
	 * @param comment
	 */
	public boolean removeComment(AssessedWork work, String comment) {
		if(work!=null){
		    List<String> comments = work.getComments();
		    if(comments.contains(comment)){
		        comments.remove(comments.indexOf(comment));
		        return true;
            }
        }
        return false;
	}

}