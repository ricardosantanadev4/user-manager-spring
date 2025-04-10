package com.ricardosantana.spring.usermanager.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.ricardosantana.spring.usermanager.models.Usuario;

@Service
public class ExcelExportService {
    DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public ByteArrayInputStream exportClientsToExcel(List<Usuario> usuarios) throws IOException {
        String[] columns = { "Código", "Data/Hora Cadastro", "Usuário Cadastrado", "Nome", "Email", "Telefone" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Clients");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Dados
            int rowIdx = 1;
            for (Usuario usuario : usuarios) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(usuario.getId());
                row.createCell(1).setCellValue(usuario.getDataHoraCadastro().format(fomatter));
                row.createCell(2).setCellValue(usuario.getUsuarioCadastrado());
                row.createCell(3).setCellValue(usuario.getNome());
                row.createCell(4).setCellValue(usuario.getEmail());
                row.createCell(5).setCellValue(usuario.getTelefone());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
