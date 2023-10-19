package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.List;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
public class PublicationDetails {

    private String pubmedId;
    private String title;
    private String journal;
    private List<String> authors;
    private String publicationDate;
    private Set<Xref> publicationXrefs;
    private Set<Annotation> publicationAnnotations;

    public PublicationDetails(String pubmedId, String title, String journal, List<String> authors, String publicationDate,
                              Set<Xref> publicationXrefs, Set<Annotation> publicationAnnotations) {
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

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Set<Xref> getPublicationXrefs() {
        return publicationXrefs;
    }

    public void setPublicationXrefs(Set<Xref> publicationXrefs) {
        this.publicationXrefs = publicationXrefs;
    }

    public Set<Annotation> getPublicationAnnotations() {
        return publicationAnnotations;
    }

    public void setPublicationAnnotations(Set<Annotation> publicationAnnotations) {
        this.publicationAnnotations = publicationAnnotations;
    }
}

