package com.erp.controller;

import com.erp.constant.CommonConstants;
import com.erp.dto.PersonPrivilegeDTO;
import com.erp.service.PersonPrivilegeService;
import com.erp.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp/person-privilege-management")
public class PersonPrivilegeController {

    private final PersonPrivilegeService personPrivilegeService;

    @Autowired
    public PersonPrivilegeController(PersonPrivilegeService personPrivilegeService) {
        this.personPrivilegeService = personPrivilegeService;
    }

    @PostMapping("/")
    public CommonResponse saveUpdatePersonPrivilege(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                                    @RequestBody PersonPrivilegeDTO personPrivilegeDTO){
        return personPrivilegeService.saveUpdatePersonPrivilege("1234", personPrivilegeDTO);
    }

    @DeleteMapping("/{personPrivilegeId}")
    public CommonResponse deletePersonPrivilegeById(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                                    @PathVariable final String personPrivilegeId){
        return personPrivilegeService.deletePersonPrivilegeById("1234", personPrivilegeId);
    }

    @GetMapping("/{personPrivilegeId}")
    public CommonResponse getPersonPrivilegeById(@PathVariable final String personPrivilegeId){
        return personPrivilegeService.getPersonPrivilegeById(personPrivilegeId);
    }

    @GetMapping("personId/{personId}")
    public CommonResponse getPersonPrivilegeByPersonId(@PathVariable final String personId){
        return personPrivilegeService.getPersonPrivilegeByPersonId(personId);
    }

    @GetMapping("/")
    public CommonResponse getAllPersonPrivilege(){
        return personPrivilegeService.getAllPersonPrivilege();
    }
}
