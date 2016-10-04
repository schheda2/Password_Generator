package com.example.himanshu.passwordgenerator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    String[] PasswordArray=new String[10];
    int j=0;
    ExecutorService threadPool;
    SeekBar seekBar_count, seekBar_length;
    TextView textView_count, textView_length;
   public int indicator_count,indicator_length;
    Button btn_thread,btn_async;
    int minimum_count=1, minimum_length=8;
    Handler handler;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadPool= Executors.newFixedThreadPool(2);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Computing progress");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);

        seekBar_count=(SeekBar)findViewById(R.id.seekBar_count);
        seekBar_count.setProgress(1);

        seekBar_length=(SeekBar)findViewById(R.id.seekBar_length);
        seekBar_length.setProgress(8);

        textView_count=(TextView)findViewById(R.id.textView_countResult);
        textView_length=(TextView)findViewById(R.id.textView_lengthResult);

        btn_thread=(Button)findViewById(R.id.button_thread);
        btn_async=(Button)findViewById(R.id.button_Async);




        btn_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                Log.d("demo", "button clicked");


                //String password = Util.getPassword(indicator_length);
                //Toast.makeText(getApplicationContext(),password,Toast.LENGTH_LONG).show();
                handler = new Handler(new Handler.Callback() {
                    @Override


                    public boolean handleMessage(Message message) {

                        switch (message.what) {
                            case DoWork.STATUS_START:
                                Log.d("demo", "thread started");
                                progressDialog.show();


                                break;
                            case DoWork.STATUS_STEP:
                                Log.d("demo", "thread resume");
                                //progressDialog.setProgress((Integer)message.obj);
                                progressDialog.setProgress(message.getData().getInt("PROGRESS"));
                                //PasswordArray[j]=message.getData().getString("PASSWORD");
                                //j=j+1;
                                Toast.makeText(getApplicationContext(),message.getData().getString("PASSWORD"),Toast.LENGTH_LONG).show();
                                break;

                            case DoWork.STATUS_DONE:
                                Log.d("demo", "thread ended");
                                //final AlertDialog alert=builder.create();
                                //alert.show();

                                progressDialog.dismiss();
                                //builder.setTitle("Pick a color").setItems(PasswordArray, new DialogInterface.OnClickListener() {
                                //@Override
                                //public void onClick(DialogInterface dialog, int which) {
                                  //  Log.d("demo","Color got checked "+PasswordArray[which]);


                              //  }
                            //});


                                break;
                        }
                        //final AlertDialog alert=builder.create();
                        //alert.show();
                        return false;
                    }
                });

                for(int i=0;i<indicator_count;i++)

                threadPool.execute(new DoWork());

            }
        });




        seekBar_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress <= minimum_count)
                {

                    indicator_count=minimum_count;
                    textView_count.setText("    "+String.valueOf(indicator_count));

                }

                else
                {
                    indicator_count=progress;
                    textView_count.setText("    "+String.valueOf(indicator_count));

                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress <= minimum_length)
                {

                    indicator_length=minimum_length;
                    textView_length.setText("    "+String.valueOf(indicator_length));

                }

                else
                {
                    indicator_length=progress;
                    textView_length.setText("    "+String.valueOf(indicator_length));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    class DoWork implements Runnable{

        public static final int STATUS_START = 0;
        public static final int STATUS_STEP = 1;
        public static final int STATUS_DONE = 10;

        @Override
        public void run() {

            Log.d("demo","in run method");
            Message message = new Message();
            message.what = STATUS_START;
           handler.sendMessage(message);

            //for (int i = 0; i <= 1000; i++) {
                //for (int j = 0; j <= 10000000; j++) {

                //}
                message = new Message();
                message.what = STATUS_STEP;
                String password = Util.getPassword(indicator_length);

                //message.obj = i + 1;


                Bundle data=new Bundle();
                data.putInt("PROGRESS",10);
            data.putString("PASSWORD",password);
                message.setData(data);
             handler.sendMessage(message);

            //}
            message = new Message();
            message.what = STATUS_DONE;
            handler.sendMessage(message);
        }
    }

}
