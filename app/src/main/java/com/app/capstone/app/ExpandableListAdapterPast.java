package com.app.capstone.app;

/**
 * Created by root on 10/08/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import com.app.capstone.app.Goals.PastGoals;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterPast extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapterPast(Context context, List<String> listDataHeader,
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

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_past, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItemPast);

        Button btn = (Button) convertView.findViewById(R.id.uncompleteGoal);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GONE");

                Fragment fragment = null;
                Class fragmentClass = PastGoals.class;
                try {
                    View parent = (View) v.getParent().getParent().getParent();
                    TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItemPast);
                    String b = String.valueOf(taskTextViewItem.getText());
                    String[] splitStr = b.split("-");
                    String idStr = splitStr[0].replace(" ", "");
                    int id2 = Integer.parseInt(idStr);
                    //final Goal g = goalsMap.get(id2);
                    //g.setCompleted(true);
                    update(id2, "DELETE", v);
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
            convertView = infalInflater.inflate(R.layout.list_group_past, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeaderPast);
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
        System.out.println("Goal uncompleting");
        data.put("Student_ID", id2);
        data.put("Completed", "0");

        int req = 0;
        JSONObject jsonData = null;
        String uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/goals/" + id2;
        String requestBody = null;

        req = com.android.volley.Request.Method.PUT;
        jsonData = data;
        requestBody = jsonData.toString();

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