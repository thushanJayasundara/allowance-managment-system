package com.erp.service;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.validations.CommonValidationMessages;
import com.erp.constant.validations.PrivilegeValidationMessage;
import com.erp.dto.PrivilegeDTO;
import com.erp.entity.AuditData;
import com.erp.entity.Privilege;
import com.erp.repository.PrivilegeRepository;
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
 * This class is responsible handle all logic's in Privilege.
 * ========================================================================
 * @author thushan vimukthi
 */
@Service
public class PrivilegeService {

    private final Logger LOGGER = LoggerFactory.getLogger(PrivilegeService.class);

    private final PrivilegeRepository privilegeRepository;
    private final UserService userService;


    @Autowired
    public PrivilegeService(PrivilegeRepository privilegeRepository, UserService userService) {
        this.privilegeRepository = privilegeRepository;
        this.userService = userService;
    }

    /**
     * ========================================================================
     * This method is responsible save and update {@link Privilege} details.
     * ========================================================================
     * @param privilegeDTO
     * @return
     */
    @Transactional
    public CommonResponse saveUpdatePrivilege(final String token,PrivilegeDTO privilegeDTO){
        CommonResponse commonResponse = new CommonResponse();
        Privilege privilege;
        try {
            //User user = userService.findUserByToken(token);
            List<String> validations = validatePrivileges(privilegeDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }

            if (CommonValidation.stringNullValidation(privilegeDTO.getId())){
                privilege = new Privilege();
                AuditData auditData = AuditData
                        .builder()
                        .createdBy(Long.valueOf(token))
                        .createdOn(DateTimeUtil.getSriLankaTime())
                        .build();
                privilege.setAuditData(auditData);
            }else {
                privilege = findPrivilegeById(privilegeDTO.getId());
                privilege.getAuditData().setUpdatedBy(Long.valueOf(token));
                privilege.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }

            privilege = castPrivilegeDTOIntoPrivilege(privilege,privilegeDTO);
            privilegeRepository.save(privilege);
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in PrivilegeService -> saveUpdatePrivilege()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible delete {@link Privilege} by privilegeId.
     * ========================================================================
     * @param token
     * @param privilegeId
     * @return
     */
    @Transactional
    public CommonResponse deletePrivilegeById(final String token,String privilegeId){
        CommonResponse commonResponse = new CommonResponse();
        Privilege privilege;
        try {
          //  User user = userService.findUserByToken(token);
            privilege = findPrivilegeById(privilegeId);
            privilege.setCommonStatus(CommonStatus.DELETE);
            privilege.getAuditData().setDeleteBy(Long.valueOf(token));
            privilegeRepository.save(privilege);
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in PrivilegeService -> deletePrivilegeById()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get {@link CommonResponse} by privilegeId.
     * ========================================================================
     * @param privilegeId
     * @return
     */
    public CommonResponse getPrivilegeById(final String privilegeId){
        CommonResponse commonResponse = new CommonResponse();
        Privilege privilege;
        try {
            privilege = findPrivilegeById(privilegeId);
            commonResponse.setPayload(Collections.singletonList(castPrivilegeIntoPrivilegeDTO(privilege)));
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in PrivilegeService -> getPrivilegeById()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get {@link Privilege} by privilegeName.
     * ========================================================================
     * @param privilegeName
     * @return
     */
    public CommonResponse getPrivilegeByName(final String privilegeName){
        CommonResponse commonResponse = new CommonResponse();
        Privilege privilege;
        try {
            privilege = findPrivilegeByName(privilegeName);
            commonResponse.setPayload(Collections.singletonList(castPrivilegeIntoPrivilegeDTO(privilege)));
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in PrivilegeService -> getPrivilegeById()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get all {@link CommonResponse}.
     * ========================================================================
     * @return
     */
    @Transactional
    public CommonResponse getAllPrivilege(){
        CommonResponse commonResponse = new CommonResponse();
        try {
            Predicate<Privilege> filterOnStatus = privilege -> privilege.getCommonStatus() != CommonStatus.DELETE;
            List<PrivilegeDTO> privilegeDTOS = privilegeRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castPrivilegeIntoPrivilegeDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(privilegeDTOS));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in PrivilegeService -> getAll()" + e);
        }
        return commonResponse;
    }

    /**
     * ============================================================================
     * This method is responsible get {@link Privilege} by privilegeId.
     * ============================================================================
     * @param privilegeId
     * @return
     */
    @Transactional
    public Privilege findPrivilegeById(final String privilegeId){
        return privilegeRepository.getById(Long.parseLong(privilegeId));
    }

    /**
     * ============================================================================
     * This method is responsible get {@link Privilege} by privilege.
     * ============================================================================
     * @param privilege
     * @return
     */
    @Transactional
    public Privilege findPrivilegeByName(final String privilege){
        return privilegeRepository.getPrivilegeByPrivilegeName(privilege);
    }

    /**
     * ============================================================================
     * This method is responsible get count all privilege.
     * ============================================================================
     * @return long
     */
    @Transactional
    public long getCount(){
        return privilegeRepository.count();
    }

    /**
     * ============================================================================
     * This method is responsible cast {@link PrivilegeDTO} into {@link Privilege}.
     * ============================================================================
     * @param privilegeDTO
     * @return
     */
    public Privilege castPrivilegeDTOIntoPrivilege(Privilege privilege,PrivilegeDTO privilegeDTO){
         privilege.setId(privilegeDTO.getId().equals("") ? null : Long.parseLong(privilegeDTO.getId()));
         privilege.setPrivilegeName(privilegeDTO.getPrivilege());
         privilege.setPrivilegePolices(privilegeDTO.getPrivilegePolices());
         privilege.setPrivilegeDescription(privilegeDTO.getPrivilegeDescription());
         privilege.setCommonStatus(privilegeDTO.getCommonStatus());
        return privilege;
    }

    /**
     * ============================================================================
     * This method is responsible cast {@link Privilege} into {@link PrivilegeDTO}.
     * ============================================================================
     * @param privilege
     * @return
     */
    public PrivilegeDTO castPrivilegeIntoPrivilegeDTO(Privilege privilege){
        return PrivilegeDTO
                .builder()
                .id(privilege.getId().toString())
                .privilege(privilege.getPrivilegeName())
                .privilegePolices(privilege.getPrivilegePolices())
                .privilegeDescription(privilege.getPrivilegeDescription())
                .commonStatus(privilege.getCommonStatus())
                .build();
    }

    /**
     * ============================================================================
     * This method is responsible validate {@link PrivilegeDTO}.
     * ============================================================================
     * @param privilegeDTO
     * @return
     */
    private List<String> validatePrivileges(PrivilegeDTO privilegeDTO){
        List<String> validations = new ArrayList<>();
        if (privilegeDTO.getId().equals("") && privilegeRepository.existsByPrivilegeName(privilegeDTO.getPrivilege()))
            validations.add(CommonValidationMessages.RECORDS_ALREADY_EXIT);
        if ( CommonValidation.stringNullValidation(privilegeDTO.getPrivilege()))
            validations.add(PrivilegeValidationMessage.EMPTY_PRIVILEGE);
        if (CommonValidation.stringNullValidation(privilegeDTO.getPrivilegeDescription()))
            validations.add(PrivilegeValidationMessage.EMPTY_PRIVILEGE_DESCRIPTION);
        if (CommonValidation.stringNullValidation(privilegeDTO.getPrivilegePolices()))
            validations.add(PrivilegeValidationMessage.EMPTY_PRIVILEGE_POLICES);


        return validations;
    }
}
