package ui.file

import service.preferences.PreferencesService
import com.outsidesource.oskitkmp.filesystem.*
import com.outsidesource.oskitkmp.filesystem.KmpFileMetadata
import com.outsidesource.oskitkmp.filesystem.KmpFsRef
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.io.use
import com.outsidesource.oskitkmp.outcome.Outcome
import com.outsidesource.oskitkmp.outcome.unwrapOrNull
import com.outsidesource.oskitkmp.outcome.unwrapOrReturn
import kotlinx.coroutines.launch

data class FileSystemScreenViewState(
    val fsType: KmpFsType = KmpFsType.Internal,
    val activeRefType: ActiveRefType = ActiveRefType.InternalRoot,
    val activeRef: KmpFsRef? = null,

    val persistedRef: KmpFsRef? = null,
    val activeRefMetadataResult: Outcome<KmpFileMetadata, Any>? = null,
    val pickedFileResult: Outcome<KmpFsRef?, Any>? = null,
    val pickedFilesResult: Outcome<List<KmpFsRef>?, Any>? = null,
    val pickedSaveFileResult: Outcome<KmpFsRef?, Any>? = null,
    val pickedDirectoryResult: Outcome<KmpFsRef?, Any>? = null,
    val saveFileContent: String = "",
    val saveFileResult: Outcome<Unit, Any>? = null,
    val resolveFileName: String = "",
    val resolveFileCreate: Boolean = false,
    val resolveFileResult: Outcome<KmpFsRef, Any>? = null,
    val resolveNestedFilePath: String = "",
    val resolveNestedFileCreate: Boolean = false,
    val resolveNestedFileResult: Outcome<KmpFsRef, Any>? = null,
    val resolveDirectoryName: String = "",
    val resolveDirectoryCreate: Boolean = false,
    val resolveDirectoryResult: Outcome<KmpFsRef, Any>? = null,
    val resolveNestedDirectoryPath: String = "",
    val resolveNestedDirectoryCreate: Boolean = false,
    val resolveNestedDirectoryResult: Outcome<KmpFsRef, Any>? = null,
    val resolveRefFromPathPath: String = "",
    val resolveRefFromPathResult: Outcome<KmpFsRef, Any>? = null,
    val deleteRefResult: Outcome<Unit, Any>? = null,
    val isDeleteModalVisible: Boolean = false,
    val listRecursive: Boolean = false,
    val listResult: Outcome<List<Any>, Any>? = null,
    val refExistsResult: Boolean? = null,
    val readFileResult: Outcome<String, Any>? = null,
    val writeFileMode: KmpFsWriteMode = KmpFsWriteMode.Append,
    val writeFileContent: String = "",
    val writeFileResult: Outcome<Unit, Any>? = null,
    val isNoRefSelectedModalVisible: Boolean = false,
)

enum class ActiveRefType {
    PersistedRef,
    InternalRoot,
    PickedFile,
    PickedDirectory,
    ResolvedFile,
    ResolvedDirectory,
    ResolvedFromPath,
    ;

    companion object {
        fun forFsType(type: KmpFsType): List<ActiveRefType> = when (type) {
            KmpFsType.Internal -> listOf(
                InternalRoot,
                PersistedRef,
                ResolvedFile,
                ResolvedDirectory,
            )

            KmpFsType.External -> listOf(
                PickedFile,
                PickedDirectory,
                PersistedRef,
                ResolvedFile,
                ResolvedDirectory,
                ResolvedFromPath,
            )
        }
    }
}

class FileSystemViewInteractor(
    private val externalFs: IExternalKmpFs,
    private val internalFs: IInternalKmpFs,
    private val preferencesService: PreferencesService,
): Interactor<FileSystemScreenViewState>(
    initialState = FileSystemScreenViewState()
) {

    val fs: IKmpFs
        get() = when (state.fsType) {
            KmpFsType.Internal -> internalFs
            KmpFsType.External -> externalFs
        }

    override fun computed(state: FileSystemScreenViewState): FileSystemScreenViewState {
        return state.copy(
            activeRef = when (state.activeRefType) {
                ActiveRefType.PickedFile -> state.pickedFileResult?.unwrapOrNull()
                ActiveRefType.PickedDirectory -> state.pickedDirectoryResult?.unwrapOrNull()
                ActiveRefType.ResolvedFile -> state.resolveFileResult?.unwrapOrNull()
                ActiveRefType.PersistedRef -> state.persistedRef
                ActiveRefType.ResolvedDirectory -> state.resolveDirectoryResult?.unwrapOrNull()
                ActiveRefType.ResolvedFromPath -> state.resolveRefFromPathResult?.unwrapOrNull()
                ActiveRefType.InternalRoot -> internalFs.root
            }
        )
    }

    fun onMounted() {
        loadPersistedRef()
    }

    fun fsTypeClicked(type: KmpFsType) {
        update { state ->
            state.copy(
                fsType = type,
                activeRefType = when (type) {
                    KmpFsType.Internal -> ActiveRefType.InternalRoot
                    KmpFsType.External -> ActiveRefType.PickedFile
                },
                persistedRef = null,
                activeRefMetadataResult = null,
                pickedFileResult = null,
                pickedFilesResult = null,
                pickedSaveFileResult = null,
                pickedDirectoryResult = null,
                saveFileResult = null,
                resolveFileResult = null,
                resolveDirectoryResult = null,
                resolveRefFromPathResult = null,
                deleteRefResult = null,
                listResult = null,
                refExistsResult = null,
                readFileResult = null,
                writeFileResult = null,
            )
        }

        loadPersistedRef()
    }

    fun activeRefTypeChanged(type: ActiveRefType) {
        update { state -> state.copy(activeRefType = type) }
    }

    fun persistRef() {
        interactorScope.launch {
            preferencesService.setPersistedRef(state.activeRef, state.fsType)
            update { state -> state.copy(persistedRef = state.activeRef) }
        }
    }

    fun loadPersistedRef() {
        interactorScope.launch {
            val ref = preferencesService.getPersistedRef(state.fsType)
            update { state -> state.copy(persistedRef = ref) }
        }
    }

    fun pickFile() {
        interactorScope.launch {
            val result = externalFs.pickFile()
            update { state -> state.copy(pickedFileResult = result) }
        }
    }

    fun pickFiles() {
        interactorScope.launch {
            val result = externalFs.pickFiles()
            update { state -> state.copy(pickedFilesResult = result) }
        }
    }

    fun pickSaveFile() {
        interactorScope.launch {
            val saveFileOutcome = externalFs.pickSaveFile("untitled.txt")
            update { state -> state.copy(pickedSaveFileResult = saveFileOutcome) }
        }
    }

    fun saveFileContentChanged(value: String) {
        update { state -> state.copy(saveFileContent = value) }
    }

    fun saveFile() {
        interactorScope.launch {
            val result = externalFs.saveFile(state.saveFileContent.encodeToByteArray(), "Untitled.txt")
            update { state -> state.copy(saveFileResult = result) }
        }
    }

    fun pickDirectory() {
        interactorScope.launch {
            val result = externalFs.pickDirectory()
            update { state -> state.copy(pickedDirectoryResult = result) }
        }
    }

    fun appendToFileContentChanged(value: String) {
        interactorScope.launch {
            update { state -> state.copy(writeFileContent = value) }
        }
    }

    fun writeToFile() {
       interactorScope.launch {
            val ref = getActiveRef() ?: return@launch
            val sink = ref.sink(mode = state.writeFileMode).unwrapOrReturn {
                update { state -> state.copy(writeFileResult = it) }
                return@launch
            }

            sink.use {
               it.writeUtf8(state.writeFileContent)
            }

            update { state -> state.copy(writeFileResult = Outcome.Ok(Unit)) }
       }
    }

    fun writeModeChanged(value: KmpFsWriteMode) {
        update { state -> state.copy(writeFileMode = value) }
    }

    fun resolveFileNameChanged(value: String) {
        update { state -> state.copy(resolveFileName = value) }
    }

    fun resolveFileCreateChanged(value: Boolean) {
        update { state -> state.copy(resolveFileCreate = value) }
    }

    fun resolveFile() {
        interactorScope.launch {
            val ref = getActiveRef() ?: return@launch

            val file = fs.resolveFile(ref, state.resolveFileName, create = state.resolveFileCreate).unwrapOrReturn {
                update { state -> state.copy(resolveFileResult = it) }
                return@launch
            }

            update { state -> state.copy(resolveFileResult = Outcome.Ok(file)) }
        }
    }

    fun resolveNestedFilePathChanged(value: String) {
        update { state -> state.copy(resolveNestedFilePath = value) }
    }

    fun resolveNestedFileCreateChanged(value: Boolean) {
        update { state -> state.copy(resolveNestedFileCreate = value) }
    }

    fun resolveNestedFile() {
        interactorScope.launch {
            val ref = getActiveRef() ?: return@launch

            val file = fs.resolveFileWithPath(
                dir = ref,
                path = state.resolveNestedFilePath,
                create = state.resolveNestedFileCreate
            ).unwrapOrReturn {
                update { state -> state.copy(resolveNestedFileResult = it) }
                return@launch
            }

            update { state -> state.copy(resolveNestedFileResult = Outcome.Ok(file)) }
        }
    }

    fun resolveDirectoryNameChanged(value: String) {
        update { state -> state.copy(resolveDirectoryName = value) }
    }

    fun resolveDirectoryCreateChanged(value: Boolean) {
        update { state -> state.copy(resolveDirectoryCreate = value) }
    }

    fun resolveDirectory() {
        interactorScope.launch {
            val ref = getActiveRef() ?: return@launch
            val createdFolder = fs.resolveDirectory(
                dir = ref,
                name = state.resolveDirectoryName,
                create = state.resolveDirectoryCreate
            ).unwrapOrReturn {
                update { state -> state.copy(resolveDirectoryResult = it) }
                return@launch
            }

            update { state -> state.copy(resolveDirectoryResult = Outcome.Ok(createdFolder)) }
        }
    }

    fun resolveNestedDirectoryPathChanged(value: String) {
        update { state -> state.copy(resolveNestedDirectoryPath = value) }
    }

    fun resolveNestedDirectoryCreateChanged(value: Boolean) {
        update { state -> state.copy(resolveNestedDirectoryCreate = value) }
    }

    fun resolveNestedDirectory() {
        interactorScope.launch {
            val ref = getActiveRef() ?: return@launch

            val file = fs.resolveDirectoryWithPath(
                dir = ref,
                path = state.resolveNestedDirectoryPath,
                create = state.resolveNestedDirectoryCreate,
            ).unwrapOrReturn {
                update { state -> state.copy(resolveNestedDirectoryResult = it) }
                return@launch
            }

            update { state -> state.copy(resolveNestedDirectoryResult = Outcome.Ok(file)) }
        }
    }

    fun resolveRefFromPathChanged(value: String) {
        update { state -> state.copy(resolveRefFromPathPath = value) }
    }

    fun resolveRefFromPath() {
        interactorScope.launch {
            val result = externalFs.resolveRefFromPath(state.resolveRefFromPathPath)
            update { state -> state.copy(resolveRefFromPathResult = result)}
        }
    }

    fun readRef() {
        interactorScope.launch {
            val source = getActiveRef()?.source()?.unwrapOrReturn {
                update { state -> state.copy(readFileResult = it) }
                return@launch
            } ?: return@launch

            val content = source.use { it.readAllUtf8() }
            update { state -> state.copy(readFileResult = content)}
        }
    }

    fun readMetadata() {
        interactorScope.launch {
            val outcome = fs.readMetadata(getActiveRef() ?: return@launch)
            update { state -> state.copy(activeRefMetadataResult = outcome) }
        }
    }

    fun deleteActiveRefClicked() {
        update { state -> state.copy(isDeleteModalVisible = true) }
    }

    fun deleteModalDismissed() {
        update { state -> state.copy(isDeleteModalVisible = false) }
    }

    fun deleteModalConfirmed() {
        interactorScope.launch {
            update { state -> state.copy(isDeleteModalVisible = false) }
            val outcome = fs.delete(getActiveRef() ?: return@launch)
            update { state -> state.copy(deleteRefResult = outcome) }
        }
    }

    fun listRecursiveCheckChanged(value: Boolean) {
        update { state -> state.copy(listRecursive = value) }
    }

    fun list() {
        interactorScope.launch {
            val ref = getActiveRef() ?: return@launch
            val result = if (state.listRecursive) {
                fs.listWithDepth(ref)
            } else {
                fs.list(ref, isRecursive = false)
            }

            update { state -> state.copy(listResult = result) }
        }
    }

    fun exists() {
        interactorScope.launch {
            val ref = getActiveRef() ?: return@launch
            val refExists = fs.exists(ref)

            update { state ->
                state.copy(refExistsResult = refExists)
            }
        }
    }

    fun dismissNoRefSelectedModal() {
        update { state -> state.copy(isNoRefSelectedModalVisible = false) }
    }

    private fun getActiveRef(): KmpFsRef? {
        val ref = state.activeRef
        if (ref == null) update { state -> state.copy(isNoRefSelectedModalVisible = true) }
        return ref
    }
}