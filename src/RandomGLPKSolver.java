import org.gnu.glpk.*;

import java.util.ArrayList;
import java.util.Random;

public class RandomGLPKSolver
{
    public static ArrayList<Integer> solve(ProblemInstance instance)
    {
        ArrayList<Double> openingProbabilities = solveLP(instance);

        int bestCost = Integer.MAX_VALUE;
        ArrayList<Integer> bestList = new ArrayList<>();

        for(int i = 0; i < 1000; i++)
        {
            ArrayList<Integer> randomList = randomProviderSelection(openingProbabilities);
            int cost = customEval(instance, randomList);
            if(cost < bestCost)
            {
                bestCost = cost;
                bestList = randomList;
            }
        }

        return bestList;
    }

    public static ArrayList<Integer> randomProviderSelection(ArrayList<Double> openingProbabilities)
    {
        Random seed = new Random();
        ArrayList<Integer> openedProviders = new ArrayList<>();

        for(int i = 0; i < openingProbabilities.size(); i++)
        {
            double randomNumber = seed.nextDouble();
            if(openingProbabilities.get(i) > randomNumber)
                openedProviders.add(i);
        }

        return openedProviders;
    }

    public static ArrayList<Double> solveLP(ProblemInstance instance)
    {
        System.out.println("Résolution de " + instance.name + " avec GLPK (version " + GLPK.glp_version() + ")\n");
        ArrayList<Double> openingProbabilities = new ArrayList<Double>();

        glp_prob LP = GLPK.glp_create_prob();

        // Objective function
        GLPK.glp_set_obj_dir(LP, GLPKConstants.GLP_MIN);

        GLPK.glp_add_cols(LP, instance.providerAmount + (instance.clientAmount * instance.providerAmount));

        // X
        for(int i = 0; i < instance.providerAmount; i++)
        {
            GLPK.glp_set_obj_coef(LP, i + 1, (double)instance.providerOpeningCosts[i]);

            String colName = "x" + (i + 1);
            GLPK.glp_set_col_name(LP, i + 1, colName);
            GLPK.glp_set_col_bnds(LP, i + 1, GLPKConstants.GLP_DB, 0, 1);
        }

        // Y
        int colCounter = 0;
        for(int i = 0; i < instance.providerAmount; i++)
        {
            for(int j = 0; j < instance.clientAmount; j++)
            {
                GLPK.glp_set_obj_coef(LP, instance.providerAmount + ++colCounter, (double)instance.clientConnectionCosts[i][j]);

                String colName = "y" + (i+1) + "," + (j+1);
                GLPK.glp_set_col_name(LP, instance.providerAmount + colCounter, colName);
                GLPK.glp_set_col_bnds(LP, instance.providerAmount + colCounter, GLPKConstants.GLP_LO, 0, 0);
            }
        }

        // yi,j <= xi

        GLPK.glp_add_rows(LP, GLPK.glp_get_num_cols(LP) - instance.providerAmount);

        int rowCounter = 1;
        double[] values = {-1, 1};
        for(int i = 0; i < instance.providerAmount; i++)
        {
            int[] colIndices = new int[2];
            colIndices[0] = i + 1;
            for(int j = 0; j < instance.clientAmount; j++)
            {
                colIndices[1] = instance.providerAmount + rowCounter;
                GLPK.glp_set_row_bnds(LP, rowCounter, GLPKConstants.GLP_UP, 0, 0);
                setMatrixRow(LP, rowCounter, colIndices, values);
                rowCounter++;
            }
        }

        int rowAmount = GLPK.glp_get_num_rows(LP);

        // sum(yi,j) = 1

        GLPK.glp_add_rows(LP, instance.clientAmount);

        for(int j = 0; j < instance.clientAmount ; j++)
        {
            values = new double[instance.providerAmount];
            int[] colIndices = new int[instance.providerAmount];
            colCounter = 0;
            for(int i = instance.providerAmount + 1 + j; i < GLPK.glp_get_num_cols(LP) + 1; i += instance.clientAmount)
            {
                colIndices[colCounter] = i;
                values[colCounter++] = 1;
            }
            GLPK.glp_set_row_bnds(LP, rowAmount + j + 1, GLPKConstants.GLP_LO, 1, 0);
            setMatrixRow(LP, rowAmount + j + 1, colIndices, values);
        }

        // solve
        glp_smcp simplexParams = new glp_smcp();
        GLPK.glp_init_smcp(simplexParams);
        simplexParams.setMsg_lev(GLPKConstants.GLP_MSG_OFF);

        GLPK.glp_simplex(LP, simplexParams);

        for(int i = 1; i < instance.providerAmount + 1; i++)
        {
            openingProbabilities.add(GLPK.glp_get_col_prim(LP, i));
        }

        return openingProbabilities;
    }

    private static void setMatrixRow(glp_prob LP, int row, int[] colIndices, double[] values)
    {
        SWIGTYPE_p_int glpkIndices = Utils.intArrayToSwig(colIndices.length, colIndices);
        SWIGTYPE_p_double glpkValues = Utils.doubleArrayToSwig(values.length, values);

        GLPK.glp_set_mat_row(LP, row, colIndices.length , glpkIndices, glpkValues);

        GLPK.delete_intArray(glpkIndices);
        GLPK.delete_doubleArray(glpkValues);
    }

    private static int customEval(ProblemInstance instance, ArrayList<Integer> solution)
    {
        if(solution.size() == 0)
            return Integer.MAX_VALUE;
        else
            return instance.eval(solution);
    }
}
