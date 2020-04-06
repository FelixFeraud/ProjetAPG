import java.util.ArrayList;

public class Glouton
{
    public static ArrayList<Integer> solve(ProblemInstance instance)
    {
        ArrayList<Integer> solution = new ArrayList<>();

        while(betterSolutionExists(instance, solution))
        {
            int currentMinEval = customEval(instance, solution);
            int bestProviderIndex = 0;
            for(int providerIndex = 0; providerIndex < instance.providerAmount; providerIndex++)
            {
                if(!solution.contains(providerIndex))
                {
                    ArrayList<Integer> augmentedSolution = new ArrayList<>(solution);
                    augmentedSolution.add(providerIndex);

                    int newEval = customEval(instance, augmentedSolution);
                    if(newEval < currentMinEval)
                    {
                        currentMinEval = newEval;
                        bestProviderIndex = providerIndex;
                    }
                }
            }
            solution.add(bestProviderIndex);
        }


        return solution;
    }

    private static boolean betterSolutionExists(ProblemInstance instance, ArrayList<Integer> currentSolution)
    {
        for(int providerIndex = 0; providerIndex < instance.providerAmount; providerIndex++)
        {
            if(!currentSolution.contains(providerIndex))
            {
                ArrayList<Integer> augmentedSolution = new ArrayList<>(currentSolution);
                augmentedSolution.add(providerIndex);
                if(customEval(instance, augmentedSolution) < customEval(instance, currentSolution))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static int customEval(ProblemInstance instance, ArrayList<Integer> solution)
    {
        if(solution.size() == 0)
            return Integer.MAX_VALUE;
        else
            return instance.eval(solution);
    }
}
