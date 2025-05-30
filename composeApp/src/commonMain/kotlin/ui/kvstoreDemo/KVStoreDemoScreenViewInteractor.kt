package ui.kvstoreDemo

import com.outsidesource.oskitkmp.interactor.Interactor
import service.kvstoreDemo.IKVStoreDemoService

data class KVStoreDemoScreenViewState(
    val test: String = "",
)

class KVStoreDemoScreenViewInteractor(
    private val kvStoreDemoService: IKVStoreDemoService,
) : Interactor<KVStoreDemoScreenViewState>(
    initialState = KVStoreDemoScreenViewState()
) {


}
