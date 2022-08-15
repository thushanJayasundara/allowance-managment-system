package com.erp.controller;

import com.erp.dto.PrivilegeDTO;
import com.erp.service.PrivilegeService;
import com.erp.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp/privilege-management")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @Autowired
    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @PostMapping("/")
    public CommonResponse saveUpdatePrivilege(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                              @RequestBody PrivilegeDTO privilegeDTO){
        return privilegeService.saveUpdatePrivilege("1234",privilegeDTO);
    }

    @DeleteMapping("/{privilegeId}")
    public CommonResponse deletePrivilegeById(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                              @PathVariable final String privilegeId){
        return privilegeService.deletePrivilegeById("1234", privilegeId);
    }

    @GetMapping("/{privilegeId}")
    public CommonResponse getPrivilegeById(@PathVariable final String privilegeId){
        return privilegeService.getPrivilegeById(privilegeId);
    }

    @GetMapping("/getByName/{privilege}")
    public CommonResponse getPrivilegeByName(@PathVariable final String privilege){
        return privilegeService.getPrivilegeByName(privilege);
    }

    @GetMapping("/")
    public CommonResponse getAllPrivilege(){
        return privilegeService.getAllPrivilege();
    }

}
