package com.erp.controller;

import com.erp.service.PersonService;
import com.erp.service.PrivilegeService;
import com.erp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    private final UserService userService;
    private final PersonService personService;
    private final PrivilegeService privilegeService;

    public UIController(UserService userService, PersonService personService, PrivilegeService privilegeService) {
        this.userService = userService;
        this.personService = personService;
        this.privilegeService = privilegeService;
    }

    @GetMapping({"/","/login"})
    public String index(){
            return "login";
        }

    @GetMapping("/dash")
    public String dashBord(Model model){
        model.addAttribute("userCount", userService.getCount());
        model.addAttribute("privilegeCount",privilegeService.getCount());
        model.addAttribute("personCount", personService.getCount());
        return "dashbord";
    }

    @GetMapping("/uReg")
    public String userReg(Model model) {
        model.addAttribute("departments", null);
        return "userRegistration";
    }

    @GetMapping("/pReg")
    public String depReg() {
        return "privilege";
    }

    @GetMapping("/personAndPrivilege")
    public String attendanceRepo() {
        return "personAndPrivilegeManagment";
    }

    @GetMapping("/person")
    public String complain() {
        return "person";
    }

    @GetMapping("/gramaNiladhariDivision")
    public String gramaNiladhariDivision() {
        return "gramaNiladhariDivision";
    }


}
