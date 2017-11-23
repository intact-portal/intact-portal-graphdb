package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

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

    public static Collection<GraphParameter> convertParameterIntoGraphModel(Collection<Parameter> parameters){

        List<GraphParameter> graphParameters = parameters.stream().map(GraphParameter::new).collect(Collectors.toList());
        return graphParameters;

    }

    public static Collection<GraphConfidence> convertConfidenceIntoGraphModel(Collection<Confidence> confidences){

        List<GraphConfidence> graphConfidences = confidences.stream().map(GraphConfidence::new).collect(Collectors.toList());
        return graphConfidences;

    }

    public static Collection<GraphVariableParameterValueSet> convertvariableParameterValueIntoGraphModel(Collection<VariableParameterValueSet> variableParameterValueSets){

        List<GraphVariableParameterValueSet> graphVariableParameterValueSet = variableParameterValueSets.stream().map(GraphVariableParameterValueSet::new).collect(Collectors.toList());
        return graphVariableParameterValueSet;

    }
}
