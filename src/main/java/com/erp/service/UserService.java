package com.erp.service;

import com.erp.constant.enums.CommonStatus;
import com.erp.constant.validations.CommonValidationMessages;
import com.erp.dto.UserDTO;
import com.erp.entity.AuditData;
import com.erp.entity.Privilege;
import com.erp.entity.User;
import com.erp.repository.UserRepository;
import com.erp.utils.CommonResponse;
import com.erp.utils.CommonValidation;
import com.erp.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ========================================================================
 * This class is responsible handle all logic's in User.
 * ========================================================================
 * @author thushan vimukthi
 */
@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * ========================================================================
     * This method is responsible save and update {@link User} details.
     * ========================================================================
     * @param token
     * @param userDTO
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateUser(final String token, UserDTO userDTO){
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
          //  User logonUser = findUserByToken(token);

            if (!userDTO.getId().equals("") && userDTO.getPassword().equals(""))
                userDTO.setPassword(getUserDTOByUserName(userDTO.getUserName()).getPassword());

            List<String> validations = validationUser(userDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }

            if (CommonValidation.stringNullValidation(userDTO.getId())){
                user = new User();
                AuditData auditData = AuditData
                        .builder()
                        .createdBy(Long.valueOf(1234))
                        .createdOn(DateTimeUtil.getSriLankaTime())
                        .build();
                user.setAuditData(auditData);
            }else {
                user = findUserById(userDTO.getId());
                user.getAuditData().setUpdatedBy(Long.valueOf(1234));
                user.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }

            user = castUserDTOIntoUser(user,userDTO);
            userRepository.save(user);
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in UserService -> saveUpdateUser()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible delete {@link Privilege} by privilegeId.
     * ========================================================================
     * @param token
     * @param userId
     * @return
     */
    @Transactional
    public CommonResponse deleteUserById( String token,String userId) {
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
          //  User logonUser = findUserByToken(token);
            user = findUserById(userId);
            user.setCommonStatus(CommonStatus.DELETE);
            user.getAuditData().setDeleteBy(Long.parseLong(token));
            userRepository.save(user);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> deleteUserById()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get {@link UserDTO} by userId.
     * ========================================================================
     * @param userId
     * @return
     */
    public CommonResponse getUserById(final String userId) {
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
            user = findUserById(userId);
            commonResponse.setPayload(Collections.singletonList(castUserIntoUserDTO(user)));
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> getUserById()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible get all {@link List<UserDTO>}.
     * ========================================================================
     * @return
     */
    @Transactional
    public CommonResponse getAllUsers() {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Predicate<User> filterOnStatus = user -> user.getCommonStatus() != CommonStatus.DELETE;
            List<UserDTO> userDTOS = userRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castUserIntoUserDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(userDTOS));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> getAllUsers()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible update token by {@link UserDTO}
     * ========================================================================
     * @param userDTO
     * @return
     */
    @Transactional
    public CommonResponse updateToken(UserDTO userDTO){
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {

            List<String> validations = validateToken(userDTO);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }

            user = findUserByUserName(userDTO.getUserName());
            if (user == null) {
                LOGGER.info("/******** User Account is not available");
                commonResponse.setErrorMessages(Arrays.asList(CommonValidationMessages.USER_ACCOUNT_IS_NOT_AVAILABLE));
                return commonResponse;
            }

            user.setToken(userDTO.getToken());
            userRepository.save(user);
            commonResponse.setStatus(true);
            LOGGER.info("/******** saved successfully");
        }catch (Exception e){
            LOGGER.error("/**************** Exception in UserService -> updateToken()" + e);
        }
        return commonResponse;
    }

    /**
     * ============================================================================
     * This method is responsible get {@link UserDTO} by userName.
     * ============================================================================
     * @param userName
     * @return
     */
    @Transactional
    public UserDTO getUserDTOByUserName(final String userName){
        return castUserIntoUserDTO(findUserByUserName(userName));
    }

    /**
     * ============================================================================
     * This method is responsible get {@link UserDTO} by userName.
     * ============================================================================
     * @param userName
     * @return
     */
    @Transactional
    public User findUserByUserName(final String userName){
        return userRepository.getUserByUserName(userName);
    }


    /**
     * ============================================================================
     * This method is responsible get {@link User} by userId.
     * ============================================================================
     * @param userId
     * @return
     */
    @Transactional
    public User findUserById(final String userId){
        return userRepository.getById(Long.parseLong(userId));
    }

    /**
     * ============================================================================
     * This method is responsible get {@link User} by token.
     * ============================================================================
     * @param token
     * @return
     */
    @Transactional
    public User findUserByToken(final String token) {
        return userRepository.getUserByToken(token);
    }

    /**
     * ============================================================================
     * This method is responsible get count all users.
     * ============================================================================
     * @return long
     */
    @Transactional
    public  long getCount(){
        return userRepository.count();
    }

    /**
     * ============================================================================
     * This method is responsible cast {@link UserDTO} into {@link User}.
     * ============================================================================
     * @param user
     * @param userDTO
     * @return
     */
    public User castUserDTOIntoUser(User user,UserDTO userDTO){
        user.setId(userDTO.getId().equals("") ? null : Long.parseLong(userDTO.getId()));
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setToken(null);
        user.setRole(userDTO.getRole());
        user.setCommonStatus(userDTO.getCommonStatus());
        return user;
    }

    /**
     * ============================================================================
     * This method is responsible cast {@link User} into {@link UserDTO}.
     * ============================================================================
     * @param user
     * @return
     */
    public UserDTO castUserIntoUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId().toString())
                .userName(user.getUserName())
                .password(user.getPassword())
                .token(user.getToken())
                .role(user.getRole())
                .commonStatus(user.getCommonStatus())
                .build();
    }

    /**
     * ========================================================================
     * This method is responsible validate {@link UserDTO} for token update.
     * ========================================================================
     * @return
     */
    private List<String> validateToken(UserDTO userDTO){
        List<String> validations = new ArrayList<>();
        if (userDTO == null)
            validations.add(CommonValidationMessages.CHECK_INPUT_DATA);
        if (CommonValidation.stringNullValidation(userDTO.getUserName()))
            validations.add(CommonValidationMessages.EMPTY_USERNAME);
        if (CommonValidation.stringNullValidation(userDTO.getToken()))
            validations.add(CommonValidationMessages.TOKEN_IS_NULL);
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible validate {@link UserDTO} details.
     * ========================================================================
     * @param userDTO
     * @return
     */
    private List<String> validationUser(UserDTO userDTO){
        List<String> validations = new ArrayList<>();

        if (userDTO.getId().equals("") && userRepository.existsByUserName(userDTO.getUserName()))
                validations.add(CommonValidationMessages.RECORDS_ALREADY_EXIT_BY_USERNAME);
        if (CommonValidation.stringNullValidation(userDTO.getPassword()))
                validations.add(CommonValidationMessages.EMPTY_PASSWORD);
        if (CommonValidation.stringNullValidation(userDTO.getPassword()))
                validations.add(CommonValidationMessages.ENTER_VALID_PASSWORD);
        if (CommonValidation.stringNullValidation(userDTO.getRole().toString()))
            validations.add(CommonValidationMessages.EMPTY_USER_ROLE);

        return validations;
    }
}
