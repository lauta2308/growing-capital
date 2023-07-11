package mindhub.homebanking.controllers;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import mindhub.homebanking.Dtos.PosnetPaymentDto;
import mindhub.homebanking.Dtos.TransactionDTO;
import mindhub.homebanking.Dtos.TransactionFilterDto;
import mindhub.homebanking.services.AccountService;
import mindhub.homebanking.services.ClientService;
import mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;


@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    private final Resource logoResource = new ClassPathResource("static/web/imagenes/GrowingCapitalTransparent-logo.png");

    private final ResourceLoader resourceLoader;


    @Autowired
    public TransactionController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> makeTransaction(@RequestParam Double ammount, @RequestParam String description, @RequestParam String sender, @RequestParam String receiver, Authentication authentication) {

        return transactionService.makeTransaction(ammount, description, sender, receiver, authentication);


    }


    @Transactional
    @PostMapping("/api/posnetpayment")
    public ResponseEntity<Object> paymentWithPosnet(@RequestBody PosnetPaymentDto posnetPaymentDto) {


        return transactionService.paymentWithPosnet(posnetPaymentDto);


    }


    @GetMapping("/api/transactions/filter")
    public Set<TransactionDTO> filterTransactions(Authentication authentication, TransactionFilterDto transactionFilterDto) {


        return transactionService.filterTransactions(authentication, transactionFilterDto);

    }




    // mismo q se retorna en el método de filtro

    @PostMapping("/api/transactions/pdf")
    public void exportTransactionsToPDF(@RequestBody Set<TransactionDTO> transactions, HttpServletResponse response) throws IOException {


        try{
            // Crear un nuevo documento PDF
            PDDocument document = new PDDocument();

            PDRectangle pageSize = PDRectangle.A4; // Tamaño de página A4
            float marginLeft = 50f; // Margen izquierdo en puntos
            float marginTop = 50f; // Margen superior en puntos
            float marginRight = 50f; // Margen derecho en puntos
            float marginBottom = 50f; // Margen inferior en puntos

            PDPage page = new PDPage(pageSize);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float startX = marginLeft;
            float startY = page.getMediaBox().getHeight() - marginTop;

            float columnWidth = (pageSize.getWidth() - marginLeft - marginRight) / 6;
            float rowHeight = 20f;
            int maxRowsPerPage = (int) ((pageSize.getHeight() - marginTop - marginBottom) / rowHeight);

            // Establecer la fuente y el tamaño de fuente para los nombres de las columnas
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float fontSize = 12f;
            float lineSpacing = 14.5f;

            int rowIndex = 0;
            int pageNumber = 1;

            for (TransactionDTO transaction : transactions) {
                System.out.println(transaction);
                // Verificar si se alcanza el límite de filas por página
                if (rowIndex >= maxRowsPerPage) {
                    contentStream.close();
                    page = new PDPage(pageSize);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
                    startX = marginLeft;
                    startY = page.getMediaBox().getHeight() - marginTop;
                    rowIndex = 0;
                    pageNumber++;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY - (rowIndex * lineSpacing));
                contentStream.showText(transaction.getId() != null ? transaction.getId().toString() : "");
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(transaction.getAmmount().toString());
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(transaction.getDescription());
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(transaction.getType().toString());
                contentStream.newLineAtOffset(columnWidth, 0);
                contentStream.showText(Double.toString(transaction.getBalanceLeft()));
                contentStream.endText();

                rowIndex++;
            }

            // Cerrar el contenido de la página
            contentStream.close();

            // Configurar la respuesta HTTP para descargar el archivo PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

            // Enviar el contenido del documento PDF al navegador
            document.save(response.getOutputStream());


            // Guardar el contenido del documento PDF en un archivo local
            File outputFile = new File(System.getProperty("user.home") + "\\Desktop\\transactions.pdf");
            document.save(new FileOutputStream(outputFile));


            // Cerrar el documento PDF
            document.close();

        } catch (IOException e) {
            // Manejar la excepción
            e.printStackTrace();
        }




        }









}




