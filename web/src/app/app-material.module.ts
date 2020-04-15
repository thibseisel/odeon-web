import { NgModule } from "@angular/core";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FlexLayoutModule } from "@angular/flex-layout";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatDividerModule } from "@angular/material/divider";
import { MatCardModule } from "@angular/material/card";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatRippleModule } from '@angular/material/core';
import { MatButtonModule } from "@angular/material/button";

/**
 * Groups imports of modules from @angular/material.
 *
 * All modules imported here should also be exported.
 * This prevents from filling the main app module with imports for each component.
 * Also, should we use another Material Design library,
 * all declarations required for Angular Material to work are listed in this only file.
 */
@NgModule({
    imports: [
        BrowserAnimationsModule,
        FlexLayoutModule,
        MatFormFieldModule,
        MatInputModule,
        MatListModule,
        MatDividerModule,
        MatCardModule,
        MatProgressBarModule,
        MatToolbarModule,
        MatRippleModule,
        MatButtonModule,
    ],
    exports: [
        BrowserAnimationsModule,
        FlexLayoutModule,
        MatFormFieldModule,
        MatInputModule,
        MatListModule,
        MatDividerModule,
        MatCardModule,
        MatProgressBarModule,
        MatToolbarModule,
        MatRippleModule,
        MatButtonModule,
    ]
})
export class AppMaterialModule { }
