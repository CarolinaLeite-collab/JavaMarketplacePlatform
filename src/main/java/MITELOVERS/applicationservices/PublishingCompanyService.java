package MITELOVERS.applicationservices;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import MITELOVERS.mapper.PublishingCompanyResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PublishingCompanyService {

    private PublishingCompanyFactory _publishingCompanyFactory;
    private IPublishingCompanyRepo _iPublishingCompanyRepo;
    private final PublishingCompanyResponseDTOMapper _publishingCompanyResponseDTOMapper;

    public PublishingCompanyService(PublishingCompanyFactory publishingCompanyFactory,
                                    IPublishingCompanyRepo iPublishingCompanyRepo,
                                    PublishingCompanyResponseDTOMapper publishingCompanyResponseDTOMapper) {

        _publishingCompanyFactory = publishingCompanyFactory;
        _iPublishingCompanyRepo = iPublishingCompanyRepo;
        _publishingCompanyResponseDTOMapper = publishingCompanyResponseDTOMapper;
    }

    public PublishingCompanyResponseDTO registerPublishingCompany(PublishingCompanyRequestDTO publishingCompanyName) {

        String newPubCompName = publishingCompanyName.toString();

        PublishingCompany newPublishingCompany = _publishingCompanyFactory.createPublishingCompany(newPubCompName);

        if (_iPublishingCompanyRepo.containsOfIdentity(newPublishingCompany.identity())) {

            return _publishingCompanyResponseDTOMapper.toModel(newPublishingCompany);

        }

        PublishingCompany saved = _iPublishingCompanyRepo.save(newPublishingCompany);

        return _publishingCompanyResponseDTOMapper.toModel(saved);

    }

    public List<PublishingCompanyResponseDTO> getAllPublishingCompanies() {

        Iterable<PublishingCompany> publishingCompanies = _iPublishingCompanyRepo.findAll();

        List<PublishingCompanyResponseDTO> responseDTOs = new ArrayList<>();

        for (PublishingCompany publishingCompany : publishingCompanies) {

            responseDTOs.add(_publishingCompanyResponseDTOMapper.toModel(publishingCompany));

        }

        return responseDTOs;

    }

    public PublishingCompany getPublishingCompanyById(String publishingCompanyId) {

        PublishingCompanyId id = new  PublishingCompanyId(publishingCompanyId);

        PublishingCompany publishingCompany = _iPublishingCompanyRepo.ofIdentity(id)
                .orElseThrow(() -> new NoSuchElementException("Publishing Company not found"));

        return publishingCompany;

    }

}
