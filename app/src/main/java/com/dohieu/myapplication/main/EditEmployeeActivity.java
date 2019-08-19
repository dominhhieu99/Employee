package com.dohieu.myapplication.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dohieu.myapplication.APIService;
import com.dohieu.myapplication.R;
import com.dohieu.myapplication.model.Request_Creat_Update;
import com.dohieu.myapplication.model.ResultEdit;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditEmployeeActivity extends AppCompatActivity {
    private TextInputEditText edName;
    private TextInputEditText edSalary;
    private TextInputEditText edAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        edName = findViewById(R.id.edName);
        edSalary =  findViewById(R.id.edSalary);
        edAge =  findViewById(R.id.edAge);

        edAge.setText(getIntent().getExtras().getInt("age")+"");
        edName.setText(getIntent().getExtras().getString("name"));
        edSalary.setText(getIntent().getExtras().getInt("salary")+"");
    }

    public void edit(View view) {
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
            Call<ResultEdit> call = service.updateEmployee(new Request_Creat_Update(name,Integer.parseInt(salary),Integer.parseInt(age)),getIntent().getExtras().getInt("id"));
            call.enqueue(new Callback<ResultEdit>() {
                @Override
                public void onResponse(Call<ResultEdit> call, Response<ResultEdit> response) {
                    if(response!=null){
                        if(response.body().getAge()==Integer.parseInt(age)&&response.body().getName().equals(name)&&response.body().getSalary()==Integer.parseInt(salary)){
                            Toast.makeText(EditEmployeeActivity.this, "Đã sửa", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();

                            finish();
                        }else {
                            Toast.makeText(EditEmployeeActivity.this, "Không thể sửa", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(EditEmployeeActivity.this, "Dữ liệu rỗng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultEdit> call, Throwable t) {
                    if(t.getMessage().equals("Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 18 path $.error.")){
                        Toast.makeText(EditEmployeeActivity.this,"Trùng tên", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(EditEmployeeActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
