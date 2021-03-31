package it.jump3.util;

import io.quarkus.panache.common.Sort;
import io.smallrye.common.constraint.Assert;
import io.vertx.core.http.HttpServerRequest;
import it.jump3.controller.model.PaginatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jboss.resteasy.spi.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class Utility {

    public static final String SPACE = " ";
    private static final char HIDE_CHAR = '*';
    private static final int LAST_CHARS_MOBILE_NUMBER = 4;
    private static final int LAST_CHARS_EMAIL = 2;
    public static final char START_JSON = '{';
    public static final char END_JSON = '}';
    public static final char START_LIST_JSON = '[';
    public static final char END_LIST_JSON = ']';

    public static final String FISCAL_CODE_REGEX = "(^$|^[a-zA-Z]{6}[a-zA-Z0-9]{2}[a-zA-Z][a-zA-Z0-9]{2}[a-zA-Z][a-zA-Z0-9]{3}[a-zA-Z]$)";
    public static final String FISCAL_PIVA_REGEX = "(^$|^[a-zA-Z]{6}[a-zA-Z0-9]{2}[a-zA-Z][a-zA-Z0-9]{2}[a-zA-Z][a-zA-Z0-9]{3}[a-zA-Z]$|^[0-9]{11}$)";
    public static final String PIVA_REGEX = "(^$|^[0-9]{11}$)";
    public static final String CAP_REGEX = "(^$|^[0-9]{5}$)";
    public static final String PROVINCE_REGEX = "(^$|^[a-zA-Z]{2}$)";
    //public static final String CIVIC_NUMBER_REGEX = "(^$|^[0-9]{1,3}[a-z]?$)";
    public static final String CIVIC_NUMBER_REGEX = "(^$|^[0-9]{1,3}\\/?[a-zA-z]?$)";
    public static final String CIVIC_NUMBER_INT_REGEX = "(^$|^[0-9]{1,3}$)";

    /**
     * Logica per nascondere il numero di telefono in output
     *
     * @param mobileNumber
     * @return
     */
    public static String hideMobileNumber(String mobileNumber) {

        if (StringUtils.isNotEmpty(mobileNumber)) {
            StringBuilder hideMobileNumber = new StringBuilder();
            int charToShow = mobileNumber.length() - LAST_CHARS_MOBILE_NUMBER;

            if (charToShow > 2) {
                for (int i = 0; i < mobileNumber.length(); i++) {

                    if (i > 1 && i < charToShow) {
                        hideMobileNumber.append(mobileNumber.charAt(i));
                    } else {
                        hideMobileNumber.append(HIDE_CHAR);
                    }
                }
            } else {
                return mobileNumber;
            }

            return hideMobileNumber.toString();
        } else
            return null;
    }

    /**
     * Logica per nascondere la mail in output
     *
     * @param email
     * @return
     */
    public static String hideEmail(String email) {

        if (StringUtils.isNotEmpty(email)) {

            String emailOut = email;

            try {
                StringBuilder hideEmail = new StringBuilder();
                int idxLastDot = email.lastIndexOf('.');
                int idxAt = email.indexOf('@');

                for (int i = 0; i < email.length(); i++) {

                    if ((i < (idxAt - LAST_CHARS_EMAIL) || i > (idxAt + LAST_CHARS_EMAIL)) &&
                            i < idxLastDot) {

                        hideEmail.append(HIDE_CHAR);
                    } else {
                        hideEmail.append(email.charAt(i));
                    }
                }

                emailOut = hideEmail.toString();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return emailOut;
        } else
            return null;
    }

    /**
     * Metodo per restituire una lista in Chunk
     *
     * @param completeList
     * @return
     */
    public static Collection<? extends List<?>> getChunkedList(List<?> completeList) {
        return getChunkedList(completeList, 1000);
    }

    public static Collection<? extends List<?>> getChunkedList(List<?> completeList, Integer size) {

        AtomicInteger counter = new AtomicInteger(0);
        return completeList.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size))
                .values();
    }

    public static int getTotalPages(Long count, Integer size) {
        int totalPages;
        if (count.intValue() % size == 0) {
            totalPages = count.intValue() / size;
        } else {
            totalPages = (count.intValue() / size) + 1;
        }
        return totalPages;
    }

    /**
     * Si occupa di produrre la IN CLAUSE come Stringa
     *
     * @param field
     * @param ids
     * @return
     */
    public static String appendIds(String field, Long... ids) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(field).append(" IN ( ");
        for (int i = 0; i < ids.length; i++) {
            if (i != 0) {
                queryBuilder.append(",");
            }
            queryBuilder.append(ids[i]);
        }
        queryBuilder.append(" ) ");

        return queryBuilder.toString();
    }

    public static String appendIds(String field, List<Long> idList) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(field).append(" IN ( ");

        boolean first = true;
        for (Long id : idList) {
            if (first) {
                first = false;
            } else {
                queryBuilder.append(",");
            }
            queryBuilder.append(id);
        }
        queryBuilder.append(" ) ");

        return queryBuilder.toString();
    }

    public static String joinListWithChar(List<String> list, String c) {
        return String.join(c, list);
    }

    public static String joinListWithCharAsJson(List<String> list, String c) {
        return START_LIST_JSON + String.join(c, list) + END_LIST_JSON;
    }

    public static String getBase64String(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }

    public static String convertListInString(List<?> list, String delimeter, String prefix, String suffix) {
        Assert.assertNotNull(prefix);
        Assert.assertNotNull(suffix);
        return list.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(delimeter, prefix, suffix));
    }

    public static String convertListInString(List<?> list, String delimiter) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(delimiter));
    }

    public static String convertListInString(List<?> list, String prefix, String suffix) {
        return convertListInString(list, ",", prefix, suffix);
    }

    public static String convertListInString(List<?> list) {
        return convertListInString(list, ",");
    }

    public static List<NameValuePair> splitQuery(String url) throws URISyntaxException {
        return URLEncodedUtils.parse(new URI(url), StandardCharsets.UTF_8);
    }

    public static String hashSetForLog(Set<?> set) {
        return !CollectionUtils.isEmpty(set) ? set.toString() : null;
    }

    public static String getIpClient(HttpServletRequest request) {
        String remoteAddr = EnvironmentConstants.Headers.IP_HEADERS.stream()
                .map(request::getHeader)
                .filter(Objects::nonNull)
                .filter(ip -> !ip.isEmpty() && !ip.equalsIgnoreCase("unknown"))
                .findFirst()
                .orElse(request.getRemoteAddr().split(",")[0]);
        return evaluateIpClient(remoteAddr);
    }

    public static String getIpClient(HttpRequest request) {
        List<String> headers = new ArrayList<>();
        request.getHttpHeaders().getRequestHeaders().forEach((k, v) -> {
            if (EnvironmentConstants.Headers.IP_HEADERS.contains(k)) {
                headers.addAll(v);
            }
        });
        String remoteAddr = headers.stream()
                .filter(Objects::nonNull)
                .filter(ip -> !ip.isEmpty() && !ip.equalsIgnoreCase("unknown"))
                .findFirst()
                .orElse(request.getRemoteAddress().split(",")[0]);
        return evaluateIpClient(remoteAddr);
    }

    public static HeaderData headerData(HttpRequest request, HttpServerRequest httpServerRequest) {
        HeaderData headerData = new HeaderData();
        headerData.setIpClient(Utility.getIpClient(request));
        headerData.setPort(httpServerRequest.remoteAddress().port());
        headerData.setUsername(request.getHttpHeaders().getHeaderString(EnvironmentConstants.Headers.HEADER_USERNAME));
        return headerData;
    }

    public static String evaluateIpClient(String ipClient) {
        return StringUtils.isEmpty(ipClient) ? "::1" : ipClient;
    }

    public static void setPaginatedResponse(PaginatedResponse response, Long count, Integer size) {
        int totalPages = getTotalPages(count, size);
        response.setTotalPages(totalPages);
        response.setTotalElements(count);
    }

    public static Sort getSortFromQuery(String sortQuery) {
        if (StringUtils.isEmpty(sortQuery)) return null;

        String[] querys = sortQuery.split(",");
        Sort sort = null;
        int count = 0;
        for (String query : querys) {
            query = query.trim();
            String[] querysCheck = query.split(";");
            for (String queryCheck : querysCheck) {
                if (count == 0) {
                    if (queryCheck.startsWith("-")) {
                        sort = Sort.by(queryCheck.substring(1), Sort.Direction.Descending);
                    } else if (queryCheck.contains(":")) {
                        String[] queryPart = queryCheck.split(":");
                        if (queryPart[1].trim().equalsIgnoreCase("desc")) {
                            sort = Sort.by(queryPart[0], Sort.Direction.Descending);
                        } else {
                            sort = Sort.by(queryPart[0]);
                        }
                    } else {
                        sort = Sort.by(queryCheck);
                    }
                } else {
                    if (queryCheck.startsWith("-")) {
                        sort = sort.and(queryCheck.substring(1), Sort.Direction.Descending);
                    } else if (queryCheck.contains(":")) {
                        String[] queryPart = queryCheck.split(":");
                        if (queryPart[1].trim().equalsIgnoreCase("desc")) {
                            sort = sort.and(queryPart[0], Sort.Direction.Descending);
                        } else {
                            sort = sort.and(queryPart[0]);
                        }
                    } else {
                        sort = sort.and(queryCheck);
                    }
                }
                count++;
            }
        }

        return sort;
    }

    public static String localeToString(Locale l) {
        return l.getLanguage() + "," + l.getCountry();
    }

    public static Locale stringToLocale(String s) {
        if (StringUtils.isNotEmpty(s)) {
            StringTokenizer tempStringTokenizer = new StringTokenizer(s, ",");
            String l, c;
            if (tempStringTokenizer.hasMoreTokens()) {
                l = tempStringTokenizer.nextToken();
                if (tempStringTokenizer.hasMoreTokens()) {
                    c = tempStringTokenizer.nextToken();
                    return new Locale(l, c);
                } else {
                    return new Locale(l);
                }
            }
            //return LocaleUtils.toLocale(s);
        }
        return null;
    }
}
