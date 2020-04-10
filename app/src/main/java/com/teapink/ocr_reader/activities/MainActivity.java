package com.teapink.ocr_reader.activities;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.teapink.ocr_reader.R;

import com.teapink.ocr_reader.adapter.ComputersAdapter;
import com.teapink.ocr_reader.db.ComputersAppDatabase;
import com.teapink.ocr_reader.db.entity.Computer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Computer> computerArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ComputersAppDatabase computersAppDatabase;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private Button copyButton;
    //private Button mailTextButton;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view_computers);
        computersAppDatabase= Room.databaseBuilder(getApplicationContext(),ComputersAppDatabase.class,"ComputerDB").addCallback(callback).build();


        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        Button readTextButton = (Button) findViewById(R.id.read_text_button);
        readTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

//        copyButton = (Button) findViewById(R.id.copy_text_button);
//        copyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//                    android.text.ClipboardManager clipboard =
//                            (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    clipboard.setText(textValue.getText().toString());
//                } else {
//                    android.content.ClipboardManager clipboard =
//                            (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", textValue.getText().toString());
//                    clipboard.setPrimaryClip(clip);
//                }
//                Toast.makeText(getApplicationContext(), R.string.clipboard_copy_successful_message, Toast.LENGTH_SHORT).show();
//            }
//        });

//        mailTextButton = (Button) findViewById(R.id.mail_text_button);
//        mailTextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_SUBJECT, "Text Read");
//                i.putExtra(Intent.EXTRA_TEXT, textValue.getText().toString());
//                try {
//                    startActivity(Intent.createChooser(i, getString(R.string.mail_intent_chooser_text)));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(getApplicationContext(),
//                            R.string.no_email_client_error, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        Button storeTextButton = (Button) findViewById(R.id.mail_text_button);
        storeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditComputers(false, null, -1);
            }
        });

        Button detectLogoButton = (Button) findViewById(R.id.copy_text_button);
        detectLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetectLogoActivity();
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (textValue.getText().toString().isEmpty()) {
//            //copyButton.setVisibility(View.GONE);
//            //mailTextButton.setVisibility(View.GONE);
//        } else {
//            //copyButton.setVisibility(View.VISIBLE);
//            //mailTextButton.setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void openDetectLogoActivity(){
        Intent intent = new Intent(this, DetectLogo.class);
        startActivity(intent);
    }

    public void addAndEditComputers(final boolean isUpdate, final Computer computer, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_computer, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView computerTitle = view.findViewById(R.id.new_computer);
        final EditText newComputer = view.findViewById(R.id.name);
        final EditText computerSerial = view.findViewById(R.id.serial);

        computerTitle.setText(!isUpdate ? "Add New Computer" : "Edit Info");

        if (isUpdate && computer != null) {
            newComputer.setText(computer.getName());
            computerSerial.setText(computer.getSerial());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                if (isUpdate) {

                                    deleteComputer(computer, position);
                                } else {

                                    dialogBox.cancel();

                                }

                            }
                        });


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(textValue.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter computer brand!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }


                if (isUpdate && computer != null) {

                    updateComputer(newComputer.getText().toString(), computerSerial.getText().toString(), position);
                } else {

                    createComputer(newComputer.getText().toString(), computerSerial.getText().toString());
                }
            }
        });
    }

    private void deleteComputer(Computer computer, int position) {

        computerArrayList.remove(position);

    }

    private void updateComputer(String name, String email, int position) {

        Computer computer = computerArrayList.get(position);

        computer.setName(name);
        computer.setSerial(email);

        computerArrayList.set(position, computer);




    }

    private void createComputer(String name, String email) {

    }


    RoomDatabase.Callback callback= new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //Toast.makeText(getApplicationContext()," On Create Called ",Toast.LENGTH_LONG).show();
            Log.i(TAG, " on create invoked ");


        }


        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //  Toast.makeText(getApplicationContext()," On Create Called ",Toast.LENGTH_LONG).show();
            Log.i(TAG, " on open invoked ");

        }

    };


}