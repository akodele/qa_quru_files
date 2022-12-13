package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import guru.qa.model.Marketplace;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {
    ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void zipParseTest() throws Exception {

        InputStream resource = cl.getResourceAsStream("zipFile.zip");
        ZipInputStream zis = new ZipInputStream(resource);
        try {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("got entry " + entry);

                if (entry.getName().contains(".pdf")){
                    PDF content = new PDF(zis);
                    assertThat(content.text).contains("Роман Савин");
                }

                if (entry.getName().contains(".csv")){
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(6)[0]).contains("Campbell");
                }

                if (entry.getName().contains(".xls")){
                    XLS content = new XLS(zis);
                    assertThat(content.excel.getSheetAt(2).getRow(18).getCell(6).getNumericCellValue()).isEqualTo(0.423);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        zis.close();
        resource.close();
    }

    @Test
    void jsonParseTest() throws IOException{
        ObjectMapper objectMapper=new ObjectMapper();
        Marketplace marketplace = objectMapper.readValue(new File("src/test/resources/jmart.json"), Marketplace.class);

        String[] sellers={"Small", "A-Store"};
        String[] categories={"Meat", "Milk", "Candy"};

        assertThat(marketplace.getLink()).contains("jmart.kz");
        assertThat(marketplace.getSeller().length).isEqualTo(2);
        for (int i=0;i<marketplace.getSeller().length;i++){
            assertThat(marketplace.getSeller()[i].getId()).isEqualTo(i);
            assertThat(marketplace.getSeller()[i].getName()).isEqualTo(sellers[i]);
            assertThat(marketplace.getSeller()[i].getActive()).isEqualTo(true);
            for (int j=0;j<marketplace.getSeller()[i].getCategories().length;j++){
                assertThat(marketplace.getSeller()[i].getCategories()[j]).isEqualTo(categories[j]);
            }
        }
    }
}
