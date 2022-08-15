package com.erp.service;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.validations.PrivilegeValidationMessage;
import com.erp.dto.PersonPrivilegeDTO;
import com.erp.entity.*;
import com.erp.repository.PersonPrivilegeRepository;
import com.erp.utils.CommonResponse;
import com.erp.utils.CommonValidation;
import com.erp.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ========================================================================
 * This class is responsible handle all logic's in PersonPrivilegeService.
 * ========================================================================
 * @author thushan vimukthi
 */
@Service
public class PersonPrivilegeService {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonPrivilegeService.class);

    private final PersonPrivilegeRepository personPrivilegeRepository;
    private final PersonService personService;
    private final PrivilegeService privilegeService;
    private final UserService userService;

    @Autowired
    public PersonPrivilegeService(PersonPrivilegeRepository personPrivilegeRepository,
                                  PersonService personService,
                                  PrivilegeService privilegeService,
                                  UserService userService) {
        this.personPrivilegeRepository = personPrivilegeRepository;
        this.personService = personService;
        this.privilegeService = privilegeService;
        this.userService = userService;
    }

    /**
     * ====================================================================================
     * This method is responsible save and update {@link PersonPrivilege} details.
     * ====================================================================================
     * @param token
     * @param personPrivilegeDTO
     * @return
     */
    @Transactional
    public CommonResponse saveUpdatePersonPrivilege(final String token, PersonPrivilegeDTO personPrivilegeDTO) {
        CommonResponse commonResponse = new CommonResponse();
        PersonPrivilege personPrivilege;
        try {
           // User user = userService.findUserByToken(token);

            List<String> validations = validatePersonPrivilege(personPrivilegeDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }

            if (CommonValidation.stringNullValidation(personPrivilegeDTO.getId())){
                personPrivilege = new PersonPrivilege();
                AuditData auditData = AuditData
                        .builder()
                        .createdBy(Long.valueOf(1234))
                        .createdOn(DateTimeUtil.getSriLankaTime())
                        .build();
                personPrivilege.setAuditData(auditData);
            }
            else {
                personPrivilege = findPersonPrivilegeByPersonId(personPrivilegeDTO.getPerson().getId());
                personPrivilege.getAuditData().setUpdatedBy(Long.valueOf(1234));
                personPrivilege.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            personPrivilege = castPersonPrivilegeInsertDtoToPersonPrivilege(personPrivilege, personPrivilegeDTO);
            personPrivilegeRepository.save(personPrivilege);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonPrivilegeService -> saveUpdatePersonPrivilege()" + e);
        }
        return commonResponse;
    }

    /**
     * ===============================================================================
     * This method is responsible delete {@link CommonResponse} by personPrivilegeId.
     * ===============================================================================
     * @param token
     * @param personPrivilegeId
     * @return
     */
    @Transactional
    public CommonResponse deletePersonPrivilegeById(final String token, final String personPrivilegeId) {
        CommonResponse commonResponse = new CommonResponse();
        PersonPrivilege personPrivilege;
        try {
           // User user = userService.findUserByToken(token);
            personPrivilege = findPersonPrivilege(personPrivilegeId);
            personPrivilege.setCommonStatus(CommonStatus.DELETE);
            personPrivilege.getAuditData().setDeleteBy(Long.valueOf(token));
            personPrivilegeRepository.save(personPrivilege);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonPrivilegeService -> deletePersonPrivilegeById()" + e);
        }
        return commonResponse;
    }

    /**
     * ============================================================================
     * This method is responsible get {@link CommonResponse} by personPrivilegeId.
     * ============================================================================
     * @param personPrivilegeId
     * @return
     */
    public CommonResponse getPersonPrivilegeById(final String personPrivilegeId) {
        CommonResponse commonResponse = new CommonResponse();
        PersonPrivilege personPrivilege;
        try {
            personPrivilege = findPersonPrivilege(personPrivilegeId);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castPersonPrivilegeIntoPersonPrivilegeResponseDTO(personPrivilege)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonPrivilegeService -> getPersonPrivilegeById()" + e);
        }
        return commonResponse;
    }

    /**
     * ============================================================================
     * This method is responsible get {@link CommonResponse} by personId.
     * ============================================================================
     * @param personId
     * @return
     */
    public CommonResponse getPersonPrivilegeByPersonId(final String personId) {
        CommonResponse commonResponse = new CommonResponse();
        PersonPrivilege personPrivilege;
        try {
            personPrivilege = findPersonPrivilegeByPersonId(personId);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castPersonPrivilegeIntoPersonPrivilegeResponseDTO(personPrivilege)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonPrivilegeService -> getPersonPrivilegeById()" + e);
        }
        return commonResponse;
    }

    /**
     * ===============================================================================
     * This method is responsible get all {@link List< PersonPrivilegeDTO >}.
     * ===============================================================================
     * @return
     */
    @Transactional
    public CommonResponse getAllPersonPrivilege() {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Predicate<PersonPrivilege> filterOnStatus = personPrivilege -> personPrivilege.getCommonStatus() != CommonStatus.DELETE;
            List<PersonPrivilegeDTO> personPrivilegeResponseDTOS = personPrivilegeRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castPersonPrivilegeIntoPersonPrivilegeResponseDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(personPrivilegeResponseDTOS));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonPrivilegeService -> getAllPersonPrivilege()" + e);
        }
        return commonResponse;
    }

    /**
     * ============================================================================
     * This method is responsible get {@link PersonPrivilege} by personPrivilegeId.
     * ============================================================================
     * @param personPrivilegeId
     * @return
     */
    @Transactional
    public PersonPrivilege findPersonPrivilege(final String personPrivilegeId){
        return personPrivilegeRepository.getById(Long.parseLong(personPrivilegeId));
    }

    /**
     * ============================================================================
     * This method is responsible get {@link PersonPrivilege} by personId.
     * ============================================================================
     * @param personId
     * @return
     */
    @Transactional
    public PersonPrivilege findPersonPrivilegeByPersonId(final String personId){
        return personPrivilegeRepository.findByPersonId(Long.parseLong(personId));
    }

    /**
     * ==============================================================================================
     * This method is responsible cast {@link PersonPrivilege} into {@link PersonPrivilegeDTO}.
     * ==============================================================================================
     * @param personPrivilege
     * @return
     */
    @Transactional
    public PersonPrivilegeDTO castPersonPrivilegeIntoPersonPrivilegeResponseDTO(PersonPrivilege personPrivilege){
        return PersonPrivilegeDTO.builder()
                .id(personPrivilege.getId().toString())
                .person(personService.castPersonIntoPersonDTO(personPrivilege.getPerson()))
                .privilege(personPrivilege.getPrivileges().stream()
                        .map(privilegeService::castPrivilegeIntoPrivilegeDTO)
                        .collect(Collectors.toSet()))
                .status(personPrivilege.getCommonStatus())
                .build();
    }

    /**
     * ==============================================================================================
     * This method is responsible cast {@link PersonPrivilegeDTO} into {@link PersonPrivilege}.
     * ==============================================================================================
     * @param personPrivilege
     * @param personPrivilegeDTO
     * @return
     */
    public PersonPrivilege castPersonPrivilegeInsertDtoToPersonPrivilege(PersonPrivilege personPrivilege,
                                                                         PersonPrivilegeDTO personPrivilegeDTO){
        personPrivilege.setId(personPrivilegeDTO.getId().equals("")  ? null : Long.parseLong(personPrivilegeDTO.getId()));
        personPrivilege.setPerson(personService.findPersonById(personPrivilegeDTO.getPerson().getId()));
        personPrivilege.setPrivileges(personPrivilegeDTO.getPrivilege().stream()
                .map(privilegeDTO -> privilegeService.findPrivilegeById(privilegeDTO.getId()))
                .collect(Collectors.toSet()));
        personPrivilege.setCommonStatus(personPrivilegeDTO.getStatus());
        return personPrivilege;
    }
    /**
     * ============================================================================
     * This method is responsible validate {@link PersonPrivilegeDTO}.
     * ============================================================================
     * @param personPrivilegeDTO
     * @return
     */
    private List<String> validatePersonPrivilege(PersonPrivilegeDTO personPrivilegeDTO){
        List<String> validations = new ArrayList<>();
        if (personPrivilegeDTO.getPerson().getId().equals("-1"))
            validations.add(PrivilegeValidationMessage.SELECT_PERSON);
        if (personPrivilegeDTO.getPrivilege().isEmpty())
            validations.add(PrivilegeValidationMessage.SELECT_PRIVILEGE);
        return validations;
    }
}
