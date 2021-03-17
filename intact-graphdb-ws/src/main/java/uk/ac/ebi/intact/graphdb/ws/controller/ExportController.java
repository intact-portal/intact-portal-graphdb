package uk.ac.ebi.intact.graphdb.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import psidev.psi.mi.jami.binary.expansion.InteractionEvidenceSpokeExpansion;
import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.ols.CachedOlsOntologyTermFetcher;
import psidev.psi.mi.jami.commons.MIWriterOptionFactory;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.datasource.InteractionWriter;
import psidev.psi.mi.jami.factory.InteractionWriterFactory;
import psidev.psi.mi.jami.json.InteractionViewerJson;
import psidev.psi.mi.jami.json.MIJsonOptionFactory;
import psidev.psi.mi.jami.json.MIJsonType;
import psidev.psi.mi.jami.model.ComplexType;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.tab.MitabVersion;
import psidev.psi.mi.jami.xml.PsiXmlVersion;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.service.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.ws.controller.model.InteractionExportFormat;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/export")
public class ExportController {

    //TODO temporary?
    private static final String UPLOADED_BATCH_FILE_PREFIX = "file_";
    private static final int FIRST_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 100;

    private final GraphInteractionService graphInteractionService;
    private final InteractionSearchService interactionSearchService;

    @Value("${server.upload.batch.file.path}")
    private String uploadBatchFilePath;

    @Autowired
    public ExportController(GraphInteractionService graphInteractionService, InteractionSearchService interactionSearchService) {
        this.graphInteractionService = graphInteractionService;
        this.interactionSearchService = interactionSearchService;

        PsiJami.initialiseAllInteractionWriters();
        InteractionViewerJson.initialiseAllMIJsonWriters();

    }

    //TODO Why binaryInteractionId AND interactorAc are arrays?
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/interaction/list")
    public ResponseEntity<StreamingResponseBody> exportInteraction(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "interactionDetectionMethodFilter", required = false) Set<String> interactionDetectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore", defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore", defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "binaryInteractionId[]", required = false) Set<Integer> binaryInteractionIdFilter,
            @RequestParam(value = "interactorAc[]", required = false) Set<String> interactorAcFilter,
            @RequestParam(value = "format", defaultValue = "json", required = false) String format) {

        //TODO Sort the code repetition

        InteractionExportFormat formatEnum = InteractionExportFormat.findByFormat(format);
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'." + formatEnum.getExtension() + "'").format(new Date());
        try {
            long results = interactionSearchService.countInteractionResult(
                    extractSearchTerms(query),
                    batchSearch,
                    interactorSpeciesFilter,
                    interactorTypeFilter,
                    interactionDetectionMethodFilter,
                    interactionTypeFilter,
                    interactionHostOrganismFilter,
                    isNegativeFilter,
                    minMiscore,
                    maxMiscore,
                    interSpecies,
                    binaryInteractionIdFilter,
                    interactorAcFilter);
            if (results == 0) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        StreamingResponseBody responseBody = response -> {

                InteractionWriter writer = createInteractionEvidenceWriterFor(formatEnum, response);

                Page<SearchInteraction> interactionIdentifiers;
                Pageable interactionsPage = PageRequest.of(FIRST_PAGE, DEFAULT_PAGE_SIZE);

                try {
                    writer.start();

                    // TODO check when we have the binary identifiers if we need to check from duplicated interactions. Probably not
                    do {
                        interactionIdentifiers = interactionSearchService.findInteractionIdentifiers(
                                extractSearchTerms(query),
                                batchSearch,
                                interactorSpeciesFilter,
                                interactorTypeFilter,
                                interactionDetectionMethodFilter,
                                interactionTypeFilter,
                                interactionHostOrganismFilter,
                                isNegativeFilter,
                                minMiscore,
                                maxMiscore,
                                interSpecies,
                                binaryInteractionIdFilter,
                                interactorAcFilter,
                                interactionsPage);

                        // do processing
                        Set<String> acs = new HashSet<>();
                        for (SearchInteraction interactionIdentifier : interactionIdentifiers) {
                            acs.add(interactionIdentifier.getAc());
                        }

                        Page<GraphInteractionEvidence> graphInteractionEvidences;
                        Pageable identifierPage = PageRequest.of(FIRST_PAGE, DEFAULT_PAGE_SIZE);


                        do {
                            graphInteractionEvidences = graphInteractionService.findByInteractionAcs(acs, identifierPage);
                            assert graphInteractionEvidences.getTotalElements() != 0;

                            for (GraphInteractionEvidence graphInteractionEvidence : graphInteractionEvidences) {
                                writer.write(graphInteractionEvidence);
                            }

                            //advance to next page
                            identifierPage = identifierPage.next();

                        } while (graphInteractionEvidences.hasNext());

                        //advance to next page
                        interactionsPage = interactionsPage.next();

                    } while (interactionIdentifiers.hasNext());

                    writer.end();

                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            };

        return ResponseEntity.ok()
                .contentType(formatEnum.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .header("X-Clacks-Overhead", "GNU Terry Pratchett") //In memory of Sir Terry Pratchett)
                .body(responseBody);

//        //For debugging
//        return ResponseEntity.ok()
//                .contentType(formatEnum.getContentType())
//                .header("X-Clacks-Overhead", "GNU Terry Pratchett") //In memory of Sir Terry Pratchett)
//                .body(responseBody);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/interaction/{ac}")
    public ResponseEntity<StreamingResponseBody> exportInteraction(@PathVariable String ac,
                                                                   @RequestParam(value = "format",
                                                                           defaultValue = "json",
                                                                           required = false) String format) {

        //TODO Find a way to avoid the sorting of the interaction. It is needed for comparison in the tests
        try {
            InteractionEvidence interactionEvidence = graphInteractionService.findByInteractionAcForMiJson(ac);
            if (interactionEvidence != null) {
                return ResponseEntity.notFound().build();
            }

            InteractionExportFormat formatEnum = InteractionExportFormat.findByFormat(format);

            StreamingResponseBody responseBody = response -> {
                InteractionWriter writer = createInteractionEvidenceWriterFor(formatEnum, response);
                try {
                    writer.start();
                    writer.write(interactionEvidence);
                    writer.end();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            };
            return ResponseEntity.ok()
                    .contentType(formatEnum.getContentType())
                    .header("X-Clacks-Overhead", "GNU Terry Pratchett") //In memory of Sir Terry Pratchett)
                    .body(responseBody);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    private InteractionWriter createInteractionEvidenceWriterFor(InteractionExportFormat format, Object output) {

        InteractionWriterFactory writerFactory = InteractionWriterFactory.getInstance();
        MIWriterOptionFactory optionFactory = MIWriterOptionFactory.getInstance();
        MIJsonOptionFactory miJsonOptionFactory = MIJsonOptionFactory.getInstance();

        InteractionWriter writer = null;

        switch (format) {
            /* For the XML formats we are going to write in expanded format (not compact) to ease the streaming */
            case MIXML25:
                writer = writerFactory.getInteractionWriterWith(optionFactory.getDefaultExpandedXmlOptions(output, InteractionCategory.evidence,
                        ComplexType.n_ary, PsiXmlVersion.v2_5_4));
                break;
            case MIXML30:
                writer = writerFactory.getInteractionWriterWith(optionFactory.getDefaultExpandedXmlOptions(output, InteractionCategory.evidence,
                        ComplexType.n_ary, PsiXmlVersion.v3_0_0));
                break;
            case MITAB25:
                writer = writerFactory.getInteractionWriterWith(optionFactory.getMitabOptions(output, InteractionCategory.evidence,
                        ComplexType.n_ary, new InteractionEvidenceSpokeExpansion(), true, MitabVersion.v2_5, false));
                break;
            case MITAB26:
                writer = writerFactory.getInteractionWriterWith(optionFactory.getMitabOptions(output, InteractionCategory.evidence,
                        ComplexType.n_ary, new InteractionEvidenceSpokeExpansion(), true, MitabVersion.v2_6, false));
                break;
            case MITAB27:
                writer = writerFactory.getInteractionWriterWith(optionFactory.getMitabOptions(output, InteractionCategory.evidence, ComplexType.n_ary,
                        new InteractionEvidenceSpokeExpansion(), true, MitabVersion.v2_7, false));
                break;
            case MIJSON:
                try {
                    writer = writerFactory.getInteractionWriterWith(miJsonOptionFactory.getJsonOptions(output, InteractionCategory.evidence, null,
                            MIJsonType.n_ary_only, new CachedOlsOntologyTermFetcher(), null));
                } catch (BridgeFailedException e) {
                    writer = writerFactory.getInteractionWriterWith(miJsonOptionFactory.getJsonOptions(output, InteractionCategory.evidence, null,
                            MIJsonType.n_ary_only, null, null));
                }
                break;
            default:
                //TODO Revisit the exception, potentially a default json or mitab default format should be better
                throw new IllegalArgumentException("The format " + format + " is not recognized");
        }
        return writer;
    }

    private String extractSearchTerms(String query) {

        StringBuilder searchTerms = new StringBuilder();

        if (query.startsWith(UPLOADED_BATCH_FILE_PREFIX)) {
            File uploadedBatchFile = new File(uploadBatchFilePath + File.separator + query);
            if (uploadedBatchFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(uploadedBatchFile));
                    String line;
                    int count = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (count > 0) {
                            searchTerms.append(",").append(line);
                        } else {
                            searchTerms = new StringBuilder(line);
                        }
                        count++;
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            searchTerms = new StringBuilder(query);
        }

        return searchTerms.toString();
    }
}
