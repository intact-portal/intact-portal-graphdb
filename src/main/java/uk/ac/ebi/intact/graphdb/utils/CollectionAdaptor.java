package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.Checksum;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphAlias;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphAnnotation;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphChecksum;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anjali on 22/11/17.
 */
public class CollectionAdaptor {


    public static Collection<GraphXref> convertXrefIntoGraphModel(Collection<Xref> xrefs){

        List<GraphXref> graphXrefs = xrefs.stream().map(GraphXref::new).collect(Collectors.toList());
        return graphXrefs;

    }

    public static Collection<GraphChecksum> convertChecksumIntoGraphModel(Collection<Checksum> checksums){

        List<GraphChecksum> graphChecksums = checksums.stream().map(GraphChecksum::new).collect(Collectors.toList());
        return graphChecksums;

    }

    public static Collection<GraphAlias> convertAliasIntoGraphModel(Collection<GraphAlias> aliases){

        List<GraphAlias> graphAliases = aliases.stream().map(GraphAlias::new).collect(Collectors.toList());
        return graphAliases;

    }

    public static Collection<GraphAnnotation> convertAnnotationIntoGraphModel(Collection<Annotation> annotations){

        List<GraphAnnotation> graphAnnotations = annotations.stream().map(GraphAnnotation::new).collect(Collectors.toList());
        return graphAnnotations;

    }
}
