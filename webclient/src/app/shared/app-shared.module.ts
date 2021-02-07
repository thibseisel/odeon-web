import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { AppMaterialModule } from "./app-material.module"
import { ImgSetPipe } from "./imgset.pipe"

@NgModule({
  declarations: [ImgSetPipe],
  imports: [
    CommonModule,
    AppMaterialModule,
  ],
  exports: [
    AppMaterialModule,
    ImgSetPipe,
  ]
})
export class AppSharedModule { }
