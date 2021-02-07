/**
 * A type wrapper that makes all properties of a type `T` writeable (i.e. not `readonly`).
 * This is the opposite of the `Readonly<T>` type wrapper.
 */
export type Mutable<T> = { -readonly [P in keyof T]: T[P] }
