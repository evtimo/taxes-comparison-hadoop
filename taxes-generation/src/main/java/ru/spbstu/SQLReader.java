package ru.spbstu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

class NoNameQueryException extends Exception {

    public NoNameQueryException(String msg) {
        super(msg);
    }

}

public class SQLReader {

    private static HashMap<String, String> mapOfQueries = null;

    public SQLReader() {
        mapOfQueries = new HashMap<String, String>();
    }

    /**
     * Method finds in input string $-end and $-start symbols,
     * gets substring between these positions
     * In substring deletes all spaces and returns real name of SQL query
     * that user added by himself in input file
     * @param query
     * @return query name
     * @throws NoNameQueryException
     */
    private String getQueryName(StringBuffer query) throws NoNameQueryException {

        String queryName;
        int indexOfNameStart = query.indexOf("$");
        int indexOfNameEnd = query.lastIndexOf("$");

        if (indexOfNameStart != -1 && indexOfNameEnd != -1) {
            queryName = query.substring(indexOfNameStart + 1, indexOfNameEnd);
            query.delete(indexOfNameStart, indexOfNameEnd + 1);
            return queryName.replace(" ", "");
        } else {
            throw new NoNameQueryException("There is no name for this query in file!");
        }

    }

    /**
     * @param path Path to the SQL file
     * @return list of query strings
     */
    public Map<String, String> readQueries(String path) {
        String queryLine;
        StringBuffer sBuffer = new StringBuffer();


        try {
            FileReader fr = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(fr);
            while ((queryLine = br.readLine()) != null) {
                int indexOfCommentSign = queryLine.indexOf('#');
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("#")) {
                        queryLine = new String("");
                    } else
                        queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
                }
                indexOfCommentSign = queryLine.indexOf("--");
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("--")) {
                        queryLine = new String("");
                    } else
                        queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
                }
                indexOfCommentSign = queryLine.indexOf("/*");
                if (indexOfCommentSign != -1) {
                    if (queryLine.startsWith("#")) {
                        queryLine = new String("");
                    } else
                        queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));

                    sBuffer.append(queryLine + " ");
                    do {
                        queryLine = br.readLine();
                    }
                    while (queryLine != null && !queryLine.contains("*/"));
                    indexOfCommentSign = queryLine.indexOf("*/");
                    if (indexOfCommentSign != -1) {
                        if (queryLine.endsWith("*/")) {
                            queryLine = new String("");
                        } else
                            queryLine = new String(queryLine.substring(indexOfCommentSign + 2, queryLine.length() - 1));
                    }
                }

                if (queryLine != null)
                    sBuffer.append(queryLine + " ");
            }
            br.close();

            String[] splittedQueries = sBuffer.toString().split(";");

            try {
                for (int i = 0; i < splittedQueries.length; i++) {

                    StringBuffer query = new StringBuffer(splittedQueries[i]);

                    if (!splittedQueries[i].trim().equals("") && !splittedQueries[i].trim().equals("\t")) {
                        mapOfQueries.put(getQueryName(query), query.toString());
                    }
                }

            } catch (NoNameQueryException e) {
                System.out.println("*** Error : " + e.toString());
            }
        } catch (Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println(sBuffer.toString());
        }
        return mapOfQueries;
    }

}
