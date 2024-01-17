package com.nphc.hr.spec;

import com.nphc.hr.dao.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification implements Specification<Employee> {


    private final List<SearchCriteria> searchCriteriaList;

    public EmployeeSpecification() {
        this.searchCriteriaList = new ArrayList<>();
    }


    public void add(SearchCriteria searchCriteria) {
        searchCriteriaList.add(searchCriteria);
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : searchCriteriaList) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(criteriaBuilder.not(root.get(criteria.getKey())).in(criteria.getValue()));
            }

        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    }
}
