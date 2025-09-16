// package com.management.building.exception;

// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

// import org.springframework.stereotype.Component;

// import jakarta.annotation.PostConstruct;

// @Component
// public class ValidationRegistry {
//     private final Map<String, ValidationFunction> handlers = new ConcurrentHashMap<>();

//     @PostConstruct
//     public void initialize() {
//         registerHandler("Size", this::handleSizeError);
//         registerHandler("ValidEnum", this::handleValidEnumError);
//         registerHandler("NotBlank", this::mapAttributesToMessage);
//         registerHandler("NotNull", this::mapAttributesToMessage);
//         registerHandler("NotEmpty", this::mapAttributesToMessage);
//         registerHandler("Min", this::handleMinError);
//         registerHandler("Max", this::handleMaxError);
//         registerHandler("Email", this::mapAttributesToMessage);
//         registerHandler("Pattern", this::mapAttributesToMessage);
//         registerHandler("DateOfBirthConstraint", this::handleDateOfBirth);

//         System.out.println("ValidationRegistry initialized with " + handlers.size() + " handlers");
//     }
// }
