/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nhan.Lam
 */
public class TextFile {

    public void writeText(String fileName, String content)throws IOException{
        try {
            BufferedWriter bufferedWriter = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(fileName, true), 
                            StandardCharsets.UTF_8));
            //Write data.
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            // Always close files.
            bufferedWriter.close();
        }catch(Exception ex) {
            throw ex;
        }
    }
    
}
