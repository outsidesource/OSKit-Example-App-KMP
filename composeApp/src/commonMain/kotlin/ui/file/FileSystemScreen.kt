package ui.file

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute
import com.outsidesource.oskitcompose.popup.Modal
import com.outsidesource.oskitcompose.scrollbars.KmpVerticalScrollbar
import com.outsidesource.oskitcompose.scrollbars.rememberKmpScrollbarAdapter
import com.outsidesource.oskitkmp.filesystem.KmpFsType
import com.outsidesource.oskitkmp.filesystem.KmpFsWriteMode
import com.outsidesource.oskitkmp.outcome.Outcome
import ui.app.theme.AppTheme
import ui.common.Screen
import ui.common.Tab

@Composable
fun FileSystemScreen(
    interactor: FileSystemViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    LaunchedEffect(Unit) {
        interactor.onMounted()
    }

    Screen(
        title = "FileSystem",
        paddingValues = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimensions.screenPadding.calculateStartPadding(LayoutDirection.Ltr))
                .padding(vertical = 8.dp)
        ) {
            Tab(
                label = "Internal",
                isActive = state.fsType == KmpFsType.Internal,
                onClick = { interactor.fsTypeClicked(KmpFsType.Internal) }
            )
            Tab(
                label = "External",
                isActive = state.fsType == KmpFsType.External,
                onClick = { interactor.fsTypeClicked(KmpFsType.External) }
            )
        }

        Box {
            val scrollState = rememberScrollState()

            KmpVerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.TopEnd)
                    .zIndex(1f),
                adapter = rememberKmpScrollbarAdapter(scrollState),
            )

            when (state.fsType) {
                KmpFsType.Internal -> InternalFs(interactor, scrollState)
                KmpFsType.External -> ExternalFs(interactor, scrollState)
            }
        }
    }
}

@Composable
private fun InternalFs(interactor: FileSystemViewInteractor, scrollState: ScrollState) {
    SharedFileSystemControls(interactor, scrollState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExternalFs(interactor: FileSystemViewInteractor, scrollState: ScrollState) {
    val state = interactor.collectAsState()

    SharedFileSystemControls(interactor, scrollState) {
        Column {
            Text("Picked File:")
            if (state.pickedFileResult != null) Text(state.pickedFileResult.toString())
            Button(
                onClick = interactor::pickFile
            ) {
                Text("Pick File")
            }
        }

        Column {
            Text("Picked Files:")
            if (state.pickedFilesResult != null) Text(state.pickedFilesResult.toString())
            Button(
                onClick = interactor::pickFiles
            ) {
                Text("Pick Files")
            }
        }

        Column {
            Text("Picked Directory:")
            if (state.pickedDirectoryResult != null) Text(state.pickedDirectoryResult.toString())
            Button(
                onClick = interactor::pickDirectory
            ) {
                Text("Pick Directory")
            }
        }

        Column {
            Text("Pick Save File:")
            if (state.pickedSaveFileResult != null) Text(state.pickedSaveFileResult.toString())
            Button(
                onClick = interactor::pickSaveFile
            ) {
                Text("Pick Save File")
            }
        }

        Column {
            Text("Save File: ${state.saveFileResult ?: ""}")
            if (state.saveFileResult != null) Text(state.saveFileResult.toString())
            TextField(
                value = state.saveFileContent,
                placeholder = { Text("Content") },
                onValueChange = interactor::saveFileContentChanged
            )
            Button(
                onClick = interactor::saveFile
            ) {
                Text("Save File")
            }
        }

        Column {
            Text("Resolve Ref from Path (only supported on JVM):")
            if (state.resolveRefFromPathResult != null) Text(state.resolveRefFromPathResult.toString())
            TextField(
                value = state.resolveRefFromPathPath,
                placeholder = { Text("Path") },
                onValueChange = interactor::resolveRefFromPathChanged
            )
            Button(
                onClick = interactor::resolveRefFromPath
            ) {
                Text("Resolve Ref")
            }
        }
    }
}

@Composable
private fun SharedFileSystemControls(
    interactor: FileSystemViewInteractor,
    scrollState: ScrollState,
    content: @Composable () -> Unit = {},
) {
    val state = interactor.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.screenPadding)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Active Ref:")
            Column {
                var isExpanded by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(color = AppTheme.colors.menuBorder, width = 1.dp, shape = RoundedCornerShape(4.dp))
                        .clickable { isExpanded = true }
                        .background(AppTheme.colors.menu)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = state.activeRefType.toString())
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AppTheme.colors.fontColor,
                    )
                }
                DropdownMenu(
                    modifier = Modifier.background(AppTheme.colors.menu),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    if (state.fsType == KmpFsType.External) {
                        DropdownMenuItem(
                            content = { Text("PickedFile") },
                            onClick = {
                                interactor.activeRefTypeChanged(ActiveRefType.PickedFile)
                                isExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("PickedDirectory") },
                            onClick = {
                                interactor.activeRefTypeChanged(ActiveRefType.PickedDirectory)
                                isExpanded = false
                            }
                        )
                    }
                    if (state.fsType == KmpFsType.Internal) {
                        DropdownMenuItem(
                            content = { Text("InternalRoot") },
                            onClick = {
                                interactor.activeRefTypeChanged(ActiveRefType.InternalRoot)
                                isExpanded = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        content = { Text("ResolvedFile") },
                        onClick = {
                            interactor.activeRefTypeChanged(ActiveRefType.ResolvedFile)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        content = { Text("ResolvedDirectory") },
                        onClick = {
                            interactor.activeRefTypeChanged(ActiveRefType.ResolvedDirectory)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        content = { Text("ResolvedFromPath") },
                        onClick = {
                            interactor.activeRefTypeChanged(ActiveRefType.ResolvedFromPath)
                            isExpanded = false
                        }
                    )
                }
            }

            Text(state.activeRef?.toString() ?: "None")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Column {
                Text("Persisted Ref:")
                Text("${state.persistedRef ?: "None"}")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = interactor::loadPersistedRef
                    ) {
                        Text("Load Persisted Ref")
                    }
                    Button(
                        onClick = interactor::persistRef
                    ) {
                        Text("Persist Active Ref")
                    }
                }
            }

            content()

            Column {
                Text("Resolve File in Active Ref (active ref must be directory):")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Create: ")
                    Checkbox(checked = state.resolveFileCreate, onCheckedChange = interactor::resolveFileCreateChanged)
                }
                if (state.resolveFileResult != null) Text(state.resolveFileResult.toString())
                TextField(
                    value = state.resolveFileName,
                    placeholder = { Text("Name") },
                    onValueChange = interactor::resolveFileNameChanged
                )
                Button(
                    onClick = interactor::resolveFile
                ) {
                    Text("Resolve File")
                }
            }

            Column {
                Text("Resolve Directory in Active Ref (active ref must be directory):")
                if (state.resolveDirectoryResult != null) Text(state.resolveDirectoryResult.toString())
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Create: ")
                    Checkbox(checked = state.resolveDirectoryCreate, onCheckedChange = interactor::resolveDirectoryCreateChanged)
                }
                TextField(
                    value = state.resolveDirectoryName,
                    placeholder = { Text("Name") },
                    onValueChange = interactor::resolveDirectoryNameChanged
                )
                Button(
                    onClick = interactor::resolveDirectory
                ) {
                    Text("Resolve Directory")
                }
            }

            Column {
                Text("Read Active Ref Content (active ref must be file):")
                if (state.readFileResult != null) Text(state.readFileResult.toString())
                Button(
                    onClick = interactor::readRef
                ) {
                    Text("Read")
                }
            }

            Column {
                Text("Write Active Ref Content (active ref must be file):")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Mode:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = state.writeFileMode == KmpFsWriteMode.Append,
                            onClick = { interactor.writeModeChanged(KmpFsWriteMode.Append) }
                        )
                        Text("Append")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = state.writeFileMode == KmpFsWriteMode.Overwrite,
                            onClick = { interactor.writeModeChanged(KmpFsWriteMode.Overwrite) }
                        )
                        Text("Overwrite")
                    }
                }
                if (state.writeFileResult != null) Text(state.writeFileResult.toString())
                TextField(
                    value = state.writeFileContent,
                    placeholder = { Text("Content") },
                    onValueChange = interactor::appendToFileContentChanged
                )
                Button(
                    onClick = interactor::writeToFile
                ) {
                    Text("Write")
                }
            }

            Column {
                Text("Read Active Ref Metadata:")
                if (state.activeRefMetadataResult != null) Text(state.activeRefMetadataResult.toString())
                Button(
                    onClick = interactor::readMetadata
                ) {
                    Text("Read Metadata")
                }
            }

            Column {
                Text("Delete Active Ref:")
                if (state.deleteRefResult != null) Text(state.deleteRefResult.toString())
                Button(
                    onClick = interactor::deleteActiveRefClicked
                ) {
                    Text("Delete Ref")
                }
            }

            Column {
                Text("List Files In Active Ref (active ref must be directory):")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Recursive:")
                    Checkbox(checked = state.listRecursive, onCheckedChange = interactor::listRecursiveCheckChanged)
                }
                Column {
                    when (val result = state.listResult) {
                        is Outcome.Error -> Text(text = state.listResult.toString())
                        is Outcome.Ok -> {
                            Text(text = "Ok: ${result.value.size} Refs")
                            result.value.forEach { Text(it.name) }
                        }
                        null -> {}
                    }
                }
                Button(
                    onClick = interactor::list
                ) {
                    Text("List Files")
                }
            }

            Column {
                Text("Check Active Ref Exists:")
                if (state.refExistsResult != null) Text(state.refExistsResult.toString())
                Button(
                    onClick = interactor::exists
                ) {
                    Text("Check Exists")
                }
            }
        }
    }

    Modal(
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth(),
        isVisible = state.isDeleteModalVisible,
        onDismissRequest = interactor::deleteModalDismissed,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Are you sure you want to delete this ref: ${state.activeRef?.name}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                Button(
                    onClick = interactor::deleteModalDismissed,
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = interactor::deleteModalConfirmed
                ) {
                    Text("Delete")
                }
            }
        }
    }
}