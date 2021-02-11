import { LayoutModule } from "@angular/cdk/layout"
import { NgModule } from "@angular/core"
import { MatButtonModule } from "@angular/material/button"
import { MatCardModule } from "@angular/material/card"
import { MatRippleModule } from "@angular/material/core"
import { MatDividerModule } from "@angular/material/divider"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatIconModule } from "@angular/material/icon"
import { MatInputModule } from "@angular/material/input"
import { MatListModule } from "@angular/material/list"
import { MatProgressBarModule } from "@angular/material/progress-bar"
import { MatToolbarModule } from "@angular/material/toolbar"

const modules = [
  LayoutModule,
  MatFormFieldModule,
  MatInputModule,
  MatListModule,
  MatDividerModule,
  MatCardModule,
  MatProgressBarModule,
  MatToolbarModule,
  MatRippleModule,
  MatButtonModule,
  MatIconModule,
]

/**
 * Groups imports of modules from @angular/material.
 *
 * All modules imported here should are also exported.
 * This prevents from filling the main app module with imports for each component.
 * Also, should we use another Material Design library,
 * all declarations required for Angular Material to work are listed in this only file.
 */
@NgModule({
  imports: modules,
  exports: modules
})
export class AppMaterialModule { }
