package com.myscheduler.HomeScreen;



        import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.myscheduler.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment
{

    /*https://www.mkyong.com/android/android-spinner-drop-down-list-example/*/

    Spinner classroom;
    Button ibNewButton;
    LinearLayout input;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_main, container, false);

        classroom = (Spinner) v.findViewById(R.id.classroom);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroom.setAdapter(dataAdapter);

        input = (LinearLayout) v.findViewById(R.id.input);

        input.setVisibility(View.GONE);

        ibNewButton = (Button) v.findViewById(R.id.event_add);

        ibNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (input.getVisibility()==View.VISIBLE)
                {
                    input.setVisibility(View.GONE);
                }
                else
                {
                    input.setVisibility(View.VISIBLE);
                }
            }
        });




        return v;
    }
}
