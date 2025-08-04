package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.model.entity.ExtractedFieldEntity;
import edu.icet.ecom.model.entity.FieldEntity;
import edu.icet.ecom.model.entity.TemplateEntity;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.ExtractedFieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final DocumentRepository documentRepo;
    private final ExtractedFieldRepository fieldRepo;
    private final TemplateRepository templateRepo;

    public ByteArrayResource generateExcel(Long documentId) {
        DocumentEntity doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        List<ExtractedFieldEntity> fields = fieldRepo.findByDocument_DocumentId(documentId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Extracted Data");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Field");
            header.createCell(1).setCellValue("Value");
            header.createCell(2).setCellValue("Confidence");

            int rowNum = 1;
            for (ExtractedFieldEntity ef : fields) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ef.getField().getFieldName());
                row.createCell(1).setCellValue(ef.getValue());
                row.createCell(2).setCellValue(ef.getConfidenceScore());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }

    @Override
    public ByteArrayResource exportTemplateToExcel(Long templateId, String userEmail) {
        TemplateEntity template = templateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        List<FieldEntity> fields = template.getFields();

        // Filter documents by both template and user
        List<DocumentEntity> documents = documentRepo.findByTemplate_TemplateIdAndUploadedBy_Email(templateId, userEmail);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(template.getTemplateName());

            Row header = sheet.createRow(0);
            int col = 0;
            header.createCell(col++).setCellValue("Document ID");
            header.createCell(col++).setCellValue("Uploaded By");

            for (FieldEntity field : fields) {
                header.createCell(col++).setCellValue(field.getFieldName());
            }

            int rowNum = 1;
            for (DocumentEntity doc : documents) {
                Row row = sheet.createRow(rowNum++);
                int i = 0;
                row.createCell(i++).setCellValue(doc.getDocumentId());
                row.createCell(i++).setCellValue(doc.getUploadedBy().getEmail());

                for (FieldEntity field : fields) {
                    String value = doc.getExtractedFields().stream()
                            .filter(f -> f.getField().getFieldId().equals(field.getFieldId()))
                            .map(ExtractedFieldEntity::getValue)
                            .findFirst()
                            .orElse("");
                    row.createCell(i++).setCellValue(value);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }


}
