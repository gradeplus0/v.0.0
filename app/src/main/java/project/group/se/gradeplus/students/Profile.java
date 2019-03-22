package project.group.se.gradeplus.students;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import project.group.se.gradeplus.R;
import project.plusPlatform.CurrentUser;
import project.plusPlatform.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private Student user;
    private EditText id;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button saveBtn;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view!=null){
            System.out.println(view);
            user = (Student) CurrentUser.getInstance().getUser();
            id = (EditText) view.findViewById(R.id.student_id);
            name = (EditText) view.findViewById(R.id.student_name);
            email = (EditText)view.findViewById(R.id.student_email);
            password = (EditText) view.findViewById(R.id.student_password);
            saveBtn = (Button) view.findViewById(R.id.save_button);
            id.setText(user.getId()+"");
            if(!user.getName().equals("")){
                name.setText(user.getName());
            }
            email.setText(user.getEmailAddress());
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tempName = name.getText().toString();
                    if(!tempName.equals("")){
                        user.setName(tempName);
                    }
                    String tempPass = password.getText().toString();
                    if(!tempPass.equals("")){
                        user.setPassword(tempPass);
                    }
                }
            });
        }
    }
}
