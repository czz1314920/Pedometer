package cn.bluemobi.dylan.step.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.bmobJava.MyUser;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class Rankings extends AppCompatActivity {
    private ListView lv;
    private TextView rankings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankings);
        Bmob.initialize(this, "df877b92936d74812d2b67a4c6c8c43d");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        rankings=(TextView)findViewById(R.id.Rankings);
        lv = (ListView) findViewById(R.id.lv);


        String bql = "select * from _User";
        new BmobQuery<MyUser>().doSQLQuery(bql, new SQLQueryListener<MyUser>() {
            @Override
            public void done(BmobQueryResult<MyUser> bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<MyUser> list = (List<MyUser>) bmobQueryResult.getResults();

                    //实现排名
                    if (list!=null && list.size()>0){
                        Integer temp;
                       for (int i=0;i<list.size();i++){
                           for (int j=i+1;j<list.size();j++){
                               if (list.get(i).getStep() < list.get(j).getStep()){
                                   temp=list.get(i).getStep();
                                   list.get(i).setStep(list.get(j).getStep());
                                   list.get(j).setStep(temp);
                               }
                           }
                       }
                    }

                    if (list != null && list.size() > 0) {
                        ArrayList<HashMap<String,Object>> listitem=new ArrayList<HashMap<String,Object>>();
                        for (int i = 0; i < list.size(); i++) {
                            HashMap<String,Object> map=new HashMap<String, Object>();
                            map.put("username",list.get(i).getUsername());
                            map.put("step",list.get(i).getStep());
                            map.put("ranking",i+1);
                            listitem.add(map);
                        }
                        SimpleAdapter simpleAdapter=new SimpleAdapter(Rankings.this,listitem,R.layout.listitem,new String[]{"username","step","ranking"},new int[]{R.id.textView22, R.id.textView11,R.id.Rankings});
                        lv.setAdapter(simpleAdapter);
                        Toast.makeText(getApplicationContext()," 查询成功，共"+list.size()+"条数据",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext()," 查询成功，无数据返回!",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"错误码："+e.getErrorCode()
                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Rankings.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
