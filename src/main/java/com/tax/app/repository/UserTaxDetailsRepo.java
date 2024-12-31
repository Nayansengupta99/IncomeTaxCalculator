package com.tax.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tax.app.model.UserTaxDetailModel;

public interface UserTaxDetailsRepo extends MongoRepository<UserTaxDetailModel, String> {
	UserTaxDetailModel findByUserName(String userName);
}
