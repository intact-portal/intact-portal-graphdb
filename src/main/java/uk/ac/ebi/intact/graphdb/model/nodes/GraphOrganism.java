package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.utils.comparator.organism.UnambiguousOrganismComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphOrganism implements Organism {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String scientificName;
    private String commonName;
    private int taxId;

    @Relationship(type = RelationshipTypes.CELL_TYPE)
    private GraphCvTerm cellType;

    @Relationship(type = RelationshipTypes.COMPARTMENT)
    private GraphCvTerm compartment;

    @Relationship(type = RelationshipTypes.TISSUE)
    private GraphCvTerm tissue;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Transient
    private boolean isAlreadyCreated;

    public GraphOrganism() {
    }

    public GraphOrganism(Organism organism) {
        setCommonName(organism.getCommonName());
        setScientificName(organism.getScientificName());
        setTaxId(organism.getTaxId());
        setCellType(organism.getCellType());
        setCompartment(organism.getCompartment());
        setTissue(organism.getTissue());
        setUniqueKey(this.toString());

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setAliases(organism.getAliases());

        if (CreationConfig.createNatively) {
            if(!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getScientificName() != null) nodeProperties.put("scientificName", this.getScientificName());
            if (this.getCommonName() != null) nodeProperties.put("commonName", this.getCommonName());
            nodeProperties.put("taxId", this.getTaxId());

            Label[] labels = CommonUtility.getLabels(GraphOrganism.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(cellType, this.graphId, RelationshipTypes.CELL_TYPE);
        CommonUtility.createRelationShip(compartment, this.graphId, RelationshipTypes.COMPARTMENT);
        CommonUtility.createRelationShip(tissue, this.graphId, RelationshipTypes.TISSUE);
        CommonUtility.createAliasRelationShips(aliases, this.graphId);
    }

    public GraphOrganism(int taxId) {
        if (taxId == -1 || taxId == -2 || taxId == -3 || taxId == -4 || taxId == -5 || taxId > 0) {
            this.taxId = taxId;
        } else {
            throw new IllegalArgumentException("The taxId " + taxId + " is not a valid taxid. Only NCBI taxid or -1, -2, -3, -4, -5 are valid taxids.");
        }
    }

    public GraphOrganism(int taxId, String commonName) {
        this(taxId);
        setCommonName(commonName);
    }

    public GraphOrganism(int taxId, String commonName, String scientificName) {
        this(taxId, commonName);
        setScientificName(scientificName);
    }

    public GraphOrganism(int taxId, CvTerm cellType, CvTerm tissue, CvTerm compartment) {
        this(taxId);
        setCellType(cellType);
        setCompartment(compartment);
        setTissue(tissue);
    }

    public GraphOrganism(int taxId, String commonName, CvTerm cellType, CvTerm tissue, CvTerm compartment) {
        this(taxId, commonName);
        setCellType(cellType);
        setCompartment(compartment);
        setTissue(tissue);
    }

    public GraphOrganism(int taxId, String commonName, String scientificName, CvTerm cellType, CvTerm tissue, CvTerm compartment) {
        this(taxId, commonName, scientificName);
        setCellType(cellType);
        setCompartment(compartment);
        setTissue(tissue);
    }

    public String getCommonName() {
        return this.commonName;
    }

    public void setCommonName(String name) {
        this.commonName = name;
    }

    public String getScientificName() {
        return this.scientificName;
    }

    public void setScientificName(String name) {
        this.scientificName = name;
    }

    public int getTaxId() {
        return this.taxId;
    }

    public void setTaxId(int id) {
        if (id == -1 || id == -2 || id == -3 || id == -4 || id == -5 || id > 0) {
            this.taxId = id;
        } else {
            throw new IllegalArgumentException("The taxId " + id + " is not a valid taxid. Only NCBI taxid or -1, -2, -3, -4, -5 are valid taxids.");
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

    public CvTerm getCellType() {
        return this.cellType;
    }

    public void setCellType(CvTerm cellType) {
        if (cellType != null) {
            if (cellType instanceof GraphCvTerm) {
                this.cellType = (GraphCvTerm) cellType;
            } else {
                this.cellType = new GraphCvTerm(cellType,false);
            }
        } else {
            this.cellType = null;
        }
    }

    public CvTerm getCompartment() {
        return this.compartment;
    }

    public void setCompartment(CvTerm compartment) {
        if (compartment != null) {
            if (compartment instanceof GraphCvTerm) {
                this.compartment = (GraphCvTerm) compartment;
            } else {
                this.compartment = new GraphCvTerm(compartment,false);
            }
        } else {
            this.compartment = null;
        }
    }

    public CvTerm getTissue() {
        return this.tissue;
    }

    public void setTissue(CvTerm tissue) {
        if (tissue != null) {
            if (tissue instanceof GraphCvTerm) {
                this.tissue = (GraphCvTerm) tissue;
            } else {
                this.tissue = new GraphCvTerm(tissue,false);
            }
        } else {
            this.tissue = null;
        }
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Organism)) {
            return false;
        }

        return UnambiguousOrganismComparator.areEquals(this, (Organism) o);
    }

    @Override
    public int hashCode() {
        return UnambiguousOrganismComparator.hashCode(this);
    }

    @Override
    public String toString() {
        String objName = "";
        if (getTaxId() != 0) {
            objName = objName + "Tax Id: " + getTaxId();
        }
        if (getCellType() != null && getCellType().getMIIdentifier() != null) {
            objName = objName + "Cell Type :" + getCellType().getMIIdentifier();
        }
        if (getTissue() != null && getTissue().getMIIdentifier() != null) {
            objName = objName + "Tissue :" + getTissue().getMIIdentifier();
        }
        if (getCompartment() != null && getCompartment().getMIIdentifier() != null) {
            objName = objName + "Compartment :" + getCompartment().getMIIdentifier();
        }

        return objName;
    }


}
