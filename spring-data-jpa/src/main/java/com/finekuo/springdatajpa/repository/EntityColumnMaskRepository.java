package com.finekuo.springdatajpa.repository;

import com.finekuo.springdatajpa.entity.EntityColumnMask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityColumnMaskRepository extends CrudRepository<EntityColumnMask, String> {

    /**
     * Find the EntityColumnMask by account and apiUrl.
     *
     * @param account the account identifier
     * @param apiUri  the API URI
     * @return the EntityColumnMask if found, otherwise null
     */
    EntityColumnMask findByAccountAndMethodAndApiUri(String account, String method, String apiUri);

}
