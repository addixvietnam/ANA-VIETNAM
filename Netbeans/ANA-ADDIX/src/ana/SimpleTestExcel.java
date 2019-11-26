package ana;

public class SimpleTestExcel {
    public static void main(String[] args) {
        String excelFileName = "D:\\MyProjects\\Sources\\Netbeans\\ANA-ADDIX\\input\\input_main.xlsx";
        int [] arrCol = {1, 16};
        String test = "c0fd93b70def8ef7bc61395cf68da9c6";
        System.out.println("Length: " + test.length());
        try{
            //Test read all columns in excel file
            System.out.println("=========== Read all columns in excel file ===========");
//            String[][] allData = AddixExcel.readExcelFile(excelFileName, "URL", true, null);    
//            for (int i = 0; i < allData.length; i++){
//                for(int j = 0; j < allData[i].length; j++){
//                    System.out.print(allData[i][j] + "\t");
//                }
//                System.out.println("");
//            }
            
            
            //Test read many columns in excel file
//            System.out.println("=========== Read many columns (array) in excel file ===========");
//            String[][] myArr = AddixExcel.readExcelFile(excelFileName, "URL", false, arrCol);    
//            for (int i = 0; i < myArr.length; i++){
//                for(int j = 0; j < myArr[i].length; j++){
//                    System.out.print(myArr[i][j] + "\t");
//                }
//                System.out.println("");
//            }
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
}
