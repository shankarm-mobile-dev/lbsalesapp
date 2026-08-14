package com.suguna.lbsalesperson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.suguna.lbsalesperson.R;

public class OutstandingMenu extends AppCompatActivity {
    LinearLayout lyt_sales_hierachy,lyt_branch_hierachy,lyt_customer_hierachy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OutStanding");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        On_FieldDeclartion();
        On_FieldClick();
    }

    private void On_FieldDeclartion() {
        lyt_sales_hierachy =(LinearLayout) findViewById(R.id.lyt_sales_hierachy);
        lyt_branch_hierachy=(LinearLayout) findViewById(R.id.lyt_branch_hierachy);
        lyt_customer_hierachy=(LinearLayout) findViewById(R.id.lyt_customer_hierachy);

    }
    private void On_FieldClick() {
        lyt_sales_hierachy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OutstandingMenu.this,OutstandingSalesHierarchy.class);
                startActivity(i);
            }
        });
        lyt_branch_hierachy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OutstandingMenu.this,OutstandingBranchHierarchy.class);
                startActivity(i);
            }
        });
        lyt_customer_hierachy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OutstandingMenu.this,OutstandingCustomerHierarchy.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
