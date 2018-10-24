package ch.epfl.sweng.favors.database;

import java.util.ArrayList;

public class computationModule {

    public static int performArrayOfTokensSum(ArrayList<Integer> tokens){
        int tokensSum = 0;
        for(int i = 0; i < tokens.size(); ++i){
            tokensSum+=tokens.get(i);
        }

        return tokensSum;
    }

    public static void resetTokensArray(ArrayList<Integer> tokens){
        tokens.clear();
    }

    public static int retrieveOneUnityperTokenAndSum(ArrayList<Integer> tokens){
        int tokensSum = 0;
        for(int i = 0; i < tokens.size(); ++i){
            tokens.set(i, tokens.get(i) - 1);
            tokensSum += tokens.get(i);
        }

        return tokensSum;
    }
}
