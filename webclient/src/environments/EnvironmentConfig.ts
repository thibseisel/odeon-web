/**
 * Configuration across environments.
 */
export interface EnvironmentConfig {

  /**
   * Whether this targets the production environment.
   */
  production: boolean

  /**
   * The base URL used to access the server's REST API.
   * The specified path should *not* end with a "/" character.
   */
  apiBase: string
}
