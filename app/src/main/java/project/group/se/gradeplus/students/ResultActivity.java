package project.group.se.gradeplus.students;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import project.group.se.gradeplus.R;
import project.plusPlatform.AssessedWork;
import project.plusPlatform.CurrentUser;
import project.plusPlatform.Registry;
import project.plusPlatform.Result;
import project.plusPlatform.Student;

public class ResultActivity extends AppCompatActivity {

    public static final String MODULE_ID="MODULEID";
    public static final String MODULE_NAME = "MODULENAME";
    public static final String WORK_ID = "WORKID";
    public static final String WORK_NAME = "WORKNAME";

    private Student student;
    private project.plusPlatform.Module module;
    private AssessedWork work;

    private GraphView graph;
    private TextView mark;
    private TextView feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);
        graph = findViewById(R.id.graph);
        mark = findViewById(R.id.student_marks_textview);
        feedback = findViewById(R.id.student_feedback_textview);
        student = (Student) CurrentUser.getInstance().getUser();
        Intent intent = getIntent();
        module = new project.plusPlatform.Module(intent.getIntExtra(MODULE_ID,-1),intent.getStringExtra(MODULE_NAME));
        work = new AssessedWork(intent.getIntExtra(WORK_ID,-1),intent.getStringExtra(WORK_NAME));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Registry registry = Registry.getInstance();
        registry.startDatabase(this);
        double avg = registry.getStudentsAverage(work.getAssessedWorkId());
        Result result = registry.getResult(work,student);
        double studentAvg = result.getMarks();
        showGraph(avg,studentAvg);
        mark.setText(studentAvg+"");
        feedback.setText(result.getFeedback());
        registry.stopDatabase();
    }

    private void showGraph(double avg, double studentAverage){

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        series.appendData(new DataPoint(1,avg),false,2);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>();
        series2.appendData(new DataPoint(1.5,studentAverage),false,2);
        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.BLACK);
        series.setTitle("Student average");
        series.setColor(Color.RED);

        series2.setTitle("Your average");
        series2.setColor(Color.BLUE);

        graph.addSeries(series);
        graph.addSeries(series2);
        Viewport port = graph.getViewport();
        port.setXAxisBoundsManual(true);
        port.setMaxX(3);
        port.setMinX(0);

        port.setYAxisBoundsManual(true);
        port.setMinY(0);
        port.setMaxY(100);

        port.setScalable(true);
        port.setScalableY(true);
        port.setScrollable(true);
        port.setScrollableY(true);

        graph.getLegendRenderer().setVisible(true);
        graph.setTitle("Student average");
        graph.setTitleTextSize(80);
    }
}
