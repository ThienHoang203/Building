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
public class TuyaDeviceDetail {
    String id;
    String category;
    String icon;
    String ip;
    String lat;
    String lon;
    String name;
    Boolean sub;
    String uuid;
    Long activeTime;
    Long createTime;
    Long updateTime;
    String customName;
    Boolean isOnline;
    String localKey;
    String productId;
    String productName;
    String timeZone;
}
