package com.example.shady.shady.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.Toast;

import com.example.shady.shady.R;
import com.example.shady.shady.xiaoling.HttpData;
import com.example.shady.shady.xiaoling.HttpGetDataListener;
import com.example.shady.shady.xiaoling.ListData;
import com.example.shady.shady.xiaoling.TextAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shady on 2015/9/14.
 */
public class HomeFragment extends Fragment implements HttpGetDataListener,View.OnClickListener {
    private HttpData httpData;
    private List<ListData> lists;
    private ListView lv;
    private EditText sendtext;
    private Button send_btn;
    private String content_str;
    private TextAdapter adapter;
    private String[] welcome_array;
    private double currentTime=0, oldTime = 0;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment, container, false);
        initView();
        Log.e("a", String.valueOf(lists.size()));
        return view;
    }

    private void initView() {
        lv = (ListView) view.findViewById(R.id.lv);
        sendtext = (EditText)view.findViewById(R.id.sendText);
        send_btn = (Button) view.findViewById(R.id.send_btn);
        lists = new ArrayList<ListData>();
        send_btn.setOnClickListener(this);
        adapter = new TextAdapter(lists,this.getActivity());
        lv.setAdapter(adapter);
        ListData listData;
        listData = new ListData(getRandomWelcomeTips(), ListData.RECEIVER,
                getTime());
        lists.add(listData);
    }

    private String getRandomWelcomeTips() {
        String welcome_tip = null;
        welcome_array = this.getResources()
                .getStringArray(R.array.welcome_tips);
        int index = (int) (Math.random() * (welcome_array.length - 1));
        welcome_tip = welcome_array[index];
        return welcome_tip;
    }

    @Override
    public void getDataUrl(String data) {
        parseText(data);
    }

    public void parseText(String str){
        try {
            JSONObject jb=new JSONObject(str);
            System.out.println(jb.getString("code"));
            System.out.println(jb.getString("text"));
            if (jb.getString("text").length()>60){
                Toast.makeText(this.getActivity(),"话痨吗？哪来这么多话",Toast.LENGTH_LONG).show();
            }
            ListData listData;
            listData=new ListData(jb.getString("text"),ListData.RECEIVER,getTime());
            lists.add(listData);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        getTime();
        content_str=sendtext.getText().toString();
        sendtext.setText("");
        String dropk = content_str.replace(" ", "");
        String droph = dropk.replace("\n", "");
        ListData listData;
        listData=new ListData(content_str,ListData.SEND,getTime());
        lists.add(listData);
        if (lists.size()>30){
            for (int i=0;i<lists.size();i++){
                lists.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        httpData= (HttpData) new HttpData(
                "http://www.tuling123.com/openapi/api?key=fc161612df0bdc01c8e6dd97a88b074f&info="+droph,this).execute();
    }

    private String getTime() {
        currentTime=System.currentTimeMillis();
        SimpleDateFormat formater=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        String str=formater.format(curDate);
        if (currentTime-oldTime>=5*60*1000){
            oldTime=currentTime;
            return str;
        }else {
            return "";
        }
    }
}
