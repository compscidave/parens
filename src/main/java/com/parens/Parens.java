package com.parens;

import static java.lang.Math.min;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Inputs: Prompts the user for a file which contains any number of
 * unparenthesized boolean expressions, each separated by a newline character.
 * The last expression in the input file MUST NOT be followed by the newline
 * character.
 * 
 * Functionality: An expression from the input file shall be read, and the
 * number of full parenthesizations of that expression which evaluate to true
 * will be tallied until all expressions from the input file have been processed
 * in this manner.
 * 
 * Output: A single text file, "output.txt" which contains each original (
 * unparenthesized ) boolean expression from the input file immediately followed
 * by the total number of parenthesizations of that expression which evaluate to
 * true. The output will have the following form:
 * 
 * boolean expression : # of true evaluations ...
 **/
public class Parens {

    private BigInteger Beta[][];
    private String exp;
    private int operands = 0;
    private BigInteger[][] BinCoeff;
    private long[][] timestamps;
    private boolean calculated = false;

    /**
     * Takes file input for multiple expressions, each separated
     * by a newline.
     * 
     * @param argsv
     * @throws IOException
     */
    public static void main(String argsv[]) throws IOException {

        String newline = System.getProperty("line.separator");

        int operands;

        char[] operators = new char[4];
        operators[0] = 'a';
        operators[1] = 'r';
        operators[2] = 'n';
        operators[3] = 'x';

        int[] orands = new int[2];
        orands[0] = 0;
        orands[1] = 1;

        BufferedWriter out = new BufferedWriter(new FileWriter(
                "src//main//resources//output"));
        BufferedWriter inOut = new BufferedWriter(new FileWriter(
                "src//main//resources//input"));
        try {
            String inputString = "1";

            for (int i = 1; i <= 3000; i++) {
                inputString += "a1";
            }
            inOut.write(inputString);
            inOut.close();

            Parens parens = new Parens(inputString);
            operands = ((parens.getExpression().length() - 1) / 2) + 1;

            BigInteger truths = parens.calculate(false);

            out.write(parens.getExpression() + ":" + truths + newline);
            // This output creates a tabular view of execution times for each
            // individual
            // entry in the 2d helper matrix. Thus entry 0,operands-1 holds the
            // total time
            // it takes to calculate which parenthesizations evaluate to true
            // over the entire expression.
            for (int i = 0; i < operands; i++) {
                out.write(newline);
                long rowSum = 0;
                for (int j = 0; j < operands; j++) {
                    out.write("[" + parens.getTimestamp(i, j) + "]");
                    if (j != operands - 1) {
                        rowSum += parens.getTimestamp(i, j);
                    } else {
                        out.write("[" + rowSum + "]");
                    }
                }
            }
            /*
             * 200! / (101! * 100!)
             */
            out.close();
        } catch (IOException e) {

        } finally {
            inOut.close();
            out.close();
        }
    }

    public Parens(String exp) throws IllegalArgumentException {
        if (exp != null && exp.matches("[01]{1}([axrn][01])+")) {
            this.exp = exp;
            operands = ((exp.length() - 1) / 2) + 1;
            Beta = new BigInteger[operands][operands];
            timestamps = new long[operands][operands + 1];
            DPBinomCoeffs(2 * operands, operands);
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected BigInteger getTotalTruths() {
        return Beta[0][operands - 1];
    }

    public BigInteger calculate(boolean outputToFile) {
        if (calculated) {
            return getTotalTruths();
        } else {
            calculateParenthesizations(0, operands - 1);
            calculated = true;
            if(outputToFile) {
            	try {
					print();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            return getTotalTruths();
        }
    }

    private void print() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(
                "src//main//resources//output"));

        String newline = System.getProperty("line.separator");
        try {

            out.write(exp + ":" + getTotalTruths() + newline);
 
            for (int i = 0; i < operands; i++) {
                out.write(newline);
                long rowSum = 0;
                for (int j = 0; j < operands; j++) {
                    out.write("[" + timestamps[i][j] + "]");
                    if (j != operands - 1) {
                        rowSum += timestamps[i][j];
                    } else {
                        out.write("[" + rowSum + "]");
                    }
                }
            }
            out.close();
        } catch (IOException e) {

        } finally {
            out.close();
        }
	}

	/**
     * A boolean expression is of the form arg_0 op_0 arg_1 op_1 ... op_n-1
     * arg_n
     * 
     * Inputs: Integers j and k referencing the subexpression arg_j op_j arg_j+1
     * ... op_k-1 arg_k 0<=j<=k<=n. Assumes exp.charAt(j) is either 1 or 0 for
     * all j = 2n, 0 <= n <= k and exp.charAt(j) is 'a','r','n','x' for all j =
     * 2n + 1; 0 <= n < k.
     * 
     * Functionality: Stores the number of full parenthesizations of the
     * expression that evaluate to true in the public two-dimensional array Beta
     * at position Beta[j][k]. In the initial call to this function j=0, k=n.
     * i.o.w. the full expression is under consideration
     * 
     * Properties: Beta[i][i] = 1 if arg_i is '1' 0 otherwise for all 0<=i<=n
     * Beta[i][i+1] = 1 if arg_i op_i arg_i+1 is '1', 0 otherwise for all 0<=i<n
     * Beta[j][k] = Beta[j][j]*Beta[j+1][k] + Beta[j][j+1]*Beta[j+2][k] + ... +
     * Beta[j][k-1]*Beta[j][k] for all j,k s.t. 0<=j<k-1<=n.
     * 
     * Output: None. However, each time the function is called, there is one
     * less entry to fill in Beta Note that only half of the entries in Beta
     * need to be filled, since Beta[r][t] is undefined for r > t
     * 
     * Exceptions: Throws OutOfMemoryError during arithmetic operations with
     * -Xmx8g, apparently with operands = 3000 around 48 hours into execution.
     **/
    private void calculateParenthesizations(int j, int k) {
        // Base Case 1
        if ((k - j) == 0) {
            // j=k
            if (exp.charAt(j * 2) == '1')
                Beta[j][k] = BigInteger.ONE;
            else
                Beta[j][k] = BigInteger.ZERO;
        }
        // Base Case 2
        else if ((k - j) == 1) {
            Beta[j][k] = evaluate(exp.charAt(j * 2), exp.charAt(k * 2),
                    exp.charAt((j * 2) + 1));
        }
        // Recursive Step
        else {
            // This is the entry that is being calculated in this function call
            Beta[j][k] = BigInteger.ZERO;
            for (int i = j; i < k; i++) {
                // Break the expression into two parts arg_j ... arg_i and
                // arg_i+1 ... arg_k
                // If the two entries in Beta haven't been computed, they are
                // computed
                // by making one recursive call for each entry that hasn't been
                // computed.
                if (Beta[j][i] == null) {
                    long startMillis = System.currentTimeMillis();
                    calculateParenthesizations(j, i);
                    timestamps[j][i] = (System.currentTimeMillis() - startMillis);
                }
                if (Beta[i + 1][k] == null) {
                    long startMillis = System.currentTimeMillis();
                    calculateParenthesizations(i + 1, k);
                    timestamps[i + 1][k] = (System.currentTimeMillis() - startMillis);
                }

                // this is the operator that connects the two sub-expressions
                // (op_i)
                char op = exp.charAt((i * 2) + 1);

                // Now that Beta[j][i] and Beta[i+1][k] have been calculated
                // we use them together with the operator connecting them (op_i)
                // to come closer to a solution for Beta[j][k].
                if (op == 'a') {
                    // 1 a 1 = 1
                    Beta[j][k] = Beta[j][k].add(Beta[j][i]
                            .multiply(Beta[i + 1][k]));
                } else {
                    // TOTAL number of full-parenthesizations of the left-hand
                    // and right-hand sub-expressions ( respectively )
                    // note that (jToiParens - Beta[j][i]) = total # of FALSE
                    // parenthesizations in the j to i sub-exp.
                    // likewise, (iAddOneTokParens - Beta[i+1][k]) = # FALSE
                    // parens in the i+1 to k sub-exp
                    BigInteger jToiParens = countParen(i - j);
                    BigInteger iAddOneTokParens = countParen(k - (i + 1));

                    if (op == 'r') {
                        //some simple math can be used to derive the solution for Beta[j][k].
                        Beta[j][k] = Beta[j][k]
                                // 1 r 1 = 1
                                .add(Beta[j][i]
                                    .multiply(Beta[i + 1][k])
                                    // 1 r 0 = 1
                                    .add(Beta[j][i].multiply(iAddOneTokParens
                                        .subtract(Beta[i + 1][k])))
                                    // 0 r 1 = 1
                                    .add((jToiParens.subtract(Beta[j][i]))
                                        .multiply(Beta[i + 1][k])));
                    } else if (op == 'x') {
                        // 0 x 1 = 1
                        Beta[j][k] = Beta[j][k].add(
                                (jToiParens.subtract(Beta[j][i]))
                                        .multiply(Beta[i + 1][k]))
                        // 1 x 0 = 1
                                .add(Beta[j][i].multiply(iAddOneTokParens
                                        .subtract(Beta[i + 1][k])));
                    } else if (op == 'n') {
                        // 0 n 1 = 1
                        Beta[j][k] = Beta[j][k]
                                .add((jToiParens.subtract(Beta[j][i]))
                                        .multiply(Beta[i + 1][k]))
                                // 1 n 0 = 1
                                .add(Beta[j][i].multiply(iAddOneTokParens
                                        .subtract(Beta[i + 1][k])))
                                // 0 n 0 = 1
                                .add((jToiParens.subtract(Beta[j][i]))
                                        .multiply(iAddOneTokParens
                                                .subtract(Beta[i + 1][k])));
                    }
                }
            } // end_for
        }// end recursive step.
    }

    /**
     * Inputs: Three characters, l, r, and op representing the left (l)
     * argument, right (r) argument, and the operator (op) that connects the to.
     * 
     * Functionality: evaluates a simple binary expression l op r
     * 
     * Output: An integer 1 iff l op r is true, 0 otherwise.
     **/
    private static BigInteger evaluate(char l, char r, char op) {
        switch(op) {
            case 'a':
                return (l == '1' && r == '1') ? BigInteger.ONE : BigInteger.ZERO;
            case 'r':
                return (l == '1' || r == '1') ? BigInteger.ONE : BigInteger.ZERO;
            case 'n':
                return !(l == '1' && r == '1')  ? BigInteger.ONE : BigInteger.ZERO;
            case 'x':
                return !(l == r)  ? BigInteger.ONE : BigInteger.ZERO;
            default:
                return BigInteger.ZERO;
        }
    }

    /**
     * Input: An integer n representing the number of operators there are in a
     * boolean expression n = { 0, ... , n }
     * 
     * Functionality: Using the function DPBinomCoeffs, calculates how many
     * possible parenthesizations there are in a boolean expression with n
     * operators ( n+1 ) operands.
     * 
     * Output: A BigInteger of said value.
     **/
    private BigInteger countParen(int n) {
        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        } else if (BinCoeff != null) {
            return BinCoeff[2 * n][n].divide(new BigInteger(String
                    .valueOf(n + 1)));
        } else {
            return DPBinomCoeffs(2 * n, n).divide(
                    new BigInteger(String.valueOf(n + 1)));
        }
    }

    /**
     * Inputs: Two integers, n and k
     * 
     * Output: A BigInteger with value n choose k
     **/
    private BigInteger DPBinomCoeffs(int n, int k) {
        BinCoeff = new BigInteger[n + 1][k + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= min(i, k); j++) {
                if (j == 0 || j == i)
                    BinCoeff[i][j] = BigInteger.ONE;
                else
                    BinCoeff[i][j] = BinCoeff[i - 1][j - 1]
                            .add(BinCoeff[i - 1][j]);
            }
        }
        return BinCoeff[n][k];
    }

    public String getExpression() {
        return exp;
    }

    public long getTimestamp(int i, int j) {
        return timestamps[i][j];
    }

} // end public class Parens
