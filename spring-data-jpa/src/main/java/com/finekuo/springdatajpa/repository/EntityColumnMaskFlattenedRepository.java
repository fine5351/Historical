package com.finekuo.springdatajpa.repository;

import com.finekuo.springdatajpa.entity.EntityColumnMaskFlattened;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityColumnMaskFlattenedRepository extends CrudRepository<EntityColumnMaskFlattened, Long> {

    /**
     * Find the EntityColumnMask by account and apiUrl.
     *
     * @param account the account identifier
     * @param apiUri  the API URI
     * @return the EntityColumnMask if found, otherwise null
     */
    EntityColumnMaskFlattened findByAccountAndMethodAndApiUri(String account, String method, String apiUri);

}
