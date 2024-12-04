package ui.file

import com.outsidesource.oskitExample.common.service.preferences.PreferencesService
import com.outsidesource.oskitkmp.file.*
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.Outcome
import com.outsidesource.oskitkmp.outcome.unwrapOrReturn
import kotlinx.coroutines.launch
import okio.buffer
import okio.use

data class FileHandlingViewState(
    val selectedFile: KmpFileRef? = null,
    val selectedFiles: List<KmpFileRef>? = null,
    val selectedDirectory: KmpFileRef? = null,
    val metadata: KMPFileMetadata? = null,
    val deleteFileResult: Outcome<Unit, Exception>? = null,
    val deleteDirectoryResult: Outcome<Unit, Exception>? = null,
    val fileList: List<KmpFileRef> = emptyList(),
    val fileExistsResult: Boolean? = null,
    val directoryExistsResult: Boolean? = null,
    val createFileName: String = "",
    val createFileResult: Outcome<KmpFileRef, Exception>? = null,
    val createFileContent: String = "",
    val createDirectoryName: String = "",
    val createDirectoryResult: Outcome<KmpFileRef, Exception>? = null,
    val saveFile: Outcome<KmpFileRef?, Exception>? = null,
    val appendFileResult: Outcome<Unit, Exception>? = null,
    val appendFileContent: String = "",
    val selectedFileContent: String = "",
)

class FileHandlingViewInteractor(
    private val fileHandler: IKmpFileHandler,
    private val preferencesService: PreferencesService,
): Interactor<FileHandlingViewState>(
    initialState = FileHandlingViewState()
) {

    fun onMounted() {
        interactorScope.launch {
            val storedFile = preferencesService.getSelectedFile()
            val storedFolder = preferencesService.getSelectedFolder()

            update { state -> state.copy(selectedFile = storedFile, selectedDirectory = storedFolder) }
        }
    }

    fun pickFile() {
        interactorScope.launch {
            val file = fileHandler.pickFile().unwrapOrReturn { return@launch }
            update { state -> state.copy(selectedFile = file) }
            preferencesService.setSelectedFile(file)
        }
    }

    fun pickFiles() {
        interactorScope.launch {
            val files = fileHandler.pickFiles().unwrapOrReturn { return@launch }
            update { state -> state.copy(selectedFiles = files) }
        }
    }

    fun pickSaveFile() {
        interactorScope.launch {
            val saveFileOutcome = fileHandler.pickSaveFile("untitled.txt")
            update { state -> state.copy(saveFile = saveFileOutcome) }
        }
    }

    fun pickDirectory() {
        interactorScope.launch {
            val folder = fileHandler.pickDirectory().unwrapOrReturn { return@launch }
            update { state -> state.copy(selectedDirectory = folder) }
            preferencesService.setSelectedFolder(folder)
        }
    }

    fun createFileNameChanged(value: String) {
        update { state -> state.copy(createFileName = value) }
    }

    fun createFileContentChanged(value: String) {
        update { state -> state.copy(createFileContent = value) }
    }

    fun createFile() {
        interactorScope.launch {
            val folder = state.selectedDirectory ?: return@launch
            val file = fileHandler.resolveFile(folder, state.createFileName, create = true).unwrapOrReturn {
                update { state -> state.copy(createFileResult = this) }
                return@launch
            }

            val sink = file.sink().unwrapOrReturn {
                update { state -> state.copy(createFileResult = this) }
                return@launch
            }

            sink.buffer().use {
                it.writeUtf8(state.createFileContent)
            }

            update { state -> state.copy(createFileResult = Outcome.Ok(file)) }
        }
    }

    fun appendToFileContentChanged(value: String) {
        interactorScope.launch {
            update { state -> state.copy(appendFileContent = value) }
        }
    }

    fun appendToFile() {
       interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val sink = file.sink(mode = KMPFileWriteMode.Append).unwrapOrReturn {
                update { state -> state.copy(appendFileResult = this) }
                return@launch
            }

           sink.buffer().use {
               it.writeUtf8(state.appendFileContent)
           }

           update { state -> state.copy(appendFileResult = Outcome.Ok(Unit)) }
       }
    }

    fun createDirectoryNameChanged(value: String) {
        update { state -> state.copy(createDirectoryName = value) }
    }

    fun createDirectory() {
        interactorScope.launch {
            val folder = state.selectedDirectory ?: return@launch
            val createdFolder = fileHandler.resolveDirectory(folder, state.createDirectoryName, create = true).unwrapOrReturn {
                update { state -> state.copy(createDirectoryResult = this) }
                return@launch
            }

            update { state -> state.copy(createDirectoryResult = Outcome.Ok(createdFolder)) }
        }
    }

    fun readData() {
        interactorScope.launch {
            println("Starting")
            val source = state.selectedFile?.source()?.unwrapOrReturn { return@launch } ?: return@launch
            val content = source.use { it.buffer().readUtf8() }
            println("Done")
            update { state -> state.copy(selectedFileContent = content)}
        }
    }

    fun readMetaData() {
        interactorScope.launch {
            val outcome = fileHandler.readMetadata(state.selectedFile ?: return@launch)
            if (outcome is Outcome.Ok) update { state -> state.copy(metadata = outcome.value) }
        }
    }

    fun deleteFile() {
        interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val outcome = fileHandler.delete(file)
            update { state -> state.copy(deleteFileResult = outcome) }
        }
    }

    fun deleteDirectory() {
        interactorScope.launch {
            val folder = state.selectedDirectory ?: return@launch
            val outcome = fileHandler.delete(folder)
            update { state -> state.copy(deleteDirectoryResult = outcome) }
        }
    }

    fun list() {
        interactorScope.launch {
            val folder = state.selectedDirectory ?: return@launch
            val files = fileHandler.list(folder, isRecursive = true).unwrapOrReturn { return@launch }
            update { state -> state.copy(fileList = files) }
        }
    }

    fun exists() {
        interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val folder = state.selectedDirectory ?: return@launch
            val fileExists = fileHandler.exists(file)
            val folderExists = fileHandler.exists(folder)

            update { state ->
                state.copy(fileExistsResult = fileExists, directoryExistsResult = folderExists)
            }
        }
    }
}