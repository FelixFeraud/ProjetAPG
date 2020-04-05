public class ProblemInstance
{
    public String name;

    public int providerAmount;
    public int clientAmount;

    public int[] providerOpeningCost;
    public int[][] clientConnectionCost;

    public ProblemInstance(String name, int providerAmount, int clientAmount)
    {
        this.name = name;
        this.providerAmount = providerAmount;
        this.clientAmount = clientAmount;

        providerOpeningCost = new int[providerAmount];
        clientConnectionCost = new int[providerAmount][clientAmount];
    }

    public void addProviderOpeningCost(int provider, int cost)
    {
        providerOpeningCost[provider] = cost;
    }

    public void addClientConnectionCost(int provider, int client, int cost)
    {
        clientConnectionCost[provider][client] = cost;
    }

    public void print()
    {
        System.out.println("Problem instance " + name + ".");

        System.out.println(providerAmount + " providers and " + clientAmount + " clients.");
    }
}
