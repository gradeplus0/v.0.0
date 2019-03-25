package project.plusPlatform;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.*;

public class Registry implements Serializable {

    private List<Lecturer> lecturers = new ArrayList<Lecturer>();
    private List<Module> modules = new ArrayList<Module>();
    private List<Student> students = new ArrayList<Student>();
    private static Registry instance;
    private SQLiteOpenHelper databaseOpener;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;
    private int totalStudents = -1;
    private int totalLecturers = -1;
    private int totalModules = -1;


    private int moduleid;

    private Registry() {
        moduleid = -1;
    }


    public static Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    public void saveModuleId(int id) {
        moduleid = id;
    }

    public int getModuleid() {
        int helper = moduleid;
        moduleid = -1;
        return helper;
    }

    public int getTotalStudents() {
        return this.totalStudents;
    }

    public int getTotalLecturers() {
        return this.totalLecturers;
    }

    public int getTotalModules() {
        return this.totalModules;
    }

    /**
     * @param id
     */
    public Lecturer findLecturerById(int id) {
        for (Lecturer lecturer : lecturers) {
            if (lecturer.getId() == id)
                return lecturer;

        }
        return null;
    }

    /**
     * @param id
     */
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id)
                return student;
        }
        return null;
    }

    /**
     * @param id
     */
    public Module findModuleById(int id) {
        for (Module module : modules) {
            if (module.getModuleId() == id) {
                return module;
            }
        }
        return null;

    }

    /**
     * @param student
     */
    public boolean addStudent(Student student) {
        if (student != null) {
            return this.students.add(student);
        }
        return false;
    }

    /**
     * @param lecturer
     */
    public boolean addLecturer(Lecturer lecturer) {
        if (lecturer != null) {
            return this.lecturers.add(lecturer);
        }
        return false;
    }

    /**
     * @param module
     */
    public boolean addModule(Module module) {
        if (module != null) {
            return this.modules.add(module);
        }
        return false;
    }

    public List<Module> getModules() {
        if (readDatabase != null) {
            try {
                Cursor resultSet = this.readDatabase.query("Module", new String[]{"module_id", "module_name"}, null, null, null, null, null);
                while (resultSet.moveToNext()) {
                    Module module = new Module(resultSet.getInt(0), resultSet.getString(1));
                    this.modules.add(module);
                }
                resultSet.close();
                return this.modules;
            } catch (SQLException e) {
                System.out.println("Error : getModules (Registry)");
            }
        }
        return null;
    }

    public List<Student> getStudents() {
        if (readDatabase != null) {
            try {
                Cursor resultSet = this.readDatabase.query("User", new String[]{"user_id", "user_name", "user_email", "user_password"},
                        "user_type = ?", new String[]{"Student"}, null, null, null);
                while (resultSet.moveToNext()) {
                    String name = resultSet.getString(1);
                    String email = resultSet.getString(2);
                    String pass = resultSet.getString(3);
                    Student student = new Student(resultSet.getInt(0), email, pass);
                    student.setName(name);
                    this.students.add(student);
                }
                resultSet.close();
                return this.students;
            } catch (SQLException e) {
                System.out.println("Error : getStudents (Registry)");
            }
        }
        return null;
    }

    public List<Lecturer> getLecturers() {
        return this.lecturers;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        users.addAll(students);
        users.addAll(lecturers);
        return users;
    }


    // ******************************** Database usage for Student ****************************************** //

    public void startDatabase(Context context) {
        try {
            this.databaseOpener = new Database(context);
            this.readDatabase = databaseOpener.getReadableDatabase();
            this.writeDatabase = databaseOpener.getWritableDatabase();
            if (this.totalStudents == -1) {
                Cursor cs = this.readDatabase.rawQuery("SELECT COUNT(user_id) FROM User WHERE user_type LIKE 'S%' ",null);
                cs.moveToFirst();
                this.totalStudents = cs.getInt(0);
                Cursor cs2 = this.readDatabase.rawQuery("SELECT COUNT(user_id) FROM User WHERE user_type LIKE 'L%'", null);
                cs2.moveToFirst();
                this.totalLecturers = cs2.getInt(0);
                Cursor cs3 = this.readDatabase.rawQuery("SELECT COUNT(module_id) FROM Module", null);
                cs3.moveToFirst();
                this.totalModules = cs3.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database problem");
        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText(context, "Database problem", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopDatabase() {
        try {
            if(this.databaseOpener !=null && this.readDatabase !=null && this.writeDatabase != null) {
                this.databaseOpener.close();
                this.readDatabase.close();
                this.writeDatabase.close();
            }
        } catch (SQLException e) {
            System.out.println("Error : Database is already closed");
        }
    }

    public List<Module> getModulesByStudentId(int id) {
        try {
            Cursor cs = this.readDatabase.rawQuery("SELECT module_id, module_name FROM Module WHERE module_id IN (SELECT module_id FROM UserModuleProxy NATURAL JOIN User where user_id = ?)", new String[]{String.valueOf(id)});
            List<Module> list = new ArrayList<Module>();
            while (cs.moveToNext()) {
                list.add(new Module(cs.getInt(0), cs.getString(1)));
            }
            cs.close();
            return list;
        } catch (SQLException e) {
            System.out.println("Error : getModulesByStudentId()");
        }
        return null;
    }

    public List<AssessedWork> getAsssessedWorkByModule(int moduleId) {
        try {
            Cursor cs = this.readDatabase.rawQuery("SELECT work_id, work_name from AssessedWork WHERE work_id IN (SELECT work_id FROM ModuleWorkProxy NATURAL JOIN Module where module_id = ?)", new String[]{String.valueOf(moduleId)});
            List<AssessedWork> work = new ArrayList<AssessedWork>();
            while (cs.moveToNext()) {
                work.add(new AssessedWork(cs.getInt(0), cs.getString(1)));
            }
            cs.close();
            return work;
        } catch (SQLException e) {
            System.out.println("Error : getModulesByStudentId()");
        }
        return null;
    }

//    public Map<Integer, Integer> getResultsFromAssessedWork(int work_id) {
//        try {
//            String query = "SELECT result_id, result_mark from Result, AssessedWork where Result.result_id = AssessedWork.result_id and Result.result_id = ?";
//            Cursor cs = this.readDatabase.rawQuery(query, new String[]{String.valueOf(work_id)});
//            Map<Integer, Integer> results = new HashMap<Integer, Integer>();
//            while (cs.moveToNext()) {
//                results.put(cs.getInt(0), cs.getInt(1));
//            }
//            cs.close();
//            return results;
//        } catch (SQLException e) {
//            System.out.println("Error : getResultsFromAssessedWork()");
//        }
//        return null;
//    }

    public int getMarksOfStudent(int id) {
        try {
            String query = "SELECT result_mark from Result where result_id = ?";
            Cursor cs = this.readDatabase.rawQuery(query, new String[]{String.valueOf(id)});
            while (cs.moveToNext()) {
                return cs.getInt(0);
            }
        } catch (SQLException e) {
            System.out.println("Error : getMarksOfStudent()");
        }
        return -1;
    }

    public List<String> getCommentsOfWork(int work_id) {
        try {
            String query = "SELECT comment_value from Comment where work_id = ?";
            Cursor cs = this.readDatabase.rawQuery(query, new String[]{String.valueOf(work_id)});
            List<String> comments = new ArrayList<String>();
            while (cs.moveToNext()) {
                comments.add(cs.getString(0));
            }
        } catch (SQLException e) {
            System.out.println("Error : getCommentsOfWork()");
        }
        return null;
    }

    // ****************************************************************************************** //

    // ******************************** Database usage for Admin ****************************************** //

    public boolean addUser(User user, String type) {
        try {
            this.writeDatabase.insert("User", null, getUserContent(user, type));
            return true;
        } catch (SQLException e) {
            System.out.println("Error : addUser()");
            return false;
        }
    }

    private ContentValues getUserContent(User user, String type) {
        ContentValues values = new ContentValues();
        values.put("user_name", user.getName());
        values.put("user_email", user.getEmailAddress());
        values.put("user_password", user.getPassword());
        values.put("user_type", type.equals("Student") ? "Student" : "Lecturer");
        return values;
    }

    public boolean addModuleToDB(Module module) {
        ContentValues values = new ContentValues();
        values.put("module_name", module.getName());
        try {
            Cursor cursor = this.readDatabase.rawQuery("SELECT module_id from Module where module_name = ?", new String[]{module.getName()});
            if (cursor.getCount() > 0) {
                return false;
            }

            this.writeDatabase.insert("Module", null, values);
            return true;

        } catch (SQLException e) {
            System.out.println("Error : addModule()");
        }
        return false;
    }

    public List<Module> getAllModules() {
        String query = "SELECT module_id, module_name FROM Module";
        try {
            Cursor cs = this.readDatabase.rawQuery(query, null);
            this.modules = new ArrayList<Module>();
            while (cs.moveToNext()) {
                Module module = new Module(cs.getInt(0), cs.getString(1));
                this.modules.add(module);
            }
            return this.modules;
        } catch (SQLException ex) {
            System.out.println("Error : getAllModules");
        }
        return null;
    }

    public List<Student> getAllStudentsFromModule(Module module) {
        String getStudentIds = "SELECT user_id FROM UserModuleProxy WHERE module_id = ?";
        String getAllStudents = "SELECT user_id, user_name, user_email FROM User where user_id IN(" + getStudentIds + ") And user_type = 'Student'";
        try {
            Cursor allStudents = this.readDatabase.rawQuery(getAllStudents, new String[]{String.valueOf(module.getModuleId())});
            List<Student> students = new ArrayList<Student>();
            if (allStudents.getCount() > 0) {
                while (allStudents.moveToNext()) {
                    Student student = new Student(allStudents.getInt(0), allStudents.getString(2), "");
                    student.setName(allStudents.getString(1));
                    students.add(student);
                }
                module.setEnrolledStudents(students);
                return students;
            }
        } catch (SQLException ex) {
            System.out.println("Error : getAllStudentsFromModule()");
        }
        return null;
    }

    public Lecturer getLectureOfModule(Module module) {
        String getStudentIds = "SELECT user_id FROM UserModuleProxy WHERE module_id = ?";
        String getAllStudents = "SELECT user_id, user_name, user_email FROM User where user_id IN(" + getStudentIds + ") And user_type = 'Lecturer'";
        try {
            Cursor lecturer = this.readDatabase.rawQuery(getAllStudents, new String[]{String.valueOf(module.getModuleId())});
            if (lecturer.getCount() > 0) {
                lecturer.moveToFirst();
                Lecturer lc = new Lecturer(lecturer.getInt(0), lecturer.getString(2), "");
                lc.setName(lecturer.getString(1));
                module.setAssignedLecturer(lc);
                return lc;
            }
        } catch (SQLException ex) {
            System.out.println("Error : getAllStudentsFromModule()");
        }
        return null;
    }

    public List<Student> getUnEnrolledStudents(Module module) {
        System.out.println(module.getModuleId());
        String getAllStudents = "SELECT user_id, user_name, user_email FROM USER where user_type like \"S%\" and user_id NOT IN(SELECT U.user_id FROM User U LEFT OUTER JOIN UserModuleProxy M WHERE M.user_id= U.user_id and U.user_type like \"S%\" and M.module_id =?)";
        try {
            Cursor allStudents = this.readDatabase.rawQuery(getAllStudents, new String[]{String.valueOf(module.getModuleId())});
            List<Student> students = new ArrayList<Student>();
            if (allStudents.getCount() > 0) {
                while (allStudents.moveToNext()) {
                    Student student = new Student(allStudents.getInt(0), allStudents.getString(2), "");
                    student.setName(allStudents.getString(1));
                    students.add(student);
                }
                return students;
            }
        } catch (SQLException ex) {
            System.out.println("Error : getAllStudentsFromModule()");
        }
        return null;
    }

    public List<Lecturer> getAllUnAssignedLecturer(Module module) {
        System.out.println("Inside database : " + module.getModuleId());
        if (this.getLectureOfModule(module) != null) {
            return null;
        }
        String dropView = "DROP VIEW AD1";
        String allUnAssigned = "CREATE VIEW AD1 AS SELECT User.user_id, User.user_email, UserModuleProxy.module_id, User.user_type from User LEFT OUTER JOIN UserModuleProxy on User.user_id = UserModuleProxy.user_id AND User.user_type = 'Lecturer'";
        String getAll = "SELECT User.user_id,User.user_name, User.user_email from User INNER JOIN AD1 WHERE User.user_id = AD1.user_id AND AD1.module_id is Null AND AD1.user_type = 'Lecturer'";
        try {
            try {
                this.writeDatabase.execSQL(dropView);
                //this.writeDatabase.rawQuery(dropView,null);
            } catch (Exception ex) {
                System.out.println("View cannot be dropped");
            }
            try {
                this.writeDatabase.execSQL(allUnAssigned);
                //this.writeDatabase.rawQuery(allUnAssigned,null);
                System.out.println("View is created");
            } catch (Exception ex) {
                System.out.println("View is not created");
            }
            Cursor cs = this.readDatabase.rawQuery(getAll, null);
            System.out.println("Result count : " + cs.getCount());
            if (cs.getCount() > 0) {
                List<Lecturer> lists = new ArrayList<Lecturer>();
                while (cs.moveToNext()) {
                    Lecturer lc = new Lecturer(cs.getInt(0), cs.getString(2), null);
                    lc.setName(cs.getString(1));
                    lists.add(lc);
                }
                return lists;
            }
            this.stopDatabase();
        } catch (SQLException ex) {
            System.out.println("Error : getAllUnAssignedLecturer");
        }
        return null;
    }

    public boolean enrollUser(User user, Module module) {
        try {
            ContentValues values = new ContentValues();
            values.put("module_id", module.getModuleId());
            values.put("user_id", user.getId());
            this.writeDatabase.insert("UserModuleProxy", null, values);
            return true;
        } catch (SQLException ex) {
            System.out.println("Error : enrollStudent");
        }
        return false;
    }


    // ********************************************************************************************************* //

    // ****************************************** LOGIN ******************************************************** //

    public List<User> getAllUsers() {
        try {
            String getAllUsers = "SELECT user_id, user_email, user_password, user_type FROM User";
            Cursor cs = this.writeDatabase.rawQuery(getAllUsers, null);
            List<User> users = new ArrayList<>();
            System.out.println(cs.getCount());
            while (cs.moveToNext()) {
                User user = new Student(cs.getInt(0), cs.getString(1), cs.getString(2));
                System.out.println("User is created");
                users.add(user);
            }
            return users;
        } catch (Exception ex) {
            System.out.println("Error : getAllUsers");
        }
        return null;
    }

    public boolean isEmailExist(String email){
        try{
            String getUser = "SELECT user_password from User where user_email = ?";
            System.out.println("Stage 1 ");
            Cursor cs = this.readDatabase.rawQuery("Select user_id user_password from User WHERE user_email = ?",new String[]{email});
            System.out.println("Stage 2 ");
            //this.readDatabase.rawQuery(getUser,new String[]{email});
            if(cs.getCount()>0){
                return true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Error : isEmailExist()");
        }
        return false;
    }

    public boolean isPasswordRight(String password, String email){
        try{
            String getUser = "SELECT user_id FROM User WHERE user_password = ? AND user_email = ?";
            Cursor cs = this.readDatabase.rawQuery(getUser,new String[]{password,email});
            if(cs.getCount()>0){
                return true;
            }
        }catch (Exception ex){
            System.out.println("Error : isPasswordRight()");
        }
        return false;
    }

    public User getUser(String email){
        try {
            String getUser = "SELECT user_id, user_name, user_email, user_password FROM User WHERE user_email = ?";
            Cursor cs = this.readDatabase.rawQuery(getUser,new String[]{email});
            cs.moveToFirst();
            User user = new Student(cs.getInt(0),cs.getString(2),cs.getString(3));
            user.setName(cs.getString(1));
            return user;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    // ********************************************************************************************************* //

    // ****************************************** Lecturer ***************************************************** //

    public Module getAllModulesOfLecturer(int lecturerid) {
        String getModule = "SELECT module_id, module_name FROM Module WHERE module_id  = (SELECT module_id from UserModuleProxy WHERE user_id = ?);";
        try {
            Cursor cs = this.readDatabase.rawQuery(getModule, new String[]{String.valueOf(lecturerid)});
            if (cs.getCount() <= 0) {
                return null;
            }
            cs.moveToFirst();
            return new Module(cs.getInt(0), cs.getString(1));
        } catch (Exception ex) {
            System.out.println("Error : getAllModulesOfLecturer()");
        }
        return null;
    }

    public List<AssessedWork> getAllAssessedWork(Module module) {
        String getWorks = "SELECT work_id, work_name FROM AssessedWork WHERE work_id In (SELECT work_id FROM ModuleWorkProxy WHERE module_id = ?)";
        try {
            Cursor cs = this.readDatabase.rawQuery(getWorks, new String[]{String.valueOf(module.getModuleId())});
            System.out.println("Read is fine");
            System.out.println(cs);
            if (cs.getCount() > 0) {
                List<AssessedWork> works = new ArrayList<AssessedWork>();
                while (cs.moveToNext()) {
                    AssessedWork work = new AssessedWork(cs.getInt(0), cs.getString(1));
                    works.add(work);
                }
                return works;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error : getAllAssessedWork()");
        }
        return null;
    }

    public boolean addAssessedWork(Module module, AssessedWork work) {
        try {
            ContentValues values = new ContentValues();
            values.put("work_name", work.getName());
            this.writeDatabase.insert("AssessedWork", null, values);
        } catch (Exception ex) {
            System.out.println("Error : addAssessedWork");
        }
        String getWorks = "SELECT work_id FROM AssessedWork";
        Cursor cs = this.readDatabase.rawQuery(getWorks, null);
        cs.moveToLast();
        int id = cs.getInt(0);

        ContentValues values2 = new ContentValues();
        values2.put("work_id", id);
        values2.put("module_id", String.valueOf(module.getModuleId()));
        try {
            this.writeDatabase.insert("ModuleWorkProxy", null, values2);
            return true;
        } catch (Exception ex) {
            System.out.println("Error in creating new assessedwork");
        }
        return false;
    }
    public boolean isResultInitialised(AssessedWork work){
        try {
            String query = "SELECT work_name FROM AssessedWork WHERE work_id = ? and result_id IS NOT NULL";
            Cursor cs = this.readDatabase.rawQuery(query, new String[]{String.valueOf(work.getAssessedWorkId())});
            if (cs.getCount() > 0) {
                return true;
            }
            return false;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean isResultUploaded(AssessedWork work){
        try {
            String query = "SELECT result_uploaded FROM AssessedWork WHERE work_id = ?";
            Cursor cs = this.readDatabase.rawQuery(query, new String[]{String.valueOf(work.getAssessedWorkId())});
            cs.moveToFirst();
            String result = cs.getString(0);
            if (result.equalsIgnoreCase("false")) {
                return false;
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Error : isResultUploaded");
        }
        return false;
    }

    public boolean addResultForStudent(Student student, AssessedWork work, String feedback,int marks){
        try {
            boolean isInitialised = this.isResultInitialised(work);
            if (!isInitialised) {
                int result_id = uploadResult(marks, feedback, student.getId(), work.getAssessedWorkId());
                if (result_id != -1) {
                    if (initialiseResult(work, result_id)) {
                        return true;
                    }
                }
            }else if(isInitialised){
                int result_id = uploadResult(marks, feedback, student.getId(), work.getAssessedWorkId());
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return false;
    }

    public int uploadResult(int marks, String feedback, int user_id, int work_id){
        try {
            int id = uploadFeedback(feedback);
            if (id != -1) {
                ContentValues values = new ContentValues();
                values.put("result_mark", marks);
                values.put("feedback_id", id);
                values.put("user_id", user_id);
                values.put("work_id",work_id);
                this.writeDatabase.insert("Result", null, values);
                Cursor cs = this.readDatabase.rawQuery("SELECT result_id FROM Result", null);
                cs.moveToLast();
                return cs.getInt(0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }

    public int uploadFeedback(String feedback){
        try {
            ContentValues v = new ContentValues();
            v.put("feedback_value", feedback);
            this.writeDatabase.insert("Feedback", null, v);
            Cursor cs = this.readDatabase.rawQuery("SELECT feedback_id FROM Feedback", null);
            cs.moveToLast();
            return cs.getInt(0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }

    private boolean initialiseResult(AssessedWork work, int result_id){
        try {
            ContentValues values = new ContentValues();
            values.put("result_id", result_id);
            this.writeDatabase.update("AssessedWork", values, "work_id = ?", new String[]{String.valueOf(work.getAssessedWorkId())});
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public List<Student> getStudentWithNoResults(AssessedWork work, Module module){
        String query = "SELECT user_id FROM Result WHERE work_id = ?";
        Cursor cs = this.readDatabase.rawQuery(query,new String[]{String.valueOf(work.getAssessedWorkId())});
        System.out.println(" *********************************** Stage 1");
        List<Student> allStudents = this.getAllStudentsFromModule(module);
        System.out.println(" *********************************** Stage 1 : count : "+cs.getCount() +" : allStudents:  "+allStudents);
        if(allStudents !=null){
            while(cs.moveToNext() && cs.getCount()>0){
                System.out.println(" *********************************** Stage 1 :  "+cs.getInt(0));
                Student student = new Student(cs.getInt(0),"","");
                if(allStudents.contains(student)){
                    allStudents.remove(student);
                }
            }
            return allStudents;
        }
        return null;
    }

    public boolean uploadResult(AssessedWork work){
        try {
            this.writeDatabase.execSQL("UPDATE AssessedWork " +
                    "SET result_uploaded = 'true' " +
                    "WHERE work_id = " + work.getAssessedWorkId());
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Error : uploadResult()");
        }
        return false;
    }


    // ********************************************************************************************************* //

    // ****************************************** Student ****************************************************** //



    public List<Module> getModulesOfStudent(Student student){
        try{
            String getAllModules = "SELECT module_id, module_name FROM Module WHERE module_id IN (SELECT module_id FROM UserModuleProxy WHERE user_id = ?)";
            Cursor cs = this.readDatabase.rawQuery(getAllModules,new String[]{String.valueOf(student.getId())});
            List<Module> modules = new ArrayList<>();
            while(cs.moveToNext()){
                modules.add(new Module(cs.getInt(0),cs.getString(1)));
            }
            student.setModules(modules);
            return modules;
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Error : getModulesOfStudent()");
        }
        return null;
    }

    public String getFeedback(int feedback_id){
        Cursor cs = this.readDatabase.rawQuery("SELECT feedback_value from Feedback where feedback_id = ?", new String[]{String.valueOf(feedback_id)});
        if(cs.getCount()>0){
            cs.moveToFirst();
            return cs.getString(0);
        }
        return null;
    }


    public Result getResult(AssessedWork work, Student student){
        boolean initialised = this.isResultInitialised(work);
        if(initialised){
            boolean uploaded = this.isResultUploaded(work);
            if(uploaded){
                Result result = new Result(work.getName());
                String query = "SELECT result_mark, feedback_id FROM Result where user_id = ? and work_id = ?";
                Cursor cs = this.readDatabase.rawQuery(query,new String[]{String.valueOf(student.getId()),String.valueOf(work.getAssessedWorkId())});
                cs.moveToFirst();
                result.setMarks(cs.getInt(0));
                String feedback = this.getFeedback(cs.getInt(1));
                System.out.println("Marks : "+cs.getInt(0) + "  :  feedback : "+feedback);
                if(feedback !=null){
                    result.setFeedback(feedback);
                }
                return result;
            }
        }
        return null;
    }

    public List<Result> getMarksForStudent(Student student){
        try {
            List<Module> modules = this.getModulesOfStudent(student);
            System.out.println("Stage___ 1");
            if (modules != null) {
                List<Result> results = new ArrayList<>();
                for (Module module : modules) {
                    System.out.println(module);
                    List<AssessedWork> works = this.getAllAssessedWork(module);
                    System.out.println("Stage___ 2");
                    if (works != null) {
                        module.setAssessedWorks(works);
                        for (AssessedWork work : works) {
                            Result result = this.getResult(work,student);
                            if(result!=null) {
                                result.setModuleName(module.getName());
                                results.add(result);
                            }
                        }
                        System.out.println("Stage___ 3");
                    }
                }
                return results;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Error : getMarksForStudent()");
        }
        return null;
    }

    public double getStudentsAverage(int workid){
        try {
            String query = "SELECT AVG(result_mark) FROM Result where work_id = ?";
            Cursor cs = this.readDatabase.rawQuery(query, new String[]{String.valueOf(workid)});
            cs.moveToFirst();
            return cs.getInt(0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 70;
    }

    public boolean updatePassword(Student student){
        try {
            ContentValues values = new ContentValues();
            values.put("user_password",student.getPassword());
            this.writeDatabase.update("User",values,"user_id=?",new String[]{String.valueOf(student.getId())});
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    // ********************************************************************************************************* //


}