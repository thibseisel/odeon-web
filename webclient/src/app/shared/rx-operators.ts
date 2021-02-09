import { MonoTypeOperatorFunction, Observable, throwError, timer } from "rxjs"
import { concatMap, retryWhen } from "rxjs/operators"

/**
 * Returns an Observable that retries the source Observable when the conditions of its `predicate` function are met.
 *
 * This function implements an exponential back-off policy:
 * when the `predicate` function first returns `true`, a new attempt is made after waiting `retryDelay` milliseconds.
 * Subsequent attempts delay re-subscription by `attempts^2 * retryDelay` milliseconds.
 * Returning `false` in the `predicate` function results in the returned observable re-throwing the source error.
 *
 * @param retryDelay The time delay to wait between retries, in milliseconds.
 * @param predicate A function that returns true when the source observable should resubscribe to the source Observable.
 * @returns The source Observable modified with retry logic.
 */
export function retryAfter<T>(
  retryDelay: number,
  predicate: (error: unknown, attempts: number) => boolean
): MonoTypeOperatorFunction<T> {
  if (retryDelay <= 0) {
    throw new Error("Retry delay should be strictly positive.")
  }

  return retryWhen((errors: Observable<unknown>) => errors.pipe(
    concatMap((error: unknown, index: number) => {
      if (predicate(error, index)) {
        return timer((2 ** index) * retryDelay)
      } else {
        return throwError(error)
      }
    })
  ))
}
