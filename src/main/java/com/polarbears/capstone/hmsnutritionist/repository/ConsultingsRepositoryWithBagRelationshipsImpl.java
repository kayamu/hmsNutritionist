package com.polarbears.capstone.hmsnutritionist.repository;

import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ConsultingsRepositoryWithBagRelationshipsImpl implements ConsultingsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Consultings> fetchBagRelationships(Optional<Consultings> consultings) {
        return consultings.map(this::fetchEpicryses).map(this::fetchMenuSuggestions);
    }

    @Override
    public Page<Consultings> fetchBagRelationships(Page<Consultings> consultings) {
        return new PageImpl<>(fetchBagRelationships(consultings.getContent()), consultings.getPageable(), consultings.getTotalElements());
    }

    @Override
    public List<Consultings> fetchBagRelationships(List<Consultings> consultings) {
        return Optional.of(consultings).map(this::fetchEpicryses).map(this::fetchMenuSuggestions).orElse(Collections.emptyList());
    }

    Consultings fetchEpicryses(Consultings result) {
        return entityManager
            .createQuery(
                "select consultings from Consultings consultings left join fetch consultings.epicryses where consultings is :consultings",
                Consultings.class
            )
            .setParameter("consultings", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Consultings> fetchEpicryses(List<Consultings> consultings) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, consultings.size()).forEach(index -> order.put(consultings.get(index).getId(), index));
        List<Consultings> result = entityManager
            .createQuery(
                "select distinct consultings from Consultings consultings left join fetch consultings.epicryses where consultings in :consultings",
                Consultings.class
            )
            .setParameter("consultings", consultings)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Consultings fetchMenuSuggestions(Consultings result) {
        return entityManager
            .createQuery(
                "select consultings from Consultings consultings left join fetch consultings.menuSuggestions where consultings is :consultings",
                Consultings.class
            )
            .setParameter("consultings", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Consultings> fetchMenuSuggestions(List<Consultings> consultings) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, consultings.size()).forEach(index -> order.put(consultings.get(index).getId(), index));
        List<Consultings> result = entityManager
            .createQuery(
                "select distinct consultings from Consultings consultings left join fetch consultings.menuSuggestions where consultings in :consultings",
                Consultings.class
            )
            .setParameter("consultings", consultings)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
