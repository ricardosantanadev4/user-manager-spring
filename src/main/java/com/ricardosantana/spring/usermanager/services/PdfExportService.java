package com.ricardosantana.spring.usermanager.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ricardosantana.spring.usermanager.models.Usuario;

@Service
public class PdfExportService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final TemplateEngine templateEngine;

    public PdfExportService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] gerarPdf(List<Usuario> usuarios) throws IOException {
        Context context = new Context();
        context.setVariable("logoBase64", this.carregarImagemComoBase64());
        context.setVariable("data", LocalDateTime.now().format(formatter));
        context.setVariable("clientes", usuarios);
        

        String html = templateEngine.process("relatorio", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    public String carregarImagemComoBase64() throws IOException {
        var resource = new ClassPathResource("static/images/logo.png");
        byte[] bytes = resource.getInputStream().readAllBytes();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }
}
