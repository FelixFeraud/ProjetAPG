import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Projet APG");

        ProblemReader pr = new ProblemReader();
        ProblemInstance instance = pr.generateInstanceFromFile("C:\\Users\\utilisateur\\Desktop\\BildeKrarup\\B\\B1.2");

        instance.print();

        System.out.println("\n--------------------\nAlgorithme Glouton :");
        ArrayList<Integer> openedProviders = Glouton.solve(instance);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = ");
        displayOpenedProviders(openedProviders);
        System.out.println("\n--------------------\n");

        System.out.println("\n--------------------\nProgramme Linéaire :");
        openedProviders = GLPKSolver.solve(instance);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = ");
        displayOpenedProviders(openedProviders);
        System.out.println("\n--------------------\n");
    }

    private static void displayOpenedProviders(ArrayList<Integer> openedProviders)
    {
        System.out.print("[");

        for(int i = 0; i < openedProviders.size(); i++)
        {
            System.out.print(openedProviders.get(i));
            if(i != openedProviders.size() - 1)
                System.out.print(", ");
        }

        System.out.print("]");
    }
}
