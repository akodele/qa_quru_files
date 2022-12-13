package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

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
        try (
                InputStream resource = cl.getResourceAsStream("zipFile.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
                if (entry.getName().contains(".xls")){
                    XLS content = new XLS(zis);
                    assertThat(content.excel.getSheetAt(2).getRow(18).getCell(6).getNumericCellValue()).isEqualTo(0.423);
                }else if(entry.getName().contains(".csv")){
                    try (
                            CSVReader reader = new CSVReader(new InputStreamReader(zis))
                    ) {
                        List<String[]> content = reader.readAll();
                        assertThat(content.get(6)[0]).contains("Campbell");
                    }
                }else if(entry.getName().contains(".pdf")){
                    PDF content = new PDF(zis);
                    assertThat(content.text).contains("Туризм. Профиль Технология и организация туроператорских и турагентских услуг.");
                }
            }
        }
    }
}
