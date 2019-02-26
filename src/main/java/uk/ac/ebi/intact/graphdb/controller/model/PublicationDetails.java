package uk.ac.ebi.intact.graphdb.controller.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Elisabet Barrera
 */
public class PublicationDetails {

    private String pubmedId;
    private String title;
    private String journal;
    private List<String> authors;
    private Date publicationDate;
    private Collection<Xref> publicationXrefs;
    private Collection<Annotation> publicationAnnotations;

    public PublicationDetails(String pubmedId, String title, String journal, List<String> authors, Date publicationDate,
                              Collection<Xref> publicationXrefs, Collection<Annotation> publicationAnnotations) {
        this.pubmedId = pubmedId;
        this.title = title;
        this.journal = journal;
        this.authors = authors;
        this.publicationDate = publicationDate;
        this.publicationXrefs = publicationXrefs;
        this.publicationAnnotations = publicationAnnotations;
    }

    public String getPubmedId() {
        return pubmedId;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Collection<Xref> getPublicationXrefs() {
        return publicationXrefs;
    }

    public void setPublicationXrefs(Collection<Xref> publicationXrefs) {
        this.publicationXrefs = publicationXrefs;
    }

    public Collection<Annotation> getPublicationAnnotations() {
        return publicationAnnotations;
    }

    public void setPublicationAnnotations(Collection<Annotation> publicationAnnotations) {
        this.publicationAnnotations = publicationAnnotations;
    }
}

