package com.akshay.jobms.job.impl;

import com.akshay.jobms.job.Job;
import com.akshay.jobms.job.JobRepository;
import com.akshay.jobms.job.JobService;
import com.akshay.jobms.job.clients.CompanyClient;
import com.akshay.jobms.job.clients.ReviewClient;
import com.akshay.jobms.job.dto.JobDTO;
import com.akshay.jobms.job.external.Company;
import com.akshay.jobms.job.external.Review;
import com.akshay.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

//    private Long nextId = 1L;

    private JobRepository jobRepository;

    @Autowired
    private CompanyClient companyClient;

    @Autowired
    private ReviewClient reviewClient;

    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepository){
        this.jobRepository = jobRepository;
    }

    //private List<Job> jobs = new ArrayList<>();

    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();

//        return jobs.stream().map( job ->convertToDto(job)).collect(Collectors.toList());

        return jobs.stream().map(this::convertToDto).collect(Collectors.toList());

    }

    public JobDTO convertToDto(Job job){
//        RestTemplate restTemplate = new RestTemplate();
//        Company company = restTemplate.getForObject("http://localhost:8081/companies/"+job.getCompanyId(),
//                Company.class);

//        Company company = restTemplate.getForObject("http://companyms:8081/companies/"+job.getCompanyId(),
//                Company.class);

        Company company = companyClient.getCompany(job.getCompanyId());

//        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                "http://reviewms:8083/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                });
//        List<Review> reviews = reviewResponse.getBody();

        List<Review> reviews = reviewClient.getReviewsByJobId(job.getCompanyId());

        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(job, company, reviews);
//        jobDTO.setCompany(company);
        return jobDTO;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

//    @Override
//    public Job getJobById(Long id) {
////        for (Job job : jobs) {
////            if (job.getId().equals(id)) {
////                return job;
////            }
////        }
//        return jobRepository.findById(id).orElse(null);
//    }

    @Override
    public JobDTO getJobById(Long id){

        Job job = jobRepository.findById(id).orElse(null);
        return convertToDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
//        Iterator<Job> iterator = jobs.iterator();
//        while(iterator.hasNext()){
//            Job job = iterator.next();
//            if(job.getId().equals(id)){
//                iterator.remove();
//                return true;
//            }
//        }
//        return false;

        try{
            jobRepository.deleteById(id);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
//        for(Job job : jobs){

            Optional<Job> jobOptional = jobRepository.findById(id);
            if(jobOptional.isPresent()){
                Job job = jobOptional.get();
                job.setDescription(updatedJob.getDescription());
                job.setLocation(updatedJob.getLocation());
                job.setTitle(updatedJob.getTitle());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setMinSalary(updatedJob.getMinSalary());
                jobRepository.save(job);
                return true;
            }
            return false;
    }
}
