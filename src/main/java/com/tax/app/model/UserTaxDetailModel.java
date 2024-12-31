package com.tax.app.model;

import java.math.BigInteger;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "UserTaxDetails")
public class UserTaxDetailModel {
	private String userId;
	private String userName;
	private BigInteger salary;
	private BigInteger taxableSalary;
	private float incomeTax;
}
