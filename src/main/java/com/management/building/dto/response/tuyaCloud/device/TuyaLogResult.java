package com.management.building.dto.response.tuyaCloud.device;

import java.util.List;

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
public class TuyaLogResult {
    String device_id;
    String last_row_key;
    Boolean has_more;
    Long total;
    List<TuyaLogDetail> logs;
}