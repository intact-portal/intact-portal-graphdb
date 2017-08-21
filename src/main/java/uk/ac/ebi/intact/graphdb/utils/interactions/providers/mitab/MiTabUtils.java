package uk.ac.ebi.intact.graphdb.utils.interactions.providers.mitab;

import psidev.psi.mi.tab.model.builder.MitabParserUtils;
import psidev.psi.mi.tab.utils.MitabEscapeUtils;
import uk.ac.ebi.intact.graphdb.model.nodes.CvParam;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 26/09/2014
 * Time: 19:00
 */
public class MiTabUtils {

    private static final String MI_PREFIX = "MI";
    private static final String FIELD_DELIMITER = "|";
    private static final String EMPTY_COLUMN = "-";
    private static final String UNKNOWN = "unknown";

    /**
         * @param collection The values can not be null.
         * @return
         */
        public static String joinCrossReferencStyleCollection(Collection<CvParam> collection) {
                StringBuilder sb = new StringBuilder();
                if (collection != null && !collection.isEmpty()) {

                    Iterator<CvParam> iterator = collection.iterator();

                        while (iterator.hasNext()) {
                            CvParam field = iterator.next();
                            if (field.getCvLabel() == null) {
                                field.setCvLabel(UNKNOWN);
                            }
                            sb.append(joinAttributes(field.getCvLabel(), field.getAccession(), field.getName()));

                            if (iterator.hasNext()) {
                                    sb.append(FIELD_DELIMITER);
                            }
                        }
                } else {
                        sb.append('-');
                }
                return sb.toString();
        }

        public static String joinAttributes(String type, String value, String description) {
                StringBuilder sb = new StringBuilder();

                boolean psiCvTerm = false;

                //Empty column
                if (type == null && value == null && description == null) {
                        return EMPTY_COLUMN;
                }

                if (type != null) {
                        if (MI_PREFIX.equals(type)) {
                                type = "psi-mi";
                                psiCvTerm = true;
                        }
                        sb.append(MitabEscapeUtils.escapeFieldElement(type));
                        sb.append(':');

                }

                if (value != null) {
                        if (type == null && description == null) {
                                sb.append(MitabEscapeUtils.escapeFieldElement(value));
                        } else {
                                if (psiCvTerm) {
                                        sb.append(MitabEscapeUtils.escapeFieldElement(MI_PREFIX + ':' + value));

                                } else {
                                        sb.append(MitabEscapeUtils.escapeFieldElement(value));
                                }
                        }
                }

                if (description != null) {
                        sb.append('(');
                        sb.append(MitabEscapeUtils.escapeFieldElement(description));
                        sb.append(')');
                }

                return sb.toString();
        }

    public static Set<CvParam> splitCrossReferences(String column) throws IllegalArgumentException {

        Set<CvParam> objects = new HashSet<>();
        CvParam object = null;

        if (column != null && !column.isEmpty()) {

            String[] fields = MitabParserUtils.quoteAwareSplit(column, new char[]{'|'}, false);
            for (String field : fields) {
                if (field != null) {

                    String[] result = MitabParserUtils.quoteAwareSplit(field, new char[]{':', '(', ')'}, true);
                    if (result != null) {

                        int length = result.length;

                        // some exception handling
                        if (length == 0 || length > 3) {
                            throw new IllegalArgumentException("String cannot be parsed to create a cross reference (check the syntax): " + Arrays.asList(result).toString());
                        }

                        if (length == 1) {
                            //Backward compatibility
                            if (field.equalsIgnoreCase("spoke")) {
                                object = new CvParam("psi-mi", "MI:1060", "spoke expansion", null);
                            } else if (field.equalsIgnoreCase("matrix")) {
                                object = new CvParam("psi-mi", "MI:1061", "matrix expansion", null);
                            } else if (field.equalsIgnoreCase("bipartite")) {
                                object = new CvParam("psi-mi", "MI:1062", "bipartite expansion", null);
                                                        } else if (!result[0].equalsIgnoreCase("-")) {
                                throw new IllegalArgumentException("String cannot be parsed to create a cross reference (check the syntax): " + Arrays.toString(result));
                            }
                        } else if (length == 2) {
                            object = new CvParam(result[0], result[1], null, null);
                        } else if (length == 3) {
                            object = new CvParam(result[0], result[1], result[2], null);
                        }

                        if (object != null) {
                            objects.add(object);
                        }
                    }
                }
            }
        }
        return objects;
    }
}
