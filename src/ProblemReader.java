import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Classe utilitaire pour générer une instance de problème à partir d'un fichier.
 */
class ProblemReader
{
    ProblemInstance generateInstanceFromFile(String path)
    {
        ProblemInstance instance;
        try
        {
            File fileToRead = new File(path);
            Scanner scanner = new Scanner(fileToRead);

            String name = scanner.nextLine().split(" ")[1];
            int providerAmount = scanner.nextInt();
            int clientAmount = scanner.nextInt();

            if(scanner.nextInt() != 0) throw new RuntimeException("Bad file format.");

            instance = new ProblemInstance(name, providerAmount, clientAmount);

            while(scanner.hasNextInt())
            {
                int provider = scanner.nextInt() - 1;
                int providerOpeningCost = scanner.nextInt();
                instance.addProviderOpeningCost(provider, providerOpeningCost);

                for(int client = 0; client < clientAmount; client++)
                {
                    int clientConnectionCost = scanner.nextInt();
                    instance.addClientConnectionCost(provider, client, clientConnectionCost);
                }
            }

            return instance;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }


        return null;
    }
}
