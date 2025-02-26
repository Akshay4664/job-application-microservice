package com.akshay.companyms.company.messaging;

import com.akshay.companyms.company.CompanyService;
import com.akshay.companyms.company.dto.ReviewMessage;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageConsumer {

    Logger log = org.slf4j.LoggerFactory.getLogger(ReviewMessageConsumer.class);

    private final CompanyService companyService;


    public ReviewMessageConsumer(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage) {
        try {
            companyService.updateCompanyRating(reviewMessage);
        }
        catch(Exception e){
            log.error("Error processing message: {}", reviewMessage, e);
        }
    }
}
