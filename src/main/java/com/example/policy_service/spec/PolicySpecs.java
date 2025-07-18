package com.example.policy_service.spec;

import com.example.policy_service.model.Policy;
import com.example.policy_service.model.Proposal;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PolicySpecs {

    public static Specification<Policy> buildCriteria(
            LocalDate date,
            List<Long> carIds,
            List<Long> subscriberIds) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Uslov za date
            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                predicates.add(cb.between(root.get("dateSigned"), startOfDay, endOfDay));
            }

            // Uslov za carIds
            if (carIds != null && !carIds.isEmpty()) {
                Join<Policy, Proposal> proposal = root.join("proposal");
                predicates.add(proposal.get("carId").in(carIds));
            }

            // Uslov za subscriberIds
            if (subscriberIds != null && !subscriberIds.isEmpty()) {
                Join<Policy, Proposal> proposal = root.join("proposal");
                predicates.add(proposal.get("subscriberId").in(subscriberIds));
            }

            // Ako nema nijedan uslov
            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
