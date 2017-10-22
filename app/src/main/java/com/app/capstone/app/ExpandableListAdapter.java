package com.app.capstone.app;

/**
 * Created by root on 10/08/17.
 */

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.app.capstone.app.Goals.CurrentGoals;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public void removeChild(int index){
        _listDataChild.remove(index);
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        Button btn = (Button) convertView.findViewById(R.id.completeGoalBtn);
        Button btn2 = (Button) convertView.findViewById(R.id.deleteGoalBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GONE");

                Fragment fragment = null;
                Class fragmentClass = CurrentGoals.class;
                try {
                    View parent = (View) v.getParent().getParent().getParent();
                    TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItem);
                    String b = String.valueOf(taskTextViewItem.getText());
                    String[] splitStr = b.split("-");
                    String idStr = splitStr[0].replace(" ", "");
                    int id2 = Integer.parseInt(idStr);
                    //final Goal g = goalsMap.get(id2);
                    //g.setCompleted(true);
                    update(id2, "PUT", v);
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentActivity a = (FragmentActivity) _context;
                    FragmentManager fragmentManager = a.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.goalsContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                System.out.println("GONE");


                try {
                    View parent = (View) v.getParent().getParent().getParent();
                    TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItem);
                    String b = String.valueOf(taskTextViewItem.getText());
                    String[] splitStr = b.split("-");
                    String idStr = splitStr[0].replace(" ", "");
                    final int id2 = Integer.parseInt(idStr);
                    //final Goal g = goalsMap.get(id);


                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(v.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(v.getContext());
                    }
                    builder.setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println("Deleting");
                                    try {
                                        Fragment fragment = null;
                                        Class fragmentClass = CurrentGoals.class;
                                        update(id2, "DELETE", v);

                                        fragment = (Fragment) fragmentClass.newInstance();
                                        FragmentActivity a = (FragmentActivity) _context;
                                        FragmentManager fragmentManager = a.getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.goalsContent, fragment).commit();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                    //final Goal g = goalsMap.get(id2);
                    //g.setCompleted(true);

                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void update(int id2, String method, final View view) throws JSONException {
        JSONObject data = new JSONObject();
        System.out.println("Goal completing");
        data.put("Student_ID", id2);
        data.put("Completed", "1");

        int req = 0;
        JSONObject jsonData = null;
        String uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/goals/" + id2;
        String requestBody = null;

        switch(method){
            case "PUT":
                req = com.android.volley.Request.Method.PUT;
                jsonData = data;
                requestBody = jsonData.toString();
                break;
            case "DELETE":
                req = com.android.volley.Request.Method.PUT;
                break;
        }


        final String finalRequestBody = requestBody;
        StringRequest jsObjRequest = new StringRequest
                (req, uri, new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());


                    }


                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() {
                try {
                    return finalRequestBody == null ? null : finalRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            finalRequestBody, "utf-8");
                    return null;
                }
            }
        };
        Requester.getInstance(_context).addToRequestQueue(jsObjRequest);
    }
}