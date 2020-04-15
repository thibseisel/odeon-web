import { Component, Input } from '@angular/core'

type CartridgeColorTheme = "default" | "primary" | "primary-dark" | "secondary"

@Component({
  selector: 'app-odeon-cartridge',
  templateUrl: './odeon-cartridge.component.html',
  styleUrls: ['./odeon-cartridge.component.scss']
})
export class OdeonCartridgeComponent {
  @Input() color: CartridgeColorTheme = "default"

  public get backgroundClass(): string {
    switch (this.color) {
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
