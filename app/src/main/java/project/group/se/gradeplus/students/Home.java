package project.group.se.gradeplus.students;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import project.group.se.gradeplus.R;
import project.plusPlatform.Messages;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] allInfo = Messages.messages;

        RecyclerView messageRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_home,container,false);
        CardMessageAdapter adapter = new CardMessageAdapter(allInfo);
        messageRecycler.setAdapter(adapter);

        GridLayoutManager layout = new GridLayoutManager(getActivity(),2);
        messageRecycler.setLayoutManager(layout);
        // Inflate the layout for this fragment
        return messageRecycler;
    }

}
