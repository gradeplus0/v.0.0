package project.plusPlatform;
import java.util.List;
import java.util.Map;

public class AssessedWork {

	private Module module;
	private int assessedWorkId;
	private String name;
	private Map<Integer, Integer> results;
	private List<String> comments;
	private Map<Integer, String> feedback;

	/**
	 * 
	 * @param assessedWorkId
	 * @param name
	 */
	public AssessedWork(int assessedWorkId, String name) {
		this.assessedWorkId = assessedWorkId;
		this.name = name;
	}

	public int getAssessedWorkId() {
		return this.assessedWorkId;
	}


	public void setAssessedWorkId(int assessedWorkId) {
		this.assessedWorkId = assessedWorkId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Module getModules() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	// student ID : Integer, Marks : Integer
	public Map<Integer, Integer> getResults() {
		return this.results;
	}

	public boolean setResults(Map<Integer, Integer> results) {
		if(results != null){
			this.results = results;
			return true;
		}
		return false;
	}

	public List<String> getComments() {
		return this.comments;
	}

	public boolean setComments(List<String> comments) {
		if(comments!=null){
			this.comments= comments;
			return true;
		}
		return false;
	}

	public Map<Integer, String> getFeedback() {
		return this.feedback;
	}

	public boolean setFeedback(Map<Integer, String> feedback) {
		if(feedback != null){
			this.feedback = feedback;
			return true;
		}
		return false;

	}

}