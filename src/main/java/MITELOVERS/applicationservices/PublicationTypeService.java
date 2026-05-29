package MITELOVERS.applicationservices;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;

import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import MITELOVERS.mapper.PublicationTypeResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
