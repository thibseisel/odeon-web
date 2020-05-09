import { Pipe, PipeTransform } from "@angular/core"
import { ImageSpec } from "./remote-models"

/**
 * Transforms a set of image specifications (`ImageSpec`) to the syntax accepted by
 * the `srcset` attribute of the `img` HTML element.
 */
@Pipe({name: "imgset"})
export class ImgSetPipe implements PipeTransform {

  transform(value: ReadonlyArray<ImageSpec>): string | undefined {
    if (value.length > 0) {
      return value.map(image => {
        if (image.width) {
          return `${image.url} ${image.width}w`
        } else {
          return `${image.url} 1x`
        }
      }).join()

    } else {
      return undefined
    }
  }
}
