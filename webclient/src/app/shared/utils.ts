/**
 * A function to use as the parameter of `rxjs filter`
 * to cast the receiver as non-null or non-undefined.
 *
 * @param value The value to check for `null` or `undefined`.
 * @returns `true` if the specified `value` is safe to call methods on.
 */
export function isDefined<T>(value: T | null | undefined): value is NonNullable<T> {
  return value != undefined
}
