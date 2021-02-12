import { ChangeDetectionStrategy, Component, Input } from "@angular/core"

type CartridgeColorTheme = "default" | "primary" | "primary-dark" | "secondary"

@Component({
  selector: "app-odeon-cartridge",
  templateUrl: "./odeon-cartridge.component.html",
  styleUrls: ["./odeon-cartridge.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OdeonCartridgeComponent {
  @Input()
  get color(): CartridgeColorTheme { return this._color }
  set color(color: CartridgeColorTheme) {
    this._color = color
    this._backgroundClass = this.backgroundClassForTheme(color)
  }

  private _color: CartridgeColorTheme = "default"
  _backgroundClass = ""

  private backgroundClassForTheme(theme: CartridgeColorTheme): string {
    switch (theme) {
    case "primary":
      return "cartridge--primary"
    case "secondary":
      return "cartridge--secondary"
    case "primary-dark":
      return "cartridge--primary-dark"
    default:
      return ""
    }
  }
}
