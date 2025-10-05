package com.management.building.dto.response.tuyaCloud.device;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuyaDevicePropertyDetail {
    String code;
    Long time;
    Object value;
    String custome_name;
    Integer dp_id;
}