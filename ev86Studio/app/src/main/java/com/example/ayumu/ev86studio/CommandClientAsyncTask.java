package com.example.ayumu.ev86studio;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Ayumu on 2015/09/12.
 */
public class CommandClientAsyncTask extends AsyncTask<String, Integer, Long>
{
    Context context;
    String IP;
    int Port;
    CarInformation carInfo;
    TextView txt;

    Socket server;

    private String replay;

    private long time;
    private long DELAY = 33;    //33[ms]����

    private boolean isUnknownHost = false;
    private boolean isLogout = false;

    public CommandClientAsyncTask(Context context, String IP, int Port, CarInformation carInfo, TextView txt)
    {
        this.context = context;
        this.IP = IP;
        this.Port = Port;
        this.carInfo = carInfo;
        this.txt = txt;
    }

    @Override
    protected Long doInBackground(String... params) {   //��������

        try {
            server = new Socket(IP, Port);  //Socket�ʐM�ڑ�
        } catch (IOException e) {
            e.printStackTrace();
            isUnknownHost = true;

            return null;
        }

        try {
            InputStream in = server.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            OutputStream out = server.getOutputStream();
            PrintWriter pw = new PrintWriter(out);

            while (!isLogout) {
                time = System.nanoTime();

                pw.print("G");  //�T�[�o(Edison)�փR�}���h���M
                pw.flush();

               replay = br.readLine();  //�T�[�o����̕ԐM(1�s�҂�)

                publishProgress(0);
                carInfo.loadData(replay);        //carInfo�փZ���T�l�i�[


                time = time - System.nanoTime();
                if ((DELAY - time / 1000000) > 0) {
                    try {
                        Thread.sleep(DELAY - time / 1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                }

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(server != null){
                try{
                    server.close();
                }
                catch(IOException e) {
                }
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, "start client thread", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);

        if(isUnknownHost){  //�T�[�o�[�ڑ����s
            Toast.makeText(context, "Unknown Host", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        txt.setText(replay);
    }

    public void logout()
    {
        isLogout = true;
        return;
    }
}
