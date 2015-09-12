package com.example.ayumu.ev86studio;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Ayumu on 2015/09/12.
 */
public class CarInformation
{
    /*�Z���T�p�I�u�W�F�N�g�쐬*/
    public X wtp1;
    public X wtp2;
    public X current;
    public X voltage;
    public X motorTemp;
    public X controllerTemp;

    public X[] sensors = {wtp1, wtp2, current, voltage, motorTemp, controllerTemp};

    public CarInformation() //�R���X�g���N�^
    {
        /*�Z���T�p�C���X�^���X�쐬*/
        wtp1 = new X("Wtp1");
        wtp2 = new X("Wtp2");
        current = new X("Current");
        voltage = new X("Voltage");
        voltage = new X("Voltage");
        motorTemp = new X("MotorTemp");
        controllerTemp = new X("ControllerTemp");
    }

    public void loadData(String receive)	//�Z���T�l���܂Ƃ߂Ċi�[���郁�\�b�h
    {
        int index = (receive.split(",", 0)).length;    //�v�f���𓾂�
        Scanner scanner = new Scanner(receive);         //�����p�X�L���i
        scanner.useDelimiter(",");                      //�J���}�ŋ�؂�

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
                //�ǂݍ��߂Ȃ��Ȃ�����
                break;
            }
        }
    }



    /*1���Z���T�p���N���X (�C���i�[�N���X)*/
   final class X
   {
       private double x;
       private final String name;

       /*�R���X�g���N�^*/
       public X(String name){
           x = 0;
           this.name = name;
       }

       public double get(){
           return x;    //�Z���T�l��double�ŕԂ�
       }

       public int getInt(){
           return (int)x;   //�Z���T�l��int�ŕԂ�
       }

       public void set(double x){
           this.x = x;      //�Z���T�l���i�[
       }
       public String toString(){
           return String.format("%s %f;", name,x);
       }

       public String getName(){
           return name;     //�Z���T����Ԃ�
       }
   }

}
