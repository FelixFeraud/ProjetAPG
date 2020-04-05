public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Projet APG\n");

        ProblemReader pr = new ProblemReader();
        ProblemInstance pi = pr.generateInstanceFromFile("C:\\Users\\utilisateur\\Desktop\\BildeKrarup\\B\\B1.1");

        pi.print();
    }
}
