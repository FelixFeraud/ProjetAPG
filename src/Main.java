public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Projet APG\n");

        ProblemReader pr = new ProblemReader();
        ProblemInstance pi = pr.generateInstanceFromFile("C:\\Users\\utilisateur\\Desktop\\BildeKrarup\\B\\B1.1");

        pi.print();
        int[] openedProviders = {3, 4, 8, 14, 25, 48};
        System.out.println(pi.eval(openedProviders));
    }
}
