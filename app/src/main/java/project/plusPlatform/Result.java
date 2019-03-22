package project.plusPlatform;

public class Result implements Comparable {
    private String workName;
    private String moduleName;
    private int marks;
    private String feedback;

    public Result(String workName){
        this.workName= workName;
    }
    public Result(String work, int marks, String feedback){
        this.workName = work;
        this.marks = marks;
        this.feedback = feedback;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getWorkName() {
        return workName;
    }

    public int getMarks() {
        return marks;
    }

    public String getWork() {
        return workName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public int compareTo(Object o) {
        Result res = (Result) o;
        if(this.marks > res.marks){
            return 1;
        }else if(this.marks == res.marks){
            return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return this.workName + " : " +this.marks;
    }
}
