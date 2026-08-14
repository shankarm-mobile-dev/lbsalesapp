package com.suguna.lbsalesperson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.suguna.lbsalesperson.POJO.User;
import com.suguna.lbsalesperson.Utils.LocalStorageSP.*;
public class OrderSummaryMenu extends AppCompatActivity {
    LinearLayout lyt_sales_hierachy, lyt_branch_hierachy, lyt_customer_hierachy,lyt_order_status;
      String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersummary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().getExtras() != null) {
            userid=(getIntent().getStringExtra("userid"));
        }
        On_FieldDeclartion();
        On_FieldClick();
    }

    private void On_FieldDeclartion() {

        lyt_sales_hierachy = (LinearLayout) findViewById(R.id.lyt_sales_hierachy);
        lyt_branch_hierachy = (LinearLayout) findViewById(R.id.lyt_branch_hierachy);
        lyt_customer_hierachy = (LinearLayout) findViewById(R.id.lyt_customer_hierachy);
        lyt_order_status = (LinearLayout) findViewById(R.id.lyt_order_status);
        if(userid.equals("1"))
        {
            lyt_order_status.setVisibility(View.VISIBLE);
        }
        else
        {
            lyt_order_status.setVisibility(View.INVISIBLE);
        }
    }

    private void On_FieldClick() {
        lyt_sales_hierachy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryMenu.this, OrderSalesHierarchy.class);
                startActivity(i);
            }
        });
        lyt_branch_hierachy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryMenu.this, OrderBranchHierarchy.class);
                startActivity(i);
            }
        });
        lyt_customer_hierachy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryMenu.this, OrderCustomerHierarchy.class);
                startActivity(i);
            }
        });
        lyt_order_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryMenu.this, MPchart_manager.class);
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
