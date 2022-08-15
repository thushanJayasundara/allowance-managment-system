package com.erp.controller;

import com.erp.constant.CommonConstants;
import com.erp.dto.PersonDTO;
import com.erp.service.PersonService;
import com.erp.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp/person-management")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/")
    public CommonResponse saveUpdatePerson(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                           @RequestBody PersonDTO personDTO){
        return personService.saveUpdatePerson("1234", personDTO);
    }

    @DeleteMapping("/{personId}")
    public CommonResponse deletePerson(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                       @PathVariable final String personId){
        return personService.deletePerson("1234", personId);
    }

    @GetMapping("/{personId}")
    public CommonResponse getPersonById(@PathVariable final String personId){
        return personService.getPersonById(personId);
    }

    @GetMapping("get-by-nic/{personNic}")
    public CommonResponse getPersonByNic(@PathVariable final String personNic){
        return personService.getPersonByNic(personNic);
    }

    @GetMapping("/")
    public CommonResponse getAllPerson(){
        return personService.getAllPerson();
    }
}
