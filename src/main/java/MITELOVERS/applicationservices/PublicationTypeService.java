package MITELOVERS.applicationservices;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;

import MITELOVERS.domain.valueobject.PublicationTypeId;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
import org.springframework.stereotype.Service;

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

    private final PublicationTypeResponseDTOMapper _mapper;

    public PublicationTypeService(IPublicationTypeRepo iPublicationTypeRepo, PublicationTypeResponseDTOMapper mapper){

        _iPublicationTypeRepo = iPublicationTypeRepo;
        _mapper = mapper;

    }

    public List<PublicationTypeResponseDTO> getAllPublicationTypes(){

        Iterable<PublicationType> publicationTypes = _iPublicationTypeRepo.findAll();

        List<PublicationTypeResponseDTO> response = new ArrayList<>();

        for (PublicationType publicationType : publicationTypes) {

            response.add (_mapper.toResponseDTO(publicationType));
        }

        return response;

    }

    public PublicationTypeResponseDTO getPublicationTypeById(String id){

        PublicationType publicationType = _iPublicationTypeRepo
                .ofIdentity(new PublicationTypeId(id))
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "PublicationType with id '" + id + "' does not exist"));

        return _mapper.toResponseDTO(publicationType);
    }
}
