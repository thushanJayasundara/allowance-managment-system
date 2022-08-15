package com.erp.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================================================
 * A Common response with payload/status used as return results
 * of Controller/Service methods
 * ==============================================================
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    private List<Object> payload = null;
    private List<String> errorMessages = new ArrayList<>();
    private boolean status = false;

}