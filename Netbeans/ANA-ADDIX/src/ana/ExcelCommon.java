package ana;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.addix.utils.AddixExcel;

public class ExcelCommon extends AddixExcel{
    private String fileName;
    public ExcelCommon(String fileName) {
        super(fileName);
        this.fileName = fileName;
    }

    @Override
    public boolean writeExcelFile(String string, String[][] strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean writeExcelFile(String string, int i, int i1, String[][] strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String[][] readColumns(String sheetName, int[] columnArray){
        String[][] arrData = null;
        try{
            String strPathInFile = URLDecoder.decode(fileName, "UTF-8");
            FileInputStream fileInputStream = new FileInputStream(fileName);                      
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();            
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fileInputStream);
            XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
            Iterator<Row> rowIterator = mySheet.iterator();
            int colDataSize = columnArray.length;
            int rowDataSize = mySheet.getLastRowNum();
            arrData = new String[rowDataSize][colDataSize];            
            //Assign data to array            
            int i = 0;
            while(rowIterator.hasNext()){                
                //Skip row (title row)                                   
                Row row = rowIterator.next();     
                if(row.getRowNum() > 0){                    
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int j = 0;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = "";
                        for(int iter = 0; iter < columnArray.length; iter++){
                            cellValue = dataFormatter.formatCellValue(cell);                                         
                            arrData[i][j] = cellValue;                                    
                            j++;
                            if(iter < columnArray.length -1){
                                cell = cellIterator.next();
                            }
                        }                     
                    }
                    i++;
                }
            }                
            myWorkBook.close();
            fileInputStream.close();            
        }catch(Exception ex){
            System.out.println("Error read input many column " + ex.toString());
        }
        return arrData;
    }
}
