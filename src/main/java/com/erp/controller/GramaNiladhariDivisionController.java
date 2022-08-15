package com.erp.controller;

import com.erp.dto.GramaNiladhariDivisionDTO;
import com.erp.service.GramaNiladhariDivisionService;
import com.erp.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erp/grama-niladhari-division-management")
public class GramaNiladhariDivisionController {

    private final GramaNiladhariDivisionService gramaNiladhariDivisionService;

    @Autowired
    public GramaNiladhariDivisionController(GramaNiladhariDivisionService gramaNiladhariDivisionService) {
        this.gramaNiladhariDivisionService = gramaNiladhariDivisionService;
    }

    @PostMapping("/")
    public CommonResponse saveUpdateGramaNiladhariDivision(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
            @RequestBody GramaNiladhariDivisionDTO gramaNiladhariDivisionDTO) {
        return gramaNiladhariDivisionService.saveUpdateGramaNiladhariDivision("1234" , gramaNiladhariDivisionDTO);
    }

    @DeleteMapping("/{gramaNiladhariDivisionId}")
    public CommonResponse deleteGramaNiladhariDivisionById(/*@RequestHeader(CommonConstants.AUTH_TOKEN) final String token,*/
                                                         @PathVariable final String gramaNiladhariDivisionId){
        return gramaNiladhariDivisionService.deleteGramaNiladhariDivisionById("1234",gramaNiladhariDivisionId);
    }

    @GetMapping("/{gramaNiladhariDivisionId}")
    public CommonResponse getGramaNiladhariDivisionById(@PathVariable final String gramaNiladhariDivisionId){
        return gramaNiladhariDivisionService.getGramaNiladhariDivisionById(gramaNiladhariDivisionId);
    }

    @GetMapping("/")
    public CommonResponse getAllGramaNiladhariDivision(){
        return gramaNiladhariDivisionService.getAllGramaNiladhariDivision();
    }
}
