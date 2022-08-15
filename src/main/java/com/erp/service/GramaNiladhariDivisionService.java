package com.erp.service;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.validations.CommonValidationMessages;
import com.erp.constant.validations.GramaNiladhariDivisionMessage;
import com.erp.dto.GramaNiladhariDivisionDTO;
import com.erp.dto.PrivilegeDTO;
import com.erp.entity.*;
import com.erp.repository.GramaNiladhariDivisionRepository;
import com.erp.utils.CommonResponse;
import com.erp.utils.CommonValidation;
import com.erp.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ========================================================================
 * This class is responsible handle all logic's in GramaNiladhariDivisionService.
 * ========================================================================
 *
 * @author thushan vimukthi
 */
@Service
public class GramaNiladhariDivisionService {

    private final Logger LOGGER = LoggerFactory.getLogger(GramaNiladhariDivisionService.class);

    private final GramaNiladhariDivisionRepository gramaNiladhariDivisionRepository;
    private final UserService userService;

    @Autowired
    public GramaNiladhariDivisionService(GramaNiladhariDivisionRepository gramaNiladhariDivisionRepository, UserService userService) {
        this.gramaNiladhariDivisionRepository = gramaNiladhariDivisionRepository;
        this.userService = userService;
    }

    /**
     * ====================================================================================
     * This method is responsible save and update {@link GramaNiladhariDivision} details.
     * ====================================================================================
     *
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateGramaNiladhariDivision(final String token, GramaNiladhariDivisionDTO gramaNiladhariDivisionDTO) {
        CommonResponse commonResponse = new CommonResponse();
        GramaNiladhariDivision gramaNiladhariDivision;
        try {
            //User user =userService.findUserByToken(token);
            List<String> validations = validateGramaNiladhariDivision(gramaNiladhariDivisionDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(gramaNiladhariDivisionDTO.getId())) {
                gramaNiladhariDivision = new GramaNiladhariDivision();
                AuditData auditData = AuditData
                        .builder()
                        .createdBy(Long.valueOf(token))
                        .createdOn(DateTimeUtil.getSriLankaTime())
                        .build();
                gramaNiladhariDivision.setAuditData(auditData);
            } else {
                gramaNiladhariDivision = findGramaNiladhariDivisionById(gramaNiladhariDivisionDTO.getId());
                gramaNiladhariDivision.getAuditData().setUpdatedBy(Long.valueOf(token));
                gramaNiladhariDivision.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            gramaNiladhariDivision = castGramaNiladhariDivisionDTOIntoGramaNiladhariDivision(gramaNiladhariDivision, gramaNiladhariDivisionDTO);
            gramaNiladhariDivisionRepository.save(gramaNiladhariDivision);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in GramaNiladhariDivisionService -> saveUpdateGramaNiladhariDivision()" + e);
        }
        return commonResponse;
    }

    /**
     * =============================================================================================
     * This method is responsible delete {@link GramaNiladhariDivision} by gramaNiladhariDivisionId.
     * =============================================================================================
     *
     * @param token
     * @param gramaNiladhariDivisionId
     * @return
     */
    @Transactional
    public CommonResponse deleteGramaNiladhariDivisionById(final String token, String gramaNiladhariDivisionId) {
        CommonResponse commonResponse = new CommonResponse();
        GramaNiladhariDivision gramaNiladhariDivision;
        try {
            //User user = userService.findUserByToken(token);
            gramaNiladhariDivision = findGramaNiladhariDivisionById(gramaNiladhariDivisionId);
            gramaNiladhariDivision.setCommonStatus(CommonStatus.DELETE);
            gramaNiladhariDivision.getAuditData().setDeleteBy(Long.valueOf(token));
            gramaNiladhariDivisionRepository.save(gramaNiladhariDivision);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in Service -> ()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================================================
     * This method is responsible get {@link CommonResponse} by gramaNiladhariDivisionId.
     * ==================================================================================
     *
     * @param gramaNiladhariDivisionId
     * @return
     */
    @Transactional
    public CommonResponse getGramaNiladhariDivisionById(final String gramaNiladhariDivisionId) {
        CommonResponse commonResponse = new CommonResponse();
        GramaNiladhariDivision gramaNiladhariDivision;
        try {
            gramaNiladhariDivision = findGramaNiladhariDivisionById(gramaNiladhariDivisionId);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castGramaNiladhariDivisionIntoGramaNiladhariDivisionDTO(gramaNiladhariDivision)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in Service -> ()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================================================
     * This method is responsible get all {@link CommonResponse} .
     * ==================================================================================
     * @return
     */
    @Transactional
    public CommonResponse getAllGramaNiladhariDivision() {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Predicate<GramaNiladhariDivision> filterOnStatus = gramaNiladhariDivision -> gramaNiladhariDivision.getCommonStatus() != CommonStatus.DELETE;
            List<GramaNiladhariDivisionDTO> gramaNiladhariDivisionDTOS = gramaNiladhariDivisionRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castGramaNiladhariDivisionIntoGramaNiladhariDivisionDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(gramaNiladhariDivisionDTOS));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in Service -> ()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================================================
     * This method is responsible save and update {@link GramaNiladhariDivision} details.
     * ==================================================================================
     *
     * @param divisionId
     * @return
     */
    @Transactional
    public GramaNiladhariDivision findGramaNiladhariDivisionById(String divisionId) {
        return gramaNiladhariDivisionRepository.getById(Long.parseLong(divisionId));
    }

    /**
     * ======================================================================================================
     * This method is responsible cast {@link GramaNiladhariDivision} into {@link GramaNiladhariDivisionDTO}.
     * ======================================================================================================
     *
     * @param gramaNiladhariDivision
     * @return
     */
    public GramaNiladhariDivisionDTO castGramaNiladhariDivisionIntoGramaNiladhariDivisionDTO(GramaNiladhariDivision gramaNiladhariDivision) {
        return GramaNiladhariDivisionDTO
                .builder()
                .id(gramaNiladhariDivision.getId().toString())
                .gramaNiladhariDivision(gramaNiladhariDivision.getGramaNiladhariDivision())
                .divisionCode(gramaNiladhariDivision.getDivisionCode())
                .commonStatus(gramaNiladhariDivision.getCommonStatus())
                .build();
    }

    /**
     * ======================================================================================================
     * This method is responsible cast {@link GramaNiladhariDivisionDTO} into {@link GramaNiladhariDivision}.
     * ======================================================================================================
     *
     * @param gramaNiladhariDivision
     * @param gramaNiladhariDivisionDTO
     * @return
     */
    public GramaNiladhariDivision castGramaNiladhariDivisionDTOIntoGramaNiladhariDivision(GramaNiladhariDivision gramaNiladhariDivision, GramaNiladhariDivisionDTO gramaNiladhariDivisionDTO) {
        gramaNiladhariDivision.setId(gramaNiladhariDivisionDTO.getId().equals("") ? null : Long.parseLong(gramaNiladhariDivisionDTO.getId()));
        gramaNiladhariDivision.setDivisionCode(gramaNiladhariDivisionDTO.getDivisionCode());
        gramaNiladhariDivision.setGramaNiladhariDivision(gramaNiladhariDivisionDTO.getGramaNiladhariDivision());
        gramaNiladhariDivision.setCommonStatus(gramaNiladhariDivisionDTO.getCommonStatus());
        return gramaNiladhariDivision;
    }

    /**
     * ============================================================================
     * This method is responsible validate {@link GramaNiladhariDivisionDTO}.
     * ============================================================================
     *
     * @param gramaNiladhariDivisionDTO
     * @return
     */
    public List<String> validateGramaNiladhariDivision(GramaNiladhariDivisionDTO gramaNiladhariDivisionDTO) {
        List<String> validations = new ArrayList<>();
        if (gramaNiladhariDivisionDTO.getId().equals("") && gramaNiladhariDivisionRepository.existsByDivisionCode(gramaNiladhariDivisionDTO.getDivisionCode()))
            validations.add(GramaNiladhariDivisionMessage.RECORDS_ALREADY_EXIT);
        if (CommonValidation.stringNullValidation(gramaNiladhariDivisionDTO.getGramaNiladhariDivision()))
            validations.add(GramaNiladhariDivisionMessage.EMPTY_DIVISION);
        if (CommonValidation.stringNullValidation(gramaNiladhariDivisionDTO.getDivisionCode()))
            validations.add(GramaNiladhariDivisionMessage.EMPTY_DIVISION_CODE);
        return validations;
    }
}
