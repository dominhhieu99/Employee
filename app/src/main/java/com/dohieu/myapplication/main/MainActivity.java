package com.dohieu.myapplication.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.dohieu.myapplication.APIService;
import com.dohieu.myapplication.R;
import com.dohieu.myapplication.adpter.EmployeeAdapter;
import com.dohieu.myapplication.listener.OnClick;
import com.dohieu.myapplication.listener.OnDelete;
import com.dohieu.myapplication.listener.OnEdit;
import com.dohieu.myapplication.model.Employees;
import com.dohieu.myapplication.model.ResultDelete;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnClick, OnDelete, OnEdit {
    private RecyclerView rvListEmployee;
    private FloatingActionButton fabAdd;
    private List<Employees> employeesList;
    private EmployeeAdapter employeeAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAction();
        employeesList = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(employeesList, this, this, this, this);
        rvListEmployee.setAdapter(employeeAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvListEmployee.setLayoutManager(manager);
        getData();
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dummy.restapiexample.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<List<Employees>> call = service.getAllEmployees();
        call.enqueue(new Callback<List<Employees>>() {
            @Override
            public void onResponse(Call<List<Employees>> call, Response<List<Employees>> response) {
                progressBar.setVisibility(View.GONE);
                for (int i = 0; i < response.body().size(); i++) {
                    employeesList.add(new Employees(response.body().get(i).getId(),
                            response.body().get(i).getEmployeeName(),
                            response.body().get(i).getEmployeeSalary(),
                            response.body().get(i).getEmployeeAge(),
                            response.body().get(i).getProfileImage()));
                    Collections.reverse(employeesList);
                    employeeAdapter.changeDataset(employeesList);
                }
            }

            @Override
            public void onFailure(Call<List<Employees>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        rvListEmployee = findViewById(R.id.rvListEmployee);
        fabAdd = findViewById(R.id.fabAdd);
        progressBar = findViewById(R.id.progressbar);
    }

    private void initAction() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreatEmployeeActivity.class));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        employeesList.clear();
        getData();
    }

    @Override
    public void OnClick(int pos) {
        Toast.makeText(MainActivity.this, "Chi tiết", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDelete(final int pos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Xóa");
        builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://dummy.restapiexample.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIService service = retrofit.create(APIService.class);
                Call<ResultDelete> call = service.deleteEmployee(employeesList.get(pos).getId());
                call.enqueue(new Callback<ResultDelete>() {
                    @Override
                    public void onResponse(Call<ResultDelete> call, Response<ResultDelete> response) {
                        if (response.body().getSuccess().getText().equals("successfully! deleted Records")) {
                            Toast.makeText(MainActivity.this, response.body().getSuccess().getText(), Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<ResultDelete> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void OnEdit(int pos) {
        Intent intent = new Intent(MainActivity.this,EditEmployeeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",employeesList.get(pos).getId());
        bundle.putInt("age",employeesList.get(pos).getEmployeeAge());
        bundle.putString("name",employeesList.get(pos).getEmployeeName());
        bundle.putInt("salary",employeesList.get(pos).getEmployeeSalary());

        //position
        bundle.putInt("pos",pos);

        intent.putExtras(bundle);
        startActivityForResult(intent,1);

    }
}

