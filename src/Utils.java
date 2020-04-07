import org.gnu.glpk.GLPK;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;

class Utils
{
    /**
     * @param length The length of the array passed as parameter, and consequently of the returned SWIG array.
     * @param array Array of integers.
     * @return Array of type SWIGTYPE_p_int for use by GLPK.
     */
    static SWIGTYPE_p_int intArrayToSwig(int length, int ... array)
    {
        if(array.length != length) throw new IllegalArgumentException("Array size must be equal to the size passed as parameter.");

        SWIGTYPE_p_int swigArray = GLPK.new_intArray(length+1);
        for(int i = 0; i < length; i++)
        {
            GLPK.intArray_setitem(swigArray, i+1, array[i]);
        }

        return swigArray;
    }

    /**
     * @param length The length of the array passed as parameter, and consequently of the returned SWIG array.
     * @param array Array of doubles
     * @return Array of type SWIGTYPE_p_double for use by GLPK.
     */
    static SWIGTYPE_p_double doubleArrayToSwig(int length, double ... array)
    {
        if(array.length != length) throw new IllegalArgumentException("Array size must be equal to the size passed as parameter.");

        SWIGTYPE_p_double swigArray = GLPK.new_doubleArray(length+1);
        for(int i = 0; i < length; i++)
        {
            GLPK.doubleArray_setitem(swigArray, i+1, array[i]);
        }

        return swigArray;
    }
}
