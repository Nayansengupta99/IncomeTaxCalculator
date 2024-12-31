package com.tax.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tax.app.model.UserModel;
import com.tax.app.model.UserTaxDetailModel;
import com.tax.app.service.IncomeTaxService;

@RestController
@RequestMapping("tax")
public class IncomeTaxController {

	@Autowired
	IncomeTaxService service;
	
	@PostMapping("/saveUser")
	public String saveUser(@RequestBody UserModel model) {
		return service.saveUser(model);
		
	}
	@PostMapping("/saveTaxDetail")
	public String saveUserTaxDetailModel(@RequestBody UserTaxDetailModel userTaxDetailModel) {
		return service.saveUserTaxDetails(userTaxDetailModel);
	}
	
	@GetMapping("/taxDetail/{userName}")
	public UserTaxDetailModel findTaxDetailsByUserName(@PathVariable String userName) {
		return service.getTaxDetailsByUserName(userName);
	}
}
