package project.group.se.gradeplus.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import project.group.se.gradeplus.R;
import project.plusPlatform.Lecturer;
import project.plusPlatform.Module;
import project.plusPlatform.Registry;

public class LecturerPage extends AppCompatActivity {

    public static final String MODULE = "M";
    private int moduleId;
    private Lecturer lc;
    private TextView id;
    private TextView idHead;
    private TextView nameHead;
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_page);
        moduleId = getIntent().getIntExtra(MODULE,0);
        Registry rs = Registry.getInstance();
        rs.startDatabase(this);
        id = findViewById(R.id.lecturer_id);
        name = findViewById(R.id.lecturer_name);
        idHead = findViewById(R.id.id_head);
        nameHead = findViewById(R.id.name_head);
        lc = rs.getLectureOfModule(new Module(moduleId,""));
        if(lc != null){
            id.setText(lc.getId()+"");
            name.setText(lc.getName());
        }else{
            idHead.setText("Lecturer not assigned yet");
            nameHead.setText("");
        }
    }
}
