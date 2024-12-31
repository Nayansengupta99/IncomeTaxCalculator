package com.tax.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "Users")
public class UserModel {
	
	
	
	
	
	@Id
	@MongoId(FieldType.OBJECT_ID)
	private String userId;
	private String userName;
	private int userAge;
	
	
	
	
}
