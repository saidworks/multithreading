package com.saidworks.challenges.athenahealth;

import java.util.*;
import java.util.stream.*;


class Result {

    /*
     * Complete the 'processData' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. 2D_STRING_ARRAY patients
     *  2. 2D_STRING_ARRAY claims
     *  3. 2D_STRING_ARRAY charges
     */

    public static List<Integer> processData(List<List<String>> patients, List<List<String>> claims, List<List<String>> charges) {
        /*
        each patient has one or multiple claims
        and each claim has one or multiple charges
        */
        // map to list of objects
        List<Charge> chargesObjects = mapToCharges(charges);
        List<Claim>  claimsObjects = mapToClaims(claims);

        // Step 1: Group charges by claimId and sum amounts
        // Returns: Map<Integer, Integer> where key=claimId, value=total charge amount
        // Uses summingInt() NOT summarizingInt() - returns int directly
        var chargesByClaimId = chargesObjects.stream()
                .collect(Collectors.groupingBy(
                        Charge::claimId, 
                        Collectors.summingInt(Charge::amount)  // Returns int, not IntSummaryStatistics
                ));

        // Step 2: Group claims by patientId, filter for multiple claims, and sum their charges
        // For each patient with 2+ claims, calculate total charge amount across all their claims
        return claimsObjects.stream()
                .collect(Collectors.groupingBy(Claim::patientId))  // Map<String, List<Claim>>
                .values().stream()
                .filter(claimList -> claimList.size() > 1)  // Only patients with multiple claims
                .map(claimList -> claimList.stream()
                        // Convert each claim to its total charge amount (creates IntStream)
                        .mapToInt(claim -> chargesByClaimId.getOrDefault(claim.claimId, 0))
                        // IMPORTANT: Must call .sum() here to convert IntStream to int
                        .sum())  // Returns int primitive, not IntStream or IntSummaryStatistics
                .collect(Collectors.toList());
        





    }
    public static List<Patient> mapToPatients(List<List<String>> patients)  {
        return patients.stream().map(list
                ->
                new Patient(list.get(0), Integer.parseInt(list.get(1)))).sorted().toList();

    }
    public static List<Claim> mapToClaims(List<List<String>> claims)  {
        return claims.stream().map(list
                ->
                new Claim(Integer.parseInt(list.getFirst()), list.get(1),list.get(2))).toList();

    }
    public static List<Charge> mapToCharges(List<List<String>> charges)  {
        return charges.stream().map(list
                ->
                new Charge(Integer.parseInt(list.getFirst()), Integer.parseInt(list.get(1)),Integer.parseInt(list.get(2)),list.get(3))).toList();

    }
    public record Patient(String patientId, int age){}
    public record Claim(int claimId, String patientId, String placeOfString){}
    public record Charge(int chargeId, int claimId, int amount, String procedureCode){}

}
