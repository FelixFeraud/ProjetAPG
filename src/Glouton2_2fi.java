import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

class Glouton2_2fi
{
    // Les derniers résultats du calcul de beta
    private static int betaBestProvider;
    private static ArrayList<Integer> betaBestY;

    /**
     * Cherche une solution optimale à une instance de problème de fournisseurs.
     * @param instance L'instance du problème à résoudre.
     * @return Une liste d'entiers indiquant les fournisseurs à ouvrir.
     */
    static ArrayList<Integer> solve(ProblemInstance instance)
    {
        ArrayList<Integer> O = new ArrayList<>();
        ArrayList<Integer> S = new ArrayList<>();

        for(int client = 0; client < instance.clientAmount; client++)
            S.add(client);

        while(!S.isEmpty())
        {
            int[] alphaResults = computeAlpha(instance, O, S);
            int beta = computeBeta(instance, O, S);
            if(alphaResults[0] <= beta)
            {
                S.remove((Integer)alphaResults[1]);
            }
            else
            {
                S.removeAll(betaBestY);
                O.add(betaBestProvider);
            }
        }

        return O;
    }

    /**
     * Calcule alpha et renvoie un tableau avec les données pertinentes.
     * @param instance L'instance du problème à résoudre.
     * @param O Liste des fournisseurs ouverts.
     * @param S Liste des clients non connectés.
     * @return Tableau d'entiers contenant le coût minimal trouvé (alpha) et le numéro du meilleur client trouvé.
     */
    private static int[] computeAlpha(ProblemInstance instance, ArrayList<Integer> O, ArrayList<Integer> S)
    {
        int minCost = Integer.MAX_VALUE;
        int bestClient = 0;
        for(Integer provider : O)
        {
            for(Integer client : S)
            {
                if(instance.clientConnectionCosts[provider][client] < minCost)
                {
                    minCost = instance.clientConnectionCosts[provider][client];
                    bestClient = client;
                }
            }
        }
        return new int[]{minCost, bestClient};
    }

    /**
     * Calcule Beta et stock les données pertinentes dans betaBestProvider et betaBestY.
     * @param instance L'instance du problème à résoudre.
     * @param O Liste des fournisseurs ouverts.
     * @param S Liste des clients non connectés.
     * @return Beta.
     */
    private static int computeBeta(ProblemInstance instance, ArrayList<Integer> O, ArrayList<Integer> S)
    {
        int minTotal = Integer.MAX_VALUE;
        int bestProvider = 0;
        ArrayList<Integer> bestY = new ArrayList<>();

        for(int provider = 0; provider < instance.providerAmount; provider++)
        {
            if(!O.contains(provider))
            {
                int firstPart = 2 * instance.providerOpeningCosts[provider];

                PriorityQueue<Integer[]> connectionCostsHeap = new PriorityQueue<>(Comparator.comparingInt(o -> o[0]));
                ArrayList<Integer> Y = new ArrayList<>();

                int alreadyConnectedSum = 0;
                for(int i = 0; i < instance.clientAmount; i++)
                {
                    connectionCostsHeap.add(new Integer[]{instance.clientConnectionCosts[provider][i], i});
                    if(!S.contains(i))
                    {
                        int gegr = minClientCost(instance, i, O) - instance.clientConnectionCosts[provider][i];
                        alreadyConnectedSum += Math.max(gegr, 0);
                    }
                }

                Y.add(Objects.requireNonNull(connectionCostsHeap.poll())[1]);
                double bestRatio = ratio(instance, provider, Y, O);

                boolean test = true;
                while(test)
                {
                    Integer[] minCostConnection = connectionCostsHeap.poll();
                    assert minCostConnection != null;
                    Y.add(minCostConnection[1]);

                    double newRatio = ratio(instance, provider, Y, O);
                    if(newRatio > bestRatio)
                    {
                        Y.remove(minCostConnection[1]);
                        test = false;
                    }
                    else
                    {
                        bestRatio = newRatio;
                    }
                }

                int newClientsCostSum = 0;

                for(Integer client : Y)
                {
                    newClientsCostSum += instance.clientConnectionCosts[provider][client];
                }

                int total = firstPart + newClientsCostSum - alreadyConnectedSum;

                if(total < minTotal)
                {
                    minTotal = total;
                    bestProvider = provider;
                    bestY = Y;
                }
            }

        }

        betaBestProvider = bestProvider;
        betaBestY = bestY;

        return minTotal;
    }

    /**
     * @param instance L'instance du problème à résoudre.
     * @param client Le client pour lequel on veut trouver le coût de connexion minimal.
     * @param O La liste des fournisseurs ouverts.
     * @return Renvoie le coût de connexion minimal pour un client et une liste de fournisseurs ouverts donnés.
     */
    private static int minClientCost(ProblemInstance instance, int client, ArrayList<Integer> O)
    {
        int minCost = Integer.MAX_VALUE;
        for(Integer provider : O)
        {
            if(instance.clientConnectionCosts[provider][client] < minCost)
            {
                minCost = instance.clientConnectionCosts[provider][client];
            }
        }
        return minCost;
    }

    /**
     * @param instance L'instance du problème à résoudre.
     * @param provider Le fournisseur pour lequel on veut calculer le ratio.
     * @param Y La liste des nouveaux clients à connecter.
     * @param O La liste des fournisseurs ouverts.
     * @return Le ratio défini dans l'énoncé du projet.
     */
    private static double ratio(ProblemInstance instance, int provider, ArrayList<Integer> Y, ArrayList<Integer> O)
    {
        int newClientsCostSum = 0;
        for(Integer client : Y)
        {
            newClientsCostSum += instance.clientConnectionCosts[provider][client];
        }

        int alreadyConnectedSum = 0;
        for(int client = 0; client < instance.clientAmount; client++)
        {
            if(!Y.contains(client))
            {
                alreadyConnectedSum += Math.max(minClientCost(instance, client, O) - instance.clientConnectionCosts[provider][client], 0);
            }
        }

        return (double)(instance.providerOpeningCosts[provider] + newClientsCostSum - alreadyConnectedSum) / Y.size();
    }
}
