package ui.file

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.scrollbars.KmpVerticalScrollbar
import com.outsidesource.oskitcompose.scrollbars.rememberKmpScrollbarAdapter

@Composable
fun FileHandlingScreen(
    interactor: FileHandlingViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    LaunchedEffect(Unit) {
        interactor.onMounted()
    }

    Screen("File Handling") {
        Box {
            val scrollState = rememberScrollState()

            KmpVerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.TopEnd)
                    .zIndex(1f),
                adapter = rememberKmpScrollbarAdapter(scrollState),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column {
                    Text("Selected File: ${state.selectedFile?.name ?: "None"}")
                    Button(
                        onClick = interactor::pickFile
                    ) {
                        Text("Pick File")
                    }
                }

                Column {
                    Text("Selected File Content")
                    if (state.selectedFileContent.isNotEmpty()) Text(state.selectedFileContent)
                    Button(
                        onClick = interactor::readData
                    ) {
                        Text("Read File")
                    }
                }

                Column {
                    Text("Selected Files: ${state.selectedFiles?.joinToString(",") { it.name } ?: "None"}")
                    Button(
                        onClick = interactor::pickFiles
                    ) {
                        Text("Pick Files")
                    }
                }

                Column {
                    Text("Selected Directory: ${state.selectedDirectory?.name ?: "None"}")
                    Button(
                        onClick = interactor::pickDirectory
                    ) {
                        Text("Pick Directory")
                    }
                }

                Column {
                    Text("Pick Save File: ${state.saveFile ?: "None"}")
                    Button(
                        onClick = interactor::pickSaveFile
                    ) {
                        Text("Pick Save File")
                    }
                }

                Column {
                    Text("Create File in Selected Folder: ${state.createFileResult ?: ""}")
                    TextField(
                        value = state.createFileName,
                        placeholder = { Text("Name") },
                        onValueChange = interactor::createFileNameChanged
                    )
                    TextField(
                        value = state.createFileContent,
                        placeholder = { Text("Content") },
                        onValueChange = interactor::createFileContentChanged
                    )
                    Button(
                        onClick = interactor::createFile
                    ) {
                        Text("Create File")
                    }
                }

                Column {
                    Text("Append To File: ${state.appendFileResult ?: ""}")
                    TextField(
                        value = state.appendFileContent,
                        placeholder = { Text("Content") },
                        onValueChange = interactor::appendToFileContentChanged
                    )
                    Button(
                        onClick = interactor::appendToFile
                    ) {
                        Text("Append")
                    }
                }

                Column {
                    Text("Create Directory: ${state.createDirectoryResult ?: ""}")
                    TextField(
                        value = state.createDirectoryName,
                        placeholder = { Text("Name") },
                        onValueChange = interactor::createDirectoryNameChanged
                    )
                    Button(
                        onClick = interactor::createDirectory
                    ) {
                        Text("Create Folder")
                    }
                }

                Column {
                    Text("Read Selected File Metadata: ${state.metadata ?: "None"}")
                    Button(
                        onClick = interactor::readMetaData
                    ) {
                        Text("Read Metadata")
                    }
                }

                Column {
                    Text("Delete Selected File: ${state.deleteFileResult ?: ""}")
                    Button(
                        onClick = interactor::deleteFile
                    ) {
                        Text("Delete File")
                    }
                }

                Column {
                    Text("Delete Selected Directory: ${state.deleteDirectoryResult ?: ""}")
                    Button(
                        onClick = interactor::deleteDirectory
                    ) {
                        Text("Delete Directory")
                    }
                }

                Column {
                    Text("List Files In Selected Directory")
                    Button(
                        onClick = interactor::list
                    ) {
                        Text("List Files")
                    }
                    Column {
                        state.fileList.forEach {
                            Text(it.name)
                        }
                    }
                }

                Column {
                    Text("Check Selected File and Directory Exists")
                    Text("File Exists: ${state.fileExistsResult}    Folder Exists: ${state.directoryExistsResult}")
                    Button(
                        onClick = interactor::exists
                    ) {
                        Text("Check Exists")
                    }
                }
            }
        }
    }
}