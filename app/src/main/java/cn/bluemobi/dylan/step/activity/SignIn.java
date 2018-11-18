package cn.bluemobi.dylan.step.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.bmobJava.MyUser;
import cn.bluemobi.dylan.step.step.service.StepService;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import static android.text.TextUtils.isEmpty;

public class SignIn extends AppCompatActivity {
    private  String name;
    private  String password;
    private EditText AccountNumber;
    private EditText LoginPassword;
    private Button Login;
    private TextView New_user_registration;
    private TextView Forget_the_password;
    public static boolean  UserBuffering=true;

    StepService stepService=new StepService();
    MyUser myUser=new MyUser();

    private void Initialization(){
        AccountNumber=(EditText)findViewById(R.id.AccountNumber);
        LoginPassword=(EditText)findViewById(R.id.LoginPassword);
        Login=(Button)findViewById(R.id.Login);
        New_user_registration=(TextView)findViewById(R.id.New_user_registration);
        Forget_the_password=(TextView)findViewById(R.id.Forget_the_password);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        Bmob.initialize(this,"df877b92936d74812d2b67a4c6c8c43d");
        Initialization();
        addListener();

        if(UserBuffering){
            MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
            if(userInfo != null){
                Intent intent1=new Intent(SignIn.this,MainActivity.class);
                startActivity(intent1);
                finish();
            }else{ }
        }else {

        }
    }

    private void addListener() {

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=AccountNumber.getText().toString();
                password=LoginPassword.getText().toString();
                if(isEmpty(AccountNumber.getText()) || isEmpty(AccountNumber.getText())){
                    Toast.makeText(getApplicationContext(),
                            "用户名称或密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    BmobUser.loginByAccount(name, password, new LogInListener<MyUser>() {
                        @Override
                        public void done(MyUser user, BmobException e) {
                            if(user!=null){
                                MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                                if(userInfo!=null){
                                    stepService.CURRENT_STEP=userInfo.getStep();
                                }
                                Toast.makeText(getApplicationContext(),
                                        "用户登陆成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignIn.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "用户登陆失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        New_user_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_registration=new Intent();
                user_registration.setClass(SignIn.this,Registration.class);
                startActivity(user_registration);
                finish();
            }
        });
        Forget_the_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
