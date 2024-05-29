package com.fpt.recruitmentsystem.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fpt.recruitmentsystem.dto.CvDTO;

public interface ICvService {
	List<CvDTO> getCvByCandidateId(String authorization);
	CvDTO uploadCv(MultipartFile multipartFile, String authorization, String data) throws IOException;
	void deleteCv(Integer cvId, String authorization) throws IOException;
	CvDTO getOneCv(Integer cvId, String authorization) throws IOException;
	List<CvDTO> search(Integer page, Integer limit, Integer candidateId);
	CvDTO getCv(Integer cvId, String authorization);

    CvDTO getCvForVacancy(Integer candidateId, Integer vacancyId, String authorizationHeader);

    CvDTO getCvByIdCv(Integer cvId);
	CvDTO updateCv(Integer cvId,MultipartFile multipartFile,String authorization, String data ) throws IOException;
}
