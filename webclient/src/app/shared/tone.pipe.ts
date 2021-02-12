import { Pipe, PipeTransform } from "@angular/core"
import { keyDisplayName, MusicalMode, Pitch } from "./remote-models"

@Pipe({
  name: "tone"
})
export class TonePipe implements PipeTransform {

  transform(key: Pitch | null | undefined, mode?: MusicalMode): string {
    const keyName = keyDisplayName(key)
    const modeName = this.getModeName(mode)
    return `${keyName}${modeName}`
  }

  private getModeName(mode: MusicalMode | undefined): string {
    switch (mode) {
    case MusicalMode.MAJOR:
      return "M"
    case MusicalMode.MINOR:
      return "m"
    default:
      return ""
    }
  }
}
