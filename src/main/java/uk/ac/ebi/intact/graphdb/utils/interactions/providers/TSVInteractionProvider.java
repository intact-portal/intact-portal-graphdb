package uk.ac.ebi.intact.graphdb.utils.interactions.providers;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.ac.ebi.intact.graphdb.error.GraphDbException;
import uk.ac.ebi.intact.graphdb.model.nodes.Protein;
import uk.ac.ebi.intact.graphdb.utils.InteractionProvider;
import uk.ac.ebi.intact.graphdb.utils.interactions.providers.mitab.MiTabUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 11/09/2014
 * Time: 09:00
 */
@Component
public class TSVInteractionProvider implements InteractionProvider {

    private static final Logger log = LoggerFactory.getLogger(TSVInteractionProvider.class);
    private CSVReader reader;

    @Value( "${psicquic.recording.dirName}/interaction_4932.tsv" )
    private String fileName = "20121128_2251_4932/interaction_4932.tsv";

    public TSVInteractionProvider() {
        try {
            reader = new CSVReader(new FileReader(fileName), '\t', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, 1);
        } catch (FileNotFoundException e) {
            log.error("The file " + fileName + " cannot be opened");
        }

    }

    public Iterator getInteractions(Set<Protein> proteins, String species) throws GraphDbException {
        return null;
    }

    @Override
    public Iterator getInteractions() throws GraphDbException {

        String[] nextLine;

        List<Interaction> interactions = new ArrayList<Interaction>();

        try {
            while ((nextLine = reader.readNext()) != null) {
                interactions.add(
                        new Interaction(
                                new Interactor(nextLine[Columns.IDA.ordinal()]),
                                new Interactor(nextLine[Columns.IDB.ordinal()]),
                                Double.parseDouble(nextLine[Columns.SCORE.ordinal()]),
                                MiTabUtils.splitCrossReferences(nextLine[Columns.PUBLICATIONS.ordinal()]),
                                MiTabUtils.splitCrossReferences(nextLine[Columns.DETECTIONMETHODS.ordinal()]),
                                MiTabUtils.splitCrossReferences(nextLine[Columns.INTERACTIONTYPES.ordinal()])
                        )
                );
            }
        } catch (IOException e) {
            throw new GraphDbException(e.getMessage());
        }
        return interactions.iterator();    }

    /**
     * Assume that the file with the correct input for the species is provided
     */
    public Iterator getInteractome(String species) throws GraphDbException {

        String[] nextLine;

        List<Interaction> interactions = new ArrayList<Interaction>();

        try {
            while ((nextLine = reader.readNext()) != null) {
                interactions.add(
                        new Interaction(
                                new Interactor(nextLine[Columns.IDA.ordinal()], Integer.parseInt(species)),
                                new Interactor(nextLine[Columns.IDB.ordinal()], Integer.parseInt(species)),
                                Double.parseDouble(nextLine[Columns.SCORE.ordinal()]),
                                MiTabUtils.splitCrossReferences(nextLine[Columns.PUBLICATIONS.ordinal()]),
                                MiTabUtils.splitCrossReferences(nextLine[Columns.DETECTIONMETHODS.ordinal()]),
                                MiTabUtils.splitCrossReferences(nextLine[Columns.INTERACTIONTYPES.ordinal()])
                        )
                );
            }
        } catch (IOException e) {
            throw new GraphDbException(e.getMessage());
        }
        return interactions.iterator();
    }

    private enum Columns {
        IDA,
        IDB,
        SCORE,
        PUBLICATIONS,
        DETECTIONMETHODS,
        INTERACTIONTYPES
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
