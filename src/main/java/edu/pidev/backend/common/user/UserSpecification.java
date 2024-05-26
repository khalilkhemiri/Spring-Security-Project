package edu.pidev.backend.common.user;


import edu.pidev.backend.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {

    private final UserFilterCriteria criteria;
    @Override
    public @NotNull Specification<User> and(Specification<User> other) {
        return Specification.super.and(other);
    }

    @Override
    public @NotNull Specification<User> or(Specification<User> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(@NotNull Root<User> root,@NotNull CriteriaQuery<?> query,@NotNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteria.isAccountLocked() ? criteriaBuilder.isTrue(root.get("accountLocked")) : criteriaBuilder.isFalse(root.get("accountLocked")));

        predicates.add(criteria.isEnabled() ? criteriaBuilder.isTrue(root.get("enabled")) : criteriaBuilder.isFalse(root.get("enabled")));


        if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
            String searchPattern = "%" + criteria.getSearch() + "%";
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("mobile")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchPattern)
            );
            predicates.add(searchPredicate);
        }

        if (criteria.getBadge() != null) {
            Predicate typePredicate = criteriaBuilder.equal(root.get("badge"), criteria.getBadge());
            predicates.add(typePredicate);
        }

        if (criteria.getCity() != null) {
            Predicate typePredicate = criteriaBuilder.equal(root.get("addressCity"), criteria.getCity());
            predicates.add(typePredicate);
        }

        if (criteria.getGender() != null) {
            Predicate typePredicate = criteriaBuilder.equal(root.get("gender"), criteria.getGender());
            predicates.add(typePredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
