package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.utils.comparator.organism.UnambiguousOrganismComparator;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class GraphOrganism implements Organism {

    @GraphId
    protected Long graphId;

    private String commonName;
    private String scientificName;
    private int taxId;
    private Collection<GraphAlias> aliases;
    private GraphCvTerm cellType;
    private GraphCvTerm compartment;
    private GraphCvTerm tissue;

    public GraphOrganism(Organism organism) {
        setCommonName(organism.getCommonName());
        setScientificName(organism.getScientificName());
        setTaxId(organism.getTaxId());
        setAliases(organism.getAliases());
        setCellType(organism.getCellType());
        setCompartment(organism.getCompartment());
        setTissue(organism.getTissue());
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
        if (taxId == -1 || taxId == -2 || taxId == -3 || taxId == -4 || taxId == -5 || taxId > 0) {
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
                this.cellType = new GraphCvTerm(cellType);
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
                this.compartment = new GraphCvTerm(compartment);
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
                this.tissue = new GraphCvTerm(tissue);
            }
        } else {
            this.tissue = null;
        }
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
        return "Organism: " + getTaxId() + "(" + (getCommonName() != null ? getCommonName() : "-") + ")";
    }
}
