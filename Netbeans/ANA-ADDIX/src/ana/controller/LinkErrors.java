/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 *
 * @author Nhan.Lam
 */
public class LinkErrors {
    public static String[] readLinkErrors(String fileName){
        try {
            ArrayList<String> myData = new ArrayList();
            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
            
            String strLine; 
            while ((strLine = bufferedReader.readLine()) != null) {
                System.out.println(strLine);
                myData.add(strLine);
            }
            bufferedReader.close();
            String[] arrResult = new String[myData.size()];
            arrResult = myData.toArray(arrResult); 
            return arrResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }           
    }
    
    public static void writeLinkErrors(String fileName, String strContent){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8));            
            bufferedWriter.write(strContent);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }         
    }
}
