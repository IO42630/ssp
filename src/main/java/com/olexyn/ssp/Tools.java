package com.olexyn.ssp;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {


    public static byte[] injectHost(byte[] message, String host) {

        String payload = new String(message, StandardCharsets.UTF_8);
        List<List<String>> matches = matchRegEx(payload, "Host: (.+)\r\n");
        String match = matches.get(0).get(1);

       payload = payload.replace(match, host);

        return payload.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] injectLocation(byte[] message, String location) {

        String payload = new String(message, StandardCharsets.UTF_8);
        System.out.println("Old Payload \n"+payload+ "\n");
        List<List<String>> matches = matchRegEx(payload, "Location: (.+)\r\n");

        try{
            String match = matches.get(0).get(1);
            payload = payload.replace(match, location);
        }catch (NullPointerException  | IndexOutOfBoundsException ignored){

        }



        return payload.getBytes(StandardCharsets.UTF_8);
    }





    /**
     * This method returns a List of "Matches".
     * Here a "Match" is a List of String.
     * The first entry of a "Match" is the Full match.
     * The second entry of a "Match" is the first Group.
     * The third entruy is the second Group, and so on.
     *
     * @param input input String
     * @param regex pattern String
     * @return matches for pattern, separated by \n
     */
    public static List<List<String>> matchRegEx(String input, String regex) {
        // Text: <ul><li>first</li><li>second</li><li>third</li></ul>
        // Regex: <li>(.*?)<\/li>
        // Match 1 Entry 0: <li>first</li>
        // Match 1 Entry 1: first
        // Match 1 Entry 0: <li>first</li>
        // Match 1 Entry 1: first


        List<List<String>> matches = new ArrayList<>();

        // guess the number of groups
        int groupN = regex.split("\\(").length;


        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        while (m.find()) {
            List<String> match = new ArrayList<>();

            for (int i = 0; i < groupN; i++) {
                try {
                    match.add(m.group(i));
                } catch (Exception ignored) {}
            }
            matches.add(match);
        }

        return matches;
    }
}


