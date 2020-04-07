import java.util.ArrayList;

public class ProblemInstance
{
    public String name;

    public int providerAmount;
    public int clientAmount;

    public int[] providerOpeningCosts;
    public int[][] clientConnectionCosts;

    public ProblemInstance(String name, int providerAmount, int clientAmount)
    {
        this.name = name;
        this.providerAmount = providerAmount;
        this.clientAmount = clientAmount;

        providerOpeningCosts = new int[providerAmount];
        clientConnectionCosts = new int[providerAmount][clientAmount];
    }

    public void addProviderOpeningCost(int provider, int cost)
    {
        providerOpeningCosts[provider] = cost;
    }

    public void addClientConnectionCost(int provider, int client, int cost)
    {
        clientConnectionCosts[provider][client] = cost;
    }

    public void print()
    {
        System.out.println("Instance " + name + ".");

        System.out.println(providerAmount + " fournisseurs et " + clientAmount + " clients.");
    }

    public int eval(ArrayList<Integer> openedProvidersIndices)
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
