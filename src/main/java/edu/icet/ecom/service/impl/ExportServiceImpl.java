package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.ExportLogDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.*;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.ExportLogRepository;
import edu.icet.ecom.repository.ExtractedFieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.service.ExportService;
import edu.icet.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final DocumentRepository documentRepo;
    private final ExtractedFieldRepository fieldRepo;
    private final TemplateRepository templateRepo;
    private final ExportLogRepository exportLogRepository;
    private final UserService userService;
    ModelMapper modelMapper = new ModelMapper();
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

    @Override
    public ByteArrayResource exportTemplate(Long templateId, String email, String type, String docStatus) {
        TemplateEntity template = templateRepo.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        List<FieldEntity> fields = template.getFields();

        // Get filtered documents
        List<DocumentEntity> documents = documentRepo
                .findByTemplate_TemplateIdAndUploadedBy_EmailAndStatusIgnoreCase(templateId, email, docStatus);
        generateExportLog( new  ExportLogDTO(
                null,
                "Excel,",
                LocalDateTime.now(),
                "",
                userService.getUserByEmail(email).getUserId(),
                templateId
        ));
        try {
            if ("xlsx".equalsIgnoreCase(type)) {
                return exportAsExcel(template, fields, documents);
            } else if ("accdb".equalsIgnoreCase(type)) {
                return exportAsAccess(template, fields, documents);
            } else {
                throw new RuntimeException("Unsupported export type: " + type);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate export", e);
        }
    }

    private ByteArrayResource exportAsExcel(TemplateEntity template, List<FieldEntity> fields,
                                            List<DocumentEntity> documents) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(template.getTemplateName());

            // Header
            Row header = sheet.createRow(0);
            int col = 0;
            header.createCell(col++).setCellValue("Document ID");
            header.createCell(col++).setCellValue("Uploaded By");
            header.createCell(col++).setCellValue("Status");
            header.createCell(col++).setCellValue("Upload Date");

            for (FieldEntity field : fields) {
                header.createCell(col++).setCellValue(field.getFieldName());
            }

            // Rows
            int rowNum = 1;
            for (DocumentEntity doc : documents) {
                Row row = sheet.createRow(rowNum++);
                int i = 0;
                row.createCell(i++).setCellValue(doc.getDocumentId());
                row.createCell(i++).setCellValue(doc.getUploadedBy().getEmail());
                row.createCell(i++).setCellValue(doc.getStatus());
                row.createCell(i++).setCellValue(doc.getUploadDate().toString());

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
        }
    }

    private ByteArrayResource exportAsAccess(TemplateEntity template, List<FieldEntity> fields,
                                             List<DocumentEntity> documents) throws IOException {
        // TODO: Implement .accdb export logic here
        // For now, return empty file or throw UnsupportedOperationException
        throw new UnsupportedOperationException("ACCDB export not yet implemented");
    }
    public void generateExportLog(ExportLogDTO dto) {
        ExportLogEntity log = modelMapper.map(dto, ExportLogEntity.class);
        modelMapper.map(exportLogRepository.save(log), ExportLogDTO.class);
        System.out.println(
                log
        );
    }


}
