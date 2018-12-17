package ch.epfl.sweng.favors.utils;

/**
 * Singleton class that give the execution Mode of the app : Test, Normal
 */
public final class ExecutionMode {
    private final static ExecutionMode instance = new ExecutionMode();

    private boolean test;
    private boolean invalidAuthTest;
    private GeocoderExecutionTestMode geocoderExecutionTestMode;

    public enum GeocoderExecutionTestMode {SUCCESS, FAILURE, EXCEPTION}

    /**
     * Construct the singleton class
     */
    private ExecutionMode(){
        if(instance != null)
            throw new IllegalStateException("ExecutionMode already instantiated");
        test = false;
        invalidAuthTest = false;
        geocoderExecutionTestMode = GeocoderExecutionTestMode.SUCCESS;
    }

    /**
     * @return the instance of the mode of execution
     */
    public final static ExecutionMode getInstance(){
        return instance;
    }

    /**
     * @throws IllegalStateException if the excecution mode is not set to test
     */
    public static void mustBeInTestMode(){
        if(!ExecutionMode.getInstance().isTest()){
            throw new IllegalStateException("Method must be accesssed only for testing purpose");
        }
    }

    /**
     * @return true if the program is running in test mode
     */
    public boolean isTest(){return test;}

    /**
     * Setter for the test mode
     * @param test boolean meaning if the test mode is activated(true) or not (false)
     */
    public void setTest(boolean test){
        this.test = test;
    }

    /**
     * Setter for the invalid user test mode
     * @param invalidAuthTest boolean meaning if the current user is invalid(true) or not (false)
     * @throws IllegalStateException if the excecution mode is not set to test
     */
    public void setInvalidAuthTest(boolean invalidAuthTest) {
        mustBeInTestMode();
        this.invalidAuthTest = invalidAuthTest;
    }

    /**
     * @return true if the user is invalid
     */
    public boolean isInvalidAuthTest() {
        return invalidAuthTest;
    }

    /**
     * @return the current execution mode of the geocoder
     */
    public GeocoderExecutionTestMode getGeocoderExecutionTestMode() {
        return geocoderExecutionTestMode;
    }

    /**
     * @param geocoderExecutionTestMode new execution test mode for the geocoder of test
     * @throws IllegalStateException if the excecution mode is not set to test
     */
    public void setGeocoderExecutionTestMode(GeocoderExecutionTestMode geocoderExecutionTestMode) {
        mustBeInTestMode();
        this.geocoderExecutionTestMode = geocoderExecutionTestMode;
    }
}
