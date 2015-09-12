package com.example.ayumu.ev86studio;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Ayumu on 2015/09/12.
 */
public class CarInformation
{
    /*センサ用オブジェクト作成*/
    public X wtp1;
    public X wtp2;
    public X current;
    public X voltage;
    public X motorTemp;
    public X controllerTemp;

    public X[] sensors = {wtp1, wtp2, current, voltage, motorTemp, controllerTemp};

    public CarInformation() //コンストラクタ
    {
        /*センサ用インスタンス作成*/
        wtp1 = new X("Wtp1");
        wtp2 = new X("Wtp2");
        current = new X("Current");
        voltage = new X("Voltage");
        voltage = new X("Voltage");
        motorTemp = new X("MotorTemp");
        controllerTemp = new X("ControllerTemp");
    }

    public void loadData(String receive)	//センサ値をまとめて格納するメソッド
    {
        int index = (receive.split(",", 0)).length;    //要素数を得る
        Scanner scanner = new Scanner(receive);         //分割用スキャナ
        scanner.useDelimiter(",");                      //カンマで区切る

        for(int i = 0; i < index; i++) {


            try {
                String data = scanner.next();

                if(data.equals("NA")){

                }
                else{
                    switch (i) {
                        case 0:
                            wtp1.set(Double.parseDouble(data));
                            break;
                        case 1:
                            wtp2.set(Double.parseDouble(data));
                            break;
                        case 2: //Current
                            current.set(Double.parseDouble(data));
                            break;
                        case 3: //Voltage
                            voltage.set(Double.parseDouble(data));
                            break;
                        case 4: //Motor Temprature
                            motorTemp.set(Double.parseDouble(data));
                            break;
                        case 5: //Controller Temprature
                            controllerTemp.set(Double.parseDouble(data));
                            break;
                        default:
                            break;
                    }
                }
            }
            catch (NoSuchElementException e){
                //読み込めなくなったら
                break;
            }
        }
    }



    /*1軸センサ用基底クラス (インナークラス)*/
   final class X
   {
       private double x;
       private final String name;

       /*コンストラクタ*/
       public X(String name){
           x = 0;
           this.name = name;
       }

       public double get(){
           return x;    //センサ値をdoubleで返す
       }

       public int getInt(){
           return (int)x;   //センサ値をintで返す
       }

       public void set(double x){
           this.x = x;      //センサ値を格納
       }
       public String toString(){
           return String.format("%s %f;", name,x);
       }

       public String getName(){
           return name;     //センサ名を返す
       }
   }

}
