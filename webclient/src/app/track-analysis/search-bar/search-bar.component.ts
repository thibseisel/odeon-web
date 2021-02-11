import { Input, OnInit } from "@angular/core"
import { ChangeDetectionStrategy, Component, EventEmitter, OnDestroy, Output } from "@angular/core"
import { FormControl } from "@angular/forms"
import { Subscription } from "rxjs"
import { startWith } from "rxjs/operators"

@Component({
  selector: "app-search-bar",
  templateUrl: "./search-bar.component.html",
  styleUrls: ["./search-bar.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SearchBarComponent implements OnInit, OnDestroy {
  @Input() readonly searchPending = false
  @Output() readonly query = new EventEmitter<string>()

  readonly _searchInput = new FormControl("")

  private readonly subscriptions = new Subscription()

  ngOnInit(): void {
    const query$ = this._searchInput.valueChanges.pipe(
      startWith(this._searchInput.value)
    )
    const subscription = query$.subscribe(query => this.query.emit(query))
    this.subscriptions.add(subscription)
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe()
  }

  clearQuery(): void {
    this._searchInput.setValue("")
  }
}
