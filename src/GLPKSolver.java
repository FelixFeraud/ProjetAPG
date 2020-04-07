import org.gnu.glpk.*;

import java.util.ArrayList;

public class GLPKSolver
{
    public static ArrayList<Integer> solve(ProblemInstance instance)
    {
        System.out.println("Résolution de " + instance.name + " avec GLPK (version " + GLPK.glp_version() + ")\n");
        ArrayList<Integer> solution = new ArrayList<Integer>();

        glp_prob LP = GLPK.glp_create_prob();

        // Objective function
        GLPK.glp_set_obj_dir(LP, GLPKConstants.GLP_MIN);


        return solution;
    }

}
