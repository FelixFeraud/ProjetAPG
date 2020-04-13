import java.util.ArrayList;

/**
 * Caractérise une instance d'un problème de connexions fournisseurs/clients
 */
public class ProblemInstance
{
    String name;

    int providerAmount;
    int clientAmount;

    int[] providerOpeningCosts;
    int[][] clientConnectionCosts;

    ProblemInstance(String name, int providerAmount, int clientAmount)
    {
        this.name = name;
        this.providerAmount = providerAmount;
        this.clientAmount = clientAmount;

        providerOpeningCosts = new int[providerAmount];
        clientConnectionCosts = new int[providerAmount][clientAmount];
    }

    void addProviderOpeningCost(int provider, int cost)
    {
        providerOpeningCosts[provider] = cost;
    }

    void addClientConnectionCost(int provider, int client, int cost)
    {
        clientConnectionCosts[provider][client] = cost;
    }

    void print()
    {
        System.out.println("Instance " + name + ".");

        System.out.println(providerAmount + " fournisseurs et " + clientAmount + " clients.");
    }

    int eval(ArrayList<Integer> openedProvidersIndices)
    {
        int providerCostSum = 0;
        int connectionCostSum = 0;

        for(int openedProviderIndex : openedProvidersIndices)
            providerCostSum += providerOpeningCosts[openedProviderIndex];

        for(int j = 0; j < clientAmount; j++)
        {
            int minConnectionCost = clientConnectionCosts[openedProvidersIndices.get(0)][j];
            for(int openedProviderIndex : openedProvidersIndices)
            {
                if(clientConnectionCosts[openedProviderIndex][j] < minConnectionCost)
                {
                    minConnectionCost = clientConnectionCosts[openedProviderIndex][j];
                }
            }

            connectionCostSum += minConnectionCost;
        }

        return providerCostSum + connectionCostSum;
    }
}
