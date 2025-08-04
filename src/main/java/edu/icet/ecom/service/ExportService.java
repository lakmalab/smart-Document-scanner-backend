package edu.icet.ecom.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public interface ExportService {

     ByteArrayResource generateExcel(Long documentId) ;
     ByteArrayResource exportTemplateToExcel(Long templateId, String userEmail);
}
