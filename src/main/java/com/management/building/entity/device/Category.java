package com.management.building.entity.device;

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "device_category"
// , uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "name" }) }
)
public class Category {

    @Id
    @Column(nullable = false, unique = true)
    String code;
    @Column(nullable = false)
    String name;
    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "category")
    Set<Device> devices;
    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "category")
    Set<CategoryStatus> categoryStatus;

}
