import { OperatorFunction, Observable } from 'rxjs';

/**
 * Transform items from that stream by applying the provided transform, 
 * ignoring elements whose transform returns null or undefined.
 * 
 * @param transform The transform function to apply to each element.
 */
export function mapIfDefined<T, R extends Object>(
  transform: (element: T) => R | null | undefined
): OperatorFunction<T, R> {
  return (source: Observable<T>) => {
    return new Observable<R>(subscriber => {

      const subscription = source.subscribe({
        next: (element: T) => {
          try {
            const transformedElement = transform(element);
            if (transformedElement) {
              subscriber.next(transformedElement);
            }
          } catch (userError) {
            subscriber.error(userError);
          }
        },
        error: (upstreamError) => subscriber.error(upstreamError),
        complete: () => subscriber.complete()
      });

      return subscription;
    });
  };
}