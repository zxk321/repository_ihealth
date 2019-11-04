import com.zxk.ihealth.domain.Phone;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

public class PoiTest {

    @Test
    public void readXlsx() throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("C:\\Users\\xkxy\\Desktop\\user.xlsx"));
        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    System.out.println(cell.getStringCellValue());
                }
            }
        }
        workbook.close();
    }

    @Test
    public void writeXlsx() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("xiaoyu");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("品牌");
        row.createCell(2).setCellValue("名称");
        row.createCell(3).setCellValue("价格");

        ArrayList<Phone> phoneList = new ArrayList<>();
        Phone phone1 = new Phone("华为","荣耀20",2999.00);
        Phone phone2 = new Phone("小米","小米9pro",3999.00);
        Phone phone3 = new Phone("Oppo","Reno",4999.00);
        Phone phone4 = new Phone("一加","1+7",3699.00);
        phoneList.add(phone1);
        phoneList.add(phone2);
        phoneList.add(phone3);
        phoneList.add(phone4);

        for (int i = 1; i <= phoneList.size(); i++) {
            XSSFRow row1 = sheet.createRow(i);
            row1.createCell(0).setCellValue(i);
            row1.createCell(1).setCellValue(phoneList.get(i-1).getBrand());
            row1.createCell(2).setCellValue(phoneList.get(i-1).getName());
            row1.createCell(3).setCellValue(phoneList.get(i-1).getPrice());
        }

        //创建输出流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("C:\\Users\\xkxy\\Desktop\\phone.xlsx"));
        workbook.write(bos);
        bos.flush();
        bos.close();
        workbook.close();
    }
}
