package com.example.ayumu.ev86studio;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.logging.LogRecord;


public class ev86Studio_1_0 extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    //CarInformationクラスのオブジェクト
    CarInformation carInfo;

    //CommandClientクラスのオブジェクト
    CommandClientAsyncTask commClientTask;

    //SensorUpdateクラスのオブジェクト
    SensorUpdate sensorUp;

    //SensorUpdate用ハンドラオブジェクト
    Handler sensorHandler;


    //ウィジェット用オブジェクト作成
    private EditText etxtIP;
    private EditText etxtPort;

    private ToggleButton tglbtnConnection;
    private TextView txtRecieveComm;

    private TextView txtWtp1;
    private TextView txtWtp2;
    private TextView txtCurrent;
    private TextView txtVoltage;
    private TextView txtMotorTemp;
    private TextView txtControllerTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev86_studio_1_0);

        //ウィジェットのインスタンス作成
        etxtIP = (EditText)findViewById(R.id.etxtIP);
        etxtPort = (EditText)findViewById(R.id.etxtPort);

        tglbtnConnection = (ToggleButton)findViewById(R.id.tglbtnConnection);
        txtRecieveComm = (TextView)findViewById(R.id.txtRecieveComm);

        txtWtp1 = (TextView)findViewById(R.id.vtxtWtmp1);
        txtWtp2 = (TextView)findViewById(R.id.vtxtWtmp2);
        txtCurrent = (TextView)findViewById(R.id.vtxtCurrent);
        txtVoltage = (TextView)findViewById(R.id.vtxtVoltage);
        txtMotorTemp = (TextView)findViewById(R.id.vtxtMotorTemp);
        txtControllerTemp = (TextView)findViewById(R.id.vtxControllerTemp);

        //イベントリスナー登録
        tglbtnConnection.setOnCheckedChangeListener(this);

        //CarInformationクラスのインスタンス作成
        carInfo = new CarInformation();

        //SensorUpdateクラスのHandlerインスタンス作成
        sensorHandler = new Handler();

        //SensorUpdateクラスインスタンス作成
        sensorUp = new SensorUpdate(sensorHandler);

        //SensorUpdateスレッドスタート
        sensorUp.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ev86_studio_1_0, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton combtn, boolean isChecked)
    {
        String tglStr = combtn.getText().toString();	//押されたボタンの名前を取得

        if(tglStr.equals("Connect")){ //サーバへのConnectionボタン
            if(isChecked) {
                String serverIP = etxtIP.getText().toString();
                int serverPort = Integer.parseInt(etxtPort.getText().toString());

                String[] splitIP = serverIP.split("\\.");    //カンマで分割

                if(splitIP.length < 4) {//IPアドレスの形式なら4つ以上の配列になるはず
                    Toast.makeText(this, "Can't use the Server IP", Toast.LENGTH_SHORT).show();
                    tglbtnConnection.setChecked(false);
                    return;
                }

                /*コマンドクライアントタスクのインスタンス作成*/
                commClientTask = new CommandClientAsyncTask(this, serverIP, serverPort, carInfo, txtRecieveComm);

                /*コマンドクライアントスレッドスタート*/
                commClientTask.execute("start");
            }
        }
        else if(tglStr.equals("DisConnect")){
            commClientTask.logout();

            if(commClientTask != null) {
                commClientTask = null;
            }

        }
        else{

        }
    }


    /*センサ値アップデート用クラス*/
    class SensorUpdate implements Runnable
    {
        Thread thread;
        Handler handler;

        private long time;
        private final int DELAY = 33;


        public SensorUpdate(Handler handler)
        {
            this.handler = handler;
        }

        @Override
        public void run() {

            while(thread != null){

                time = System.nanoTime();

                handler.post(new Runnable(){
                    public void run(){
                        /*各テキストへ値を表示*/
                        txtWtp1.setText(String.valueOf(carInfo.wtp1.get()));
                        txtWtp2.setText(String.valueOf(carInfo.wtp2.get()));
                        txtCurrent.setText(String.valueOf(carInfo.current.get()));
                        txtVoltage.setText(String.valueOf(carInfo.voltage.get()));
                        txtMotorTemp.setText(String.valueOf(carInfo.motorTemp.get()));
                        txtControllerTemp.setText(String.valueOf(carInfo.controllerTemp.get()));
                    }
                });


                time = time - System.nanoTime();

                if(DELAY - (time / 1000000) > 0){
                    try {
                        thread.sleep(DELAY - (time/1000000));
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        public void start()
        {
            thread = new Thread(this);
            thread.start();
        }

        public void stop()
        {
            thread = null;
        }
    }
}
