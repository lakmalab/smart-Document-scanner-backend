package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.FieldDTO;

import java.util.List;

public interface FieldService {
     FieldDTO createField(FieldDTO dto) ;
     List<FieldDTO> getFieldsByTemplateId(Long templateId) ;
}
