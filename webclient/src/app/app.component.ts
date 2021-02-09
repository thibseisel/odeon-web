import { MediaMatcher } from "@angular/cdk/layout"
import { Component, OnInit } from "@angular/core"

const DARK_THEME_CLASSNAME = "dark-theme-overlay"

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: []
})
export class AppComponent implements OnInit {

  constructor(private readonly mediaMatcher: MediaMatcher) { }

  ngOnInit(): void {
    const darkColorSchemeMediaQuery = this.mediaMatcher.matchMedia("(prefers-color-scheme: dark)")
    this.setTheme(darkColorSchemeMediaQuery.matches)
    darkColorSchemeMediaQuery.addEventListener("change", event => {
      this.setTheme(event.matches)
    })
  }

  private setTheme(isDarkTheme: boolean) {
    const bodyClasses = document.body.classList
    if (isDarkTheme) {
      bodyClasses.add(DARK_THEME_CLASSNAME)
    } else {
      bodyClasses.remove(DARK_THEME_CLASSNAME)
    }
  }
}
