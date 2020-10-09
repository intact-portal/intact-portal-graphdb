package uk.ac.ebi.intact.graphdb.ws.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.graphdb.ws.Neo4jTestConfiguration;
import uk.ac.ebi.intact.graphdb.ws.controller.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Neo4jTestConfiguration.class)
public class InteractionDetailsPageTest {

    @Autowired
    private InteractionController controller;

    @Test
    public void shuffleDataBetweenModels() {

        //initializing xrefs
        Set<Xref> publicationXrefs = new HashSet<>();
        publicationXrefs.add(new Xref(new CvTerm("db_shortname1", "db_identifier1"),
                "identifier1",
                new CvTerm("qualifier_shortname1", "qualifier_identifier1"),
                "ac1"
        ));
        publicationXrefs.add(new Xref(new CvTerm("db_shortname2", "db_identifier2"),
                "identifier2",
                new CvTerm("qualifier_shortname2", "qualifier_identifier2"),
                "ac2"
        ));

        Set<Xref> experimentXrefs = new HashSet<>();
        experimentXrefs.add(new Xref(new CvTerm("db_shortname3", "db_identifier3"),
                "identifier3",
                new CvTerm("qualifier_shortname3", "qualifier_identifier3"),
                "ac1"
        ));
        experimentXrefs.add(new Xref(new CvTerm("db_shortname2", "db_identifier2"),
                "identifier2",
                new CvTerm("qualifier_shortname2", "qualifier_identifier2"),
                "ac2"
        ));

        //should be removed from everywhere
        experimentXrefs.add(new Xref(new CvTerm("db_shortname5", "MI:0446"),// pubmed id
                "identifier5",
                new CvTerm("qualifier_shortname5", "qualifier_identifier5"),
                "ac5"
        ));

        Set<Xref> interactionXrefs = new HashSet<>();
        interactionXrefs.add(new Xref(new CvTerm("db_shortname4", "db_identifier4"),
                "identifier4",
                new CvTerm("qualifier_shortname4", "qualifier_identifier4"),
                "ac4"
        ));

        //initializing annotations
        Set<Annotation> publicationAnnotations = new HashSet<>();

        //should be removed from everywhere
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname7", "MI:0957"),//full-coverage
                "description4"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname8", "MI:0959"),//imex curation
                "description5"));

        //should retain in publication
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname10", "MI:0634"),//contact email
                "description10"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname11", "MI:0878"),//author-submitted
                "description11"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname12", "MI:0612"),//comments
                "description12"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname13", "MI:0618"),//cautions
                "description13"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname14", "MI:0614"),//urls
                "description14"));

        //should retain in interaction
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname9", "db_identifier9"),
                "description9"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname15", "db_identifier15"),
                "description15"));


        Set<Annotation> experimentAnnotations = new HashSet<>();

        //should be removed from everywhere
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname6", "MI:0886"),//publication year
                "description1"));
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname7", "MI:0885"),//journal
                "description2"));
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname8", "MI:0636"),//authors
                "description3"));

        //should retain in interactions
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname9", "db_identifier9"),
                "description9"));
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname16", "db_identifier16"),
                "description16"));

        Set<Annotation> interactionAnnotations = new HashSet<>();
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname17", "db_identifier17"),
                "description17"));

        PublicationDetails publicationDetails = new PublicationDetails(null, null, null,
                null, null, publicationXrefs, publicationAnnotations);
        ExperimentDetails experimentDetails = new ExperimentDetails(null, null,
                null, null, experimentXrefs, experimentAnnotations);
        InteractionDetails interactionDetails = new InteractionDetails(null, null,
                null, interactionXrefs, interactionAnnotations, null, null, null,
                null);

        controller.shuffleDataBetweenModels(experimentDetails,
                publicationDetails, interactionDetails);

        //shuffled data test
        List<Xref> modifiedInteractionXrefs = interactionXrefs.stream().filter(xref ->
                xref.getDatabase().getIdentifier().equals("db_identifier3") ||
                        xref.getDatabase().getIdentifier().equals("db_identifier4")
        ).collect(Collectors.toList());
        Assert.assertEquals(2, modifiedInteractionXrefs.size());
        Assert.assertEquals(2, interactionXrefs.size());


        List<Xref> modifiedPublicationXrefs = publicationXrefs.stream().filter(xref ->
                xref.getDatabase().getIdentifier().equals("db_identifier1") ||
                        xref.getDatabase().getIdentifier().equals("db_identifier2")
        ).collect(Collectors.toList());
        Assert.assertEquals(2, modifiedPublicationXrefs.size());
        Assert.assertEquals(2, publicationXrefs.size());

        List<Annotation> modifiedPublicationAnnotations = publicationAnnotations.stream().filter(annotation ->
                annotation.getTopic().getIdentifier().equals("MI:0634") || //contact-email
                        annotation.getTopic().getIdentifier().equals("MI:0878") || //author-submitted
                        annotation.getTopic().getIdentifier().equals("MI:0612") || //comments
                        annotation.getTopic().getIdentifier().equals("MI:0618") || //cautions
                        annotation.getTopic().getIdentifier().equals("MI:0614")   //urls
        ).collect(Collectors.toList());

        Assert.assertEquals(5, modifiedPublicationAnnotations.size());
        Assert.assertEquals(5, publicationAnnotations.size());

        List<Annotation> modifiedInteractionAnnotations = interactionAnnotations.stream().filter(annotation ->
                annotation.getTopic().getIdentifier().equals("db_identifier9") ||
                        annotation.getTopic().getIdentifier().equals("db_identifier15") ||
                        annotation.getTopic().getIdentifier().equals("db_identifier16") ||
                        annotation.getTopic().getIdentifier().equals("db_identifier17")
        ).collect(Collectors.toList());

        Assert.assertEquals(4, modifiedInteractionAnnotations.size());
        Assert.assertEquals(4, interactionAnnotations.size());


    }
}
