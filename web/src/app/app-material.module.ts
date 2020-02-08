import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';

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
        MatInputModule,
        MatListModule,
        MatDividerModule,
        MatCardModule
    ],
    exports: [
        BrowserAnimationsModule,
        MatInputModule,
        MatListModule,
        MatDividerModule,
        MatCardModule
    ]
})
export class AppMaterialModule { }
