// Project 2 Question 2
//Implemention of Lrage Integer Matrix multiplication algorithm

import java.util.Random;

public class gayke_v_pa2_lim {
    
    //Calculate two randomly generated integers (A and B) using original 0divide-and-conquer algorithm as n/2
    private static String funLargeIntegerMultiplyn2(String A, String B) {
        // Append A and B with leading zeros to make their lengths equal
        int maxLength = Math.max(A.length(), B.length());
        int extraPadding = maxLength%2;
        A = paddingZeros(A, maxLength+extraPadding);
        B = paddingZeros(B, maxLength+extraPadding);
        int n = A.length();
        // Base case: if n is 1, perform single-digit multiplication
        if (n <= 2) {
            return Long.toString(Long.parseLong(A) * Long.parseLong(B));
        
        }
        // Divide A and B into two parts as one and two
        int m = n / 2;
        String Aone = A.substring(0, m);
        String Atwo = A.substring(m);
        String Bone = B.substring(0, m);
        String Btwo = B.substring(m);

        // Recursively calculate the products of two parts
        String prod1 = funLargeIntegerMultiplyn2(Aone, Bone);
        String prod2 = funLargeIntegerMultiplyn2(Atwo, Btwo);
        String prod3 = funLargeIntegerMultiplyn2(funAdd(Aone, Atwo), funAdd(Bone, Btwo));

        // Calculate the final result 
        String prod4 = funSubstract(funSubstract(prod3, prod1), prod2);
        String part1 = leftShift(prod1, n);
        String part2 = leftShift(prod4, m);

        return funAdd(funAdd(part1, part2), prod2);
    }

    // Calculate two randomly generated integers using the modified divide-and-conquer algorithm as n/3
    private static String funLargeIntegerMultiplyn3(String A, String B) {
        int n = A.length();

        // Append A and B with leading zeros to make their lengths equal
        int maxLength = Math.max(A.length(), B.length());
        int extraPadding = 0;
        if(maxLength%3==1){
            extraPadding =2;
        }else{
            if(maxLength%3==2) {
                extraPadding = 1;
            }
        }
        A = paddingZeros(A, maxLength+extraPadding);
        B = paddingZeros(B, maxLength+extraPadding);
        n = A.length();
        // Base case: if n is 3 or less, perform single-digit multiplication
        if (n <= 3) {
            long multiply = Long.parseLong(A) * Long.parseLong(B);
            return Long.toString(multiply);
        }

        // Divide A and B into three parts as one, two and three
        int m = n / 3;
        String Aone = A.substring(0, m);
        String Atwo = A.substring(m,n-m);
        String Athree = A.substring(n-m, n);
        String Bone = B.substring(0, m);
        String Btwo = B.substring(m,n-m);
        String Bthree = B.substring(n-m,n);

        // Calculate the final result
        String part1 = leftShift(funLargeIntegerMultiplyn3(Aone, Bone),4*m);
        String part2 = leftShift(funLargeIntegerMultiplyn3(Aone, Btwo), 3*m);
        String part3 = leftShift(funLargeIntegerMultiplyn3(Aone, Bthree),2*m);
        String part4 = leftShift(funLargeIntegerMultiplyn3(Atwo, Bone),3*m);
        String part5 = leftShift(funLargeIntegerMultiplyn3(Atwo, Btwo),2*m);
        String part6 = leftShift(funLargeIntegerMultiplyn3(Atwo, Bthree),m);
        String part7 = leftShift(funLargeIntegerMultiplyn3(Athree, Bone),2*m);
        String part8 = leftShift(funLargeIntegerMultiplyn3(Athree, Btwo),m);
        String part9 = funLargeIntegerMultiplyn3(Athree, Bthree);
        return  funAdd(funAdd(funAdd(funAdd(funAdd(funAdd(funAdd(funAdd(part1,part2), part3), part4), part5), part6),part7),part8),part9);
    }

    // Function to add two integers
    private static String funAdd(String A, String B) {
        int lenA = A.length();
        int lenB = B.length();
        int maxLength = Math.max(lenA, lenB);
        A = paddingZeros(A, maxLength);
        B = paddingZeros(B, maxLength);

        int carry = 0;
        StringBuilder res = new StringBuilder();
        for (int i = maxLength - 1; i >= 0; i--) {
            int sum = carry + (A.charAt(i) - '0') + (B.charAt(i) - '0');
            carry = sum / 10;
            res.insert(0, sum % 10);
        }

        if (carry > 0) {
            res.insert(0, carry);
        }

        return res.toString();
    }

    // Function to substract two integers
    private static String funSubstract(String A, String B) {
        int lenA = A.length();
        int lenB = B.length();
        int maxLength = Math.max(lenA, lenB);
        A = paddingZeros(A, maxLength);
        B = paddingZeros(B, maxLength);

        int bor = 0;
        StringBuilder res = new StringBuilder();
        for (int i = maxLength - 1; i >= 0; i--) {
            int diff = (A.charAt(i) - '0') - (B.charAt(i) - '0') - bor;
            if (diff < 0) {
                diff += 10;
                bor = 1;
            } else {
                bor = 0;
            }
            res.insert(0, diff);
        }

        return res.toString();
    }

    // Left padding a string with zeros
    private static String paddingZeros(String str, int length) {
        StringBuilder padZero = new StringBuilder(str);
        while (padZero.length() < length) {
            padZero.insert(0, '0');
        }
        return padZero.toString();
    }

    // Function to shift a string to the left by a specified number of positions
    private static String leftShift(String str, int positions) {
        StringBuilder shift = new StringBuilder(str);
        for (int i = 0; i < positions; i++) {
            shift.append('0');
        }
        return shift.toString();
    }

    // To generate random integer Generates a random n-digit integer
    private static String generateRandomInteger(int n) {
        Random ran = new Random();
        StringBuilder num = new StringBuilder();

        num.append(ran.nextInt(8) + 1); // Here the most significant digit is between 1 and 9

        // To generate the rest of the digits
        for (int i = 1; i < n; i++) {
            num.append(ran.nextInt(10));
        }

        return num.toString();
    }

    // To remove leading zeros
    public static String removeLeadingZeros(String str) {
        if (str == null) {
            return null;
        }
    
        // Use a regular expression to remove leading zeros
        return str.replaceFirst("^0+", "");
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        //Check if teh user provides correct argument
        if (args.length != 1) {
            System.out.println("Only one argument n should be provided!");
            return;
        }
 
        //Check if the number is multiple of 6
        if (n % 6 != 0) {
            System.out.println("Argument n should be multiple of 6!");
            return;
        }

        //Generate two n-digit random integers A and B
        String A = generateRandomInteger(n);
        String B = generateRandomInteger(n);

        //print the random numbers generated as A and B
        System.out.println("A: " + A);
        System.out.println("B: " + B);

        //print the res of Large Integer Multiplication A*B
        System.out.println("\nThe large integer multiplication from the division of two smaller integers is ");
        String resOfLargeIntMul = funLargeIntegerMultiplyn2(A, B);
        resOfLargeIntMul = removeLeadingZeros(resOfLargeIntMul);

        System.out.println("A*B= " + resOfLargeIntMul);

        //print the result of Large Integer Multiplication with modification A*B
        System.out.println("\nThe large integer multiplication from the division of three smaller integers is ");
        String resOfModAlgo = funLargeIntegerMultiplyn3(A,B);
        resOfModAlgo = removeLeadingZeros(resOfModAlgo);

        System.out.println("A*B= " + resOfModAlgo);

        System.out.println(resOfLargeIntMul.equals(resOfModAlgo));

        
    }

}