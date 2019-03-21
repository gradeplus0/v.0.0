package project.group.se.gradeplus.Lecturer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.Registry;
import project.plusPlatform.Student;

public class ResultScreenActivity extends AppCompatActivity {

    public static final String WORK_ID = "WORKID";
    public static final String WORK_NAME = "WORKNAME";
    public static final String STUDENT_ID = "STUDENTID";
    public static final String STUDENT_NAME = "STUDENTNAME";
    public static final String STUDENT_EMAIL = "EMAIL";

    private AssessedWork work;
    private Student student;

    private EditText marks;
    private EditText feedback;
    private TextView name;
    private Button uploadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Intent intent = getIntent();
        marks = findViewById(R.id.student_marks_editview);
        feedback = findViewById(R.id.student_feedback_editview);
        name = findViewById(R.id.student_name_titleview_value);
        uploadButton = findViewById(R.id.upload_marks_button);
        work = new AssessedWork(intent.getIntExtra(WORK_ID,-1),intent.getStringExtra(WORK_NAME));
        student = new Student(intent.getIntExtra(STUDENT_ID,-1),intent.getStringExtra(STUDENT_EMAIL),"");
        student.setName(intent.getStringExtra(STUDENT_NAME));
        name.setText(student.getName());

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultScreenActivity that = ResultScreenActivity.this;
                Registry registry = Registry.getInstance();
                registry.startDatabase(ResultScreenActivity.this);
                int marks = (int) Double.parseDouble(that.marks.getText().toString());
                String feedback = that.feedback.getText().toString();
                boolean res= registry.addResultForStudent(that.student,that.work,feedback,marks);
                if(res){
                    Toast.makeText(that, "Result uploaded", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(that, "Result is not uploaded due to database problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
