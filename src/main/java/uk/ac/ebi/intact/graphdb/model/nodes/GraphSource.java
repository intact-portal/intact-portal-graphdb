package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import psidev.psi.mi.jami.model.Publication;
import psidev.psi.mi.jami.model.Source;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphSource extends GraphCvTerm implements Source {

    @GraphId
    private Long graphId;

    private String postalAddress;
    private String uniqueKey;
    private String ac;
    /*private Annotation url;*/
    private String url;

    @Relationship(type = RelationshipTypes.PUBLICATION)
    private GraphPublication publication;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private boolean forceHashCodeGeneration;

    public GraphSource() {
    }

    public GraphSource(Source source) {
        super(source,true);
        setForceHashCodeGeneration(true);
        setUrl(source.getUrl());
        setPostalAddress(source.getPostalAddress());
        setPublication(source.getPublication());
        setAc(CommonUtility.extractAc(source));
        setUniqueKey(createUniqueKey());

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if(!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.putAll(super.getNodeProperties());
            if (this.getPostalAddress() != null) nodeProperties.put("postalAddress", this.getPostalAddress());
            if (this.getUniqueKey() != null) nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getUrl() != null) nodeProperties.put("url", this.getUrl());


            Label[] labels = CommonUtility.getLabels(GraphSource.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(getPublication(), this.graphId, RelationshipTypes.PUBLICATION);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public Publication getPublication() {
        return publication;
    }

    @Override
    public void setPublication(Publication publication) {
        if (publication != null) {
            if (publication instanceof GraphCvTerm) {
                this.publication = (GraphPublication) publication;
            } else {
                this.publication = new GraphPublication(publication);
            }
        } else {
            this.publication = null;
        }
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }
/*
    public GraphSource(String shortName) {
        super(shortName);
    }

    public GraphSource(String shortName, Xref ontologyId) {
        super(shortName, ontologyId);
    }

    public GraphSource(String shortName, String fullName, Xref ontologyId) {
        super(shortName, fullName, ontologyId);
    }

    public GraphSource(String shortName, String url, String address, Publication bibRef) {
        super(shortName);
        this.setUrl(url);
        this.setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public GraphSource(String shortName, Xref ontologyId, String url, String address, Publication bibRef) {
        super(shortName, ontologyId);
        this.setUrl(url);
        this.setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public GraphSource(String shortName, String fullName, Xref ontologyId, String url, String address, Publication bibRef) {
        super(shortName, fullName, ontologyId);
        this.setUrl(url);
        this.setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public GraphSource(String shortName, String miId) {
        super(shortName, miId);
    }

    public GraphSource(String shortName, String fullName, String miId) {
        super(shortName, fullName, miId);
    }

    public GraphSource(String shortName, String miId, String url, String address, Publication bibRef) {
        super(shortName, miId);
        this.setUrl(url);
        this.setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public GraphSource(String shortName, String fullName, String miId, String url, String address, Publication bibRef) {
        super(shortName, fullName, miId);
        this.setUrl(url);
        this.setPostalAddress(address);
        this.bibRef = bibRef;
    }
*/

    /*protected void initialiseAnnotations() {
        this.initialiseAnnotationsWith(new DefaultSource.SourceAnnotationList());
    }*/

/*    public String getUrl() {
        return this.url != null?this.url.getValue():null;
    }*/


    public String getUrl() {
        return this.url != null ? this.url : null;
    }

    public void setUrl(String url) {
        /*DefaultSource.SourceAnnotationList sourceAnnotationList = (DefaultSource.SourceAnnotationList)this.getAnnotations();
        if(url != null) {
            CvTerm urlTopic = CvTermUtils.createMICvTerm("url", "MI:0614");
            if(this.url != null) {
                sourceAnnotationList.removeOnly(this.url);
            }

            this.url = new DefaultAnnotation(urlTopic, url);
            sourceAnnotationList.addOnly(this.url);
        } else if(!sourceAnnotationList.isEmpty()) {
            AnnotationUtils.removeAllAnnotationsWithTopic(sourceAnnotationList, "MI:0614", "url");
            this.url = null;
        }*/

        this.url = url;

    }

/*
    public String getPostalAddress() {
        return this.postalAddress != null?this.postalAddress.getValue():null;
    }
*/

    public String getPostalAddress() {
        return this.postalAddress != null ? this.postalAddress : null;
    }

    public void setPostalAddress(String address) {
        /*DefaultSource.SourceAnnotationList sourceAnnotationList = (DefaultSource.SourceAnnotationList)this.getAnnotations();
        if(address != null) {
            DefaultCvTerm addressTopic = new DefaultCvTerm("postaladdress");
            if(this.postalAddress != null) {
                sourceAnnotationList.removeOnly(this.postalAddress);
            }

            this.postalAddress = new DefaultAnnotation(addressTopic, address);
            sourceAnnotationList.addOnly(this.postalAddress);
        } else if(!sourceAnnotationList.isEmpty()) {
            AnnotationUtils.removeAllAnnotationsWithTopic(sourceAnnotationList, (String)null, "postaladdress");
            this.postalAddress = null;
        }*/

        this.postalAddress = postalAddress;

    }

   /* public Publication getPublication() {
        return this.bibRef;
    }

    public void setPublication(Publication ref) {
        this.bibRef = ref;
    }*/

/*
    protected void processAddedAnnotationEvent(Annotation added) {
        if(this.url == null && AnnotationUtils.doesAnnotationHaveTopic(added, "MI:0614", "url")) {
            this.url = added;
        } else if(this.postalAddress == null && AnnotationUtils.doesAnnotationHaveTopic(added, (String)null, "postaladdress")) {
            this.postalAddress = added;
        }

    }

    protected void processRemovedAnnotationEvent(Annotation removed) {
        if(this.url != null && this.url.equals(removed)) {
            this.url = AnnotationUtils.collectFirstAnnotationWithTopic(this.getAnnotations(), "MI:0614", "url");
        } else if(this.postalAddress != null && this.postalAddress.equals(removed)) {
            this.postalAddress = AnnotationUtils.collectFirstAnnotationWithTopic(this.getAnnotations(), (String)null, "postaladdress");
        }

    }
*/

/*    protected void clearPropertiesLinkedToAnnotations() {
        this.url = null;
        this.postalAddress = null;
    }

    private class SourceAnnotationList extends AbstractListHavingProperties<Annotation> {
        public SourceAnnotationList() {
        }

        protected void processAddedObjectEvent(Annotation added) {
            DefaultSource.this.processAddedAnnotationEvent(added);
        }

        protected void processRemovedObjectEvent(Annotation removed) {
            DefaultSource.this.processRemovedAnnotationEvent(removed);
        }

        protected void clearProperties() {
            DefaultSource.this.clearPropertiesLinkedToAnnotations();
        }
    }*/

    @Override
    public int hashCode() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "Source".hashCode();
        if (this.getShortName() != null) {
            hashcode = 31 * hashcode + this.getShortName().toLowerCase().hashCode();
        }
        return hashcode;
    }


    public String createUniqueKey(){
        return hashCode() + "";
    }

    @Override
    public boolean isForceHashCodeGeneration() {
        return forceHashCodeGeneration;
    }

    @Override
    public void setForceHashCodeGeneration(boolean forceHashCodeGeneration) {
        this.forceHashCodeGeneration = forceHashCodeGeneration;
    }
}
