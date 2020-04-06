import org.gnu.glpk.*;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Projet APG");
        System.out.println("(GLPK version " + GLPK.glp_version() + ")\n");

        ProblemReader pr = new ProblemReader();
        ProblemInstance instance = pr.generateInstanceFromFile("C:\\Users\\utilisateur\\Desktop\\BildeKrarup\\B\\B1.1");

        instance.print();
        ArrayList<Integer> openedProviders = Glouton.solve(instance);

        System.out.println("\n--------------------\nAlgorithme Glouton :");
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
