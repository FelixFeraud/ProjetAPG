import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProblemReader
{
    public ProblemInstance generateInstanceFromFile(String path)
    {
        ProblemInstance pi;
        try
        {
            File fileToRead = new File(path);
            Scanner scanner = new Scanner(fileToRead);

            String name = scanner.nextLine().split(" ")[1];
            int providerAmount = scanner.nextInt();
            int clientAmount = scanner.nextInt();

            if(scanner.nextInt() != 0) throw new RuntimeException("Bad file format.");

            pi = new ProblemInstance(name, providerAmount, clientAmount);

            while(scanner.hasNextInt())
            {
                int provider = scanner.nextInt() - 1;
                int providerOpeningCost = scanner.nextInt();
                pi.addProviderOpeningCost(provider, providerOpeningCost);

                for(int client = 0; client < clientAmount; client++)
                {
                    int clientConnectionCost = scanner.nextInt();
                    pi.addClientConnectionCost(provider, client, clientConnectionCost);
                }
            }

            return pi;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }


        return null;
    }
}
