package com.akshay.companyms.company;

import com.akshay.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {

    public List<Company> getAllCompanies();

    public void createCompany(Company company);

    public boolean updateCompany(Long id, Company company);

    public boolean deleteCompanyById(Long id);

    public Company getCompanyById(Long id);

    public void updateCompanyRating(ReviewMessage reviewMessage);
}
