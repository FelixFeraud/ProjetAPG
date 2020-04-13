import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

class Glouton2_1fi
{
    // Dernier Y calculé par computeBeta
    private static ArrayList<Integer> betaY;

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

        PriorityQueue<Object[]> heap = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o[0]));

        for(int i = 0; i < instance.providerAmount; i++)
        {
            int bestRatio = computeBeta(instance, i, O, S);
            heap.add(new Object[]{bestRatio, EventType.PROVIDER, i, betaY});
        }

        while(!heap.isEmpty())
        {
            Object[] event = heap.poll();
            if(event[1] == EventType.PROVIDER)
            {
                int provider = (int) event[2];
                int currentBestRatio = computeBeta(instance, provider, O, S);

                Object[] currentEvent = new Object[]{currentBestRatio, EventType.PROVIDER, provider, betaY};

                if((int)event[0] == currentBestRatio)
                {
                    O.add(provider);
                    S.removeAll(betaY);

                    for(int client : S)
                    {
                        heap.add(new Object[]
                                {
                                        instance.clientConnectionCosts[provider][client],
                                        EventType.CONNECTION,
                                        client
                                });
                    }
                }
                else
                {
                    heap.add(currentEvent);
                }
            }
            else
            {
                int client = (int) event[2];
                if(S.contains(client))
                {
                    S.remove((Integer)client);
                }
            }
        }

        return O;
    }

    /**
     * Calcule Beta pour un fournisseur.
     * @param instance L'instance du problème à résoudre.
     * @param provider Le fournisseur pour lequel on cherche beta.
     * @param O La liste des fournisseurs ouverts.
     * @param S La liste des clients non connectés.
     * @return Beta.
     */
    private static int computeBeta(ProblemInstance instance, int provider, ArrayList<Integer> O, ArrayList<Integer> S)
    {
        int firstPart = instance.providerOpeningCosts[provider];

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

        betaY = Y;

        return total;
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

    /**
     * Pour identifier un type d'évènement, si on ajoute un fournisseur ou une connexion au tas.
     */
    private enum EventType
    {
        PROVIDER, CONNECTION
    }
}
