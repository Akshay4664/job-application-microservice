package com.akshay.companyms.company.impl;

import com.akshay.companyms.company.Company;
import com.akshay.companyms.company.CompanyRepository;
import com.akshay.companyms.company.CompanyService;
import com.akshay.companyms.company.clients.ReviewClient;
import com.akshay.companyms.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient){
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public boolean updateCompany(Long id, Company company) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if(optionalCompany.isPresent()){
            Company updatedCompany = optionalCompany.get();
            updatedCompany.setDescription(company.getDescription());
            updatedCompany.setName(company.getName());
            companyRepository.save(updatedCompany);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCompanyById(Long id) {
        if(companyRepository.existsById(id)){
            companyRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    public Company getCompanyById(Long id){
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {

        System.out.println(reviewMessage.getDescription());
        Company company = companyRepository.findById(reviewMessage.getCompanyId()).
                orElseThrow(() -> new NotFoundException("Company not found" + reviewMessage.getCompanyId()));

        double averageRating = reviewClient.getAverageRating(reviewMessage.getCompanyId()); // get average rating from review service

        company.setRating(averageRating);
        companyRepository.save(company);
    }
}
