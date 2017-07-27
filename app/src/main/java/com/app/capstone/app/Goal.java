package com.app.capstone.app;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import static android.R.color.black;
import static android.R.color.white;

/**
 * Created by benjamin on 27/07/17.
 */

public class Goal {
    String name;
    String description;
    Date end_date;
    Context context;

    public Goal(String name, String description, Date date, Context context){
        this.name = name;
        this.description = description;
        this.end_date = date;
        this.context = context;
        System.out.println("Goal contructor");
    }

    public TextView getView(){
        System.out.println("Getting view");
        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setShadowLayer(1, 1, 1, black);
        System.out.println("returning view");
        return textView;
    }
}
