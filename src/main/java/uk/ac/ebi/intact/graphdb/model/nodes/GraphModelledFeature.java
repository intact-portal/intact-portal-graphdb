package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphModelledFeature implements ModelledFeature {

    private String shortName;
    private String fullName;

    @Relationship(type = RelationshipTypes.INTERPRO)
    private GraphXref interpro;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.TYPE)
    private GraphCvTerm type;

    @Relationship(type = RelationshipTypes.RANGES)
    private Collection<GraphRange> ranges;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Relationship(type = RelationshipTypes.ROLE)
    private GraphCvTerm role;

    @Relationship(type = RelationshipTypes.PARTICIPANT_FEATURE, direction = Relationship.INCOMING)
    private GraphModelledEntity participant;

    @Relationship(type = RelationshipTypes.LINKED_FEATURES, direction = Relationship.UNDIRECTED)
    private Collection<GraphModelledFeature> linkedFeatures;


    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInterpro() {
        return this.interpro != null ? this.interpro.getId() : null;
    }

    /**
     * {@inheritDoc}
     */
    public void setInterpro(String interpro) {
        Collection<GraphXref> featureIdentifiers = getIdentifiers();

        // add new interpro if not null
        if (interpro != null) {
            CvTerm interproDatabase = CvTermUtils.createInterproDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old chebi if not null
            if (this.interpro != null) {
                featureIdentifiers.remove(this.interpro);
            }
            this.interpro = new GraphXref(new GraphXref(interproDatabase, interpro, identityQualifier));
            featureIdentifiers.add(this.interpro);
        }
        // remove all interpro if the collection is not empty
        else if (!featureIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(featureIdentifiers, Xref.INTERPRO_MI, Xref.INTERPRO);
            this.interpro = null;
        }
    }

    public Collection<GraphXref> getIdentifiers() {
        if (identifiers == null) {
            this.identifiers = new ArrayList<GraphXref>();
        }
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if (identifiers != null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
        } else {
            this.identifiers = new ArrayList<GraphXref>();
        }
    }

    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
            this.xrefs = new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    //TODO Review it shoudl use PublicationXrefList I guess
    public void setXrefs(Collection<Xref> xrefs) {
        if (xrefs != null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        } else {
            this.xrefs = new ArrayList<GraphXref>();
        }
    }

    public Collection<GraphAnnotation> getAnnotations() {
        if (this.annotations == null) {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
        return this.annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        if (annotations != null) {
            this.annotations = CollectionAdaptor.convertAnnotationIntoGraphModel(annotations);
        } else {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
    }

    public CvTerm getType() {
        return this.type;
    }

    public void setType(CvTerm type) {
        if (type != null) {
            if (type instanceof GraphCvTerm) {
                this.type = (GraphCvTerm) type;
            } else {
                this.type = new GraphCvTerm(type, false);
            }
        } else {
            this.type = null;
        }
    }

    public Collection<GraphRange> getRanges() {
        if (this.ranges == null) {
            this.ranges = new ArrayList<GraphRange>();
        }
        return this.ranges;
    }

    public void setRanges(Collection<Range> ranges) {
        if (ranges != null) {
            this.ranges = CollectionAdaptor.convertRangeIntoGraphModel(ranges, this.getUniqueKey());
        } else {
            this.ranges = new ArrayList<GraphRange>();
        }
    }

    public CvTerm getRole() {
        return this.role;
    }

    public void setRole(CvTerm role) {
        if (role != null) {
            if (role instanceof GraphCvTerm) {
                this.role = (GraphCvTerm) role;
            } else {
                this.role = new GraphCvTerm(role, false);
            }
        } else {
            this.role = null;
        }
    }

    public Collection<GraphAlias> getAliases() {
        if (aliases == null) {
            this.aliases = new ArrayList<GraphAlias>();
        }
        return this.aliases;
    }

    public void setAliases(Collection<Alias> aliases) {
        if (aliases != null) {
            this.aliases = CollectionAdaptor.convertAliasIntoGraphModel(aliases);
        } else {
            this.aliases = new ArrayList<GraphAlias>();
        }
    }

    @Override
    public ModelledEntity getParticipant() {
        return this.participant;
    }

    @Override
    public void setParticipant(ModelledEntity participant) {
        if (participant != null) {
            if (participant instanceof GraphModelledEntity) {
                this.participant = (GraphModelledEntity) participant;
            } else if (participant instanceof ModelledParticipant) {
                this.participant = new GraphModelledParticipant((ModelledParticipant) participant);
            }
        } else {
            this.participant = null;
        }
    }

    @Override
    public void setParticipantAndAddFeature(ModelledEntity participant) {
        if (this.participant != null) {
            this.participant.removeFeature(this);
        }

        if (participant != null) {
            participant.addFeature(this);
        }
    }

    public Collection<GraphModelledFeature> getLinkedFeatures() {
        if (this.linkedFeatures == null) {
            this.linkedFeatures = new ArrayList<GraphModelledFeature>();
        }
        return this.linkedFeatures;
    }

    public void setLinkedFeatures(Collection<ModelledFeature> linkedFeatures) {
        if (linkedFeatures != null) {
            this.linkedFeatures = CollectionAdaptor.convertModelledFeatureIntoGraphModel(linkedFeatures);
        } else {
            this.linkedFeatures = new ArrayList<GraphModelledFeature>();
        }
    }

}
