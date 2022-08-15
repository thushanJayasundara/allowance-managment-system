package com.erp.controller;

import com.erp.constant.CommonConstants;
import com.erp.dto.UserDTO;
import com.erp.service.UserService;
import com.erp.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp/user-management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public CommonResponse saveUpdateUser(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                         @RequestBody UserDTO userDTO){
        return userService.saveUpdateUser("1234", userDTO);
    }

    @DeleteMapping("/{userId}")
    public CommonResponse deleteUserById(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                              @PathVariable final String userId){
        return userService.deleteUserById("1234", userId);
    }

    @GetMapping("/{userId}")
    public CommonResponse getUserById(@PathVariable final String userId){
        return userService.getUserById(userId);
    }

    @GetMapping("/")
    public CommonResponse getAllUsers(){
        return userService.getAllUsers();
    }

}

