package com.kemi.solr;

import com.kemi.entities.solr.UdcDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Created by Eugene on 29.11.2016.
 */
public interface SolrDocumentRepository extends SolrCrudRepository<UdcDocument, String> {

    @Override
    UdcDocument save(UdcDocument entity);
}
