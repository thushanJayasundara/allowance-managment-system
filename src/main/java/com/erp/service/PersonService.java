package com.erp.service;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.validations.CommonValidationMessages;
import com.erp.dto.PersonDTO;
import com.erp.dto.PrivilegeDTO;
import com.erp.entity.AuditData;
import com.erp.entity.Person;
import com.erp.entity.Privilege;
import com.erp.repository.PersonRepository;
import com.erp.utils.CommonResponse;
import com.erp.utils.CommonValidation;
import com.erp.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
                                                
/**
 * ========================================================================
 * This class is responsible handle all logic's in PersonService.
 * ========================================================================
 * @author thushan vimukthi
 */
@Service
public class PersonService {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final UserService userService;
    private final GramaNiladhariDivisionService  gramaNiladhariDivisionService;


    @Autowired
    public PersonService(PersonRepository personRepository,
                         UserService userService, GramaNiladhariDivisionService gramaNiladhariDivisionService) {
        this.personRepository = personRepository;
        this.userService = userService;
        this.gramaNiladhariDivisionService = gramaNiladhariDivisionService;
    }

    /**
     * ========================================================================
     * This method is responsible save and update {@link Person} details.
     * ========================================================================
     * @param token
     * @param personDTO
     * @return
     */
    @Transactional
    public CommonResponse saveUpdatePerson(final String token, PersonDTO personDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Person person;
        try {
          //  User user = userService.findUserByToken(token);;
            List<String> validations = validatePerson(personDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }

            if (CommonValidation.stringNullValidation(personDTO.getId())){
                person = new Person();
                AuditData auditData = AuditData
                        .builder()
                        .createdBy(Long.valueOf(token))
                        .createdOn(DateTimeUtil.getSriLankaTime())
                        .build();
                person.setAuditData(auditData);
            }else{
                person = findPersonById(personDTO.getId());
                person.getAuditData().setUpdatedBy(Long.valueOf(token));
                person.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }

            person = castPersonDtoIntoPerson(person,personDTO);
            personRepository.save(person);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonService -> saveUpdatePerson()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible delete {@link Person} by personId.
     * ========================================================================
     * @param token
     * @param personId
     * @return
     */
    @Transactional
    public CommonResponse deletePerson(final String token,String personId) {
        CommonResponse commonResponse = new CommonResponse();
        Person person;
        try {
          //  User user = userService.findUserByToken(token);;
            person = findPersonById(personId);
            person.setCommonStatus(CommonStatus.DELETE);
            person.getAuditData().setDeleteBy(Long.valueOf(token));
            personRepository.save(person);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in PersonService -> deletePerson()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get {@link Person} by personId.
     * ========================================================================
     * @param personId
     * @return
     */
    public CommonResponse getPersonById(final String personId) {
        CommonResponse commonResponse = new CommonResponse();
        Person person;
        try {
            person = findPersonById(personId);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castPersonIntoPersonDTO(person)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in Service -> ()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get {@link Person} by personNIC.
     * ========================================================================
     * @param personNic
     * @return
     */
    public CommonResponse getPersonByNic(final String personNic) {
        CommonResponse commonResponse = new CommonResponse();
        Person person;
        try {
            person = findPersonByNic(personNic);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castPersonIntoPersonDTO(person)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in Service -> ()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get all {@link List<PersonDTO>}.
     * ========================================================================
     * @return
     */
    @Transactional
    public CommonResponse getAllPerson() {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Predicate<Person> filterOnStatus = person -> person.getCommonStatus() != CommonStatus.DELETE;
            List<PersonDTO> personDTOS = personRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castPersonIntoPersonDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(personDTOS));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in Service -> ()" + e);
        }
        return commonResponse;
    }

    /**
     * ============================================================================
     * This method is responsible get {@link Person} by personId.
     * ============================================================================
     * @param personId
     * @return
     */
    @Transactional
    public Person findPersonById(String personId){
        return personRepository.getById(Long.parseLong(personId));
    }

    /**
     * ============================================================================
     * This method is responsible get {@link Person} by personId.
     * ============================================================================
     * @param personNic
     * @return
     */
    @Transactional
    public Person findPersonByNic(String personNic){
        return personRepository.getPersonByNic(personNic);
    }

    /**
     * ============================================================================
     * This method is responsible get count all persons.
     * ============================================================================
     * @return long
     */
    @Transactional
    public long getCount(){
        return personRepository.count();
    }

    /**
     * ============================================================================
     * This method is responsible cast {@link PrivilegeDTO} into {@link Privilege}.
     * ============================================================================
     * @param person
     * @param personDTO
     * @return
     */
    public Person castPersonDtoIntoPerson(Person person,PersonDTO personDTO){
        person.setId(personDTO.getId().equals("") ? null : Long.parseLong(personDTO.getId()));
        person.setFullName(personDTO.getFullName());
        person.setInitials(personDTO.getInitials());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setDob(DateTimeUtil.getFormattedDateTime(personDTO.getDob()));
        person.setRegistrationDate(DateTimeUtil.getSriLankaTime());
        person.setNic(personDTO.getNic());
        person.setAddress(personDTO.getAddress());
        person.setGender(personDTO.getGender());
        person.setCommonStatus(personDTO.getCommonStatus());
        person.setGramaNiladhariDivision(gramaNiladhariDivisionService.findGramaNiladhariDivisionById(personDTO.getGramaNiladhariDivisionDTO().getId()));
        person.setContactNumber(personDTO.getContactNumber());
        return person;
    }

    /**
     * ============================================================================
     * This method is responsible cast {@link Person} into {@link PersonDTO}.
     * ============================================================================
     * @param person
     * @return
     */
    public PersonDTO castPersonIntoPersonDTO(Person person){
        return PersonDTO
                .builder()
                .id(person.getId().toString())
                .fullName(person.getFullName())
                .initials(person.getInitials())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .dob(DateTimeUtil.getFormattedDate(person.getDob()))
                .registrationDate(person.getRegistrationDate().toString())
                .registrationDate(person.getRegistrationDate().toString())
                .nic(person.getNic())
                .address(person.getAddress())
                .gender(person.getGender())
                .commonStatus(person.getCommonStatus())
                .gramaNiladhariDivisionDTO(gramaNiladhariDivisionService.castGramaNiladhariDivisionIntoGramaNiladhariDivisionDTO(person.getGramaNiladhariDivision()))
                .contactNumber(person.getContactNumber())
                .build();
    }
    /**
     * ============================================================================
     * This method is responsible validate {@link PersonDTO}.
     * ============================================================================
     * @param personDTO
     * @return
     */
    private List<String> validatePerson(PersonDTO personDTO){
        List<String> validations = new ArrayList<>();
        if (personDTO.getId().equals("") && personRepository.existsByNic(personDTO.getNic()))
            validations.add(CommonValidationMessages.RECORDS_ALREADY_EXIT);
        if (CommonValidation.stringNullValidation(personDTO.getFullName()))
            validations.add(CommonValidationMessages.EMPTY_FULL_NAME);
        if (CommonValidation.stringNullValidation(personDTO.getInitials()))
            validations.add(CommonValidationMessages.EMPTY_INITIAL_IN_NAME);
        if (CommonValidation.stringNullValidation(personDTO.getLastName()))
            validations.add(CommonValidationMessages.EMPTY_LAST_NAME);
        if (CommonValidation.stringNullValidation(personDTO.getFirstName()))
            validations.add(CommonValidationMessages.EMPTY_FIRST_NAME);
        if (CommonValidation.isCharOnly(personDTO.getLastName()))
            validations.add(String.format(CommonValidationMessages.INVALID_NAME,"Last Name"));
        if (CommonValidation.isCharOnly(personDTO.getFullName()))
            validations.add(String.format(CommonValidationMessages.INVALID_NAME,"Full Name"));
        if (CommonValidation.isCharOnly(personDTO.getInitials()))
            validations.add(String.format(CommonValidationMessages.INVALID_NAME,"Initials"));
        if (CommonValidation.isCharOnly(personDTO.getFirstName()))
            validations.add(String.format(CommonValidationMessages.INVALID_NAME,"First Name"));
        if (CommonValidation.stringNullValidation(personDTO.getDob()))
            validations.add(CommonValidationMessages.EMPTY_DOB);
        if (CommonValidation.stringNullValidation(personDTO.getNic()))
            validations.add(CommonValidationMessages.EMPTY_NIC);
        if (CommonValidation.validNic(personDTO.getNic()))
            validations.add(CommonValidationMessages.INVALID_NIC);
        if (CommonValidation.stringNullValidation(personDTO.getAddress()))
            validations.add(CommonValidationMessages.EMPTY_ADDRESS);
        if (CommonValidation.stringNullValidation(personDTO.getGramaNiladhariDivisionDTO().getId()))
            validations.add(CommonValidationMessages.EMPTY_GRAMA_NILADHARI_DIVISION);
        if (CommonValidation.stringNullValidation(personDTO.getGender().toString()))
            validations.add(CommonValidationMessages.EMPTY_GENDER);
        if (CommonValidation.isValidMobile(personDTO.getContactNumber()))
            validations.add(CommonValidationMessages.INVALID_MOBILE_NUMBER);
        return validations;
        }

}
