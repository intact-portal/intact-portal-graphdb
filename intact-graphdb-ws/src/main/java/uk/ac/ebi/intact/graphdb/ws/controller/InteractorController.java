package uk.ac.ebi.intact.graphdb.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.graphdb.service.GraphInteractorService;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 04/09/2014
 * Time: 17:44
 */
@RestController
@RequestMapping("/interactor")
public class InteractorController {

    final GraphInteractorService graphInteractorService;

    @Autowired
    public InteractorController(GraphInteractorService graphInteractorService) {
        this.graphInteractorService = graphInteractorService;
    }

}
