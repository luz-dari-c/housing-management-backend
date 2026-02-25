package com.backend.housing.application.services;


import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.PropertyStatus;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.ports.in.properties.CreatePropertyUseCase;
import com.backend.housing.application.Commands.properties.CreatePropertyCommand;
import com.backend.housing.domain.ports.out.PropertyRepository;
import org.aspectj.bridge.ICommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class CreatePropertyService  implements CreatePropertyUseCase{

private final UserServicePort userServicePort;
private final PropertyRepository propertyRepository;

    public  CreatePropertyService(PropertyRepository propertyRepository, UserServicePort userServicePort){
        this.propertyRepository = propertyRepository;
        this.userServicePort = userServicePort;

    }

    public Property createProperty(CreatePropertyCommand command){

        if (!userServicePort.userExists(command.getOwnerId())) {
            throw new UserNotFoundException("User not found");
        }


        PropertyId id = PropertyId.generate();

        Property property = new Property(
                PropertyId.generate(),
                command.getTitle(),
                command.getDescription(),
                command.getCoordinates(),
                command.getSalePrice(),
                command.getRentPrice(),
                command.getModality(),
                PropertyStatus.DRAFT,
                command.getOwnerId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                command.getImageUrls() != null ? command.getImageUrls() : List.of(),
                command.getNumberOfBedrooms(),
                command.getNumberOfBathrooms(),
                command.getAreaInSquareMeters(),
                command.isPetsAllowed(),
                command.getAddress(),
                command.isFurnished()
        );


        return propertyRepository.save(property);

    }





}
