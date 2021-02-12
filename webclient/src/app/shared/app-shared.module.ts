import { CommonModule } from "@angular/common"
import { NgModule } from "@angular/core"
import { AppMaterialModule } from "./app-material.module"
import { ImgSetPipe } from "./imgset.pipe"
import { TonePipe } from "./tone.pipe"

@NgModule({
  declarations: [
    ImgSetPipe,
    TonePipe,
  ],
  imports: [
    CommonModule,
    AppMaterialModule,
  ],
  exports: [
    AppMaterialModule,
    ImgSetPipe,
    TonePipe,
  ]
})
export class AppSharedModule { }
