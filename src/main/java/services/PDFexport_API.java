package services;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.FileOutputStream;

public class PDFexport_API {


    public static void addCompanyHeader(Document document) {
        try {
            // 🏢 Load Logo
            Image logo = Image.getInstance("C:/Users/ACER PC2/IdeaProjects/pidev/src/main/resources/logo.PNG");
            logo.scaleToFit(100, 100); // Resize logo

            // 🏢 Add Company Name
            Font companyFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph companyText = new Paragraph("MedTravel", companyFont);
            companyText.setAlignment(Element.ALIGN_RIGHT);

            // 🏢 Arrange logo & text in a table
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell textCell = new PdfPCell(companyText);
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            headerTable.addCell(logoCell);
            headerTable.addCell(textCell);

            document.add(headerTable);
            document.add(new Paragraph("\n")); // Space below header
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error adding header.");
        }
    }


    public static <T> void exportToPDF(TableView<T> tableView, String filePath) {

        if (filePath == null) {
            System.out.println("❌ No file selected.");
            return;
        }
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            addCompanyHeader(document);

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Liste Plannings", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Create Table with the same number of columns as TableView
            PdfPTable pdfTable = new PdfPTable(tableView.getColumns().size());
            pdfTable.setWidthPercentage(100);

            // Add column headers
            for (TableColumn<T, ?> column : tableView.getColumns()) {
                pdfTable.addCell(new PdfPCell(new Phrase(column.getText(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD))));
            }

            // Add rows from TableView
            ObservableList<T> items = tableView.getItems();
            for (T item : items) {
                for (TableColumn<T, ?> column : tableView.getColumns()) {
                    Object cellValue = column.getCellData(item);
                    pdfTable.addCell(new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "")));
                }
            }

            document.add(pdfTable);
            document.close();
            System.out.println("✅ PDF Exported: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error exporting PDF.");
        }
    }
}
