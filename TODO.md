# TrailWeight TODO

## Worth addressing

- [ ] **1. `GearListOptions` needs addressing, It calls `viewModel()` with no factory, but `GearViewModel` needs a database. It only works because `GearListScreen` has already created one for the same screen.
  **Fix:** pass in the ViewModel, or just an `onExportClick` lambda.

- [ ] **2. Turn on Room schema export before the next release.**
  The database is `version = 1, exportSchema = false`. 

- [ ] **3. The iOS share sheet will crash on iPad.**
  `UIActivityViewController` must be anchored to a view on iPad (`popoverPresentationController.sourceView`). Without that anchor, UIKit throws as soon as the sheet opens.
  Also, `keyWindow` is deprecated: both `ShareText` and the new file picker should get the window from the connected scenes instead.

## Worth adding

- [ ] **4. Quantity, worn and consumable on `GearItem`, and maybe a custom category.**

- [ ] **5. CSV export in LighterPack's format.**

## Housekeeping

- [x] **6. Remove the template leftovers:** `Greeting.kt` and `GreetingUtil.kt`.

- [ ] **7. Change conversion constant.**
  `formatWeight` uses `28.3495` and the importer uses `28.349523125`.

- [ ] **8. Share one repository.**
  Three screens each build their own `GearViewModel` and repository. Shared repository would be cleaner. Passing the repository in as an interface would be cleaner and fakes can be used for testing.