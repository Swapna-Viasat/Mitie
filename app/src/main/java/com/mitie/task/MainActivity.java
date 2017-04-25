package com.mitie.task;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private String m_Text = "";
    private TextView resultText;
    private int mYear, mMonth, mDay;
    @BindView(R.id.quantityValue)
    TextView quantityValue;
    @BindView(R.id.emailValue)
    TextView emailValue;
    @BindView(R.id.dateValue)
    TextView dateValue;
    @BindView(R.id.attributeValue)
    TextView attributeValue;
    @BindView(R.id.priorityValue)
    TextView priorityValue;
    String msg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitie_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.quantityButton)
    public void quantityDialog() {
        showQuantityDialog();
    }

    @OnClick(R.id.priorityButton)
    public void priorityDialog() {
        showPriorityDialog();
    }

    @OnClick(R.id.emailButton)
    public void emailDialog() {
        showEmailDialog();
    }

    @OnClick(R.id.attributeButton)
    public void attributeDialog() {
        showAttributeDialog();
    }

    @OnClick(R.id.dateButton)
    public void dateDialog() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateValue.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public JSONObject jsonData() {
        JSONObject parent = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Quantity", quantityValue.getText());
            jsonObject.put("Priority", priorityValue.getText());
            jsonObject.put("Attributes", attributeValue.getText());
            jsonObject.put("Date", dateValue.getText());
            jsonObject.put("Email", emailValue.getText());
            parent.put("task2", jsonObject);
            Log.d("output", parent.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parent;
    }

    @OnClick(R.id.submit)
    public void submit() {
        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailValue.getText().toString(), null));
        email.putExtra(Intent.EXTRA_SUBJECT, "Task2");
        email.putExtra(Intent.EXTRA_TEXT   , jsonData().toString());
        try {
            startActivity(Intent.createChooser(email, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showAttributeDialog() {
        final CharSequence items[] = {"Power Steering", "Stereo", "Air Conditioning", "Sat Nav"};
        final ArrayList seletedItems = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Attributes");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                             seletedItems.add(indexSelected);

                        } else if (seletedItems.contains(indexSelected)) {
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                         msg="";
                        for (int i = 0; i < seletedItems.size(); i++) {

                            msg=msg+items[(int) seletedItems.get(i)];
                        }
                        attributeValue.setText(msg);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        AlertDialog dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
    }





    private void showEmailDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.email_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        emailValue.setText(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showPriorityDialog() {

        final CharSequence priority[] = new CharSequence[] {"Low", "Medium", "High"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Priority");
        builder.setItems(priority, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                priorityValue.setText(priority[which]);
            }
        });
        builder.show();
    }

    private void showQuantityDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        quantityValue.setText(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}

