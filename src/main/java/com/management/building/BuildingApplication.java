package com.management.building;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.management.building.entity.Space;
import com.management.building.entity.SpaceType;
import com.management.building.entity.User;
import com.management.building.enums.SpaceStatus;
import com.management.building.repository.SpaceRepository;
import com.management.building.repository.SpaceTypeRepository;
import com.management.building.repository.UserRepository;

@SpringBootApplication
public class BuildingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BuildingApplication.class, args);

        SpaceTypeRepository spaceTypeRepository = context.getBean(SpaceTypeRepository.class);
        SpaceRepository spaceRepository = context.getBean(SpaceRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        seedUser(userRepository, passwordEncoder);
        seedSpaceType(spaceTypeRepository);
        seedSpace(spaceRepository, spaceTypeRepository);

    }

    private static void seedSpaceType(SpaceTypeRepository spaceTypeRepository) {
        List<SpaceType> spaceTypes = new ArrayList<>();
        spaceTypes.add(SpaceType
                .builder()
                .name("LIBRARY")
                .description("library in school")
                .maxCapacity(1000)
                .requiresSpecialAccess(false)
                .build());
        spaceTypes.add(SpaceType
                .builder()
                .name("FLOOR")
                .description("floor yeah...")
                .requiresSpecialAccess(false)
                .build());
        spaceTypes.add(SpaceType
                .builder()
                .name("BLOCK")
                .description("large building...")
                .maxCapacity(10000)
                .requiresSpecialAccess(false)
                .build());

        for (SpaceType spaceType : spaceTypes) {
            if (!spaceTypeRepository.existsById(spaceType.getName())) {
                spaceTypeRepository.save(spaceType);
            }
        }
    }

    private static void seedSpace(SpaceRepository spaceRepository,
            SpaceTypeRepository spaceTypeRepository) {

        List<SpaceType> spaceTypes = spaceTypeRepository.findAll();

        SpaceType buildingType = spaceTypes.stream().filter((type) -> type.getName().equals("BLOCK")).toList()
                .getFirst();
        if (!spaceRepository.existsByName("B10")) {
            spaceRepository.save(Space
                    .builder()
                    .name("B10")
                    .status(SpaceStatus.AVAILABLE)
                    .area(100.10)
                    .capacity(1000)
                    .type(buildingType)
                    .build());
        }

        List<Space> existingSpaces = spaceRepository.findAll();
        if (!existingSpaces.isEmpty()) {
            Space parentSpace = existingSpaces.get(0);
            SpaceType floorType = spaceTypes.stream().filter((type) -> type.getName().equals("FLOOR")).toList()
                    .getFirst();
            if (!spaceRepository.existsByName("Floor 10")) {
                spaceRepository.save(Space
                        .builder()
                        .name("Floor 10")
                        .status(SpaceStatus.AVAILABLE)
                        .area(100.10)
                        .capacity(1000)
                        .type(floorType)
                        .parentSpace(parentSpace)
                        .build());
            }
        }

        List<Space> allSpaces = spaceRepository.findAll();
        if (allSpaces.size() >= 2) {
            Space parentSpace2 = allSpaces.get(1);
            SpaceType roomType = spaceTypes.stream().filter((type) -> type.getName().equals("LIBRARY")).toList()
                    .getFirst();
            if (!spaceRepository.existsByName("Room 10")) {
                spaceRepository.save(Space
                        .builder()
                        .name("Room 10")
                        .status(SpaceStatus.AVAILABLE)
                        .area(100.10)
                        .capacity(1000)
                        .type(roomType)
                        .parentSpace(parentSpace2)
                        .build());
            }
        }

    }

    private static void seedUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        List<User> users = new ArrayList<>();

        users.add(User
                .builder()
                .username("admin")
                .password(passwordEncoder.encode("123"))
                .name("Admin handsome")
                .build());
        users.add(User
                .builder()
                .username("user")
                .password(passwordEncoder.encode("321"))
                .name("User handsome")
                .build());

        for (User user : users) {
            if (!userRepository.existsByUsername(user.getUsername())) {
                userRepository.save(user);
            }
        }

    }

}
