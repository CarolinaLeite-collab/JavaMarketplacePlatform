package MITELOVERS.applicationservices;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Application service responsible for retrieving publication type information
 * and converting domain objects into response DTOs.
 */

@Service
public class PublicationTypeService {

    private final IPublicationTypeRepo _iPublicationTypeRepo;
    private final PublicationTypeFactory _publicationTypeFactory;


    public PublicationTypeService(IPublicationTypeRepo iPublicationTypeRepo, PublicationTypeFactory factory){

        _iPublicationTypeRepo = iPublicationTypeRepo;
        _publicationTypeFactory = factory;

    }

    @Transactional(readOnly = true)
    public List<PublicationType> getAllPublicationTypes(){

        Iterable<PublicationType> publicationTypes = _iPublicationTypeRepo.findAll();

        List<PublicationType> response = new ArrayList<>();

        for (PublicationType publicationType : publicationTypes) {

            response.add (publicationType);
        }

        return response;

    }

    @Transactional(readOnly = true)
    public PublicationType getPublicationTypeById(String id){

        PublicationType publicationType = _iPublicationTypeRepo
                .ofIdentity(new PublicationTypeId(id))
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "PublicationType with id '" + id + "' does not exist"));

        return publicationType;
    }

    @Transactional
    public PublicationType addPublicationType(String publicationTypeName) {

        PublicationType newPublicationType = _publicationTypeFactory.createPublicationType(publicationTypeName);

        if (_iPublicationTypeRepo.containsOfIdentity(newPublicationType.identity())) {

            throw new IllegalArgumentException("The publication type " + publicationTypeName + " already exists.");

        }

        return _iPublicationTypeRepo.save(newPublicationType);

    }


}
