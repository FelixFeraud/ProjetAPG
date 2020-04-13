import org.gnu.glpk.*;

import java.util.ArrayList;

class GLPKSolver
{
    /**
     * Cherche une solution optimale à une instance de problème de fournisseurs.
     * @param instance L'instance du problème à résoudre.
     * @return Une liste d'entiers indiquant les fournisseurs à ouvrir.
     */
    static ArrayList<Integer> solve(ProblemInstance instance)
    {
        System.out.println("Résolution de " + instance.name + " avec GLPK (version " + GLPK.glp_version() + ")\n");
        ArrayList<Integer> solution = new ArrayList<>();

        glp_prob LP = GLPK.glp_create_prob();

        GLPK.glp_set_obj_dir(LP, GLPKConstants.GLP_MIN);

        GLPK.glp_add_cols(LP, instance.providerAmount + (instance.clientAmount * instance.providerAmount));

        // On ajoute les X (fournisseurs)
        for(int i = 0; i < instance.providerAmount; i++)
        {
            GLPK.glp_set_col_kind(LP, i + 1, GLPKConstants.GLP_BV);
            GLPK.glp_set_obj_coef(LP, i + 1, (double)instance.providerOpeningCosts[i]);

            String colName = "x" + (i + 1);
            GLPK.glp_set_col_name(LP, i + 1, colName);
        }

        // Ensuite les Y (connexions clients/fournisseurs)
        int colCounter = 0;
        for(int i = 0; i < instance.providerAmount; i++)
        {
            for(int j = 0; j < instance.clientAmount; j++)
            {
                GLPK.glp_set_obj_coef(LP, instance.providerAmount + ++colCounter, (double)instance.clientConnectionCosts[i][j]);

                String colName = "y" + (i+1) + "," + (j+1);
                GLPK.glp_set_col_kind(LP, instance.providerAmount + colCounter, GLPKConstants.GLP_BV);
                GLPK.glp_set_col_name(LP, instance.providerAmount + colCounter, colName);
            }
        }

        // Faire en sorte que Yi,j = 1 seulement si Xi = 1.
        // Pour cela, chaque ligne sera construite de cette manière :
        // -Xi + Yi,j <= 0
        // Ce qui peut être simplifié à Yi,j <= Xi

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

        // Seul 1 fournisseur fournit un client.

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
            GLPK.glp_set_row_bnds(LP, rowAmount + j + 1, GLPKConstants.GLP_FX, 1, 0);
            setMatrixRow(LP, rowAmount + j + 1, colIndices, values);
        }

        // On résout le PL puis le MIP.
        // On peut alors récupérer pour chaque colonne sa valeur, qui sera soit 0 soit 1
        // car les colonnes sont des variables de type binaire (un fournisseurs et ouvert/fermé, une connexion existe/n'existe pas).

        glp_iocp mipParams = new glp_iocp();
        GLPK.glp_init_iocp(mipParams);
        mipParams.setMsg_lev(GLPKConstants.GLP_MSG_OFF);

        glp_smcp simplexParams = new glp_smcp();
        GLPK.glp_init_smcp(simplexParams);
        simplexParams.setMsg_lev(GLPKConstants.GLP_MSG_OFF);

        GLPK.glp_simplex(LP, simplexParams);
        GLPK.glp_intopt(LP, mipParams);

        for(int i = 1; i < instance.providerAmount + 1; i++)
        {
            if(GLPK.glp_mip_col_val(LP, i) == 1)
            {
                solution.add(i - 1);
            }
        }

        return solution;
    }

    /**
     * Fonction utilitaire pour simplifier la création de lignes.
     * @param LP Programme linéaire GLPK.
     * @param row Indice de la ligne.
     * @param colIndices Indices des colonnes où l'on veut insérer les valeurs.
     * @param values Valeurs à insérer aux indices indiqués précédemment.
     */
    private static void setMatrixRow(glp_prob LP, int row, int[] colIndices, double[] values)
    {
        SWIGTYPE_p_int glpkIndices = Utils.intArrayToSwig(colIndices.length, colIndices);
        SWIGTYPE_p_double glpkValues = Utils.doubleArrayToSwig(values.length, values);

        GLPK.glp_set_mat_row(LP, row, colIndices.length , glpkIndices, glpkValues);

        GLPK.delete_intArray(glpkIndices);
        GLPK.delete_doubleArray(glpkValues);
    }
}
