package project.plusPlatform;
import java.util.*;

public class Registry {

	private List<Lecturer> lecturers;
	private List<Module> modules;
	private List<Student> students;
	private static Registry instance;

	private Registry() {
		// TODO - implement Registry.Registry
		throw new UnsupportedOperationException();
	}

	public static Registry getInstance() {
		if(instance==null){
			instance = new Registry();
		}
		return instance;
	}

	/**
	 * 
	 * @param id
	 */
	public Lecturer findLecturerById(int id) {
		for(Lecturer lecturer : lecturers){
		    if(lecturer.getId() == id)
		        return lecturer;

        }
        return null;
	}

	/**
	 * 
	 * @param id
	 */
	public Student findStudentById(int id) {
		for(Student student : students){
		    if(student.getId() == id)
		        return student;
        }
        return null;
	}

	/**
	 * 
	 * @param id
	 */
	public Module findModuleById(int id) {
		for(Module module : modules){
		    if(module.getModuleId() == id){
		        return module;
            }
        }
        return null;

	}

	/**
	 * 
	 * @param student
	 */
	public boolean addStudent(Student student) {
		if(student!=null){
		    return this.students.add(student);
        }
        return false;
	}

	/**
	 * 
	 * @param lecturer
	 */
	public boolean addLecturer(Lecturer lecturer) {
		if(lecturer != null){
		    return this.lecturers.add(lecturer);
        }
        return false;
	}

	/**
	 * 
	 * @param module
	 */
	public boolean addModule(Module module) {
		if(module !=null){
		    return this.modules.add(module);
        }
        return false;
	}

	public List<Module> getModules() {
		return this.modules;
	}

	public List<Student> getStudents() {
		return this.students;
	}

	public List<Lecturer> getLecturers() {
		return this.lecturers;
	}

}