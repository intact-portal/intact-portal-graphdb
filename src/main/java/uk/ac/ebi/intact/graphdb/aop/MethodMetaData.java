package uk.ac.ebi.intact.graphdb.aop;

import org.neo4j.ogm.annotation.Relationship;

/**
 * Created by anjali on 13/08/19.
 */
public class MethodMetaData {

    private Relationship relationship;
    private Class fieldType;


    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }
}
