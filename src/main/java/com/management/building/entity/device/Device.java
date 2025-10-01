package com.management.building.entity.device;

import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.management.building.entity.space.Space;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    String id;
    @Column(nullable = false, unique = true)
    String code;
    @Column(nullable = false)
    String lat;
    @Column(nullable = false)
    String lon;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    Boolean isOnline;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_code")
    Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "supplier_code")
    Supplier supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "space_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Space space;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", cascade = { CascadeType.REMOVE })
    Set<DeviceStatus> deviceStatus;

}
