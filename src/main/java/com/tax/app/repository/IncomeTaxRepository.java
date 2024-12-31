package com.tax.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tax.app.model.UserModel;

public interface IncomeTaxRepository extends MongoRepository<UserModel, String> {
	UserModel findUserIdByUserName(String userName);
}
