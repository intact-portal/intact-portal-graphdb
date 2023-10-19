package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.alias.UnambiguousAliasComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphAlias extends GraphDatabaseObject implements Alias {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;

    @Relationship(type = RelationshipTypes.TYPE)
    private GraphCvTerm type;

    private String name;

    @Transient
    private boolean isAlreadyCreated;

    public GraphAlias() {
    }

    public GraphAlias(Alias alias) {

        if (GraphEntityCache.cvTermCacheMap.get(alias.getType().getShortName()) != null) {
            type = (GraphEntityCache.cvTermCacheMap.get(alias.getType().getShortName()));
        } else {
            setType(alias.getType());
        }

        setName(alias.getName());
        setAc(CommonUtility.extractAc(alias));
        setUniqueKey(createUniqueKey(alias));

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }

    }

    public GraphAlias(CvTerm type, String name) {
        this(name);
        setType(type);
    }

    public GraphAlias(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The alias name is required and cannot be null");
        }
        setName(name);
    }

    public void createNodeNatively() {

        BatchInserter batchInserter = CreationConfig.batchInserter;

        Map<String, Object> nodeProperties = new HashMap<String, Object>();
        nodeProperties.put("uniqueKey", this.getUniqueKey());
        if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
        if (this.getName() != null) nodeProperties.put("name", this.getName());

        Label[] labels = CommonUtility.getLabels(GraphAlias.class);

        NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
        setGraphId(nodeDataFeed.getGraphId());
        setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
    }

    private void createRelationShipNatively() {
        CommonUtility.createRelationShip(type, this.getGraphId(), RelationshipTypes.TYPE);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public CvTerm getType() {
        return type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Alias)) {
            return false;
        }

        return UnambiguousAliasComparator.areEquals(this, (Alias) o);
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        return getName() + (this.type != null ? "(" + this.type.toString() + ")" : "");
    }

    public String createUniqueKey(Alias alias) {
        return UniqueKeyGenerator.createAliasKey(alias);
    }


    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

}
