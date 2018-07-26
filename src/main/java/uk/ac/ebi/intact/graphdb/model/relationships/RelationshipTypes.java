package uk.ac.ebi.intact.graphdb.model.relationships;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 19/09/2014
 * Time: 16:48
 */
public class RelationshipTypes {


    public static final String INFERS = "INFERS";
    public static final String BIE_PARTICIPANT_A = "BIE_PARTICIPANTA";
    public static final String BIE_PARTICIPANT_B = "BIE_PARTICIPANTB";
    public static final String IE_PARTICIPANT="IE_PARTICIPANT";

    //Alias
    public static final String ALIASES = "aliases";
    public static final String SYNONYMS = "synonyms";
    public static final String TYPE = "type";
    public static final String TOPIC = "topic";

    //Xref
    public static final String XREFS = "xrefs";
    public static final String IDENTIFIERS = "identifiers";

    //Annotations
    public static final String ANNOTATIONS = "annotations";

    //BinaryInteractionEvidence
    public static final String INTERACTOR_A = "interactorA";
    public static final String INTERACTOR_B = "interactorB";
    public static final String COMPLEX_EXPANSION = "complexExpansion";

    //Parameters
    public static final String PARAMETERS = "parameters";

    //Ranges
    public static final String RANGES = "ranges";

    //Experiment
    public static final String PUB_EXP = "PUB_EXP";

    //Publication
    public static final String GRAPH_AUTHORS = "graphAuthors";

    //Feture
    public static final String DETECTION_METHOD = "detectionMethods";

    //Participant
    public static final String IDENTIFICATION_METHOD = "identificationMethods";
    public static final String EXPERIMENTAL_PREPARATION = "experimentaPreparations";

    //Entity
    public static final String INTERACTOR = "interactor";
    public static final String STOICHIOMETRY = "stoichiometry";
    public static final String CHANGE_LISTENER = "changeListener";

    //Experiment
    public static final String INTERACTION_DETECTION_METHOD = "interactionDetectionMethod";
    public static final String HOST_ORGANISM = "hostOrganism";

    //FeatureEvidence
    public static final String ROLE = "role";
    public static final String PARTICIPANT = "participant";

    //Gene
    public static final String ENSEMBL = "ensembl";
    public static final String ENSEMBL_GENOME = "ensemblGenome";
    public static final String ENTREZ_GENE_ID = "entrezGeneId";
    public static final String REFSEQ = "refseq";

    //InteractionEvidence
    public static final String EXPERIMENT = "experiment";
    public static final String INTERACTION_TYPE = "interactionType";
    public static final String HAS = "HAS";

    //Interactor
    public static final String ORGANISM = "organism";
    public static final String INTERACTOR_TYPE = "interactorType";
    public static final String CHECKSUMS = "checksums";
    public static final String INTERACTS_IN = "INTERACTS_IN";

    //NecleicAcid
    public static final String DDBJ_EMBL_GENBANK = "ddbjEmblGenbank";

    //Organism
    public static final String CELL_TYPE = "cellType";
    public static final String COMPARTMENT = "compartment";
    public static final String TISSUE =  "tissue";

    //Parameter
    public static final String VALUE = "value";

    //ParticipantEvidence
    public static final String EXPERIMENTAL_ROLE = "experimentalRole";
    public static final String BIOLOGICAL_ROLE = "biologicalRole";
    public static final String EXPRESSED_IN = "expressedIn";

    //Position
    public static final String STATUS = "status";

    //Protein
    public static final String UNIPROTKB = "uniprotkb";
    public static final String GENE_NAME = "geneName";
    public static final String ROGID = "rogid";

    //Publication
    public static final String GRAPH_CURATION_DEPTH = "graphCurationDepth";
    public static final String IMEX_ID = "imexId";
    public static final String SOURCE = "source";
    public static final String PMID = "pubmedId";
    public static final String DOI = "doi";

    //Range
    public static final String START = "start";
    public static final String END = "end";
    public static final String RESULTING_SEQUENCE = "resultingSequence";

    //Source
    public static final String PUBLICATION = "publication";
    public static final String UNIT = "unit";

    //VariableParameters
    public static final String VARIABLE_PARAMETER = "variableParameters";
    public static final String VARIABLE_VALUES = "variableValues";
    public static final String VARIABLE_PARAMETERS_VALUE_SETS = "variableParameterValueSets";

    //Xref
    public static final String DATABASE = "database";
    public static final String QUALIFIER = "qualifier";

    //TO SORT

    public static final String INTERACTIONS = "interactions";

    public static final String FEATURES = "features";
    public static final String LINKED_FEATURES = "linkedFeatures";

    public static final String CAUSAL_RELATIONSHIP = "causalRelationships";

    public static final String CONFIDENCE = "confidences";

    public static final String RELATION_TYPE = "relationType";
    public static final String TARGET = "target";
    public static final String METHOD = "method";

    public static final String INTERACTOR_PA = "interactorPA";
    public static final String INTERACTOR_PB = "interactorPB";
}
