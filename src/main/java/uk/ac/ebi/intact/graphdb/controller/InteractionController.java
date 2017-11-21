package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.graphdb.services.ImportInteractionService;

import java.util.List;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController("/")
public class InteractionController {

    final ImportInteractionService importInteractionService;

    @Autowired
    public InteractionController(ImportInteractionService importInteractionService) {
        this.importInteractionService = importInteractionService;
    }

    @RequestMapping("/populate")
    public List<Interaction> populate() {
        return importInteractionService.importInteractions();
    }
}
