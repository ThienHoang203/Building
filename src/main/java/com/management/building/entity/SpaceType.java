package com.management.building.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SpaceType {

    @Id
    @NotBlank(message = "Space type name is required")
    @Column(nullable = false)
    String name;
    
    @Column(length = 1000)
    String description;
    
    @ElementCollection
    @CollectionTable(name = "space_type_functions", joinColumns = @JoinColumn(name = "space_type_name"))
    @Column(name = "function_name")
    List<String> functions;
    
    @Min(value = 0, message = "Minimum capacity must be non-negative")
    int minCapacity;

    @OneToMany(mappedBy = "type")
    List<Space> spaces;
}
