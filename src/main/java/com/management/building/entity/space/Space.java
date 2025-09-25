package com.management.building.entity.space;

import java.util.Set;

import com.management.building.entity.smartDevice.SmartDevice;
import com.management.building.enums.space.SpaceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
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
@Table(name = "space", indexes = {
                @Index(name = "ID_status_name", columnList = "status, name"),
                @Index(name = "ID_parentId_typeName", columnList = "parent_id, type_name"),
                @Index(name = "ID_status", columnList = "status"),
                @Index(name = "ID_sname", columnList = "name"),
                @Index(name = "ID_id_level", columnList = "id, level"),
})
public class Space {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(nullable = false, length = 100)
        String name;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        SpaceStatus status;

        @DecimalMin(value = "0.0", inclusive = false)
        Double area;

        Integer capacity;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "space")
        Set<SmartDevice> devices;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id", nullable = true)
        Space parent;

        @OneToMany(mappedBy = "parent", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
        Set<Space> children;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "type_name", nullable = false)
        SpaceType type;
}
