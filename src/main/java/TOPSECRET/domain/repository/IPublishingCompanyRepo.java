package TOPSECRET.domain.repository;

import TOPSECRET.domain.publishingcompany.PublishingCompany;

public interface IPublishingCompanyRepo {

    PublishingCompany registerPublishingCompany(String publishingCompanyName);

}
