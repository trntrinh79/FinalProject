package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setSpinnerItem();
    }
    public void onConfirmClick(View view){
        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        EditText etNameUser = (EditText) findViewById(R.id.etNameUser);
        Spinner spAgeUser = (Spinner) findViewById(R.id.spinnerAgeUser);
        bundle.putString("NameUser", etNameUser.toString());
        bundle.putString("AgeUser",spAgeUser.toString());
        intent.putExtras(bundle);
        String strNameUser = new String(etNameUser.getText().toString());
        strNameUser = cleanText(strNameUser);
        int result = 0;
        if (strNameUser.equals("")){
            Toast.makeText(UserActivity.this,"Hãy điền tên bạn vào khung!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(UserActivity.this,strNameUser,Toast.LENGTH_LONG).show();
            startActivityForResult(intent,result);
        }
    }


    public String cleanText(String str){

        //Xóa khoảng cách đầu chuỗi.
        while(str.charAt(0) == ' ' ){
            str = str.substring(1);
            if (str.isEmpty()) break;
        }

        //Xóa khoảng giữa các chữ .
        for (int i=0; i<str.length(); i++){
            if (str.charAt(i)==' '&& str.charAt(i+1)==' '){
                str = str.substring(0,i)+str.substring(i+1);
                i--;
            }
        }

        //Xóa Enter giữa chuỗi.
        str = str.replace("\n"," ");
        return str;
    }





    private void setSpinnerItem(){
        Spinner spinner = (Spinner) findViewById(R.id.spinnerAgeUser);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1);
        for(int i=1;i<100;i++){
            arrayAdapter.add(i);
        }
        spinner.setAdapter(arrayAdapter);
    }
}