/**
 * Configuration accross environments.
 */
export interface EnvironmentConfig {
    
    /**
     * Whether this targets the production environmnent.
     */
    production: boolean;

    /**
     * Whether HTTP calls should be mocked to return pre-defined data.
     */
    useMockServer: boolean;
}