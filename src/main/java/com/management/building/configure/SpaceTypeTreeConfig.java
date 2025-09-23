package com.management.building.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Component
@ConfigurationProperties(prefix = "space-type.tree")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeTreeConfig {
    Integer defaultDepthLimit = 10;
    Integer maxDepthLimit = 50;
}