package com.company;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Random;

public class TaxNumberGenerator {

    @NotNull
    public static String concatenateDigits(int... digits) {
        StringBuilder resultDigit = new StringBuilder(digits.length);
        for (int digit : digits) {
            resultDigit.append(digit);
        }
        return resultDigit.toString();
    }


    // NOTE: numbers of regions are ordered from 1 to 79, others (UNORDERED_REGIONS_NUMBERS) are not
    @NotNull
    public static ArrayList getRegionNumbers() {

        ArrayList regionNumbersList = new ArrayList();
        int lastRegionOrderedNumber = 79;
        for (int i = 1; i < lastRegionOrderedNumber; i++) {
            regionNumbersList.add(i);
        }
        Integer[] UNORDERED_REGIONS_NUMBERS = {83, 86, 87, 89, 99};

        for (int i = 0; i < UNORDERED_REGIONS_NUMBERS.length; ++i) {
            regionNumbersList.add(UNORDERED_REGIONS_NUMBERS[i]);
        }
        return regionNumbersList;
    }

    public static String getRandomRegionNumber(ArrayList regions) {
        int position = new Random().nextInt(regions.size());
        Integer regionNumber = (Integer)regions.get(position);
        return regionNumber.toString();
    }

    @NotNull
    // NOTE: Tax inspection number consists of two digits in range of [1 (minValue), 99 (maxValue)]
    // ... the method generates digits separately to avoid one-digit value
    public static String getTaxInspectionNumber() {
        int minValue = 1;
        int maxValue = 9;
        int firstDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int secondDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        String taxInspectionNumber = concatenateDigits(firstDigit, secondDigit);
        return taxInspectionNumber;
    }

    @NotNull
    //  NOTE: Tax number in Government register consists of five digits, each generates ...
    // ... separately in range of [1 (minValue), 99 (maxValue)]
    public static String getTaxNumberInRegister() {
        int minValue = 1;
        int maxValue = 9;
        int firstDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int secondDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int thirdDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int fourthDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        int fifthDigit = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        String taxInspectionNumber = concatenateDigits(firstDigit, secondDigit, thirdDigit, fourthDigit, fifthDigit);
        return taxInspectionNumber;

    }

    @NotNull
    public static String getControlDigitNumber() {
        int minValue = 1;
        int maxValue = 9;
        int value = ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
        String controlValue = concatenateDigits(value);
        return controlValue;
    }

    // NOTE: Individual tax number structure: [region number (2 digits)]
    // ...                            [tax inspection number (2 digits)]
    // ...                           [tax number in register (5 digits)]
    // ...                                    [control number (1 digit)]

    @NotNull
    public static String generateIndividualTaxNumber() {
        ArrayList regionNumbers = getRegionNumbers();
        String regionNumber = getRandomRegionNumber(regionNumbers);
        String taxInspectionNumber = getTaxInspectionNumber();
        String taxNumberInRegister = getTaxNumberInRegister();
        String controlNumber = getControlDigitNumber();
        String individualTaxNumber = regionNumber + taxInspectionNumber + taxNumberInRegister + controlNumber;
        return individualTaxNumber;
    }

    public static void main(String[] args) {
        String individualTaxNumber = generateIndividualTaxNumber();
        System.out.println(individualTaxNumber);
    }
}
