workspace {
    model {
        softwareSystem "Architecture" {
            architectureOverview = container "Architecture Overview" "" "" {
                service = component "Service" "Communicates with external data sources and hardware" "" service
                interactor = component "Interactor" "- Manages application state for its domain\n- Provides observable of state" "" interactor {
                    -> service
                }
                coordinator = component "Coordinator" "Facilitates application navigation" "" coordinator
                viewInteractor = component "View Interactor" "- Provides an observable lense for application state\n- Manages view-specific state\n- Provides hooks into the view for user interaction and view lifecycle" "" viewInteractor {
                    -> interactor
                    -> coordinator
                }
                view = component "View" "A visual representation of application state to the user" "" view {
                    -> viewInteractor
                }
            }
        }
    }


    views {
        component architectureOverview "ArchitectureOverview" {
            include *
            autoLayout bt
        }

        styles {
            element "Group" {
                color #000000
            }
            element service {
                background #00B1FF
            }
            element interactor {
                background #99DFEF
            }
            element viewInteractor {
                background #01D1C3
            }
            element view {
                background #2DA84C
            }
            element coordinator {
                background #B58AFC
            }
        }
    }

}