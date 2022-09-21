package com.metgallery.ui.departments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metgallery.util.Event
import com.metgallery.data.api.model.MetDepartment
import com.metgallery.domain.GetDepartments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentsViewModel @Inject constructor(private val getDepartments: GetDepartments) : ViewModel() {

    private val _departments = MutableLiveData<List<MetDepartment>>().apply { value = emptyList() }
    val departments: LiveData<List<MetDepartment>> = _departments

    private val _selectedDepartment = MutableLiveData<Event<MetDepartment>>()
    val selectedDepartment: LiveData<Event<MetDepartment>> = _selectedDepartment

    init {
        loadDepartments()
    }

    fun selectDepartment(department: MetDepartment) {
        _selectedDepartment.value = Event(department)
    }

    private fun loadDepartments() {
        viewModelScope.launch {
            _departments.value = getDepartments().sortedBy { it.displayName }
        }
    }
}