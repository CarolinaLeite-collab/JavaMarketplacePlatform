package TOPSECRET.domain.repository;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;

public interface IPublishingCompanyRepo {

    PublishingCompany registerPublishingCompany(String publishingCompanyName);

}
