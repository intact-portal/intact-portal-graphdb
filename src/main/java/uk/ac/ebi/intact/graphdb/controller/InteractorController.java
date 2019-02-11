package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.graphdb.services.GraphInteractorService;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 04/09/2014
 * Time: 17:44
 */
@RestController
@RequestMapping("/graph/interactor")
public class InteractorController {

    final GraphInteractorService graphInteractorService;

    @Autowired
    public InteractorController(GraphInteractorService graphInteractorService) {
        this.graphInteractorService = graphInteractorService;
    }

}
