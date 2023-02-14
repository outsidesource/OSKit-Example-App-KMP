# OSKit Example App (KMP)
## Module Structure
* `android` - All Android specific code 
* `common` - All common code accessible to all modules
* `desktop` - All Desktop (JVM) specific code
* `composeUI` - All Jetpack Compose code. This is shared by the Android and Desktop modules

## Architectural Components ([diagram](architecture-diagram.png))
### View
* A visual representation of application state to the user
* Observes and communicates with a `ViewInteractor` only
### ViewInteractor
* Provides an observable [lense](https://sinusoid.es/misc/lager/lenses.pdf) for application state
* Manages view-specific state
* Provides hooks into the view for user interaction and view lifecycle
* May observe/depend on zero or many `Interactors`/`ViewInteractors`
* Communicates with `Coordinator` 
* Communicates with `Interactor`
* `ViewInteractors` extend `Interactor` and only differ in purpose
### Coordinator
* Facilitates application navigation by providing observable of the current `Route` and providing hooks to navigate from one `Route` to another
* One coordinator per user experience flow
### Interactor
* Manages application state for its domain
* May observe/depend on zero or many `Interactors`
* Communicates with `Services`
### Service
* Communicates with external data sources and hardware
### Model/Entity
* A (generally immutable) object or data structure
* Provides business rules for the given data
* Provides transformation/manipulation functions
### Route
* A data representation of a visual location in a UX workflow (typically a screen)

## Folder Structure
* `lib` - All application libraries/utilities
* `model` - All application data models/entities and pure functions that work with or transform them
* `ui` - All views/composables and UI specific logic including `ViewInteractors`
* `service` - All `Service` definition and implementations
* `state` - All `Interactors` and their state definitions