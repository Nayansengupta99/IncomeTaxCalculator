package com.tax.app.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tax.app.model.UserModel;
import com.tax.app.model.UserTaxDetailModel;
import com.tax.app.repository.IncomeTaxRepository;
import com.tax.app.repository.UserTaxDetailsRepo;
import com.tax.app.service.ifc.IncomeTaxServiceIfc;

@Service
public class IncomeTaxService implements IncomeTaxServiceIfc {

	@Autowired
	IncomeTaxRepository repo;

	@Autowired
	UserTaxDetailsRepo userTaxDetailsRepo;

	public String saveUser(UserModel model) {
		// TODO Auto-generated method stub
		List<UserModel> users = repo.findAll();
		Boolean isExistingUser = users.parallelStream()
				.filter(x -> x.getUserName().equalsIgnoreCase(model.getUserName())).collect(Collectors.toList())
				.size() > 0 ? Boolean.TRUE : Boolean.FALSE;
		if (isExistingUser) {
			return "User already exists. Please try with different username.";
		} else {
			repo.save(model);
			return "User: " + model.getUserName() + " saved successfully.";
		}

	}

	public UserTaxDetailModel getTaxDetailsByUserName(String userName) {
		return userTaxDetailsRepo.findByUserName(userName);
	}

	public Map<String, Double> getTaxDetailMapForUser(String userName) {
		UserTaxDetailModel userTaxDetailModel = getTaxDetailsByUserName(userName);
		Map<String, Double> userTaxMap = calculateTaxUserMap(userTaxDetailModel.getSalary());
		return userTaxMap;
	}

	public String saveUserTaxDetails(UserTaxDetailModel taxDetailModel) {
		if (taxDetailModel != null) {
			List<UserTaxDetailModel> list = userTaxDetailsRepo.findAll();
			Boolean existingUserTaxDetail = list.parallelStream()
					.filter(x -> x.getUserName().equalsIgnoreCase(taxDetailModel.getUserName()))
					.collect(Collectors.toList()).size() > 0 ? Boolean.TRUE : Boolean.FALSE;
			if (!existingUserTaxDetail) {
				if (repo.findUserIdByUserName(taxDetailModel.getUserName()) != null) {
					taxDetailModel.setUserId(repo.findUserIdByUserName(taxDetailModel.getUserName()).getUserId());
					taxDetailModel.setUserName(taxDetailModel.getUserName());
					taxDetailModel.setSalary(taxDetailModel.getSalary());
					taxDetailModel.setIncomeTax(calculateTax(taxDetailModel.getSalary()));
					taxDetailModel.setTaxableSalary(taxDetailModel.getSalary().add(BigInteger.valueOf(-75000)));
					userTaxDetailsRepo.save(taxDetailModel);
				} else {
					return "User not found. Please Register first.";
				}
			} else {
				return "Details already exists";
			}
		}
		return "Tax Details saved";

	}

	public Map<Integer, Integer> slabNumber(BigInteger taxableSalary) {
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		// int slabNumber = 0;
		if (taxableSalary.intValue() >= 1500000) {
			m.put(4, taxableSalary.intValue() - 1500000);
		} else if (taxableSalary.intValue() < 1500000 && taxableSalary.intValue() > 1200000) {
			m.put(3, taxableSalary.intValue() - 1200000);
		} else if (taxableSalary.intValue() < 1200000 && taxableSalary.intValue() > 1000000) {
			m.put(2, taxableSalary.intValue() - 1000000);
		} else if (taxableSalary.intValue() < 1000000 && taxableSalary.intValue() > 700000) {
			m.put(1, taxableSalary.intValue() - 700000);
		} else if (taxableSalary.intValue() < 700000 && taxableSalary.intValue() > 300000) {
			m.put(0, taxableSalary.intValue() - 300000);
		}
		return m;

	}

	public Map<Double, Integer> taxSlabs(BigInteger taxableSalary) {
		Map<Double, Integer> taxSlabs = new LinkedHashMap<Double, Integer>();

		taxSlabs.put(0.05, 400000);
		taxSlabs.put(0.1, 300000);
		taxSlabs.put(0.15, 200000);
		taxSlabs.put(0.2, 300000);
		taxSlabs.put(0.3, 400000);

		List<Map.Entry<Double, Integer>> entryList = new ArrayList<>(taxSlabs.entrySet());
		Double key = entryList.get(slabNumber(taxableSalary).entrySet().iterator().next().getKey()).getKey();
		// System.out.print(key);
		taxSlabs.put(key, slabNumber(taxableSalary).entrySet().iterator().next().getValue());
		// System.out.print(taxSlabs);
		return taxSlabs;

	}

	public float calculateTax(BigInteger inHandSalary) {

		float incTax = 0;

		BigInteger taxableSalary = inHandSalary.add(BigInteger.valueOf(-75000));

		Map<Double, Integer> taxSlabs = taxSlabs(taxableSalary);

		int slabNumber = slabNumber(taxableSalary).entrySet().iterator().next().getKey();
		int counter = 0;

		if (taxableSalary.intValue() < 300000) {
			return 0;
		}

		for (Map.Entry<Double, Integer> tSlabs : taxSlabs.entrySet()) {
			incTax = ((float) (tSlabs.getKey() * tSlabs.getValue())) + incTax;
			counter += 1;
			if (counter > slabNumber) {
				break;
			}

		}

		return incTax;

	}

	public Map<String, Double> calculateTaxUserMap(BigInteger inHandSalary) {

		float incTax = 0;

		Map<String, Double> taxMap = new LinkedHashMap<String, Double>();

		List<String> taxSlabConstant = Arrays.asList("300000  -  700000 for 5%", "700000  -  1000000 for 10%",
				"1000000  -  1200000 for 15%", "1200000  -  1500000 for 20%", "Above 1500000 for 30%");

		taxMap.put("Standard Deduction: 75000", inHandSalary.add(BigInteger.valueOf(-75000)).doubleValue());

		BigInteger taxableSalary = inHandSalary.add(BigInteger.valueOf(-75000));

		Map<Double, Integer> taxSlabs = taxSlabs(taxableSalary);

		int slabNumber = slabNumber(taxableSalary).entrySet().iterator().next().getKey();
		int counter = 0;

		if (taxableSalary.intValue() < 300000) {
			return taxMap;
		}

		for (Map.Entry<Double, Integer> tSlabs : taxSlabs.entrySet()) {
			incTax = ((float) (tSlabs.getKey() * tSlabs.getValue())) + incTax;
			taxMap.put(taxSlabConstant.get(counter), (double) (tSlabs.getKey() * tSlabs.getValue()));
			counter += 1;
			if (counter > slabNumber) {
				break;
			}

		}

		return taxMap;

	}

}
