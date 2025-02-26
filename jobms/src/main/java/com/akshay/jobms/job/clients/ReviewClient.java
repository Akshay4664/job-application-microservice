package com.akshay.jobms.job.clients;

import com.akshay.jobms.job.external.Company;
import com.akshay.jobms.job.external.Review;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "reviewms",
        url = "${reviewms.url}")
public interface ReviewClient {

    @GetMapping("/reviews")
    List<Review> getReviewsByJobId(@RequestParam("companyId") Long companyId);
}
