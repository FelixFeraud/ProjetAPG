import java.util.ArrayList;

class Glouton
{
    /**
     * Cherche une solution optimale à une instance de problème de fournisseurs.
     * @param instance L'instance du problème à résoudre.
     * @return Une liste d'entiers indiquant les fournisseurs à ouvrir.
     */
    static ArrayList<Integer> solve(ProblemInstance instance)
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

    /**
     * @param instance Instance d'un problème donné.
     * @param currentSolution Meilleure solution actuelle que l'on va tenter de rendre encore meilleure.
     * @return Vrai si une meilleure solution que l'actuelle existe, faux sinon.
     */
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

    /**
     * @param instance L'instance d'un problème donné.
     * @param solution Une liste de fournisseurs à évaluer solution du problème.
     * @return La valeur maximum possible pour les entiers si la solution ne contient aucun fournisseur (eval(Ø) = +∞), eval(solution) sinon.
     */
    private static int customEval(ProblemInstance instance, ArrayList<Integer> solution)
    {
        if(solution.size() == 0)
            return Integer.MAX_VALUE;
        else
            return instance.eval(solution);
    }
}
