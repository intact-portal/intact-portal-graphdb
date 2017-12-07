package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.graphdb.services.InteractorServiceImpl;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 04/09/2014
 * Time: 17:44
 */
@RestController("/interactor")
public class InteractorController {

    final InteractorServiceImpl interactorServiceImpl;

    @Autowired
    public InteractorController(InteractorServiceImpl interactorServiceImpl) {
        this.interactorServiceImpl = interactorServiceImpl;
    }

    @RequestMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
        return interactorServiceImpl.graph(limit == null ? 100 : limit);
    }
}
