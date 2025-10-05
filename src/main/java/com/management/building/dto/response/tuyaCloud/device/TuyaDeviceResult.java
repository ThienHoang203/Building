package com.management.building.dto.response.tuyaCloud.device;

import java.util.List;

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
public class TuyaDeviceResult {
    List<TuyaDeviceDetail> list;
    String last_id;
    Integer total;
    Boolean has_more;
}
