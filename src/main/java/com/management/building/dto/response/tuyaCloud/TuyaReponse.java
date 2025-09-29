package com.management.building.dto.response.tuyaCloud;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuyaReponse<T> {
    Boolean success;
    Long t;
    String tid;
    T result;
    String msg;
    Integer code;
}
