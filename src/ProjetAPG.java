import java.util.ArrayList;

/**
 * Projet APG
 * Félix Féraud
 * M1 Informatique - Université d'Aix Marseille
 * 13 avril 2020
 */
public class ProjetAPG
{
    /**
     * Charge un fichier en argument et renvoie les résultats de chaque algorithme : les fournisseurs ouverts et le coût final.
     * @param args Un fichier instance de problème clients/fournisseurs
     */
    public static void main(String[] args)
    {
        System.out.println("Projet APG");

        ProblemReader pr = new ProblemReader();
        ProblemInstance instance = pr.generateInstanceFromFile(args[0]);

        instance.print();

        ArrayList<Integer> openedProviders;

        // Exécute chaque algorithme sur le fichier d'instance chargé.
        System.out.println("\n----------------------------------------\nAlgorithme Glouton 1 :");
        openedProviders = Glouton.solve(instance);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = " + properDisplay(openedProviders));
        System.out.println("\n----------------------------------------\n");

        System.out.println("\n----------------------------------------\nProgramme Linéaire :");
        openedProviders = GLPKSolver.solve(instance);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = " + properDisplay(openedProviders));
        System.out.println("\n----------------------------------------\n");

        System.out.println("\n----------------------------------------\nProgramme Linéaire Aléatoire :");
        openedProviders = RandomGLPKSolver.solve(instance, 10_000);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = " + properDisplay(openedProviders));
        System.out.println("\n----------------------------------------\n");

        System.out.println("\n----------------------------------------\nAlgorithme Glouton 2-1 (1*fi) :");
        openedProviders = Glouton2_1.solve(instance, 1);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = " + properDisplay(openedProviders));
        System.out.println("\n----------------------------------------\n");

        System.out.println("\n----------------------------------------\nAlgorithme Glouton 2-1 (2*fi) :");
        openedProviders = Glouton2_1.solve(instance, 2);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = " + properDisplay(openedProviders));
        System.out.println("\n----------------------------------------\n");

        /*
        System.out.println("\n----------------------------------------\nAlgorithme Glouton 2-2*fi :");
        openedProviders = Glouton2_2.solve(instance);
        System.out.println("\tCoût = " + instance.eval(openedProviders));
        System.out.print("\tFournisseurs ouverts = " + properDisplay(openedProviders));
        System.out.println("\n----------------------------------------\n");
        */
    }

    /**
     * Affiche proprement les tableaux pour les fournisseurs ouverts.
     * Les indices commencent à 1 dans les fichiers d'instance, et à 0 dans les tableaux.
     * @param openedProviders Liste d'indices des fournisseurs ouverts.
     * @return String affichant la liste des fournisseurs ouverts en décalant les indices de +1.
     */
    private static String properDisplay(ArrayList<Integer> openedProviders)
    {
        StringBuilder stringBuilder = new StringBuilder("[");

        for(int i = 0; i < openedProviders.size(); i++)
        {
            stringBuilder.append(openedProviders.get(i)+1);
            if(i != openedProviders.size() - 1)
                stringBuilder.append(",");
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
