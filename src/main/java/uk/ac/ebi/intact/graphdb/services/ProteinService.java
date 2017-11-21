package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.repositories.ProteinRepository;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 04/09/2014
 * Time: 17:43
 */
@Service
public class ProteinService {

    final private ProteinRepository proteinRepository;

    @Autowired
    public ProteinService(ProteinRepository proteinRepository) {
        this.proteinRepository = proteinRepository;
    }
}
