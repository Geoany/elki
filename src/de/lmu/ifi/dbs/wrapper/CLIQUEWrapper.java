package de.lmu.ifi.dbs.wrapper;

import de.lmu.ifi.dbs.algorithm.AbortException;
import de.lmu.ifi.dbs.algorithm.clustering.clique.CLIQUE;
import de.lmu.ifi.dbs.utilities.Util;
import de.lmu.ifi.dbs.utilities.optionhandling.DoubleParameter;
import de.lmu.ifi.dbs.utilities.optionhandling.Flag;
import de.lmu.ifi.dbs.utilities.optionhandling.IntParameter;
import de.lmu.ifi.dbs.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.utilities.optionhandling.ParameterException;
import de.lmu.ifi.dbs.utilities.optionhandling.constraints.GreaterConstraint;
import de.lmu.ifi.dbs.utilities.optionhandling.constraints.IntervalConstraint;

import java.util.List;

/**
 * Wrapper class for CLIQUE algorithm.
 *
 * @author Elke Achtert
 */
public class CLIQUEWrapper extends FileBasedDatabaseConnectionWrapper {

    /**
     * Parameter to specify the number of intervals (units) in each dimension,
     * must be an integer greater than 0.
     * <p>Key: {@code -clique.xsi} </p>
     */
    private final IntParameter XSI_PARAM = new IntParameter(OptionID.CLIQUE_XSI, new GreaterConstraint(0));

    /**
     * Parameter to specify the density threshold for the selectivity of a unit,
     * where the selectivity is the fraction of total feature vectors contained in this unit,
     * must be a double greater than 0 and less than 1.
     * <p>Key: {@code -clique.tau} </p>
     */
    private final DoubleParameter TAU_PARAM = new DoubleParameter(OptionID.CLIQUE_TAU,
        new IntervalConstraint(0, IntervalConstraint.IntervalBoundary.OPEN, 1, IntervalConstraint.IntervalBoundary.OPEN));

    /**
     * Flag to indicate that that only subspaces with large coverage
     * (i.e. the fraction of the database that is covered by the dense units)
     * are selected, the rest will be pruned.
     * <p>Key: {@code -clique.prune} </p>
     */
    private final Flag PRUNE_FLAG = new Flag(OptionID.CLIQUE_PRUNE);


    /**
     * Main method to run this wrapper.
     *
     * @param args the arguments to run this wrapper
     */
    public static void main(String[] args) {
        CLIQUEWrapper wrapper = new CLIQUEWrapper();
        try {
            wrapper.setParameters(args);
            wrapper.run();
        }
        catch (ParameterException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            wrapper.exception(wrapper.optionHandler.usage(e.getMessage()), cause);
        }
        catch (AbortException e) {
            wrapper.verbose(e.getMessage());
        }
        catch (Exception e) {
            wrapper.exception(wrapper.optionHandler.usage(e.getMessage()), e);
        }
    }

    /**
     * Sets the parameters epsilon and minpts in the parameter map additionally to the
     * parameters provided by super-classes.
     */
    public CLIQUEWrapper() {
        super();
        //parameter xsi
        addOption(XSI_PARAM);

        //parameter tau
        addOption(TAU_PARAM);

        //flag prune
        addOption(PRUNE_FLAG);
    }

    /**
     * @see de.lmu.ifi.dbs.wrapper.KDDTaskWrapper#getKDDTaskParameters()
     */
    public List<String> getKDDTaskParameters() {
        List<String> parameters = super.getKDDTaskParameters();

        // algorithm CLIQUE
        Util.addParameter(parameters, OptionID.ALGORITHM, CLIQUE.class.getName());

        // xsi
        Util.addParameter(parameters, XSI_PARAM, Integer.toString(getParameterValue(XSI_PARAM)));

        // tau
        Util.addParameter(parameters, TAU_PARAM, Double.toString(getParameterValue(TAU_PARAM)));

        // prune
        if (isSet(PRUNE_FLAG)) {
            Util.addFlag(parameters, PRUNE_FLAG);
        }

        return parameters;
    }
}
