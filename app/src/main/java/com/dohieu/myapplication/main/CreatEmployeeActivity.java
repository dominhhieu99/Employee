package com.dohieu.myapplication.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dohieu.myapplication.APIService;
import com.dohieu.myapplication.R;
import com.dohieu.myapplication.model.Request_Creat_Update;
import com.dohieu.myapplication.model.ResultCreat;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatEmployeeActivity extends AppCompatActivity {
    private TextInputEditText edName;
    private TextInputEditText edSalary;
    private TextInputEditText edAge;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_employee);

        edName = findViewById(R.id.edName);
        edSalary =  findViewById(R.id.edSalary);
        edAge =  findViewById(R.id.edAge);
    }

    public void Creat(View view) {
        final String name = edName.getText().toString().trim();
        final String salary = edSalary.getText().toString().trim();
        final String age = edAge.getText().toString().trim();

        if(name.isEmpty()||salary.isEmpty()||age.isEmpty()){
            if(name.isEmpty())
                edName.setError("Mời nhập name");
            if(salary.isEmpty())
                edSalary.setError("Mời nhập salary");
            if(age.isEmpty())
                edAge.setError("Mời nhập age");
        }else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://dummy.restapiexample.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            APIService service = retrofit.create(APIService.class);
            Call<ResultCreat> call = service.creatEmployee(new Request_Creat_Update(name,Integer.parseInt(salary),Integer.parseInt(age)));
            call.enqueue(new Callback<ResultCreat>() {
                @Override
                public void onResponse(Call<ResultCreat> call, Response<ResultCreat> response) {
                    if(response!=null){
                        if(response.body().getAge()==Integer.parseInt(age)&&response.body().getName().equals(name)&&response.body().getSalary()==Integer.parseInt(salary)){
                            Toast.makeText(CreatEmployeeActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(CreatEmployeeActivity.this, "Không thể thêm", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(CreatEmployeeActivity.this, "Dữ liệu rỗng", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResultCreat> call, Throwable t) {
                    Log.e("onFailure", "onFailure: " + t.getMessage());
                    if(t.getMessage().equals("Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 18 path $.error.")){
                        Toast.makeText(CreatEmployeeActivity.this,"Trùng tên", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(CreatEmployeeActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
